package preevisiontosimulink.parser.elements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "KBL_container")
@XmlAccessorType(XmlAccessType.FIELD)
public class KBLContainer {
    @XmlElement(name = "Cartesian_point")
    private List<CartesianPoint> cartesianPoints;

    @XmlElement(name = "Node")
    private List<Node> nodes;

    // Getter und Setter für Cartesian Points
    public List<CartesianPoint> getCartesianPoints() {
        return cartesianPoints;
    }

    public void setCartesianPoints(List<CartesianPoint> cartesianPoints) {
        this.cartesianPoints = cartesianPoints;
    }

    // Getter und Setter für Nodes
    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}

