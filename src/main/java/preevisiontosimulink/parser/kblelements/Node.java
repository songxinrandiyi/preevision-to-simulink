package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Node")
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Id")
	private String nodeId;

	@XmlElement(name = "Cartesian_point")
	private String cartesianPoint;

	@XmlElement(name = "Referenced_components")
	private String referencedComponents;

	// Getter and Setter methods for all fields
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

	public String getCartesianPoint() {
		return cartesianPoint;
	}

	public void setCartesianPoint(String cartesianPoint) {
		this.cartesianPoint = cartesianPoint;
	}

	public String getReferencedComponents() {
		return referencedComponents;
	}

	public void setReferencedComponents(String referencedComponents) {
		this.referencedComponents = referencedComponents;
	}
}
