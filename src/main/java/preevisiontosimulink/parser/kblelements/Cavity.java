package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Cavities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cavity {
    @XmlAttribute(name = "id")
    private String id;
	
    @XmlElement(name = "Cavity_number")
    private int cavityNumber;

    // Getter and Setter for Cavity Number
    public int getCavityNumber() {
        return cavityNumber;
    }

    public void setCavityNumber(int cavityNumber) {
        this.cavityNumber = cavityNumber;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
