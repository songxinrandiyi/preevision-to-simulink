package preevisiontosimulink.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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
}
