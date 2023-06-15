package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.domain.ImageEntity;
import hu.ponte.hr.exceptions.IncorrectAlgorithmException;
import hu.ponte.hr.exceptions.IncorrectKeyException;
import hu.ponte.hr.exceptions.IncorrectSignatureException;
import hu.ponte.hr.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ExtendWith(MockitoExtension.class)
class ImageStoreTest {

    private ImageStore imageStore;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Mock
    private SignService signService;

    @Mock
    private ImageRepository imageRepository;

    ImageStoreTest() throws IOException {
    }

    @BeforeEach
    public void init() {
        imageStore = new ImageStore(imageRepository, signService, maxFileSize);

    }

    Map<String, String> files = new LinkedHashMap<String, String>() {
        {
            put("cat.jpg", "XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==");
            put("enhanced-buzz.jpg", "tsLUqkUtzqgeDMuXJMt1iRCgbiVw13FlsBm2LdX2PftvnlWorqxuVcmT0QRKenFMh90kelxXnTuTVOStU8eHRLS3P1qOLH6VYpzCGEJFQ3S2683gCmxq3qc0zr5kZV2VcgKWm+wKeMENyprr8HNZhLPygtmzXeN9u6BpwUO9sKj7ImBvvv/qZ/Tht3hPbm5SrDK4XG7G0LVK9B8zpweXT/lT8pqqpYx4/h7DyE+L5bNHbtkvcu2DojgJ/pNg9OG+vTt/DfK7LFgCjody4SvZhSbLqp98IAaxS9BT6n0Ozjk4rR1l75QP5lbJbpQ9ThAebXQo+Be4QEYV/YXf07WXTQ==");
            put("rnd.jpg", "lM6498PalvcrnZkw4RI+dWceIoDXuczi/3nckACYa8k+KGjYlwQCi1bqA8h7wgtlP3HFY37cA81ST9I0X7ik86jyAqhhc7twnMUzwE/+y8RC9Xsz/caktmdA/8h+MlPNTjejomiqGDjTGvLxN9gu4qnYniZ5t270ZbLD2XZbuTvUAgna8Cz4MvdGTmE3MNIA5iavI1p+1cAN+O10hKwxoVcdZ2M3f7/m9LYlqEJgMnaKyI/X3m9mW0En/ac9fqfGWrxAhbhQDUB0GVEl7WBF/5ODvpYKujHmBAA0ProIlqA3FjLTLJ0LGHXyDgrgDfIG/EDHVUQSdLWsM107Cg6hQg==");
        }
    };

    MockMultipartFile cat = new MockMultipartFile("cat", "cat.jpg",
            "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/cat.jpg")));
    MockMultipartFile enhancedBuzz = new MockMultipartFile("enhanced-buzz", "enhanced-buzz.jpg",
            "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/enhanced-buzz.jpg")));
    MockMultipartFile rnd = new MockMultipartFile("rnd", "rnd.jpg",
            "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/rnd.jpg")));

    @Test
    void findByNameTest() throws IOException, IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException {
        ImageEntity rndImage = new ImageEntity();

        rndImage.setName(rnd.getOriginalFilename());
        rndImage.setMimeType(rnd.getContentType());
        rndImage.setSize(rnd.getSize());
        rndImage.setDigitalSign(signService.signAndEncodeToBase64(rnd));
        rndImage.setImageData(rnd.getBytes());

        when(imageRepository.findByName(rndImage.getName())).thenReturn(Optional.of(rndImage));
        assertTrue(imageStore.findByName(rndImage.getName()).isPresent());
    }

    @Test
    void findAllTest() throws IOException, IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException {

        ImageEntity catImage = new ImageEntity();
        ImageEntity enhancedBuzzImage = new ImageEntity();
        ImageEntity rndImage = new ImageEntity();


        catImage.setName(cat.getOriginalFilename());
        catImage.setMimeType(cat.getContentType());
        catImage.setSize(cat.getSize());
        catImage.setDigitalSign(signService.signAndEncodeToBase64(cat));
        catImage.setImageData(cat.getBytes());

        enhancedBuzzImage.setName(enhancedBuzz.getOriginalFilename());
        enhancedBuzzImage.setMimeType(enhancedBuzz.getContentType());
        enhancedBuzzImage.setSize(enhancedBuzz.getSize());
        enhancedBuzzImage.setDigitalSign(signService.signAndEncodeToBase64(enhancedBuzz));
        enhancedBuzzImage.setImageData(enhancedBuzz.getBytes());

        rndImage.setName(rnd.getOriginalFilename());
        rndImage.setMimeType(rnd.getContentType());
        rndImage.setSize(rnd.getSize());
        rndImage.setDigitalSign(signService.signAndEncodeToBase64(rnd));
        rndImage.setImageData(rnd.getBytes());


        List<ImageEntity> imageEntityList = List.of(catImage, enhancedBuzzImage, rndImage);
        when(imageRepository.findAll()).thenReturn(imageEntityList);

        List<ImageMeta> imageMetaList = imageStore.findAll();
        assertEquals(3, imageMetaList.size());

    }

    @Test
    void saveImageTest() throws IOException, IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException {
        when(signService.signAndEncodeToBase64(any(MultipartFile.class))).thenReturn(files.get("enhanced-buzz.jpg"));
        imageStore.saveImage(enhancedBuzz);
        verify(imageRepository, times(1)).save(any(ImageEntity.class));
    }
}
