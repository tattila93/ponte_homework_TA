package hu.ponte.hr.controller;


import hu.ponte.hr.domain.ImageEntity;
import hu.ponte.hr.exceptions.IncorrectAlgorithmException;
import hu.ponte.hr.exceptions.IncorrectKeyException;
import hu.ponte.hr.exceptions.IncorrectSignatureException;
import hu.ponte.hr.services.ImageStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author tattila93
 * This class is for testing ImagesController class
 */
@ExtendWith(MockitoExtension.class)
class ImagesControllerTest {

    private ImagesController imagesController;
    @Mock
    private ImageStore imageStoreMock;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        imagesController = new ImagesController(imageStoreMock);
        mockMvc = MockMvcBuilders
                .standaloneSetup(imagesController)
                .build();
    }

    /**
     * Tests api/images/meta call can find all the entities in the database or not.
     * @throws Exception MockMvc.perform
     */
    @Test
    void listImagesTest() throws Exception {
        ImageEntity image = new ImageEntity("1", "a.jpg", "image/jpeg", 1, null, new byte[]  {0, 1});
        ImageMeta imageMeta = new ImageMeta(image);
        ImageEntity image2 = new ImageEntity("2", "b.jpg", "image2/jpeg", 2, null, new byte[]  {1, 0});
        ImageMeta imageMeta2 = new ImageMeta(image2);

        List<ImageMeta> imageList = List.of(imageMeta, imageMeta2);

        when(imageStoreMock.findAll()).thenReturn(imageList);
        this.mockMvc.perform(
                        get("/api/images/meta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[1].id", is("2")));


        verify(imageStoreMock, times(1)).findAll();
    }

    /**
     * Test /api/images/preview/randomId call if the picture can be found in the database or not.
     * @throws Exception in case of Files.readAllBytes or rnd.getBytes() has a problem.
     */
    @Test
    void getImageTest() throws Exception {
        MockMultipartFile rnd = new MockMultipartFile("rnd", "rnd.jpg",
                "image/jpeg", Files.readAllBytes(Path.of("src/test/resources/images/rnd.jpg")));

        ImageEntity rndImage = new ImageEntity();

        rndImage.setName(rnd.getOriginalFilename());
        rndImage.setMimeType(rnd.getContentType());
        rndImage.setSize(rnd.getSize());
        rndImage.setDigitalSign("randomString");
        rndImage.setImageData(rnd.getBytes());

        when(imageStoreMock.findById(anyString())).thenReturn(rndImage);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        get("/api/images/preview/randomId")
                )
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(rndImage.getImageData().length, mvcResult.getResponse().getContentAsByteArray().length);

        verify(imageStoreMock, times(1)).findById(anyString());

    }
}
