package ftrank.tech.avis.service;

import ftrank.tech.avis.entite.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
     JavaMailSender javaMailSender;

   public void envoyer(Validation validation){
       SimpleMailMessage message = new  SimpleMailMessage();
       message.setFrom("info@skysoft.com");
       message.setTo(validation.getUtilisateur().getEmail());
       message.setSubject("Code de validation");

       // %s fait reférence à celui ) qui on envoie le mail
       String text = String.format("Bonjour %s, <br/> voici votre code de validation",
               validation.getUtilisateur().getNom(),
               validation.getCode());
       message.setText(text);

       this.javaMailSender.send(message);
   }

}
