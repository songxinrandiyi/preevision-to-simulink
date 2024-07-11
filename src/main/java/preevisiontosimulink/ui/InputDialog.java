package preevisiontosimulink.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import preevisiontosimulink.util.UIUtils;

public class InputDialog extends Dialog {
    private Shell dialogShell;
    private Text inputText;
    private Button acceptButton;
    private double inputValue;

    public InputDialog(Shell parent) {
        super(parent);
        this.dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        this.dialogShell.setText("Input Current Value");
        this.dialogShell.setLayout(new GridLayout(2, false));

        new org.eclipse.swt.widgets.Label(dialogShell, SWT.NONE).setText("Current (A):");
        this.inputText = new Text(dialogShell, SWT.BORDER);
        inputText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        this.acceptButton = new Button(dialogShell, SWT.PUSH);
        acceptButton.setText("Accept");
        acceptButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

        acceptButton.addListener(SWT.Selection, event -> {
            try {
                inputValue = Double.parseDouble(inputText.getText());
                dialogShell.close();
            } catch (NumberFormatException e) {
                // Handle invalid input
                inputText.setText("");
            }
        });

        dialogShell.setSize(300, 150); 
        UIUtils.centerWindow(dialogShell);
    }

    public Double open() {
        dialogShell.open();
        Display display = getParent().getDisplay();
        while (!dialogShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return inputValue;
    }
}
