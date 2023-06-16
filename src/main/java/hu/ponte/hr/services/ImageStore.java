package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.domain.ImageEntity;
import hu.ponte.hr.exceptions.IncorrectAlgorithmException;
import hu.ponte.hr.exceptions.IncorrectKeyException;
import hu.ponte.hr.exceptions.IncorrectSignatureException;
import hu.ponte.hr.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * The business logic of storing images.
 * <p>
 * @author tattila93
 * <p>
 *
 * -> findById()
 * parameter: String
 * finds the image entity with the given id in the database
 * if there is no entity with the given id it throws an EntityNotFoundException
 * else returns with the entity
 *<p>
 *
 * -> findByName()
 * parameter: String
 * finds the image entity with the given name in the database
 * and returns with the entity
 * <p>
 *
 * -> findAll()
 * parameter: -
 * finds all the image entities in the database and converts them into a DTO (ImageMeta) list
 * returns with the list
 * <p>
 *
 * -> saveImage()<
 * parameter: Multipart file
 * sets the imageEntity instance field with the given values if it is a picture and its size is not bigger than 2MB
 * else writes a warning message to the console
 * return value is void
 * <p>
 *
 * -> isPicture()
 * parameter: Multipart file
 * checks whether the file you want to upload is a picture or not
 * cannot be null
 * returns a boolean
 * <p>
 *
 * -> isSufficientSize()
 * parameter: Multipart file
 * checks whether the file you want to upload is not bigger than the max limit (currently 2Mb)
 * returns a boolean
 */
@Service
@Transactional
public class ImageStore {

    private final String maxFileSize;

    private static final Logger logger = LoggerFactory.getLogger(ImageStore.class);
    private final ImageRepository imageRepository;

    private final SignService signService;


    @Autowired
    public ImageStore(ImageRepository imageRepository, SignService signService,  @Value("${spring.servlet.multipart.max-file-size}") String maxFileSize) {
        this.imageRepository = imageRepository;
        this.signService = signService;
        this.maxFileSize = maxFileSize;
    }


    /**
     *  Finds the image entity with the given id in the database.
     *  If there is no entity with the given id it throws an EntityNotFoundException.
     *  Else returns with the entity.
     * @param id the id of the ImageEntity you are looking for
     * @return ImageEntity
     */
    public ImageEntity findById(String id) {
        return imageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     *  Finds the image entity with the given name in the database.
     * @param name the name of the ImageEntity you are looking for
     * @return ImageEntity
     */
    public Optional<ImageEntity> findByName(String name) {
        return imageRepository.findByName(name);
    }

    /**
     *  Finds all the image entities in the database and converts them into a DTO (ImageMeta) list.
     * @return List<ImageMeta>
     */
    public List<ImageMeta> findAll() {
        return imageRepository.findAll()
                .stream()
                .map(ImageMeta::new)
                .collect(Collectors.toList());
    }


    /**
     *  parameter: Multipart file
     *  sets the imageEntity instance field with the given values if it is a picture and its size is not bigger than 2MB
     *  else writes a warning message to the console
     * @param file (MultipartFile)
     * @throws IncorrectSignatureException check signService.signAndEncodeToBase64 description
     * @throws IncorrectKeyException check signService.signAndEncodeToBase64 description
     * @throws IncorrectAlgorithmException check signService.signAndEncodeToBase64 description
     * @throws IOException if file not found
     */
    public void saveImage(MultipartFile file) throws IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException, IOException {

        if (isSufficientSize(file) && isPicture(file)) {
            ImageEntity image = new ImageEntity();
            image.setId(UUID.randomUUID().toString());
            image.setName(file.getOriginalFilename());
            image.setMimeType(file.getContentType());
            image.setSize(file.getSize());
            image.setDigitalSign(signService.signAndEncodeToBase64(file));
            image.setImageData(file.getBytes());
            imageRepository.save(image);
            logger.info("Image saved successfully.");
        } else {
            logger.warn("The file format or file size does not met the expectations.");
        }
    }


    /**
     * isPicture()
     * Checks whether the file, you want to upload is a picture or not.
     * @param file (MultipartFile)
     * @return boolean
     */
    private boolean isPicture(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).toLowerCase().contains("image");
    }


    /**
     * Tests whether the size of the file is below the maximum limit or not.
     * This value can be edited in application.properties
     * @param file (MultipartFile)
     * @return boolean
     */
    private boolean isSufficientSize(MultipartFile file) {
        return file.getSize() < DataSize.parse(maxFileSize).toBytes();
    }

}
