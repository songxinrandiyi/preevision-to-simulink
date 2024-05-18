package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "KBL_container")
@XmlAccessorType(XmlAccessType.FIELD)
public class KBLContainer {
    @XmlElement(name = "Cartesian_point")
    private List<CartesianPoint> cartesianPoints;

    @XmlElement(name = "Node")
    private List<Node> nodes;

    @XmlElement(name = "Connector_housing")
    private List<ConnectorHousing> connectorHousings;

    @XmlElement(name = "General_terminal")
    private List<GeneralTerminal> generalTerminals;
    
    @XmlElement(name = "General_wire")
    private List<GeneralWire> generalWires;
    
    @XmlElement(name = "Harness")
    private Harness harness;
    
    @XmlElement(name = "Routing")
    private List<Routing> routings;
    
    @XmlElement(name = "Segment")
    private List<Segment> segments;
    
    @XmlElement(name = "Unit")
    private List<Unit> units;
    
    @XmlElement(name = "Wire_protection")
    private List<WireProtection> wireProtections;

    public List<WireProtection> getWireProtections() {
		return wireProtections;
	}

	public void setWireProtections(List<WireProtection> wireProtections) {
		this.wireProtections = wireProtections;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public List<Routing> getRoutings() {
		return routings;
	}

	public void setRoutings(List<Routing> routings) {
		this.routings = routings;
	}

	// Getter and Setter for Cartesian Points
    public List<CartesianPoint> getCartesianPoints() {
        return cartesianPoints;
    }

    public void setCartesianPoints(List<CartesianPoint> cartesianPoints) {
        this.cartesianPoints = cartesianPoints;
    }

    // Getter and Setter for Nodes
    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    // Getter and Setter for Connector Housings
    public List<ConnectorHousing> getConnectorHousings() {
        return connectorHousings;
    }

    public void setConnectorHousings(List<ConnectorHousing> connectorHousings) {
        this.connectorHousings = connectorHousings;
    }

    // Getter and Setter for General Terminals
    public List<GeneralTerminal> getGeneralTerminals() {
        return generalTerminals;
    }

    public void setGeneralTerminals(List<GeneralTerminal> generalTerminals) {
        this.generalTerminals = generalTerminals;
    }

    // Getter and Setter for General Wires
    public List<GeneralWire> getGeneralWires() {
        return generalWires;
    }

    public void setGeneralWires(List<GeneralWire> generalWires) {
        this.generalWires = generalWires;
    }

    // Getter and Setter for Harness
    public Harness getHarness() {
        return harness;
    }

    public void setHarness(Harness harness) {
        this.harness = harness;
    }
}
