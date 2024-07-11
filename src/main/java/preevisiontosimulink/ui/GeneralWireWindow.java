package preevisiontosimulink.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import preevisiontosimulink.parser.KBLParser;
import preevisiontosimulink.parser.kblelements.GeneralWire;
import preevisiontosimulink.util.CalculatorUtils;

import java.util.List;

public class GeneralWireWindow {

    static final String[] CROSS_SECTIONS = {"0.35", "0.5", "1", "1.5", "2.5", "6"};
    static final String[] INSULATION_THICKNESSES = {"0.25", "0.3", "0.35", "0.5", "1"};
    static final String[] MATERIALS = {"Cu", "Al"};

    private Display display;
    private Shell shell;
    private KBLParser thermalSimulation;
    private Table table;

    public GeneralWireWindow(KBLParser thermalSimulation) {
        this.thermalSimulation = thermalSimulation;
        this.display = Display.getDefault();
        this.shell = new Shell(display);
        initializeShell();
        createContent();
        openShell();
    }

    private void initializeShell() {
        shell.setText("General Wire Information");
        shell.setLayout(new GridLayout(1, false));
        Image icon = new Image(display, "src/main/resources/Edag.png");
        shell.setImage(icon);
    }

    private void createContent() {
        table = createTable();
        List<GeneralWire> validGeneralWires = thermalSimulation.getValidGeneralWires();

        if (validGeneralWires.isEmpty()) {
            new Label(shell, SWT.NONE).setText("No valid General Wires found.");
        } else {
            for (GeneralWire wire : validGeneralWires) {
                createTableItem(table, wire);
            }
        }

        setupColumnListeners();
    }

    private Table createTable() {
        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createTableColumn(table, "General Wire", 200);
        createTableColumn(table, "Connections", 400);
        createTableColumn(table, "Cross Section Area/mm²", 200);
        createTableColumn(table, "Insulation Thicknesses/mm", 200);
        createTableColumn(table, "Material of the Cables", 150);
        createTableColumn(table, "Current/A", 150);
        createTableColumn(table, "Temperature Increase (K)", 200);

        return table;
    }

    private void createTableColumn(Table table, String text, int width) {
        TableColumn column = new TableColumn(table, SWT.LEFT);
        column.setText(text);
        column.setWidth(width);
    }

