package bu.eugene.map.repository;

import bu.eugene.map.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

    Optional<Place> findByPlaceId(String placeId);

    @Query(
            value = "SELECT * FROM place p " +
                    "WHERE p.name ILIKE CONCAT('%', :name, '%') " +
                    "OR p.country ILIKE CONCAT('%', :name, '%') " +
                    "OR p.type ILIKE CONCAT('%', :name, '%') " +
                    "OR p.suburb ILIKE CONCAT('%', :name, '%')",
            nativeQuery = true)
    Page<Place> findByParamIgnoreCase(Pageable pageable, @Param("name") String name);
}
