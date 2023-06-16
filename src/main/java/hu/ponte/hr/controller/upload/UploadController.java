package hu.ponte.hr.controller.upload;

import hu.ponte.hr.exceptions.IncorrectAlgorithmException;
import hu.ponte.hr.exceptions.IncorrectKeyException;
import hu.ponte.hr.exceptions.IncorrectSignatureException;
import hu.ponte.hr.services.ImageStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author tattila93
 * the OpenAPI descriptions will be available at the path /v3/api-docs by default:
 * http://localhost:8080/v3/api-docs/
 * http://localhost:8080/swagger-ui.html
 */
@Component
@RestController
@RequestMapping("api/file")
@Tag(name = "Image_upload")
public class UploadController {

    private final ImageStore imageStore;

    @Autowired
    public UploadController(ImageStore imageStore) {
        this.imageStore = imageStore;
    }

    @PostMapping("post")
    @Operation(summary = "Uploads the picture.", description = "Uploads the chosen picture if it is in image format, and not bigger as 2MB")
    public ResponseEntity<Void> handleFormUpload(@RequestParam("file") MultipartFile file) throws IncorrectSignatureException, IncorrectKeyException, IncorrectAlgorithmException, IOException {
        imageStore.saveImage(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
