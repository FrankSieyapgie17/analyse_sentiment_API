package ftrank.tech.avis.service;

import ftrank.tech.avis.entite.Avis;
import ftrank.tech.avis.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AvisService {

    private  AvisRepository avisRepository;
    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }
}
