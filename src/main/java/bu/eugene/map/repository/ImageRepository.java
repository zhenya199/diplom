package bu.eugene.map.repository;

import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository  extends JpaRepository<ImageEntity, Integer> {

        @Query(value = "SELECT i FROM ImageEntity i WHERE i.person.id = :personId")
        Page<ImageEntity> findAllByPersonId(@Param("personId") Integer personId, Pageable pageable);

        @Query(value = "SELECT p FROM ImageEntity p WHERE p.place.placeId = :placeId")
        Page<ImageEntity> findAllByPlaceId(@Param("placeId") String placeId, Pageable pageable);

        @Query(value = "SELECT i FROM ImageEntity i JOIN i.likes le WHERE le.author.username = :username")
        Page<ImageEntity> getAllLikedImagesByPersonUsername(@Param("username") String username, Pageable pageable);

        @Query(value = "SELECT i FROM ImageEntity i WHERE i.person.username = :username")
        Page<ImageEntity> findAllByAuthorUsername(Pageable pageable, @Param("username") String username);

        @Query("SELECT i FROM ImageEntity i WHERE i.place.name LIKE %:name%")
        Page<ImageEntity> findByPlaceNameContaining(Pageable pageable, @Param("name") String name);

}
