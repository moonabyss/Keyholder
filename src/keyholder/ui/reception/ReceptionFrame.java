package keyholder.ui.reception;

import com.jgoodies.forms.factories.Borders;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import keyholder.domain.Person;
import keyholder.ui.config.LogView;
import keyholder.ui.reception.KeysUIModel;
import keyholder.ui.reception.KeysView;
import keyholder.ui.reception.PersonDlg;
import keyholder.ui.reception.PersonUIModel;

public class ReceptionFrame extends JFrame {
    KeysView keyView;
    KeysUIModel keyModel;
    PersonDlg personDlg;
    PersonUIModel personModel;

    public ReceptionFrame() {
        this.setTitle("Ключник - рецепция");
        this.setDefaultCloseOperation(3);
        JTabbedPane var1 = new JTabbedPane();
        var1.addTab("Ключи", new ImageIcon(this.getClass().getResource("/resource/key_view.png")), this.getKeyView());
        var1.addTab("История", new ImageIcon(this.getClass().getResource("/resource/log_view.png")), this.getLogView());
        var1.setFocusable(false);
        var1.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
        var1.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        this.getContentPane().add(var1);
    }

    private KeysView getKeyView() {
        if(this.keyView == null) {
            this.keyModel = new KeysUIModel();
            this.keyView = new KeysView(this.keyModel);
            this.keyView.removeRefreshButton();
            this.keyView.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
        }

        return this.keyView;
    }

    public void showPerson(long var1) {
        Person var3 = Person.find(var1);
        if(var3 != null) {
            if(this.personDlg == null) {
                this.personModel = new PersonUIModel(this.keyModel);
                this.personDlg = new PersonDlg(this.personModel, this);
                this.personModel.setBean(var3);
                this.personDlg.pack();
                this.personDlg.setLocationRelativeTo((Component)null);
            }

            this.personModel.setBean(var3);
            this.personDlg.setVisible(true);
        }
    }

    private Component getLogView() {
        LogView var1 = new LogView();
        var1.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
        var1.removeClearButton();
        return var1;
    }
}
