package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Unit")
@XmlAccessorType(XmlAccessType.FIELD)
public class Unit {

	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Unit_name")
	private String unitName;

	@XmlElement(name = "Si_unit_name")
	private String siUnitName;

	@XmlElement(name = "Si_prefix")
	private String siPrefix;

	@XmlElement(name = "Si_dimension")
	private String siDimension;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSiUnitName() {
		return siUnitName;
	}

	public void setSiUnitName(String siUnitName) {
		this.siUnitName = siUnitName;
	}

	public String getSiPrefix() {
		return siPrefix;
	}

	public void setSiPrefix(String siPrefix) {
		this.siPrefix = siPrefix;
	}

	public String getSiDimension() {
		return siDimension;
	}

	public void setSiDimension(String siDimension) {
		this.siDimension = siDimension;
	}
}
