package ftrank.tech.avis.Securite;

import ftrank.tech.avis.entite.Jwt;
import ftrank.tech.avis.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Service
public class JwtFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UtilisateurService utilisateurService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Jwt tokenDBD = null;
        String username = null;
        boolean isTokenExpired = true;

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")){
            token = header.substring(7);
            tokenDBD = jwtUtils.tokenValue(token);
            isTokenExpired = jwtUtils.isTokenExpired(token);
            username = jwtUtils.extractUsername(token);
        }

        if(!isTokenExpired && tokenDBD.getUtilisateur().getEmail().equals(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
