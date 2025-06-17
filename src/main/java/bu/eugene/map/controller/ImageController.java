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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/image")
@CrossOrigin("*")
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

        @GetMapping("/all")
        public Page<ImageDto> getAllImages(Pageable pageable) {
            return imageService.getAllImages(pageable);
        }

        @GetMapping("/byPerson")
        public Page<ImageDto> getImageByUsername(Pageable pageable, @RequestParam String username) {
            return imageService.getImagesByUsername(pageable, username);
        }

        @GetMapping("/{id}")
        public ImageDto getImage(@PathVariable("id") Integer imageId) {
            return imageService.getImageDtoById(imageId);
        }

        @GetMapping("/find")
        public Page<ImageDto> findImagesByPlaceName(Pageable pageable, @RequestParam("name") String name) {
            return imageService.findByPlaceName(pageable, name);
        }

        @GetMapping("/my")
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

        @GetMapping("/all/byPlace/{id}")
        public Page<ImageDto> getImagesByPlace(@PathVariable("id") String placeId, Pageable pageable) {
            return imageService.getImagesByPlace(placeId, pageable);
        }

        @GetMapping("/all/liked")
        public Page<ImageDto> getLikedImages(@RequestHeader("Authorization") String token,
                                             Pageable pageable) {
            return imageService.getLikedImages(token, pageable);
        }
}