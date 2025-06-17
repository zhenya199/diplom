package bu.eugene.map.controller;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.facade.ImageFacade;
import bu.eugene.map.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/image")
@CrossOrigin(origins = "*",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"})
public class ImageController {

    private final ImageService imageService;
    private final ImageFacade imageFacade;

        @RequestMapping(method = RequestMethod.OPTIONS)
        public ResponseEntity<Void> handleOptions() {
            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "POST, OPTIONS")
                    .header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With")
                    .header("Access-Control-Max-Age", "3600")
                    .build();
        }

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ImageDto> save(
                @RequestPart("image") MultipartFile file,
                @RequestPart("imageDto") ImageDto imageDto,
                @RequestParam("placeId") String placeId,
                @RequestHeader("Authorization") String token) {

            log.info("Received file: {} ({} bytes)", file.getOriginalFilename(), file.getSize());
            log.info("ImageDto: {}", imageDto);

            ImageDto savedImage = imageFacade.saveImage(imageDto, file, placeId, token);
            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "https://effortless-douhua-d77333.netlify.app")
                    .body(savedImage);
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