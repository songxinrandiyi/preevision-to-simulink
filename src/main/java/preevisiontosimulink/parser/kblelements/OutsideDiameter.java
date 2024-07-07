package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OutsideDiameter {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Unit_component")
	private String unitComponent;

	@XmlElement(name = "Value_component")
	private String valueComponent;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitComponent() {
		return unitComponent;
	}

	public void setUnitComponent(String unitComponent) {
		this.unitComponent = unitComponent;
	}

	public String getValueComponent() {
		return valueComponent;
	}

	public void setValueComponent(String valueComponent) {
		this.valueComponent = valueComponent;
	}
}
