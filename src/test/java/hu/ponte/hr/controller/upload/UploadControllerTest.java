package hu.ponte.hr.controller.upload;

import hu.ponte.hr.services.ImageStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author tattila93
 * This class is for testing UploadController class
 */

@RunWith(SpringRunner.class)
@SpringBootTest()
@ExtendWith(MockitoExtension.class)
class UploadControllerTest {

    private UploadController uploadController;
    @Mock
    private ImageStore imageStoreMock;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        uploadController = new UploadController(imageStoreMock);
        mockMvc = MockMvcBuilders
                .standaloneSetup(uploadController)
                .build();
    }

    /**
     * Tests the /api/file/post call with cat.jpg image can be uploaded or not.
     * @throws Exception if it can't find the image on the given path.
     */
    @Test
    void handleFormUploadTest() throws Exception {
        MockMultipartFile cat = new MockMultipartFile("file", "cat.jpg",
                "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/cat.jpg")));

        this.mockMvc.perform(multipart("/api/file/post")
                        .file(cat))
                        .andExpect(status().is(200));

        verify(imageStoreMock, times(1)).saveImage(any(MultipartFile.class));
    }

}


