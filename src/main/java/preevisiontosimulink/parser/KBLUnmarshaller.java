package preevisiontosimulink.parser;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import preevisiontosimulink.parser.elements.*;

import java.io.File;
import java.util.List;

public class KBLUnmarshaller {
    public static void main(String[] args) {
        try {
            File file = new File("BUMPER_FRONT_RIGHT_02132024_1527 1.kbl");
            JAXBContext jaxbContext = JAXBContext.newInstance(KBLContainer.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            JAXBElement<KBLContainer> rootElement = jaxbUnmarshaller.unmarshal(new StreamSource(file), KBLContainer.class);
            KBLContainer kblContainer = rootElement.getValue();
            List<CartesianPoint> cartesianPoints = kblContainer.getCartesianPoints();
            List<Node> nodes = kblContainer.getNodes();
            
            for (Node node : nodes) {
                System.out.println("Node ID: " + node.getNodeId());
                String cartesianPointId = node.getCartesianPointId();
                CartesianPoint correspondingCartesianPoint = findCartesianPoint(cartesianPoints, cartesianPointId);
                if (correspondingCartesianPoint != null) {
                    List<Double> coordinates = correspondingCartesianPoint.getCoordinates();
                    System.out.println("Coordinate: " + coordinates);
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    private static CartesianPoint findCartesianPoint(List<CartesianPoint> cartesianPoints, String id) {
        for (CartesianPoint cartesianPoint : cartesianPoints) {
            if (cartesianPoint.getId().equals(id)) {
                return cartesianPoint;
            }
        }
        return null; // If no corresponding CartesianPoint is found
    }
}
