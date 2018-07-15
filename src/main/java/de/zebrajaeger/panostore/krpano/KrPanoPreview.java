package de.zebrajaeger.panostore.krpano;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class KrPanoPreview {
    @JacksonXmlProperty(isAttribute = true)
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
