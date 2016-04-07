package keyholder.ui.config;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import idb.commands.CommandContext;
import idb.commands.CommandManager;
import idb.components.VokiJToolBar;
import idb.models.table.TableHeader;
import idb.models.table.TableHeaderModel;
import idb.models.table.TableModel;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import keyholder.ui.config.ConfigUIModel;

public class ConfigView extends JPanel {
    ConfigUIModel model;
    JTable personTable;
    JTable keyTable;
    JTable keyPermitTable;
    VokiJToolBar keyToolbar;
    VokiJToolBar keyPermitToolbar;
    VokiJToolBar addKeyPermitButton;
    VokiJToolBar findButton;

    public ConfigView(ConfigUIModel var1) {
        this.model = var1;
        this.initComponents();
        this.buildPanel();
    }

    private void initComponents() {
        TableModel var1 = this.model.getPersonTable();
        this.personTable = new JTable(var1, var1.getColumnModel(), var1.getSelection());
        this.personTable.scrollRectToVisible((Rectangle)null);
        this.personTable.setTableHeader(new TableHeader(new TableHeaderModel(var1)));
        CommandManager.setCommandContext(this.personTable, this.model.getPersonCommands());
        var1 = this.model.getKeyTable();
        this.keyTable = new JTable(var1, var1.getColumnModel(), var1.getSelection());
        this.keyTable.setTableHeader(new TableHeader(new TableHeaderModel(var1)));
        this.keyToolbar = new VokiJToolBar();
        this.addKeyPermitButton = new VokiJToolBar();
        this.keyPermitToolbar = new VokiJToolBar();
        this.findButton = new VokiJToolBar();
        CommandContext var2 = this.model.getKeyCommands();
        CommandManager.setCommandContext(this.keyTable, var2);
        this.keyToolbar.addCommand(var2.getCommand("addKey"));
        this.keyToolbar.addCommand(var2.getCommand("deleteKey"));
        this.addKeyPermitButton.addCommand(var2.getCommand("addKeyPermit"));
        this.findButton.addCommand(this.model.getPersonCommands().getCommand("find"));
        var1 = this.model.getKeyPermitTable();
        this.keyPermitTable = new JTable(var1, var1.getColumnModel(), var1.getSelection());
        this.keyPermitTable.setTableHeader(new TableHeader(new TableHeaderModel(var1)));
        var2 = this.model.getKeyPermitCommands();
        CommandManager.setCommandContext(this.keyPermitTable, var2);
        this.keyPermitToolbar.addCommand(var2.getCommand("deleteKeyPermit"));
    }

    private void buildPanel() {
        FormLayout var1 = new FormLayout("f:d:grow, 5dlu, left:p, 5dlu, f:d:grow", "p, p, f:d:grow, 14dlu, d,  d,  max(45dlu;d), p, max(45dlu;d)");
        var1.setRowGroups(new int[][]{{1, 5}});
        PanelBuilder var2 = new PanelBuilder(var1, this);
        CellConstraints var3 = new CellConstraints();
        var2.addTitle("Сотрудники", var3.xy(1, 1));
        var2.add(this.findButton, var3.xy(1, 2));
        var2.add(new JScrollPane(this.personTable), var3.xyw(1, 3, 5));
        var2.addTitle("Все ключи", var3.xy(1, 5));
        var2.addTitle("Права сотрудника на получение ключей", var3.xy(5, 5));
        var2.add(this.keyToolbar, var3.xy(1, 6));
        var2.add(new JScrollPane(this.keyTable), var3.xywh(1, 7, 1, 3));
        var2.add(this.keyPermitToolbar, var3.xy(5, 6));
        var2.add(this.addKeyPermitButton, var3.xy(3, 8));
        var2.add(new JScrollPane(this.keyPermitTable), var3.xywh(5, 7, 1, 3));
    }
}
