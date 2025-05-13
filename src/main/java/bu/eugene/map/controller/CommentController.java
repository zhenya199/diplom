package bu.eugene.map.controller;

import bu.eugene.map.dto.CommentDto;
import bu.eugene.map.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@CrossOrigin("http://localhost:8080")
public class CommentController {

        private final CommentService commentService;

        @PostMapping("/new")
        public ResponseEntity<?> addComment(@RequestBody CommentDto comment) {
                commentService.addComment(comment);
                return ResponseEntity.ok().build();
        }

        @DeleteMapping("/{id}")
        public void deleteComment(@PathVariable("id") Integer id,
                                  @RequestHeader("Authorization") String token) {
                commentService.deleteComment(id, token);
        }
}
