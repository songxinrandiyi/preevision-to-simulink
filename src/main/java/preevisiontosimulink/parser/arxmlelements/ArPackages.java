package preevisiontosimulink.parser.arxmlelements;

import javax.xml.bind.annotation.*;
import java.util.List;

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
