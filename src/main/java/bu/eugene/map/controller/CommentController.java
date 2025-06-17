package bu.eugene.map.controller;

import bu.eugene.map.dto.CommentDto;
import bu.eugene.map.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class CommentController {

        private final CommentService commentService;

        @RequestMapping(method = RequestMethod.OPTIONS, value = "/comment")
        public ResponseEntity<Void> handleAccountOptions() {
                return ResponseEntity.ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                        .header("Access-Control-Allow-Headers", "Authorization")
                        .build();
        }

        @PostMapping("/new")
        public ResponseEntity<?> addCommentToImage(@RequestBody CommentDto comment) {
                commentService.addComment(comment);
                return ResponseEntity.ok().build();
        }

        @PostMapping("/new/route")
        public ResponseEntity<?> addCommentToRoute(@RequestBody CommentDto comment) {
                commentService.addCommentToRoute(comment);
                return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{id}")
        public void deleteComment(@PathVariable("id") Integer id,
                                  @RequestHeader("Authorization") String token) {
                commentService.deleteComment(id, token);
        }
}
