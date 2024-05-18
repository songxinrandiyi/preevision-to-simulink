package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleConfiguration {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Logistic_control_information")
    private String logisticControlInformation;

    @XmlElement(name = "Configuration_type")
    private String configurationType;

    @XmlElement(name = "Controlled_components")
    private String controlledComponents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogisticControlInformation() {
		return logisticControlInformation;
	}

	public void setLogisticControlInformation(String logisticControlInformation) {
		this.logisticControlInformation = logisticControlInformation;
	}

	public String getConfigurationType() {
		return configurationType;
	}

	public void setConfigurationType(String configurationType) {
		this.configurationType = configurationType;
	}

	public String getControlledComponents() {
		return controlledComponents;
	}

	public void setControlledComponents(String controlledComponents) {
		this.controlledComponents = controlledComponents;
	}

    public boolean containsId(String id) {
        if (controlledComponents != null && !controlledComponents.isEmpty()) {
            String[] ids = controlledComponents.split("\\s+");
            for (String component : ids) {
                if (component.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }
}