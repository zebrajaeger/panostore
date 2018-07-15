package de.zebrajaeger.panostore.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.nio.charset.StandardCharsets;

@Entity
@Table(indexes = {
        @Index(name = "idx_filename", columnList = "pano_id,fileName", unique = true)
})
public class PanoFile {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "pano_id")
    private Pano pano;

    private byte[] content;

    public String contentAsString() {
        if (content == null) {
            return null;
        }

        return new String(content, StandardCharsets.UTF_8);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Pano getPano() {
        return pano;
    }

    public void setPano(Pano pano) {
        this.pano = pano;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
