package preevisiontosimulink.parser.arxmlelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AUTOSAR")
@XmlAccessorType(XmlAccessType.FIELD)
public class AUTOSAR {

	@XmlElement(name = "AR-PACKAGES")
	private ArPackages arPackages;

	// Getters and setters for arPackages
	public ArPackages getArPackages() {
		return arPackages;
	}

	public void setArPackages(ArPackages arPackages) {
		this.arPackages = arPackages;
	}
}
