package keyholder.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Utilities {
    public Utilities() {
    }

    public static void maximizeFrame(JFrame var0) {
        Dimension var1 = var0.getToolkit().getScreenSize();
        var0.setBounds(100, 100, var1.width - 200, var1.height - 200);
    }

    public static void toCenter(Component var0) {
        Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension var2 = var0.getSize();
        if(var2.height > var1.height) {
            var2.height = var1.height;
        }

        if(var2.width > var1.width) {
            var2.width = var1.width;
        }

        var0.setLocation((var1.width - var2.width) / 2, (var1.height - var2.height) / 2);
    }
}
