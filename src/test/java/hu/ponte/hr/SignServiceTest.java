package hu.ponte.hr;


import hu.ponte.hr.exceptions.IncorrectAlgorithmException;
import hu.ponte.hr.exceptions.IncorrectKeyException;
import hu.ponte.hr.exceptions.IncorrectSignatureException;
import hu.ponte.hr.services.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is for testing SignService class.
 * @author tattila93
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    private SignService signService;

    @BeforeEach
    public void init(){
        signService = new SignService();
    }

    Map<String, String> files = new LinkedHashMap<String, String>() {
        {
            put("cat.jpg","XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==");
            put("enhanced-buzz.jpg","tsLUqkUtzqgeDMuXJMt1iRCgbiVw13FlsBm2LdX2PftvnlWorqxuVcmT0QRKenFMh90kelxXnTuTVOStU8eHRLS3P1qOLH6VYpzCGEJFQ3S2683gCmxq3qc0zr5kZV2VcgKWm+wKeMENyprr8HNZhLPygtmzXeN9u6BpwUO9sKj7ImBvvv/qZ/Tht3hPbm5SrDK4XG7G0LVK9B8zpweXT/lT8pqqpYx4/h7DyE+L5bNHbtkvcu2DojgJ/pNg9OG+vTt/DfK7LFgCjody4SvZhSbLqp98IAaxS9BT6n0Ozjk4rR1l75QP5lbJbpQ9ThAebXQo+Be4QEYV/YXf07WXTQ==");
            put("rnd.jpg","lM6498PalvcrnZkw4RI+dWceIoDXuczi/3nckACYa8k+KGjYlwQCi1bqA8h7wgtlP3HFY37cA81ST9I0X7ik86jyAqhhc7twnMUzwE/+y8RC9Xsz/caktmdA/8h+MlPNTjejomiqGDjTGvLxN9gu4qnYniZ5t270ZbLD2XZbuTvUAgna8Cz4MvdGTmE3MNIA5iavI1p+1cAN+O10hKwxoVcdZ2M3f7/m9LYlqEJgMnaKyI/X3m9mW0En/ac9fqfGWrxAhbhQDUB0GVEl7WBF/5ODvpYKujHmBAA0ProIlqA3FjLTLJ0LGHXyDgrgDfIG/EDHVUQSdLWsM107Cg6hQg==");
        }
    };

    /**
     * Tests the signing of cat.jpg
     * @throws IOException if file not found.
     * @throws IncorrectSignatureException check signService.signAndEncodeToBase64() description
     * @throws IncorrectKeyException check signService.signAndEncodeToBase64() description
     * @throws IncorrectAlgorithmException check signService.signAndEncodeToBase64() description
     */
    @Test
    void signAndEncodeToBase64Test1() throws IOException, IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException {
        MockMultipartFile catImage = new MockMultipartFile("file", "cat.jpg", "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/cat.jpg")));
        assertEquals(files.get("cat.jpg"), signService.signAndEncodeToBase64(catImage));

    }

    /**
     * Tests the signing of enhanced-buzz.jpg
     * @throws IOException if file not found.
     * @throws IncorrectSignatureException check signService.signAndEncodeToBase64() description
     * @throws IncorrectKeyException check signService.signAndEncodeToBase64() description
     * @throws IncorrectAlgorithmException check signService.signAndEncodeToBase64() description
     */
    @Test
    void signAndEncodeToBase64Test2() throws IOException, IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException {
        MockMultipartFile enhancedBuzzImage = new MockMultipartFile("file", "enhanced-buzz.jpg", "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/enhanced-buzz.jpg")));
        assertEquals(files.get("enhanced-buzz.jpg"), signService.signAndEncodeToBase64(enhancedBuzzImage));
    }

    /**
     * Tests the signing of rnd.jpg
     * @throws IOException if file not found.
     * @throws IncorrectSignatureException check signService.signAndEncodeToBase64() description
     * @throws IncorrectKeyException check signService.signAndEncodeToBase64() description
     * @throws IncorrectAlgorithmException check signService.signAndEncodeToBase64() description
     */
    @Test
    void signAndEncodeToBase64Test3() throws IOException, IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException {
        MockMultipartFile rndImage = new MockMultipartFile("file", "rnd.jpg", "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/rnd.jpg")));
        assertEquals(files.get("rnd.jpg"), signService.signAndEncodeToBase64(rndImage));
    }

}
