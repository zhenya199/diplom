package bu.eugene.map.repository;

import bu.eugene.map.model.RouteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Integer> {

    @Query("SELECT re FROM RouteEntity  re WHERE re.author.id=:userId")
    Page<RouteEntity> findAllByUserId(Integer userId, Pageable pageable);


}
