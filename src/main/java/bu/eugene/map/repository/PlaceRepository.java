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

    Optional<Place> findByNameAndLatAndLonAndCountryAndTypeOfPlaceAndSuburb(String name,
                                                                            Double lat,
                                                                            Double lon,
                                                                            String country,
                                                                            String typeOfPlace,
                                                                            String suburb);

    @Query("SELECT p FROM Place p WHERE p.name ILIKE %:name% " +
            "OR p.country ILIKE %:name% " +
            "OR p.typeOfPlace ILIKE %:name% " +
            "OR p.suburb ILIKE %:name%")
    Page<Place> findByParamIgnoreCase(Pageable pageable, @Param("name") String name);
}
