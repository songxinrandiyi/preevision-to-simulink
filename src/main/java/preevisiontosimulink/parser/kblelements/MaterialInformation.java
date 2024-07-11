package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MaterialInformation {

	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Material_key")
	private String materialKey;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMaterialKey() {
		return materialKey;
	}

	public void setMaterialKey(String materialKey) {
		this.materialKey = materialKey;
	}
}
