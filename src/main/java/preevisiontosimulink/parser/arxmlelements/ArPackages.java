package preevisiontosimulink.parser.arxmlelements;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AR-PACKAGES")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArPackages {
	@XmlElement(name = "AR-PACKAGE")
	private List<ArPackage> arPackages;

	// Getters and setters

	public List<ArPackage> getArPackages() {
		return arPackages;
	}

	public void setArPackages(List<ArPackage> arPackages) {
		this.arPackages = arPackages;
	}
}
