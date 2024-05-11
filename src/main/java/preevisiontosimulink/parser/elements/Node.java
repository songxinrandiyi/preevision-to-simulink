package preevisiontosimulink.parser.elements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {
    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Id")
    private String nodeId;

    @XmlElement(name = "Cartesian_point")
    private String cartesianPointId;

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getCartesianPointId() {
        return cartesianPointId;
    }

    public void setCartesianPointId(String cartesianPointId) {
        this.cartesianPointId = cartesianPointId;
    }
}