    private void createTableItem(Table table, GeneralWire wire) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, wire.getPartNumber());

        createConnectionsCombo(table, item, wire);
        createCrossSectionCombo(table, item, wire);
        createInsulationThicknessCombo(table, item);
        createMaterialCombo(table, item);
        createCurrentText(table, item);
    }

    private void createConnectionsCombo(Table table, TableItem item, GeneralWire wire) {
        Combo comboConnections = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        String[] connections = wire.getConnections().stream()
                .map(conn -> conn.getSignalName() != null ? conn.getSignalName() : conn.getId())
                .toArray(String[]::new);
        comboConnections.setItems(connections);

        if (connections.length > 0) {
            comboConnections.select(0);
        }

        setEditorForTable(table, item, comboConnections, 1);
    }

    private void createCrossSectionCombo(Table table, TableItem item, GeneralWire wire) {
        Combo comboCrossSection = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboCrossSection.setItems(CROSS_SECTIONS);
        setEditorForTable(table, item, comboCrossSection, 2);
        item.setData("comboCrossSectionCombo", comboCrossSection);
        selectClosestCrossSection(comboCrossSection, wire.getCrossSectionArea().getValueComponent());
    }

    private void createInsulationThicknessCombo(Table table, TableItem item) {
        Combo comboInsulationThickness = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboInsulationThickness.setItems(INSULATION_THICKNESSES);
        comboInsulationThickness.select(1);
        setEditorForTable(table, item, comboInsulationThickness, 3);
        item.setData("insulationThicknessCombo", comboInsulationThickness);
    }

    private void createMaterialCombo(Table table, TableItem item) {
        Combo comboMaterial = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboMaterial.setItems(MATERIALS);
        comboMaterial.select(0);
        setEditorForTable(table, item, comboMaterial, 4);
        item.setData("cableMaterialCombo", comboMaterial);
    }

    private void createCurrentText(Table table, TableItem item) {
        Text textCurrent = new Text(table, SWT.NONE);
        textCurrent.setText("15.0");
        setEditorForTable(table, item, textCurrent, 5);

        textCurrent.addListener(SWT.Verify, event -> {
            String currentText = ((Text) event.widget).getText();
            String newText = currentText.substring(0, event.start) + event.text + currentText.substring(event.end);

            try {
                Double.parseDouble(newText);
            } catch (NumberFormatException e) {
                event.doit = false;
            }
        });

        item.setData("currentText", textCurrent);
    }

    private void setEditorForTable(Table table, TableItem item, Text text, int columnIndex) {
        TableEditor editor = new TableEditor(table);
        editor.grabHorizontal = true;
        editor.setEditor(text, item, columnIndex);
    }

    private void setEditorForTable(Table table, TableItem item, Combo combo, int columnIndex) {
        TableEditor editor = new TableEditor(table);
        editor.grabHorizontal = true;
        editor.setEditor(combo, item, columnIndex);
    }

    private void selectClosestCrossSection(Combo combo, double wireCrossSection) {
        int closestIndex = 0;
        double closestDifference = Double.MAX_VALUE;
        for (int i = 0; i < CROSS_SECTIONS.length; i++) {
            double value = Double.parseDouble(CROSS_SECTIONS[i]);
            double difference = Math.abs(value - wireCrossSection);
            if (difference < closestDifference) {
                closestDifference = difference;
                closestIndex = i;
            }
        }
        combo.select(closestIndex);
    }

    private void setupColumnListeners() {
        TableColumn insulationThicknessColumn = table.getColumn(3);
        insulationThicknessColumn.addListener(SWT.Selection, event -> {
            TableItem item = (TableItem) event.widget.getData();
            if (item != null) {
                SelectionDialog dialog = new SelectionDialog(shell, INSULATION_THICKNESSES);
                String selectedThickness = dialog.open();
                if (selectedThickness != null) {
                    updateCombo(item, selectedThickness, "insulationThickness");
                }
            }
        });

        TableColumn cableMaterialColumn = table.getColumn(4);
        cableMaterialColumn.addListener(SWT.Selection, event -> {
            TableItem item = (TableItem) event.widget.getData();
            if (item != null) {
                SelectionDialog dialog = new SelectionDialog(shell, MATERIALS);
                String selectedMaterial = dialog.open();
                if (selectedMaterial != null) {
                    updateCombo(item, selectedMaterial, "cableMaterial");
                }
            }
        });

        TableColumn currentColumn = table.getColumn(5);
        currentColumn.addListener(SWT.Selection, event -> {
            TableItem item = (TableItem) event.widget.getData();
            if (item != null) {
                InputDialog dialog = new InputDialog(shell);
                Double selectedCurrent = dialog.open();
                if (selectedCurrent != null) {
                    updateCurrent(item, selectedCurrent);
                }
            }
        });

        // Listener for the temperature column header
        TableColumn temperatureColumn = table.getColumn(6);
        temperatureColumn.addListener(SWT.Selection, event -> {
            // Calculate temperature for each row
            for (TableItem item : table.getItems()) {
                calculateAndSetTemperature(item);
            }
        });
    }

    private void updateCombo(TableItem item, String selectedValue, String comboType) {
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

    private void updateCurrent(TableItem item, Double selectedValue) {
        Text textCurrent = (Text) item.getData("currentText");
        if (textCurrent != null) {
            textCurrent.setText(selectedValue.toString());
        }
    }

    private void calculateAndSetTemperature(TableItem item) {
    	Combo crossSectionArea = (Combo) item.getData("comboCrossSectionCombo");
        Combo comboInsulationThickness = (Combo) item.getData("insulationThicknessCombo");
        Combo comboMaterial = (Combo) item.getData("cableMaterialCombo");
        Text textCurrent = (Text) item.getData("currentText");

        if (crossSectionArea != null && comboInsulationThickness != null 
        		&& comboMaterial != null && textCurrent != null) {
        	String crossSectionAreaStr = crossSectionArea.getText();
            String thicknessIsoStr = comboInsulationThickness.getText();
            String material = comboMaterial.getText();
            String currentStr = textCurrent.getText();

            try {
                double crossSection = Double.parseDouble(crossSectionAreaStr);
                double thicknessIso = Double.parseDouble(thicknessIsoStr);
                double current = Double.parseDouble(currentStr);

                // Calculate temperature increase using your existing method
                double temperatureIncrease = CalculatorUtils.calculateTemperatureIncrease(
                        crossSection, thicknessIso, material, current);

                // Update the temperature value in the table
                item.setText(6, String.format("%.2f", temperatureIncrease)); // Assuming temperature column index is 6
            } catch (NumberFormatException e) {
                // Handle parsing errors or invalid data gracefully
                item.setText(6, "Error"); // Set error message or handle accordingly
            }
        } else {
            System.err.println("Error: Data not found in TableItem.");
        }
    }


    private void openShell() {
        shell.setMaximized(true);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
