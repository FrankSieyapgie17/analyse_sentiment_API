package ftrank.tech.avis.service;

import ftrank.tech.avis.entite.Avis;
import ftrank.tech.avis.repository.AvisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class AvisService {

    private  AvisRepository avisRepository;
    public void creer(Avis avis){
        this.avisRepository.save(avis);
    }
    public void delete(Integer id){
        if(id == null){
            log.error("Aucun avis trouvé");
            return ;
        }
        this.avisRepository.deleteById(id);
    }
}
