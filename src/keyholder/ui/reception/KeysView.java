package keyholder.ui.reception;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import idb.components.VokiJButton;
import idb.components.VokiJToolBar;
import idb.models.table.TableHeader;
import idb.models.table.TableHeaderModel;
import idb.models.table.TableModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import keyholder.Application;
import keyholder.ui.reception.KeysUIModel;

public class KeysView extends JPanel {
    KeysUIModel model;
    private JTextPane textPane1;
    private JLabel title1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private VokiJButton closeButton;
    private VokiJToolBar toolbar;

    public KeysView(KeysUIModel var1) {
        this.model = var1;
        this.initComponents();
        this.buildPanel();
    }

    public void removeCloseButton() {
        this.remove(this.closeButton);
    }

    public void removeRefreshButton() {
        this.remove(this.toolbar);
    }

    private void initComponents() {
        this.radioButton1 = new JRadioButton("все");
        this.radioButton2 = new JRadioButton("выданные");
        this.radioButton3 = new JRadioButton("охрана");
        this.closeButton = new VokiJButton(Application.getExitCommand());
        this.toolbar = new VokiJToolBar();
        this.toolbar.addCommand(this.model.getCommands().getCommand("refresh_keys"));
        this.table1 = new JTable();
        TableModel var1 = this.model.getKeyTableModel();
        this.table1.setModel(var1);
        this.table1.setColumnModel(var1.getColumnModel());
        this.table1.setTableHeader(new TableHeader(new TableHeaderModel(var1)));
        Bindings.bind(this.radioButton1, this.model.getStateValue(), "show_all");
        Bindings.bind(this.radioButton2, this.model.getStateValue(), "show_handed");
        Bindings.bind(this.radioButton3, this.model.getStateValue(), "show_freekey");
    }

    private void buildPanel() {
        FormLayout var1 = new FormLayout("f:200dlu:grow, 5dlu, p", "p,max(2dlu;p),  p, 2dlu , p, 2dlu, p, p:grow, p");
        PanelBuilder var2 = new PanelBuilder(var1, this);
        CellConstraints var3 = new CellConstraints();
        var2.setBorder(Borders.DLU14_BORDER);
        var2.addTitle("Все ключи", var3.xy(1, 1));
        var2.add(this.toolbar, var3.xy(1, 2));
        var2.add(new JScrollPane(this.table1), var3.xywh(1, 3, 1, 7));
        var2.add(this.radioButton1, var3.xy(3, 3));
        var2.add(this.radioButton3, var3.xy(3, 5));
        var2.add(this.radioButton2, var3.xy(3, 7));
        var2.add(this.closeButton, var3.xy(3, 9));
    }
}
