package bu.eugene.map.controller;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.dto.LikeDto;
import bu.eugene.map.facade.LikeFacade;
import bu.eugene.map.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
@CrossOrigin("*")
public class LikeController {

        private final LikeService likeService;
        private final LikeFacade likeFacade;

        @RequestMapping(method = RequestMethod.OPTIONS, value = "/lik")
        public ResponseEntity<Void> handleAccountOptions() {
                return ResponseEntity.ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                        .header("Access-Control-Allow-Headers", "Authorization")
                        .build();
        }

        @PostMapping("/{id}")
        public ImageDto saveLike(@PathVariable("id") Integer imageId,
                                 @RequestHeader("Authorization") String token) {
            return likeFacade.createLike(token, imageId);
        }

        @DeleteMapping("/{id}")
        public ImageDto removeLike(@PathVariable("id") Integer imageId,
                                            @RequestHeader("Authorization") String token) {
                return likeFacade.deleteLike(token, imageId);
        }
}
