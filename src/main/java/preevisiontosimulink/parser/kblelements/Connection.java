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
	private Integer startPin = null;
	private Integer endPin = null;
	private ConnectorOccurrence startConnector = null;
	private ConnectorOccurrence endConnector = null;
	String startConnectorName = null;
	String endConnectorName = null;
	private GeneralWireOccurrence generalWireOccurrence = null;
	
	private Double crossSectionArea = null;
	private Double thicknessIso = null;
	private Double current = null;
	private String material = null;
	
	public boolean isValid() {
		return getStartConnector() != null && getEndConnector() != null
				&& getStartPin() != null && getEndPin() != null;
	}
		
	public String getStartConnectorName() {
		return startConnectorName;
	}

	public void setStartConnectorName(String startConnectorName) {
		this.startConnectorName = startConnectorName;
	}

	public String getEndConnectorName() {
		return endConnectorName;
	}

	public void setEndConnectorName(String endConnectorName) {
		this.endConnectorName = endConnectorName;
	}

	public Double getCrossSectionArea() {
		return crossSectionArea;
	}

	public void setCrossSectionArea(Double crossSectionArea) {
		this.crossSectionArea = crossSectionArea;
	}
	
    public Double getThicknessIso() {
        return thicknessIso;
    }
    
    public void setThicknessIso(Double thicknessIso) {
        this.thicknessIso = thicknessIso;
    }
    
    public Double getCurrent() {
        return current;
    }
    
    public void setCurrent(Double current) {
        this.current = current;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public void setMaterial(String material) {
        this.material = material;
    }

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
