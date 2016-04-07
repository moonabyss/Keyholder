package keyholder.ui.config;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import idb.commands.Command;
import idb.commands.CommandContext;
import idb.commands.CommandManager;
import idb.commands.FindCommand;
import idb.components.VokiJToolBar;
import idb.models.fieldaccessors.Field;
import idb.models.fieldaccessors.FieldFactory;
import idb.models.table.TableHeader;
import idb.models.table.TableHeaderModel;
import idb.models.table.TableModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import keyholder.Application;
import keyholder.domain.KeyMovement;
import keyholder.ui.config.DateRenderer;

public class LogView extends JPanel {
    TableModel table;
    JTable jTable;
    VokiJToolBar toolbar;
    CommandContext commands;

    public LogView() {
        this.initComponents();
        this.buildPanel();
    }

    private void initComponents() {
        TableModel var1 = this.getTableModel();
        this.jTable = new JTable(var1, var1.getColumnModel(), (ListSelectionModel)null);
        this.jTable.setTableHeader(new TableHeader(new TableHeaderModel(var1)));
        this.jTable.setDefaultRenderer(Timestamp.class, new DateRenderer());
        CommandManager.setCommandContext(this.jTable, this.getCommands());
        var1.getColumnModel().getColumnByField("event").setMaxWidth(120);
        var1.getColumnModel().getColumnByField("event").setPreferredWidth(120);
        var1.getColumnModel().getColumnByField("time").setMaxWidth(150);
        var1.getColumnModel().getColumnByField("time").setPreferredWidth(150);
        this.toolbar = new VokiJToolBar();
        this.toolbar.addCommand(this.getCommands().getCommand("clear_log"));
        this.toolbar.addCommand(this.getCommands().getCommand("refresh_log"));
        this.toolbar.addCommand(this.getCommands().getCommand("find"));
    }

    private void buildPanel() {
        FormLayout var1 = new FormLayout("f:p:grow", "p,p,f:max(140dlu;p):grow");
        PanelBuilder var2 = new PanelBuilder(var1, this);
        CellConstraints var3 = new CellConstraints();
        var2.addTitle("История выдачи ключей", var3.xy(1, 1));
        var2.add(this.toolbar, var3.xy(1, 2));
        var2.add(new JScrollPane(this.jTable), var3.xy(1, 3));
    }

    private TableModel getTableModel() {
        if(this.table != null) {
            return this.table;
        } else {
            Field[] var1 = FieldFactory.createFieldList(KeyMovement.class, new String[]{"key; Ключ", "person;Сотрудник", "event; Событие", "prevPerson; Передал", "time; Время"});
            ArrayListModel var2 = new ArrayListModel(KeyMovement.findAll());
            this.table = new TableModel(var1, var2);
            return this.table;
        }
    }

    CommandContext getCommands() {
        if(this.commands != null) {
            return this.commands;
        } else {
            this.commands = new CommandContext();
            Command var1 = new Command("clear_log") {
                public void execute() {
                    String var1 = Application.showInputDialog(new String[]{"Введите количество последних месяцев,", "события которых надо сохранить.", "Записи о предыдущих месяцах будут удалены"}, "Удаление записей");

                    int var2;
                    try {
                        var2 = Integer.parseInt(var1);
                    } catch (NumberFormatException var4) {
                        return;
                    }

                    KeyMovement.deleteAllLater(var2);
                    LogView.this.getCommands().getCommand("refresh_log").execute();
                }
            };
            this.commands.addLocalCommand(var1);
            var1 = new Command("refresh_log") {
                public void execute() {
                    ArrayList var1 = KeyMovement.findAll();
                    ArrayListModel var2 = (ArrayListModel)LogView.this.getTableModel().getList();
                    var2.clear();
                    var2.addAll(var1);
                }
            };
            this.commands.addLocalCommand(var1);
            FindCommand var2 = new FindCommand("find");
            this.commands.addLocalCommand(var2);
            return this.commands;
        }
    }

    public void removeClearButton() {
        this.toolbar.remove(0);
    }
}
