package de.zebrajaeger.panostore.controller;

import de.zebrajaeger.panostore.data.Pano;
import de.zebrajaeger.panostore.data.PanoFile;
import de.zebrajaeger.panostore.repo.PanoFileRepository;
import de.zebrajaeger.panostore.repo.PanoRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private PanoRepository panoRepository;
    @Autowired
    private PanoFileRepository panoFileRepository;

    @GetMapping("{panoId}/preview/")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable long panoId) {
        Optional<Pano> pano = panoRepository.findById(panoId);
        if (pano.isPresent()) {
            PanoFile preview = pano.get().getPreview();
            if (preview != null) {
                return sendFile(preview);
            }
        }

        return notFound();
    }

    @GetMapping("{panoId}/**")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable long panoId, HttpServletRequest request) {

        Optional<Pano> pano = panoRepository.findById(panoId);
        if (pano.isPresent()) {
            String[] parts = StringUtils.split(request.getRequestURI(), '/');
            parts = ArrayUtils.removeAll(parts, 0, 1);
            String file = StringUtils.join(parts, '/');
            Optional<PanoFile> panoFile = panoFileRepository.findByPanoAndFileName(pano.get(), file);
            if (panoFile.isPresent()) {
                return sendFile(panoFile.get());
            }
        }
        return notFound();
    }

    private ResponseEntity<ByteArrayResource> notFound() {
        return ResponseEntity
                .notFound()
                .build();
    }


    private ResponseEntity<ByteArrayResource> sendFile(PanoFile preview) {
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(preview.getFileName());
        ByteArrayResource resource = new ByteArrayResource(preview.getContent());

        return ResponseEntity
                .ok()
                .contentLength(preview.getContent().length)
                .contentType(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
    }
}