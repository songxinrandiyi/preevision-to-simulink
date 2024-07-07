package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CrossSectionAreaInformation {

	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Value_determination")
	private String valueDetermination;

	@XmlElement(name = "Area")
	private Area area;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValueDetermination() {
		return valueDetermination;
	}

	public void setValueDetermination(String valueDetermination) {
		this.valueDetermination = valueDetermination;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
