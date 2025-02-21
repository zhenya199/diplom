package bu.eugene.map.repository;

import bu.eugene.map.model.ImageEntity;
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
}
