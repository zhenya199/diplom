package bu.eugene.map.controller;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.facade.ImageFacade;
import bu.eugene.map.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/image")
@CrossOrigin("http://localhost:8080")
public class ImageController {

        private final ImageService imageService;
        private final ImageFacade imageFacade;

        @PostMapping("/")
        public ImageDto save(@ModelAttribute ImageDto imageDto,
                             @RequestParam("placeId") String placeId,
                             @RequestHeader("Authorization") String token) {
            return imageFacade.saveImage(imageDto, placeId, token);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteImage(@RequestHeader("Authorization") String token,
                                             @PathVariable("id") Integer imageId) {
            return imageFacade.deletePhoto(token, imageId);
        }

        @GetMapping("/{id}")
        public ImageDto getImage(@PathVariable("id") Integer imageId) {
            return imageService.getImageDtoById(imageId);
        }

        @GetMapping("/all")
        public Page<ImageDto> getMyImages(@RequestHeader("Authorization") String token, Pageable pageable) {
            return imageFacade.getMyImages(token, pageable);
        }

        /**
         * Method for updating photo.
         * You can update only description field
         * */
        @PatchMapping("/{id}")
        public ImageDto updatePhoto(@PathVariable("id") Integer imageId,
                                    @RequestHeader("Authorization") String token,
                                    @RequestBody String description) {
            return imageFacade.updateImage(imageId, token, description);
        }
}