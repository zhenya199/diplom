package bu.eugene.map.repository;

import bu.eugene.map.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
