package preevisiontosimulink.util;

import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import preevisiontosimulink.parser.KBLParser;
import preevisiontosimulink.parser.kblelements.Connection;
import preevisiontosimulink.parser.kblelements.GeneralWire;

public class UIUtils {
	public static void centerWindow(Shell shell) {
		Display display = shell.getDisplay();
		int screenWidth = display.getPrimaryMonitor().getBounds().width;
		int screenHeight = display.getPrimaryMonitor().getBounds().height;
		int shellWidth = shell.getSize().x;
		int shellHeight = shell.getSize().y;
		int x = (screenWidth - shellWidth) / 2;
		int y = (screenHeight - shellHeight) / 2;
		shell.setLocation(x, y);
	}

	public static void updateCombo(TableItem item, String selectedValue, String comboType) {
		Combo combo = null;
		switch (comboType) {
		case "insulationThickness":
			combo = (Combo) item.getData("insulationThicknessCombo");
			break;
		case "cableMaterial":
			combo = (Combo) item.getData("cableMaterialCombo");
			break;
		}

		if (combo != null) {
			combo.setText(selectedValue);
		}
	}

	public static void updateCurrent(TableItem item, Double selectedValue) {
		Text textCurrent = (Text) item.getData("currentText");
		if (textCurrent != null) {
			textCurrent.setText(selectedValue.toString());
		}
	}
	
	public static void generateThermalSimulinkModel(TableItem item, GeneralWire generalWire, KBLParser thermalSimulation) {
		Combo crossSectionArea = (Combo) item.getData("comboCrossSectionCombo");
		Combo comboInsulationThickness = (Combo) item.getData("insulationThicknessCombo");
		Combo comboMaterial = (Combo) item.getData("cableMaterialCombo");
		Text textCurrent = (Text) item.getData("currentText");

		if (crossSectionArea != null && comboInsulationThickness != null && comboMaterial != null
				&& textCurrent != null) {
			String crossSectionAreaStr = crossSectionArea.getText();
			String thicknessIsoStr = comboInsulationThickness.getText();
			String material = comboMaterial.getText();
			String currentStr = textCurrent.getText();

			try {
				double crossSection = Double.parseDouble(crossSectionAreaStr);
				double thicknessIso = Double.parseDouble(thicknessIsoStr);
				double current = Double.parseDouble(currentStr);
				
				for (Connection connection : generalWire.getValidConnections()) {
					connection.setCrossSectionArea(crossSection);
					connection.setThicknessIso(thicknessIso);
					connection.setCurrent(current);
					connection.setMaterial(material);
				}
				
				thermalSimulation.generateThermalModel(generalWire);
			} catch (NumberFormatException e) {
				item.setText(7, "Error");
			}
		} else {
			System.err.println("Error: Data not found in TableItem.");
		}
	}

	public static void calculateAndSetTemperature(TableItem item) {
		Combo crossSectionArea = (Combo) item.getData("comboCrossSectionCombo");
		Combo comboInsulationThickness = (Combo) item.getData("insulationThicknessCombo");
		Combo comboMaterial = (Combo) item.getData("cableMaterialCombo");
		Text textCurrent = (Text) item.getData("currentText");

		if (crossSectionArea != null && comboInsulationThickness != null && comboMaterial != null
				&& textCurrent != null) {
			String crossSectionAreaStr = crossSectionArea.getText();
			String thicknessIsoStr = comboInsulationThickness.getText();
			String material = comboMaterial.getText();
			String currentStr = textCurrent.getText();

			try {
				double crossSection = Double.parseDouble(crossSectionAreaStr);
				double thicknessIso = Double.parseDouble(thicknessIsoStr);
				double current = Double.parseDouble(currentStr);

				double temperatureIncrease = CalculatorUtils.calculateTemperatureIncrease(crossSection, thicknessIso,
						material, current);

				item.setText(7, String.format("%.2f", temperatureIncrease));
			} catch (NumberFormatException e) {
				item.setText(7, "Error");
			}
		} else {
			System.err.println("Error: Data not found in TableItem.");
		}
	}

	public static void updateGeneralWireName(TableItem item) {
		String partNumber = item.getText(0);
		Combo comboCrossSection = (Combo) item.getData("comboCrossSectionCombo");
		if (comboCrossSection != null) {
			double crossSectionValue = Double.parseDouble(comboCrossSection.getText());
			DecimalFormat df = new DecimalFormat("0.00");
			String crossSectionFormatted = df.format(crossSectionValue).replace(".", ",");

			String[] parts = partNumber.split("-");
			if (parts.length == 3) {
				String newPartNumber = parts[0] + "-" + crossSectionFormatted + "-" + parts[2];
				item.setText(0, newPartNumber);
			}
		}
	}
}
