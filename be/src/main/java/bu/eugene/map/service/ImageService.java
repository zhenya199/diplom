package bu.eugene.map.service;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.exception.CannotDeleteLikeException;
import bu.eugene.map.exception.FileExtensionException;
import bu.eugene.map.exception.FileUploadException;
import bu.eugene.map.jwt.JwtUtil;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.LikeEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.model.Place;
import bu.eugene.map.repository.ImageRepository;
import bu.eugene.map.util.Dto2EntityConverter;
import bu.eugene.map.util.Entity2DtoConverter;
import bu.eugene.map.util.PersonRoleEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

        private final ImageRepository imageRepository;
        private final Dto2EntityConverter dto2EntityConverter;
        private final Entity2DtoConverter entity2DtoConverter;
        private final String  UPLOAD_DIR = "/Users/mihail/WebstormProjects/map/uploads";

        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

        private final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "jpeg", "png"};

        public ImageDto saveImage(ImageDto imageDto, Place place, Person person) {
                ImageEntity image = dto2EntityConverter.convertImageDto2ImageEntity(imageDto);
                image.setPerson(person);
                image.setPathToFile(saveImagesLocal(imageDto.getImage()));
                image.setPlace(place);
                return entity2DtoConverter.convertImageEntity2Dto(imageRepository.save(image));
        }

        public ResponseEntity<?> deletePhoto(Person author, Integer imageId) {
                ImageEntity image = getImageById(imageId);

                if (canPersonDeletePhoto(author, image)) {
                        imageRepository.delete(image);
                } else {
                        throw new AccessDeniedException("You can delete only your images");
                }
                return ResponseEntity.ok().build();
        }

        public ImageEntity getImageById(Integer imageId) {
                return imageRepository.findById(imageId).orElseThrow(
                        () -> new EntityNotFoundException("Image not found")
                );
        }

        public ImageDto getImageDtoById(Integer imageId) {
                return entity2DtoConverter.convertImageEntity2Dto(getImageById(imageId));
        }

        public Page<ImageDto> getMyImages(Person person, Pageable pageable) {
                Page<ImageEntity> imageEntityPage = imageRepository.findAllByPersonId(person.getId(), pageable);

                List<ImageDto> imageDtos = imageEntityPage.getContent().stream()
                        .map(entity2DtoConverter::convertImageEntity2Dto)
                        .toList();
                return new PageImpl<>(imageDtos);
        }

        public ImageDto updatePhoto(Integer photoId, Person author, String description) {
                ImageEntity image = getImageById(photoId);
                if(image.getPerson().getUsername().equals(author.getUsername())) {
                        image.setDescription(description);
                }
                return entity2DtoConverter.convertImageEntity2Dto(imageRepository.save(image));
        }

        public String saveImagesLocal(MultipartFile file) {

                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                        if (uploadDir.mkdirs()) {
                                log.info("Директория для загрузки создана: {}", UPLOAD_DIR);
                        } else {
                                log.error("Не удалось создать директорию: {}", UPLOAD_DIR);
                                throw new FileUploadException("Ошибка при создании директории для загрузки");
                        }
                }

                        log.info("start saving image");
                        if(file.getSize() < MAX_FILE_SIZE) {
                                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                                String baseName = FilenameUtils.getBaseName(
                                        file.getOriginalFilename()).replaceAll(" ", "_");

                                if (!Arrays.stream(IMAGE_EXTENSIONS).anyMatch(extension::equalsIgnoreCase)) {
                                        throw new FileExtensionException("неверный формат файла, должен быть только: jpg, jpeg или png");
                                }

                                String uniqueFilename = createUniqueFilename(baseName, extension);
                                File destinationFile = new File(UPLOAD_DIR, uniqueFilename);

                                try {
                                        file.transferTo(destinationFile);
                                } catch (IOException e) {
                                        log.error("smth went wrong while file saving");
                                        throw new FileUploadException("ошибка загрузки файла");
                                }
                                return "/uploads/" + uniqueFilename;
                        } else {
                                throw new MaxUploadSizeExceededException(MAX_FILE_SIZE);
                        }
        }

        private  String createUniqueFilename(String baseName, String extension) {

                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                return baseName + "_" + timestamp + "." + extension;
        }

        private boolean canPersonDeletePhoto(Person person, ImageEntity image) {
                String personRole = person.getRole();

                if(image.getPerson().getUsername().equals(person.getUsername())
                        || personRole.equals(PersonRoleEnum.ADMIN.name())) {
                        return true;
                }
                return false;
        }
}