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

    @Query(value = "SELECT * FROM place p " +
            "WHERE p.street LIKE %:searchTerm% " +
            "OR p.name LIKE %:searchTerm% " +
            "OR p.suburb LIKE %:searchTerm% " +
            "OR p.street LIKE %:searchTerm% ",
            nativeQuery = true)
    Optional<Place> findByParamIgnoreCase(@Param("searchTerm") String name);

}
