package bu.eugene.map.repository;

import bu.eugene.map.model.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    @Query("SELECT l FROM LikeEntity l WHERE l.author.id = :authorId AND l.image.id = :imageId")
    LikeEntity findLikeByImageIdAndPersonId(@Param("authorId") Integer authorId, @Param("imageId") Integer imageId);
}
