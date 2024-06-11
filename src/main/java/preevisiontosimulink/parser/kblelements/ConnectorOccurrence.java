package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Connector_occurrence")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectorOccurrence {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Id")
	private String connectorId;

	@XmlElement(name = "Part")
	private String part;

	@XmlElement(name = "Contact_points")
	private List<ContactPoint> contactPoints;

	@XmlElement(name = "Slots")
	private Slots slots;

	// Getter und Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLargeId() {
		return connectorId;
	}

	public void setLargeId(String connectorId) {
		this.connectorId = connectorId;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public List<ContactPoint> getContactPoints() {
		return contactPoints;
	}

	public void setContactPoints(List<ContactPoint> contactPoints) {
		this.contactPoints = contactPoints;
	}

	public Slots getSlots() {
		return slots;
	}

	public void setSlot(Slots slot) {
		this.slots = slot;
	}
}
