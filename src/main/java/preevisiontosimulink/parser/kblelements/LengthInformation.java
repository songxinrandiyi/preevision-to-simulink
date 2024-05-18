package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class LengthInformation {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Length_type")
    private String lengthType;

    @XmlElement(name = "Length_value")
    private LengthValue lengthValue;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLengthType() {
        return lengthType;
    }

    public void setLengthType(String lengthType) {
        this.lengthType = lengthType;
    }

    public LengthValue getLengthValue() {
        return lengthValue;
    }

    public void setLengthValue(LengthValue lengthValue) {
        this.lengthValue = lengthValue;
    }
}
