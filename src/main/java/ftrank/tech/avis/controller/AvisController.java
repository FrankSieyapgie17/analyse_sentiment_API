package ftrank.tech.avis.controller;

import ftrank.tech.avis.entite.Avis;
import ftrank.tech.avis.entite.Utilisateur;
import ftrank.tech.avis.service.AvisService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping(path = "avis")
public class AvisController {

    private final AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis){
       Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUtilisateur(utilisateur);
        this.avisService.creer(avis);
    }
    @DeleteMapping(path = "delete/{id}")
    public void delete(@PathVariable Integer id){
        this.avisService.delete(id);
    }
}
