package de.zebrajaeger.panostore.krpano;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.IOException;

@JacksonXmlRootElement(localName = "krpano")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrPanoConfig {

    private KrPanoPreview preview;

    public static KrPanoConfig fromString(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml, KrPanoConfig.class);
    }

    public KrPanoPreview getPreview() {
        return preview;
    }

    public void setPreview(KrPanoPreview preview) {
        this.preview = preview;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
