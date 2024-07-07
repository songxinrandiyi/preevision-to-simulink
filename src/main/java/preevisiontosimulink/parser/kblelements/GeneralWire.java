package preevisiontosimulink.parser.kblelements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "General_wire")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralWire {
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

	@XmlElement(name = "Wire_type")
	private String wireType;

	@XmlElement(name = "Cross_section_area")
	private CrossSectionArea crossSectionArea;

	@XmlElement(name = "Outside_diameter")
	private OutsideDiameter outsideDiameter;

	@XmlElement(name = "Cover_colour")
	private CoverColour coverColour;

	@XmlElement(name = "Core")
	private List<Core> cores;

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

	public MassInformation getMassInformation() {
		return massInformation;
	}

	public void setMassInformation(MassInformation massInformation) {
		this.massInformation = massInformation;
	}

	public String getWireType() {
		return wireType;
	}

	public void setWireType(String wireType) {
		this.wireType = wireType;
	}

	public CrossSectionArea getCrossSectionArea() {
		return crossSectionArea;
	}

	public void setCrossSectionArea(CrossSectionArea crossSectionArea) {
		this.crossSectionArea = crossSectionArea;
	}

	public OutsideDiameter getOutsideDiameter() {
		return outsideDiameter;
	}

	public void setOutsideDiameter(OutsideDiameter outsideDiameter) {
		this.outsideDiameter = outsideDiameter;
	}

	public CoverColour getCoverColour() {
		return coverColour;
	}

	public void setCoverColour(CoverColour coverColour) {
		this.coverColour = coverColour;
	}
}
