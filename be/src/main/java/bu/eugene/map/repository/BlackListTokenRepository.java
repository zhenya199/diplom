package bu.eugene.map.repository;

import bu.eugene.map.model.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Integer> {
}
