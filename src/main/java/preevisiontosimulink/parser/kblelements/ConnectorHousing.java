package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Connector_housing")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectorHousing {
    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Part_number")
    private String partNumber;

    @XmlElement(name = "Company_name")
    private String companyName;

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "Abbreviation")
    private String abbreviation;

    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "Mass_information")
    private MassInformation massInformation;

    @XmlElement(name = "Slots")
    private Slots slots;

    // Getter and Setter methods for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MassInformation getMassInformation() {
        return massInformation;
    }

    public void setMassInformation(MassInformation massInformation) {
        this.massInformation = massInformation;
    }

    public Slots getSlots() {
        return slots;
    }

    public void setSlots(Slots slots) {
        this.slots = slots;
    }
}
