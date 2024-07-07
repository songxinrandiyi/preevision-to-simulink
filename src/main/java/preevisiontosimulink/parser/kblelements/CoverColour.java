package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CoverColour {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Colour_type")
	private String colourType;

	@XmlElement(name = "Colour_value")
	private String colourValue;

	// Getter and Setter methods for all fields
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getColourType() {
		return colourType;
	}

	public void setColourType(String colourType) {
		this.colourType = colourType;
	}

	public String getColourValue() {
		return colourValue;
	}

	public void setColourValue(String colourValue) {
		this.colourValue = colourValue;
	}
}
