package preevisiontosimulink.parser.kblelements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Connection {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Signal_name")
	private String signalName;

	@XmlElement(name = "Wire")
	private String wire;

	@XmlElement(name = "Extremities")
	private List<Extremity> extremities;

	// Getter und Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public String getWire() {
		return wire;
	}

	public void setWire(String wire) {
		this.wire = wire;
	}

	public List<Extremity> getExtremities() {
		return extremities;
	}

	public void setExtremities(List<Extremity> extremities) {
		this.extremities = extremities;
	}
}
