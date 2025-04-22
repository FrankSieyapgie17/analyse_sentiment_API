package ftrank.tech.avis.entite;

import ftrank.tech.avis.TypeDeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    @Enumerated(EnumType.STRING) // permet de specifier comment l'énumeration sera stockée dans la bd
    private TypeDeRole libelle;

    /*public TypeDeRole getLibelle() {
        return libelle;
    } */
}
