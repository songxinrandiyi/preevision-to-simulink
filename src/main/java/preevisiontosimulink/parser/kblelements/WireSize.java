package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class WireSize {

	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Unit_component")
	private String unitComponent;

	@XmlElement(name = "Minimum")
	private double minimum;

	@XmlElement(name = "Maximum")
	private double maximum;

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

	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}
}
