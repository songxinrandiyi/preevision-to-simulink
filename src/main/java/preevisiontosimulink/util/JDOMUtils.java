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

	public static void setGeneralWirePartNumber(List<Element> generalWires, String id, String partNumberValue) {
		for (Element generalWire : generalWires) {
			if (generalWire.getAttributeValue("id").equals(id)) {
				Element partNumber = generalWire.getChild("Part_number");
				if (partNumber != null) {
					partNumber.setText(partNumberValue);
				}
			}
		}
	}
}
