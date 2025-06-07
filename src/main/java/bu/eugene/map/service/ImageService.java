package bu.eugene.map.service;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.exception.FileExtensionException;
import bu.eugene.map.exception.FileUploadException;
import bu.eugene.map.mapper.ImageMapper;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.model.Place;
import bu.eugene.map.model.RouteEntity;
import bu.eugene.map.repository.ImageRepository;
import bu.eugene.map.util.Dto2EntityConverter;
import bu.eugene.map.util.PersonRoleEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        private final ImageMapper imageMapper;
        private final PersonService personService;
        @Value("${saving_dir}")
        private String  UPLOAD_DIR ;

        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

        private final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "jpeg", "png"};

        public ImageDto saveImage(ImageDto imageDto, Place place, Person person) {
                ImageEntity image = dto2EntityConverter.convertImageDto2ImageEntity(imageDto);
                image.setPerson(person);
                image.setPathToFile(saveImagesLocal(imageDto.getImage()));
                image.setPlace(place);
                return imageMapper.image2Dto(imageRepository.save(image));
        }

        public ImageEntity saveImageWithoutPlace(ImageDto imageDto, RouteEntity route) {
                ImageEntity image = new ImageEntity();
                image.setPathToFile(saveImagesLocal(imageDto.getImage()));
                image.setDescription(imageDto.getDescription());
                image.setRoute(route);
                return imageRepository.save(image);
        }

        public Page<ImageDto> getLikedImages(String token, Pageable pageable) {
                Person person = personService.getPersonFromToken(token);
                Page<ImageEntity> images = imageRepository.getAllLikedImagesByPersonUsername(person.getUsername(), pageable);
                return images.map(imageMapper::image2Dto);
        }

        public Page<ImageDto> findByPlaceName(Pageable pageable, String name) {
                Page<ImageEntity> images = imageRepository.findByPlaceNameContaining(pageable, name);
                return images.map(imageMapper::image2Dto);
        }

        public Page<ImageDto> getImagesByUsername(Pageable pageable, String username) {
                Page<ImageEntity> page = imageRepository.findAllByAuthorUsername(pageable, username);
                return page.map(imageMapper::image2Dto);
        }

        public Page<ImageDto> getImagesByPlace(String placeId, Pageable pageable) {
                Page<ImageEntity> imagesByPlace = imageRepository.findAllByPlaceId(placeId, pageable);
                return  imagesByPlace.map(imageMapper::image2Dto);
        }

        public Page<ImageDto> getAllImages(Pageable pageable) {
                Page<ImageEntity> images = imageRepository.findByPlaceIsNotNull(pageable);
                return images.map(imageMapper::image2Dto);
        }

        public ResponseEntity<?> deletePhoto(Person author, Integer imageId) {
                ImageEntity image = getImageById(imageId);

                if (canPersonDeletePhoto(author, image) || author.getRole().equals("ADMIN")) {
                        imageRepository.delete(image);
                } else {
                        throw new AccessDeniedException("Вы не можете удалить чужое фото");
                }
                return ResponseEntity.ok().build();
        }

        public ImageEntity getImageById(Integer imageId) {
                return imageRepository.findById(imageId).orElseThrow(
                        () -> new EntityNotFoundException("Image not found")
                );
        }

        public ImageDto getImageDtoById(Integer imageId) {
                return imageMapper.image2Dto(getImageById(imageId));
        }

        public Page<ImageDto> getMyImages(Person person, Pageable pageable) {
                Page<ImageEntity> imageEntityPage = imageRepository.findAllByPersonId(person.getId(), pageable);

                List<ImageDto> imageDtos = imageEntityPage.getContent().stream()
                        .map(imageMapper::image2Dto)
                        .toList();
                return new PageImpl<>(imageDtos);
        }

        public ImageDto updatePhoto(Integer photoId, Person author, String description) {
                ImageEntity image = getImageById(photoId);
                if(image.getPerson().getUsername().equals(author.getUsername())) {
                        image.setDescription(description);
                }
                return imageMapper.image2Dto(imageRepository.save(image));
        }

        public String saveImagesLocal(MultipartFile file) {
                // 1. Проверяем, не пустой ли файл
                if (file.isEmpty()) {
                        throw new FileUploadException("Файл не может быть пустым");
                }

                // 2. Создаем директорию для загрузок
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                        if (!uploadDir.mkdirs()) {
                                log.error("Не удалось создать директорию: {}", UPLOAD_DIR);
                                throw new FileUploadException("Ошибка при создании директории для загрузки");
                        }
                        log.info("Директория для загрузки создана: {}", UPLOAD_DIR);
                }

                try {
                        // 3. Проверяем размер файла
                        if (file.getSize() > MAX_FILE_SIZE) {
                                throw new MaxUploadSizeExceededException(MAX_FILE_SIZE);
                        }

                        // 4. Проверяем расширение файла
                        String originalFilename = file.getOriginalFilename();
                        if (originalFilename == null || originalFilename.isEmpty()) {
                                throw new FileUploadException("Имя файла не может быть пустым");
                        }

                        String extension = FilenameUtils.getExtension(originalFilename);
                        String baseName = FilenameUtils.getBaseName(originalFilename).replaceAll(" ", "_");

                        if (!Arrays.stream(IMAGE_EXTENSIONS).anyMatch(ext -> ext.equalsIgnoreCase(extension))) {
                                throw new FileExtensionException("Неверный формат файла. Допустимые форматы: " +
                                        String.join(", ", IMAGE_EXTENSIONS));
                        }

                        // 5. Создаем уникальное имя файла
                        String uniqueFilename = createUniqueFilename(baseName, extension);
                        File destinationFile = new File(uploadDir, uniqueFilename);

                        try (InputStream inputStream = file.getInputStream()) {
                                Files.copy(inputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }

                        log.info("Файл успешно сохранен: {}", destinationFile.getAbsolutePath());
                        return "/uploads/" + uniqueFilename;

                } catch (IOException e) {
                        log.error("Ошибка при сохранении файла: {}", e.getMessage(), e);
                        throw new FileUploadException("Ошибка загрузки файла: " + e.getMessage());
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