package de.zebrajaeger.panostore.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Config {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private PanoFile panoFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PanoFile getPanoFile() {
        return panoFile;
    }

    public void setPanoFile(PanoFile panoFile) {
        this.panoFile = panoFile;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
