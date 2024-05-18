package preevisiontosimulink.parser.arxmlelements;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "AR-PACKAGE")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArPackage {
    @XmlAttribute(name = "UUID")
    private String uuid;

    @XmlElement(name = "SHORT-NAME")
    private String shortName;
    
    @XmlElement(name = "AR-PACKAGES")
    private ArPackages arPackages;

    // Getters and setters

    public ArPackages getArPackages() {
		return arPackages;
	}

	public void setArPackages(ArPackages arPackages) {
		this.arPackages = arPackages;
	}

	public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
