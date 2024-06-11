package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Slots {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Id")
	private String largeId;

	@XmlElement(name = "Part")
	private String part;

	@XmlElement(name = "Number_of_cavities")
	private int numberOfCavities;

	@XmlElement(name = "Cavities")
	private List<Cavity> cavities;

	// Getter and Setter for id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// Getter and Setter for Part
	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	// Getter and Setter for Id
	public String getLargeId() {
		return largeId;
	}

	public void setLargeId(String id) {
		this.largeId = id;
	}

	// Getter and Setter for numberOfCavities
	public int getNumberOfCavities() {
		return numberOfCavities;
	}

	public void setNumberOfCavities(int numberOfCavities) {
		this.numberOfCavities = numberOfCavities;
	}

	// Getter and Setter for cavities
	public List<Cavity> getCavities() {
		return cavities;
	}

	public void setCavities(List<Cavity> cavities) {
		this.cavities = cavities;
	}
}
