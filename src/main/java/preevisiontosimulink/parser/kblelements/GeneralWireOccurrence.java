package preevisiontosimulink.parser.kblelements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "General_wire_occurrence")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralWireOccurrence {

	@XmlAttribute(name = "id")
	private String id;

	@XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
	private String type;

	@XmlElement(name = "Part")
	private String part;

	@XmlElement(name = "Length_information")
	private List<LengthInformation> lengthInformation;

	@XmlElement(name = "Core_occurrence")
	private List<CoreOccurrence> coreOccurrences;

	public List<CoreOccurrence> getCoreOccurrences() {
		return coreOccurrences;
	}

	public void setCoreOccurrences(List<CoreOccurrence> coreOccurrences) {
		this.coreOccurrences = coreOccurrences;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "Wire_number")
	private Integer wireNumber;

	@XmlElement(name = "Special_wire_id")
	private String specialWireId;

	public String getSpecialWireId() {
		return specialWireId;
	}

	public void setSpecialWireId(String specialWireId) {
		this.specialWireId = specialWireId;
	}

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

	public List<LengthInformation> getLengthInformation() {
		return lengthInformation;
	}

	public void setLengthInformation(List<LengthInformation> lengthInformation) {
		this.lengthInformation = lengthInformation;
	}

	public Integer getWireNumber() {
		return wireNumber;
	}

	public void setWireNumber(Integer wireNumber) {
		this.wireNumber = wireNumber;
	}
}
