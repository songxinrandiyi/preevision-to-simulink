package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Routing")
@XmlAccessorType(XmlAccessType.FIELD)
public class Routing {

	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Routed_wire")
	private String routedWire;

	@XmlElement(name = "Segments")
	private String segments;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoutedWire() {
		return routedWire;
	}

	public void setRoutedWire(String routedWire) {
		this.routedWire = routedWire;
	}

	public String getSegments() {
		return segments;
	}

	public void setSegments(String segments) {
		this.segments = segments;
	}

	// Method to check if the segments contain a specific ID
	public boolean containsSegmentId(String id) {
		if (segments != null && !segments.isEmpty()) {
			String[] ids = segments.split("\\s+");
			for (String segmentId : ids) {
				if (segmentId.equals(id)) {
					return true;
				}
			}
		}
		return false;
	}
}
