package keyholder.ui.config;

import com.jgoodies.forms.factories.Borders;
import idb.commands.CommandContext;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import keyholder.ui.config.ConfigUIModel;
import keyholder.ui.config.ConfigView;
import keyholder.ui.config.LogView;
import keyholder.ui.reception.KeysUIModel;
import keyholder.ui.reception.KeysView;

public class ConfigFrame extends JFrame {
    JPanel activePanel;
    CommandContext commands;
    ConfigView configView;
    ConfigUIModel configManagerModel;
    KeysView keyView;
    LogView logView;

    public ConfigFrame() {
        this.setTitle("Ключник - консоль администратора");
        JTabbedPane var1 = new JTabbedPane();
        var1.addTab("Ключи", new ImageIcon(this.getClass().getResource("/resource/key_view.png")), this.getKeyView());
        var1.addTab("История", new ImageIcon(this.getClass().getResource("/resource/log_view.png")), this.getLogView());
        var1.addTab("Конфигурация", new ImageIcon(this.getClass().getResource("/resource/config_view.png")), this.getConfigView());
        var1.setFocusable(false);
        var1.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
        var1.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        this.getContentPane().add(var1);
    }

    JPanel getConfigView() {
        if(this.configView == null) {
            this.configView = new ConfigView(new ConfigUIModel());
            this.configView.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
        }

        return this.configView;
    }

    JPanel getKeyView() {
        if(this.keyView == null) {
            this.keyView = new KeysView(new KeysUIModel());
            this.keyView.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
            this.keyView.removeCloseButton();
        }

        return this.keyView;
    }

    JPanel getLogView() {
        if(this.logView == null) {
            this.logView = new LogView();
            this.logView.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
        }

        return this.logView;
    }
}

