package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "black_list_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlackListToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String token;

        @CreationTimestamp
        private LocalDateTime expiredAt;
}
