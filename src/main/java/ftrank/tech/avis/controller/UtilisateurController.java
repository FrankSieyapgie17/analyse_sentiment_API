package ftrank.tech.avis.controller;

import ftrank.tech.avis.AuthentificationDto;
import ftrank.tech.avis.Securite.JwtUtils;
import ftrank.tech.avis.entite.Utilisateur;
import ftrank.tech.avis.service.UtilisateurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@Slf4j // pour les loggers
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class UtilisateurController {
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private UtilisateurService utilisateurService;

    @PostMapping(path = "/inscription")
    public void inscription(@RequestBody Utilisateur utilisateur) {
        this.utilisateurService.create(utilisateur);

       log.info("Inscription");
    }

    @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activation) {
        this.utilisateurService.activation(activation);

        //log.info("activation");

    }

    @PostMapping(path = "/deconnexion")
    public void deconnexion() {
        this.jwtUtils.deconnexion();
    }

    @PostMapping(path = "/connexion")
    public Map<String, String> connexion (@RequestBody AuthentificationDto authentificationDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authentificationDto.username(), authentificationDto.password())
        );
        if(authenticate.isAuthenticated()){
            return jwtUtils.generate(authentificationDto.username());
        }
        //log.info("resultat {}", authenticate.isAuthenticated());
        return null;
    }

}
