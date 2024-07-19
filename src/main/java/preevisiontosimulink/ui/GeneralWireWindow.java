package preevisiontosimulink.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import preevisiontosimulink.parser.KBLParser;
import preevisiontosimulink.parser.kblelements.GeneralWire;
import preevisiontosimulink.util.UIUtils;

public class GeneralWireWindow {

    static final String[] CROSS_SECTIONS = { "0.35", "0.5", "1", "1.5", "2.5", "6" };
    static final String[] INSULATION_THICKNESSES = { "0.25", "0.3", "0.35", "0.5", "1" };
    static final String[] MATERIALS = { "Cu", "Al" };

    private Display display;
    private Shell shell;
    private KBLParser thermalSimulation;
    private List<GeneralWire> validGeneralWires = new ArrayList<>();
    private Table table;
    private Label statusLabel;

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
        InputStream inputStream = getClass().getResourceAsStream("/resources/Edag.png");
        if (inputStream != null) {
            Image icon = new Image(display, inputStream);
            shell.setImage(icon);
        } else {
            System.err.println("Failed to load icon: /resources/Edag.png");
        }
    }

    private void createContent() {
        // Create the table
        table = createTable();
        validGeneralWires = thermalSimulation.getValidGeneralWires();

        if (validGeneralWires.isEmpty()) {
            new Label(shell, SWT.NONE).setText("No valid General Wires found.");
        } else {
            for (GeneralWire wire : validGeneralWires) {
                createTableItem(table, wire);
            }
        }

        // Create buttons
        createButtons();
        setupColumnListeners();
    }

    private Table createTable() {
        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.CHECK);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createTableColumn(table, "", 50); // Column for checkboxes
        createTableColumn(table, "General Wire", 200);
        createTableColumn(table, "Connections", 400);
        createTableColumn(table, "Cross Section Area/mm²", 200);
        createTableColumn(table, "Current/A", 150);
        createTableColumn(table, "Insulation Thicknesses/mm", 200);
        createTableColumn(table, "Material of the Cables", 150);
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
        item.setText(1, wire.getPartNumber());

        createConnectionsCombo(table, item, wire);
        createCrossSectionCombo(table, item, wire);
        createCurrentText(table, item);
        createInsulationThicknessCombo(table, item);
        createMaterialCombo(table, item);
    }

    private void createConnectionsCombo(Table table, TableItem item, GeneralWire wire) {
        Combo comboConnections = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        String[] connections = wire.getConnections().stream()
                .map(conn -> conn.getSignalName() != null ? conn.getSignalName() : conn.getId()).toArray(String[]::new);
        comboConnections.setItems(connections);

        if (connections.length > 0) {
            comboConnections.select(0);
        }
        setEditorForTable(table, item, comboConnections, 2);
    }

    private void createCrossSectionCombo(Table table, TableItem item, GeneralWire wire) {
        Combo comboCrossSection = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboCrossSection.setItems(CROSS_SECTIONS);
        setEditorForTable(table, item, comboCrossSection, 3);
        item.setData("comboCrossSectionCombo", comboCrossSection);
        selectClosestCrossSection(comboCrossSection, wire.getCrossSectionArea().getValueComponent());

        comboCrossSection.addListener(SWT.Selection, event -> UIUtils.updateGeneralWireName(item));
    }

    private void createCurrentText(Table table, TableItem item) {
        Text textCurrent = new Text(table, SWT.NONE);
        textCurrent.setText("15.0");
        setEditorForTable(table, item, textCurrent, 4);

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

    private void createInsulationThicknessCombo(Table table, TableItem item) {
        Combo comboInsulationThickness = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboInsulationThickness.setItems(INSULATION_THICKNESSES);
        comboInsulationThickness.select(1);
        setEditorForTable(table, item, comboInsulationThickness, 5);
        item.setData("insulationThicknessCombo", comboInsulationThickness);
    }

    private void createMaterialCombo(Table table, TableItem item) {
        Combo comboMaterial = new Combo(table, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboMaterial.setItems(MATERIALS);
        comboMaterial.select(0);
        setEditorForTable(table, item, comboMaterial, 6);
        item.setData("cableMaterialCombo", comboMaterial);
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

    private void createButtons() {
        // Create a composite to hold the buttons section
        Composite buttonComposite = new Composite(shell, SWT.NONE);
        GridLayout buttonLayout = new GridLayout(3, false);
        buttonComposite.setLayout(buttonLayout);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Create the Select All checkbox
        Button selectAllButton = new Button(buttonComposite, SWT.CHECK);
        selectAllButton.setText("Select All");
        selectAllButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));

        selectAllButton.addListener(SWT.Selection, event -> {
            boolean isSelected = selectAllButton.getSelection();
            for (TableItem item : table.getItems()) {
                item.setChecked(isSelected);
            }
        });

        // Create the Modify KBL button
        Button modifyKBLButton = new Button(buttonComposite, SWT.PUSH);
        modifyKBLButton.setText("Modify KBL");
        GridData modifyKBLButtonData = new GridData(SWT.END, SWT.CENTER, false, false);
        modifyKBLButtonData.horizontalIndent = 100; // Add some indent to move it to the right
        modifyKBLButton.setLayoutData(modifyKBLButtonData);

        modifyKBLButton.addListener(SWT.Selection, event -> {
            modifyKBLButton.setEnabled(false);
            updateValidGeneralWires();
            statusLabel.setText("KBL modified");
            display.timerExec(2000, () -> statusLabel.setText("Status: Ready"));
            modifyKBLButton.setEnabled(true);
        });

        // Create the Generate button
        Button generateButton = new Button(buttonComposite, SWT.PUSH);
        generateButton.setText("Generate");
        generateButton.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
        generateButton.addListener(SWT.Selection, event -> {
            boolean itemSelected = false;
            for (TableItem item : table.getItems()) {
                if (item.getChecked()) {
                    itemSelected = true;
                }
            }
            if (!itemSelected) {
                statusLabel.setText("No item selected!");
                display.timerExec(1000, () -> statusLabel.setText("Status: Ready"));
            } else {
            	statusLabel.setText("Generating Simulink Model...");
            	thermalSimulation.startMatlabEngine();
                for (TableItem item : table.getItems()) {
                    if (item.getChecked()) {
                        int index = table.indexOf(item);
                        GeneralWire wire = validGeneralWires.get(index);
                        UIUtils.generateThermalSimulinkModel(item, wire, thermalSimulation);
                        itemSelected = true;
                    }
                }
                thermalSimulation.closeMatlabEngine();
                statusLabel.setText("Simulink Model Generated!");
                display.timerExec(2000, () -> statusLabel.setText("Status: Ready"));
            }
        });

        // Create the status label beneath the buttons
        statusLabel = new Label(shell, SWT.NONE);
        GridData statusLabelData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        statusLabelData.horizontalIndent = 2;
        statusLabel.setLayoutData(statusLabelData);
        Font font = new Font(display, "Arial", 11, SWT.BOLD);
        statusLabel.setFont(font);
        statusLabel.setText("Status: Ready"); // Set initial status text
    }

    private void updateValidGeneralWires() {
        TableItem[] items = table.getItems();
        for (int i = 0; i < items.length; i++) {
            TableItem item = items[i];
            GeneralWire wire = validGeneralWires.get(i);

            String partNumber = item.getText(1);
            wire.setPartNumber(partNumber);

            Combo crossSectionCombo = (Combo) item.getData("comboCrossSectionCombo");
            double crossSectionValue = Double.parseDouble(crossSectionCombo.getText());
            wire.getCrossSectionArea().setValueComponent(crossSectionValue);
        }
        thermalSimulation.generateModifiedKBL();
    }

    private void setupColumnListeners() {
        TableColumn currentColumn = table.getColumn(4);
        currentColumn.addListener(SWT.Selection, event -> {
            InputDialog dialog = new InputDialog(shell);
            Double selectedCurrent = dialog.open();
            if (selectedCurrent != null) {
                for (TableItem item : table.getItems()) {
                    UIUtils.updateCurrent(item, selectedCurrent);
                }
            }
        });

        TableColumn insulationThicknessColumn = table.getColumn(5);
        insulationThicknessColumn.addListener(SWT.Selection, event -> {
            SelectionDialog dialog = new SelectionDialog(shell, INSULATION_THICKNESSES);
            String selectedThickness = dialog.open();
            if (selectedThickness != null) {
                for (TableItem item : table.getItems()) {
                    UIUtils.updateCombo(item, selectedThickness, "insulationThickness");
                }
            }
        });

        TableColumn cableMaterialColumn = table.getColumn(6);
        cableMaterialColumn.addListener(SWT.Selection, event -> {
            SelectionDialog dialog = new SelectionDialog(shell, MATERIALS);
            String selectedMaterial = dialog.open();
            if (selectedMaterial != null) {
                for (TableItem item : table.getItems()) {
                    UIUtils.updateCombo(item, selectedMaterial, "cableMaterial");
                }
            }
        });

        // Listener for the temperature column header
        TableColumn temperatureColumn = table.getColumn(7);
        temperatureColumn.addListener(SWT.Selection, event -> {
            for (TableItem item : table.getItems()) {
                UIUtils.calculateAndSetTemperature(item);
            }
        });
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
