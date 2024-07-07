package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Cavity_seal")
@XmlAccessorType(XmlAccessType.FIELD)
public class CavitySeal {

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

    @XmlElement(name = "Predecessor_part_number")
    private String predecessorPartNumber;

    @XmlElement(name = "Degree_of_maturity")
    private String degreeOfMaturity;

    @XmlElement(name = "Copyright_note")
    private String copyrightNote;

    @XmlElement(name = "Colour")
    private String colour;

    @XmlElement(name = "Seal_type")
    private String sealType;

    @XmlElement(name = "Wire_size")
    private WireSize wireSize;

    // Getters and setters
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

    public String getPredecessorPartNumber() {
        return predecessorPartNumber;
    }

    public void setPredecessorPartNumber(String predecessorPartNumber) {
        this.predecessorPartNumber = predecessorPartNumber;
    }

    public String getDegreeOfMaturity() {
        return degreeOfMaturity;
    }

    public void setDegreeOfMaturity(String degreeOfMaturity) {
        this.degreeOfMaturity = degreeOfMaturity;
    }

    public String getCopyrightNote() {
        return copyrightNote;
    }

    public void setCopyrightNote(String copyrightNote) {
        this.copyrightNote = copyrightNote;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getSealType() {
        return sealType;
    }

    public void setSealType(String sealType) {
        this.sealType = sealType;
    }

    public WireSize getWireSize() {
        return wireSize;
    }

    public void setWireSize(WireSize wireSize) {
        this.wireSize = wireSize;
    }
}
