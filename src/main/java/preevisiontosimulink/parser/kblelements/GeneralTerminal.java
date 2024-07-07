package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "General_wire")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralTerminal {
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
}
