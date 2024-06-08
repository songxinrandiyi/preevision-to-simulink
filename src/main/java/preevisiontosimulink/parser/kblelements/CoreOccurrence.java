package preevisiontosimulink.parser.kblelements;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class CoreOccurrence {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Wire_number")
    private String wireNumber;

    @XmlElement(name = "Part")
    private String part;

    @XmlElement(name = "Length_information")
    private List<LengthInformation> lengthInformation;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWireNumber() {
        return wireNumber;
    }

    public void setWireNumber(String wireNumber) {
        this.wireNumber = wireNumber;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public List<LengthInformation> getLengthInformation() {
        return lengthInformation;
    }

    public void setLengthInformation(List<LengthInformation> lengthInformation) {
        this.lengthInformation = lengthInformation;
    }
}
