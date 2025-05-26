package bu.eugene.map.repository;

import bu.eugene.map.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByPlaceId(String placeId);

    @Query("SELECT p FROM Place p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(p.country) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(p.typeOfPlace) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(p.suburb) LIKE LOWER(CONCAT('%', :name, '%'))")
    Optional<Place> findByParamIgnoreCase(@Param("name") String name);

}
