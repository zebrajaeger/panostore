package de.zebrajaeger.panostore.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.zebrajaeger.panostore.service.NoKrPanoConfigFound;
import de.zebrajaeger.panostore.service.PanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("api/upload")
public class UploadController {

    @Autowired
    private PanoService panoService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public @ResponseBody
    List<FileMeta> submit(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, NoKrPanoConfigFound {
        Iterator<String> itr = request.getFileNames();

        List<FileMeta> result = new LinkedList<>();
        while (itr.hasNext()) {
            MultipartFile mpf = request.getFile(itr.next());
            File tempFile = File.createTempFile("fileupload", "zip");
            try {
                mpf.transferTo(tempFile);
                panoService.importPano(tempFile);

                FileMeta fileMeta = new FileMeta();
                fileMeta.setFileName(mpf.getOriginalFilename());
                fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
                fileMeta.setFileType(mpf.getContentType());
                result.add(fileMeta);
            } finally {
                tempFile.delete();
            }
        }

        return result;
    }

    @JsonIgnoreProperties({"bytes"})
    public class FileMeta {

        private String fileName;
        private String fileSize;
        private String fileType;

        private byte[] bytes;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }
    }
}
