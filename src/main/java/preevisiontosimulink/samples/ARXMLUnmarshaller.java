package preevisiontosimulink.samples;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import preevisiontosimulink.parser.arxmlelements.AUTOSAR;
import preevisiontosimulink.parser.arxmlelements.ArPackage;
import preevisiontosimulink.parser.arxmlelements.ArPackages;

public class ARXMLUnmarshaller {

	public static void main(String[] args) {
		try {
			File file = new File("BODY_CTRL_MDL_GEN_20240430 1.arxml");

			JAXBContext jaxbContext = JAXBContext.newInstance(AUTOSAR.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			JAXBElement<AUTOSAR> rootElement = jaxbUnmarshaller.unmarshal(new StreamSource(file), AUTOSAR.class);
			AUTOSAR autosar = rootElement.getValue();

			ArPackages arPackages = autosar.getArPackages();
			System.out.println(arPackages.getArPackages().size());
			List<ArPackage> arPackageList = arPackages.getArPackages();
			for (ArPackage arPackage : arPackageList) {
				System.out.println(arPackage.getUuid());
				System.out.println(arPackage.getShortName());
				System.out.println();
				if (arPackage.getArPackages() != null) {
					List<ArPackage> arPackageList2 = arPackage.getArPackages().getArPackages();
					if (arPackageList2 != null) {
						for (ArPackage arPackage2 : arPackageList2) {
							System.out.println(arPackage2.getUuid());
							System.out.println(arPackage2.getShortName());
							System.out.println();
						}
					}
				}
				System.out.println();
				System.out.println();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
