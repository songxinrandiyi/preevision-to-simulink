package preevisiontosimulink.parser.kblelements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Connection {
	private String name = null;
	private Double resistance = null;
	private Double length = null;
	private Double crossSectionArea = null;
	private Integer startPin = null;
	private Integer endPin = null;
    private ConnectorOccurrence startConnector = null;
    private ConnectorOccurrence endConnector = null;
    private GeneralWireOccurrence generalWireOccurrence = null;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Double getResistance() {
        return resistance;
    }
	
	public void setResistance(Double resistance) {
        this.resistance = resistance;
    }

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getCrossSectionArea() {
		return crossSectionArea;
	}

	public void setCrossSectionArea(Double crossSectionArea) {
		this.crossSectionArea = crossSectionArea;
	}

	public Integer getStartPin() {
		return startPin;
	}

	public void setStartPin(Integer startPin) {
		this.startPin = startPin;
	}

	public Integer getEndPin() {
		return endPin;
	}

	public void setEndPin(Integer endPin) {
		this.endPin = endPin;
	}

	public ConnectorOccurrence getStartConnector() {
        return startConnector;
    }

    public void setStartConnector(ConnectorOccurrence startConnector) {
        this.startConnector = startConnector;
    }

    public ConnectorOccurrence getEndConnector() {
        return endConnector;
    }

    public void setEndConnector(ConnectorOccurrence endConnector) {
        this.endConnector = endConnector;
    }
    
	public GeneralWireOccurrence getGeneralWireOccurrence() {
		return generalWireOccurrence;
	}

	public void setGeneralWireOccurrence(GeneralWireOccurrence generalWireOccurrence) {
		this.generalWireOccurrence = generalWireOccurrence;
	}

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
