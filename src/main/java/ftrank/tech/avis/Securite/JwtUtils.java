package ftrank.tech.avis.Securite;

import ftrank.tech.avis.entite.Jwt;
import ftrank.tech.avis.entite.Utilisateur;
import ftrank.tech.avis.repository.JwtRepository;
import ftrank.tech.avis.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class JwtUtils {
    private JwtRepository jwtRepository;
    public static final String BEARER = "bearer";
    private final String ENCRYPTION_KEY = "7c72284b3b5f40527979202b3e3656547945394f3732643a2a212b4a75";
    private UtilisateurService utilisateurService;

    public Jwt tokenValue(String valeur) {
       return this.jwtRepository.findByValeur(valeur).orElseThrow(() -> new RuntimeException("Token inconnu"));
    }

    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        Map<String, String> jwtMap = this.createdJwt(utilisateur);
        Jwt Myjwt = Jwt
                        .builder()
                        .valeur(jwtMap.get(BEARER))
                        .desactive(false)
                        .expire(false)
                        .utilisateur(utilisateur)
                        .build();

        this.jwtRepository.save(Myjwt);
        return jwtMap;
    }

    public void desableToken(Utilisateur utilisateur) {
       final List<Jwt> jwtList = jwtRepository.findUtilisateur(utilisateur.getEmail()).peek(
       jwt -> {
           jwt.setExpire(true);
           jwt.setDesactive(true);

       }).collect(Collectors.toList());
       this.jwtRepository.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaims(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaims(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private <T> T getClaims(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKeys())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> createdJwt(Utilisateur utilisateur) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        final Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.SUBJECT, utilisateur.getEmail(),
                Claims.EXPIRATION, new Date()
        );
        String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKeys(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(BEARER, bearer);
    }

    private Key getKeys() {
        byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public void deconnexion() {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByValidToken(utilisateur.getEmail(), true, true).orElseThrow(()-> new RuntimeException("Token invalid"));
            jwt.setDesactive(false);
            jwt.setExpire(false );
            this.jwtRepository.save(jwt);
    }

    @Scheduled(cron = "@hourly")
    public void removeUnlessToken() {
        log.info("suppression des token à {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndAndDesactive(true, true);
    }
}
