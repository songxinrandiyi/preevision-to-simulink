package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Extremity {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Position_on_wire")
	private double positionOnWire;

	@XmlElement(name = "Contact_point")
	private String contactPoint;

	// Getter und Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPositionOnWire() {
		return positionOnWire;
	}

	public void setPositionOnWire(double positionOnWire) {
		this.positionOnWire = positionOnWire;
	}

	public String getContactPoint() {
		return contactPoint;
	}

	public void setContactPoint(String contactPoint) {
		this.contactPoint = contactPoint;
	}
}
