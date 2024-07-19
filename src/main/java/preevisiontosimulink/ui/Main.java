package preevisiontosimulink.ui;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import preevisiontosimulink.parser.KBLParser;
import preevisiontosimulink.util.StringUtils;
import preevisiontosimulink.util.UIUtils;

public class Main {

	private static Text modelNameField;
	private static Label fileLabel;
	private static List<File> selectedFiles = new ArrayList<>();
	private static Combo operationComboBox;
	private static Button generateButton;
	private static Button clearFilesButton;
	private static Label statusLabel;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Wiring Harness Optimizer");
		shell.setLayout(new GridLayout(3, false)); // 3 columns for layout

		// Load the icon from the resources folder within the JAR
		InputStream inputStream = Main.class.getResourceAsStream("/resources/Edag.png");
		if (inputStream != null) {
			Image icon = new Image(display, inputStream);
			shell.setImage(icon);
		} else {
			System.err.println("Failed to load icon: /resources/Edag.png");
		}

		createContents(shell);

		shell.setSize(800, 350); // Larger initial size
		UIUtils.centerWindow(shell); // Center the window on the screen
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void createContents(final Shell shell) {
		// Model Name components
		Label modelNameLabel = new Label(shell, SWT.NONE);
		modelNameLabel.setText("Model Name:");
		modelNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		modelNameField = new Text(shell, SWT.BORDER);
		GridData modelNameGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		modelNameField.setLayoutData(modelNameGridData);

		// File selection components
		Label fileChooserLabel = new Label(shell, SWT.NONE);
		fileChooserLabel.setText("Select Files:");
		fileChooserLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		Button fileButton = new Button(shell, SWT.PUSH);
		fileButton.setText("Browse");
		GridData fileButtonGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		fileButton.setLayoutData(fileButtonGridData);
		fileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setFilterPath(System.getProperty("user.dir"));
				fileDialog.setFilterExtensions(new String[] { "*.kbl", "*.*" });
				String filePath = fileDialog.open();
				if (filePath != null) {
					String[] fileNames = fileDialog.getFileNames();
					for (String fileName : fileNames) {
						File file = new File(fileDialog.getFilterPath() + File.separator + fileName);
						if (!selectedFiles.contains(file)) {
							selectedFiles.add(file);
							fileLabel.setText(fileLabel.getText() + file.getName() + "; ");
						}
					}
					checkEnableGenerateButtons();
				}
			}
		});

		clearFilesButton = new Button(shell, SWT.PUSH);
		clearFilesButton.setText("Clear Files");
		GridData clearFilesButtonGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		clearFilesButton.setLayoutData(clearFilesButtonGridData);
		clearFilesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedFiles.clear();
				fileLabel.setText("");
				checkEnableGenerateButtons();
			}
		});

		fileLabel = new Label(shell, SWT.NONE);
		GridData fileLabelGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		fileLabel.setLayoutData(fileLabelGridData);

		// Operation selection components
		Label operationLabel = new Label(shell, SWT.NONE);
		operationLabel.setText("Operation:");
		operationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		operationComboBox = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		operationComboBox.setItems(new String[] { "Thermal Simulation", "Wiring Harness From KBL" });
		operationComboBox.select(0); // Initialize with "Thermal Simulation"
		GridData operationComboGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		operationComboBox.setLayoutData(operationComboGridData);

		operationComboBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedFiles.clear();
				fileLabel.setText("");
				statusLabel.setText("");
				checkEnableGenerateButtons();
			}
		});

		// Generate button
		generateButton = new Button(shell, SWT.PUSH);
		generateButton.setText("Generate");
		GridData generateButtonGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		generateButton.setLayoutData(generateButtonGridData);
		generateButton.setEnabled(false);
		generateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String modelName = modelNameField.getText();
				if (modelName.isEmpty() && !selectedFiles.isEmpty()) {
					List<String> fileNameParts = new ArrayList<>();
					String fileName = null;
					for (File file : selectedFiles) {
						if (file.getName().endsWith(".kbl")) {
							fileNameParts.add(StringUtils.getFirstPart(file.getName()));
							fileName = StringUtils.removeEnding(file.getName());
							if (fileNameParts.size() == 2) {
								break;
							}
						}
					}
					if (fileNameParts.size() == 2) {
						modelName = fileNameParts.get(0) + "_" + fileNameParts.get(1);
					} else {
						modelName = fileName;
					}
				}

				if (modelName.isEmpty() || selectedFiles.isEmpty()) {
					statusLabel.setText("Model name or files not selected.");
					return;
				}

				String selectedOperation = operationComboBox.getText();
				if (selectedOperation == null) {
					statusLabel.setText("No operation selected.");
					return;
				}

				List<String> filePaths = new ArrayList<>();
				for (File file : selectedFiles) {
					filePaths.add(file.getAbsolutePath());
				}

				switch (selectedOperation) {
				case "Thermal Simulation":
					statusLabel.setText("Getting Information from KBL file...");
					KBLParser thermalSimulation = new KBLParser(modelName, filePaths);
					thermalSimulation.getGeneralWireInformation();
					new GeneralWireWindow(thermalSimulation);
					break;
				case "Wiring Harness From KBL":
					statusLabel.setText("Generating Simulink model...");
					KBLParser wiringHarness = new KBLParser(modelName, filePaths);
					wiringHarness.generateModel();
					statusLabel.setText("Simulink model generated.");
					break;
				default:
					statusLabel.setText("Unknown operation selected.");
					return;
				}

				Display.getDefault().timerExec(2000, new Runnable() {
					@Override
					public void run() {
						statusLabel.setText("");
					}
				});
			}
		});

		// Status label
		statusLabel = new Label(shell, SWT.NONE);
		GridData statusLabelGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		statusLabel.setLayoutData(statusLabelGridData);

		// Add spacing between elements
		GridLayout layout = new GridLayout(3, false);
		layout.verticalSpacing = 15; // Increase vertical spacing between elements
		shell.setLayout(layout);

		// Initial state check for Generate button
		checkEnableGenerateButtons();
	}

	private static void checkEnableGenerateButtons() {
		String selectedOperation = operationComboBox.getText();
		boolean isEnabled = false;

		if (selectedOperation.equals("Thermal Simulation")) {
			isEnabled = selectedFiles.size() == 1 && selectedFiles.get(0).getName().endsWith(".kbl");
		} else if (selectedOperation.equals("Wiring Harness From KBL")) {
			isEnabled = selectedFiles.stream().anyMatch(file -> file.getName().endsWith(".kbl"));
		}

		generateButton.setEnabled(isEnabled);
	}
}
