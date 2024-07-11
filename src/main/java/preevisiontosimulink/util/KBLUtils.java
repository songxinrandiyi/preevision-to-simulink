package preevisiontosimulink.util;

import java.util.List;

import preevisiontosimulink.parser.kblelements.Cavity;
import preevisiontosimulink.parser.kblelements.Connection;
import preevisiontosimulink.parser.kblelements.ConnectorHousing;
import preevisiontosimulink.parser.kblelements.ConnectorOccurrence;
import preevisiontosimulink.parser.kblelements.ContactPoint;
import preevisiontosimulink.parser.kblelements.GeneralWire;
import preevisiontosimulink.parser.kblelements.GeneralWireOccurrence;
import preevisiontosimulink.parser.kblelements.Node;
import preevisiontosimulink.parser.kblelements.Slots;

public class KBLUtils {
	public static GeneralWireOccurrence findGeneralWireOccurrence(List<GeneralWireOccurrence> generalWireOccurrences,
			String id) {
		if (generalWireOccurrences == null) {
			return null;
		}
		for (GeneralWireOccurrence generalWireOccurrence : generalWireOccurrences) {
			if (generalWireOccurrence.getId().equals(id)) {
				return generalWireOccurrence;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	public static Integer getCavityNumberById(ConnectorHousing connectorHousing, String cavityId) {
		if (connectorHousing == null || connectorHousing.getSlots() == null) {
			return null;
		}

		Slots slots = connectorHousing.getSlots();
		List<Cavity> cavities = slots.getCavities();

		if (cavities != null) {
			for (Cavity cavity : cavities) {
				if (cavityId.equals(cavity.getId())) {
					return cavity.getCavityNumber();
				}
			}
		}
		return null; // Return null if the cavity with the given ID is not found
	}

	public static ConnectorOccurrence findConnectorOccurrence(List<ConnectorOccurrence> connectorOccurrences,
			String largeId) {
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			if (connectorOccurrence.getLargeId().equals(largeId)) {
				return connectorOccurrence;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	public static Node findNode(List<Node> nodes, String id) {
		for (Node node : nodes) {
			if (node.getId().equals(id)) {
				return node;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	public static Connection findConnection(List<Connection> connections, String id) {
		for (Connection connection : connections) {
			if (connection.getId().equals(id)) {
				return connection;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	public static Cavity findCavityInConnectorOccurrence(ConnectorOccurrence connectorOccurrence, String id) {
		if (connectorOccurrence != null) {
			List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
			for (Cavity cavity : cavities) {
				if (cavity.getId().equals(id)) {
					return cavity;
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	public static Cavity findCavityInConnectorHousing(ConnectorHousing connectorHousing, String id) {
		if (connectorHousing == null || connectorHousing.getSlots() == null) {
			return null;
		}

		List<Cavity> cavities = connectorHousing.getSlots().getCavities();
		for (Cavity cavity : cavities) {
			if (cavity.getId().equals(id)) {
				return cavity;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	public static Integer findPinNumWithContactPointId(ConnectorOccurrence connectorOccurrence,
			ConnectorHousing connectorHousing, String id) {
		if (connectorOccurrence != null && connectorHousing != null) {
			List<ContactPoint> contactPoints = connectorOccurrence.getContactPoints();
			ContactPoint contactPoint = null;
			Cavity cavityInConnectorOccurrence = null;
			Cavity cavityInConnectorHousing = null;
			if (contactPoints != null) {
				for (ContactPoint contact : contactPoints) {
					if (contact.getId().equals(id)) {
						contactPoint = contact;
					}
				}
			}
			if (contactPoint == null) {
				return null;
			}
			cavityInConnectorOccurrence = findCavityInConnectorOccurrence(connectorOccurrence,
					contactPoint.getContactedCavity());
			cavityInConnectorHousing = findCavityInConnectorHousing(connectorHousing,
					cavityInConnectorOccurrence.getPart());
			if (cavityInConnectorHousing != null) {
				return cavityInConnectorHousing.getCavityNumber();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static ConnectorOccurrence findConnectorOccurrenceWithContactPoint(
			List<ConnectorOccurrence> connectorOccurrences, String id) {
		if (connectorOccurrences != null) {
			for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
				List<ContactPoint> contactPoints = connectorOccurrence.getContactPoints();
				if (contactPoints != null) {
					for (ContactPoint contactPoint : contactPoints) {
						if (contactPoint.getId().equals(id)) {
							return connectorOccurrence;
						}
					}
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	public static GeneralWire findGeneralWire(List<GeneralWire> generalWires, String id) {
		if (generalWires != null) {
			for (GeneralWire generalWire : generalWires) {
				if (generalWire.getId().equals(id)) {
					return generalWire;
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	public static ConnectorHousing findConnectorHousing(List<ConnectorHousing> connectorHousings, String id) {
		if (connectorHousings != null) {
			for (ConnectorHousing connectorHousing : connectorHousings) {
				if (connectorHousing.getId().equals(id)) {
					return connectorHousing;
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}
}
