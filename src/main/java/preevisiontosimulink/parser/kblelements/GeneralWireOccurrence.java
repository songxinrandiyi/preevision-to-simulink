package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "General_wire_occurrence")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralWireOccurrence {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Part")
    private String part;

    @XmlElement(name = "Length_information")
    private LengthInformation lengthInformation;

    @XmlElement(name = "Wire_number")
    private int wireNumber;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public LengthInformation getLengthInformation() {
        return lengthInformation;
    }

    public void setLengthInformation(LengthInformation lengthInformation) {
        this.lengthInformation = lengthInformation;
    }

    public int getWireNumber() {
        return wireNumber;
    }

    public void setWireNumber(int wireNumber) {
        this.wireNumber = wireNumber;
    }
}

