package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Type")
public class Type {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Key")
    private String key;

    @XmlElement(name = "Reference_system")
    private String referenceSystem;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReferenceSystem() {
        return referenceSystem;
    }

    public void setReferenceSystem(String referenceSystem) {
        this.referenceSystem = referenceSystem;
    }
}
