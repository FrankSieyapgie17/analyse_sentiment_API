package ftrank.tech.avis.service;


import ftrank.tech.avis.TypeDeRole;
import ftrank.tech.avis.entite.Role;
import ftrank.tech.avis.entite.Utilisateur;
import ftrank.tech.avis.entite.Validation;
import ftrank.tech.avis.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
@Service
public class UtilisateurService implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ValidationService validationService;

    public void create(Utilisateur utilisateur) {

        if(!utilisateur.getEmail().contains("@") && utilisateur.getEmail().contains(".")){
            throw new RuntimeException("votre email est invalide");
        }

         Optional<Utilisateur> optionalUtilisateur =  this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if(optionalUtilisateur.isPresent()){
            throw new RuntimeException("cet email existe déjà, veuillez le réinitialiser");
        }

        // création du role de l'utilisateur
        Role roleUtilisateur = new Role();
        roleUtilisateur.setLibelle(TypeDeRole.UTILISATEUR);
        utilisateur.setRole(roleUtilisateur);


        // Encrypetage du mot de passe
        String mdpCrypte = this.bCryptPasswordEncoder.encode(utilisateur.getMdp());
        utilisateur.setMdp(mdpCrypte);


        utilisateur = this.utilisateurRepository.save(utilisateur);
        this.validationService.enregistrer(utilisateur);
    }

    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.lireEnFonctionDuCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("votre code a expiré");
        }

        Utilisateur utilisateurActiver = this.utilisateurRepository.findById(validation.getUtilisateur().getId()).orElseThrow(() -> new RuntimeException("utilisateur inconnu"));
        utilisateurActiver.setActif(true);
        this.utilisateurRepository.save(utilisateurActiver);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // il permet d'aller dans la bd, chercher l'utilisateur en fonction de son user name
       return this.utilisateurRepository.findByEmail(username).orElseThrow(()-> new RuntimeException("Aucun utilisateur trouvé dans la base de données"));
    }
}
