package preevisiontosimulink.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import preevisiontosimulink.util.UIUtils;

public class SelectionDialog extends Dialog {
	private Shell dialogShell;
	private List list;
	private Button selectButton;
	private String selectedItem;

	public SelectionDialog(Shell parent, String[] items) {
		super(parent);
		this.dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.dialogShell.setText("Select Insulation Thickness");
		this.dialogShell.setLayout(new GridLayout());

		this.list = new List(dialogShell, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		list.setItems(items);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		this.selectButton = new Button(dialogShell, SWT.PUSH);
		selectButton.setText("Select");
		selectButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		selectButton.addListener(SWT.Selection, event -> {
			int index = list.getSelectionIndex();
			if (index != -1) {
				selectedItem = list.getItem(index);
				dialogShell.close();
			}
		});

		dialogShell.setSize(300, 200);
		UIUtils.centerWindow(dialogShell);
	}

	public String open() {
		dialogShell.open();
		Display display = getParent().getDisplay();
		while (!dialogShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return selectedItem;
	}
}
