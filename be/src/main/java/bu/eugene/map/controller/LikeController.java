package bu.eugene.map.controller;

import bu.eugene.map.dto.LikeDto;
import bu.eugene.map.facade.LikeFacade;
import bu.eugene.map.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

        private final LikeService likeService;
        private final LikeFacade likeFacade;

        @PostMapping("/{id}")
        public LikeDto saveLike(@PathVariable("id") Integer imageId,
                                @RequestHeader("Authorization") String token,
                                @RequestParam Integer rate) {
            return likeFacade.createLike(token, imageId, rate);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> removeLike(@PathVariable("id") Integer likeId,
                                            @RequestHeader("Authorization") String token) {
                return likeFacade.deleteLike(token, likeId);
        }
}
