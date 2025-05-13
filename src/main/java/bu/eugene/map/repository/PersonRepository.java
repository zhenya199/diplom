package bu.eugene.map.repository;

import bu.eugene.map.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByUsername(String username);

    @Query("SELECT p FROM Person p WHERE p.username =:username OR p.email =:email")
    Optional<Person> findByUsernameOrEmail(@Param("username") String username,@Param("email") String email);
}
