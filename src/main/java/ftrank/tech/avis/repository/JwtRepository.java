package ftrank.tech.avis.repository;

import ftrank.tech.avis.entite.Jwt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends CrudRepository<Jwt, Integer> {

    Optional<Jwt> findByValeur(String valeur);

    @Query("FROM Jwt J WHERE J.desactive = :desactive and J.expire = :expire and J.utilisateur.email = :email")
    Optional<Jwt> findByValidToken(String valeur, boolean desactive, boolean expire);

    @Query("FROM Jwt J WHERE J.utilisateur.email = :email")
    Stream<Jwt> findUtilisateur(String email);

    void deleteAllByExpireAndAndDesactive(boolean expire, boolean desactive);

}
