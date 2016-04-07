package keyholder;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import idb.commands.Command;
import idb.commands.CommandManager;
import idb.util.WidgetLookLoader;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import keyholder.ApplicationError;
import keyholder.DBMonitoringThread;
import keyholder.ui.Utilities;
import keyholder.ui.config.ConfigFrame;
import keyholder.ui.reception.ReceptionFrame;

public class Application {
    static JFrame frame;
    static Command exitCommand;
    static int i;

    public Application() {
    }

    public static void RunKeyholder() {
        RunFrame(new ReceptionFrame());
        (new DBMonitoringThread()).start();
    }

    public static void RunKeyholderConfig() {
        RunFrame(new ConfigFrame());
    }

    private static void RunFrame(JFrame var0) {
        frame = var0;
        frame.pack();
        CommandManager.getInstance().setMainFrame(frame);
        Utilities.maximizeFrame(frame);
        frame.setDefaultCloseOperation(0);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
                Application.getExitCommand().execute();
            }
        });
        frame.setVisible(true);
    }

    static void showPerson(long var0) {
        if(frame instanceof ReceptionFrame) {
            ((ReceptionFrame)frame).showPerson(var0);
        }

    }

    public static Command getExitCommand() {
        if(exitCommand != null) {
            return exitCommand;
        } else {
            exitCommand = new Command("exit") {
                public void execute() {
                    if(Application.showConfirmDialog("Вы действительно хотите выйти?", "Выход")) {
                        System.exit(1);
                    }
                }
            };
            CommandManager.getInstance().registerCommand(exitCommand);
            return exitCommand;
        }
    }

    public static String showInputDialog(String var0, String var1) {
        return JOptionPane.showInputDialog(frame, var0, var1, -1);
    }

    public static String showInputDialog(String[] var0, String var1) {
        return JOptionPane.showInputDialog(frame, var0, var1, -1);
    }

    public static boolean showConfirmDialog(String var0, String var1) {
        return JOptionPane.showConfirmDialog(frame, var0, var1, 0) == 0;
    }

    public static void showApplicationError(ApplicationError var0) {
        JOptionPane.showMessageDialog(frame, var0.getErrorMessage(), "Ошибка", 0);
    }

    static {
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException var1) {
            var1.printStackTrace();
        }

        WidgetLookLoader.init("/resource/", "commands.xml");
        i = 0;
    }
}
