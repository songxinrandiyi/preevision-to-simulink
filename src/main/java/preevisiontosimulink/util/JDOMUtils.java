package preevisiontosimulink.util;

import java.util.List;

import org.jdom2.Element;

public class JDOMUtils {

	public static void setCrossSectionArea(List<Element> generalWires, String id, Double value) {
		for (Element generalWire : generalWires) {
			if (generalWire.getAttributeValue("id").equals(id)) {
				Element crossSectionArea = generalWire.getChild("Cross_section_area");
				if (crossSectionArea != null) {
					Element valueComponent = crossSectionArea.getChild("Value_component");
					if (valueComponent != null) {
						valueComponent.setText(value.toString());
					}
				}
			}
		}
	}

	public static void setLength(List<Element> generalWireOccurrences, String id, Double value) {
		for (Element generalWireOccurrence : generalWireOccurrences) {
			if (generalWireOccurrence.getAttributeValue("id").equals(id)) {
				List<Element> lengthInformationList = generalWireOccurrence.getChildren("Length_information");
				if (!lengthInformationList.isEmpty()) {
					for (Element lengthInformation : lengthInformationList) {
						Element lengthValue = lengthInformation.getChild("Length_value");
						if (lengthValue != null) {
							Element valueComponent = lengthValue.getChild("Value_component");
							if (valueComponent != null) {
								valueComponent.setText(value.toString());
							}
						}
					}
				}
			}
		}
	}
}
