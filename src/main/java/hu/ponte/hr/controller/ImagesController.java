package hu.ponte.hr.controller;


import hu.ponte.hr.services.ImageStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


//  the OpenAPI descriptions will be available at the path /v3/api-docs by default:
//  http://localhost:8080/v3/api-docs/
//  http://localhost:8080/swagger-ui.html


@RestController()
@RequestMapping("api/images")
@Tag(name = "Image")
public class ImagesController {


    private final ImageStore imageStore;

    @Autowired
    public ImagesController(ImageStore imageStore) {
        this.imageStore = imageStore;
    }


    @GetMapping("meta")
    @Operation(summary = "Lists images.", description = "Lists all the uploaded images.")
    public ResponseEntity<List<ImageMeta>> listImages() {
		List<ImageMeta> imageMetaList = imageStore.findAll();
        return new ResponseEntity<>(imageMetaList, HttpStatus.OK);

    }

    @GetMapping("preview/{id}")
    @Operation(summary = "Image information.", description = "Shows the information of the pictures." )
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        byte[] byteData = imageStore.findById(id).getImageData();
        response.getOutputStream().write(byteData);
	}

}
