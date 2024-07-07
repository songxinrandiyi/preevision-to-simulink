package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Core {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Id")
	private String coreId;

	@XmlElement(name = "Wire_type")
	private String wireType;

	@XmlElement(name = "Cross_section_area")
	private CrossSectionArea crossSectionArea;

	@XmlElement(name = "Outside_diameter")
	private OutsideDiameter outsideDiameter;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLargeId() {
		return coreId;
	}

	public void setLargeId(String coreId) {
		this.coreId = coreId;
	}

	public String getWireType() {
		return wireType;
	}

	public void setWireType(String wireType) {
		this.wireType = wireType;
	}

	public CrossSectionArea getCrossSectionArea() {
		return crossSectionArea;
	}

	public void setCrossSectionArea(CrossSectionArea crossSectionArea) {
		this.crossSectionArea = crossSectionArea;
	}

	public OutsideDiameter getOutsideDiameter() {
		return outsideDiameter;
	}

	public void setOutsideDiameter(OutsideDiameter outsideDiameter) {
		this.outsideDiameter = outsideDiameter;
	}
}
