package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContactPoint {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Id")
	private String contactPointId;

	@XmlElement(name = "Associated_parts")
	private String associatedParts;

	@XmlElement(name = "Contacted_cavity")
	private String contactedCavity;

	// Getter und Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContactPointId() {
		return contactPointId;
	}

	public void setContactPointId(String contactPointId) {
		this.contactPointId = contactPointId;
	}

	public String getAssociatedParts() {
		return associatedParts;
	}

	public void setAssociatedParts(String associatedParts) {
		this.associatedParts = associatedParts;
	}

	public String getContactedCavity() {
		return contactedCavity;
	}

	public void setContactedCavity(String contactedCavity) {
		this.contactedCavity = contactedCavity;
	}
}
