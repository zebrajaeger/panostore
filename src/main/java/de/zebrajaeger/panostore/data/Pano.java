package de.zebrajaeger.panostore.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Pano {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private PanoFile preview;

    @OneToMany(mappedBy = "pano", cascade = CascadeType.ALL)
    private List<PanoFile> panoFiles = new LinkedList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Config config;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PanoFile getPreview() {
        return preview;
    }

    public void setPreview(PanoFile preview) {
        this.preview = preview;
    }

    public List<PanoFile> getPanoFiles() {
        return panoFiles;
    }

    public void setPanoFiles(List<PanoFile> panoFiles) {
        this.panoFiles = panoFiles;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
