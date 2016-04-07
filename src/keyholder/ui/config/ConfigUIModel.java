package keyholder.ui.config;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.value.ValueModel;
import idb.commands.Command;
import idb.commands.CommandContext;
import idb.commands.FindCommand;
import idb.commands.SelectionCommand;
import idb.models.BooleanModels;
import idb.models.SelectionInList;
import idb.models.fieldaccessors.Field;
import idb.models.fieldaccessors.FieldFactory;
import idb.models.table.ParentChildTableUpdater;
import idb.models.table.TableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import keyholder.Application;
import keyholder.db.KeyMapper;
import keyholder.domain.Key;
import keyholder.domain.Person;

public class ConfigUIModel {
    TableModel personTable;
    TableModel keyTable;
    TableModel keyPermitTable;
    CommandContext keyPermitCommands;
    CommandContext keyCommands;
    CommandContext personCommands;

    public ConfigUIModel() {
    }

    TableModel getPersonTable() {
        if(this.personTable == null) {
            Field[] var1 = FieldFactory.createFieldList(Person.class, new String[]{"name; Имя", "middleName; Отчество", "surName; Фамилия", "status; Статус", "workPhone; Телефон"});
            ArrayListModel var2 = new ArrayListModel(Person.findAll());
            this.personTable = new TableModel(var1, var2);
            this.personTable.setSelection(new SelectionInList(0));
        }

        return this.personTable;
    }

    TableModel getKeyTable() {
        if(this.keyTable == null) {
            Field[] var1 = FieldFactory.createFieldList(Key.class, new String[]{"number; Ключ; edit"});
            ArrayListModel var2 = new ArrayListModel(KeyMapper.findAll());
            this.keyTable = new TableModel(var1, var2);
            this.keyTable.setSelection(new SelectionInList(0));
            this.keyTable.addTableModelListener(new ConfigUIModel.KeyTableL());
        }

        return this.keyTable;
    }

    TableModel getKeyPermitTable() {
        if(this.keyPermitTable == null) {
            Field[] var1 = FieldFactory.createFieldList(Key.class, new String[]{"number; Ключ"});
            ArrayListModel var2 = new ArrayListModel();
            this.keyPermitTable = new TableModel(var1, var2);
            this.keyPermitTable.setSelection(new SelectionInList(0));
            new ParentChildTableUpdater(this.getPersonTable(), this.keyPermitTable, Person.class, "keys");
        }

        return this.keyPermitTable;
    }

    CommandContext getPersonCommands() {
        if(this.personCommands != null) {
            return this.personCommands;
        } else {
            this.personCommands = new CommandContext();
            FindCommand var1 = new FindCommand("find");
            this.personCommands.addLocalCommand(var1);
            return this.personCommands;
        }
    }

    CommandContext getKeyCommands() {
        if(this.keyCommands != null) {
            return this.keyCommands;
        } else {
            this.keyCommands = new CommandContext();
            Command var1 = new Command("addKey") {
                public void execute() {
                    ConfigUIModel.this.addKey();
                }
            };
            this.keyCommands.addLocalCommand(var1);
            SelectionCommand var3 = new SelectionCommand("deleteKey", this.getKeyTable().getSelection()) {
                public void execute() {
                    ConfigUIModel.this.deleteKey((Key)this.getSelected());
                }
            };
            this.keyCommands.addLocalCommand(var3);
            final ValueModel var2 = BooleanModels.and(this.getKeyTable().getSelection().getExistSelectionValue(), this.getPersonTable().getSelection().getExistSelectionValue());
            var1 = new Command("addKeyPermit", var2) {
                public void execute() {
                    ConfigUIModel.this.addKeyPermit();
                }
            };
            this.keyCommands.addLocalCommand(var1);
            return this.keyCommands;
        }
    }

    CommandContext getKeyPermitCommands() {
        if(this.keyPermitCommands != null) {
            return this.keyPermitCommands;
        } else {
            this.keyPermitCommands = new CommandContext();
            SelectionCommand var1 = new SelectionCommand("deleteKeyPermit", this.getKeyPermitTable().getSelection()) {
                public void execute() {
                    ConfigUIModel.this.deleteKeyPermit();
                }
            };
            this.keyPermitCommands.addLocalCommand(var1);
            return this.keyPermitCommands;
        }
    }

    void addKey() {
        String var1 = Application.showInputDialog("Введите название ключа", "Добавление ключа");
        if(var1 != null) {
            var1 = var1.trim();
            if(var1.length() != 0) {
                Key var2 = new Key(var1);
                if(var2.insert()) {
                    ArrayListModel var3 = (ArrayListModel)this.getKeyTable().getList();
                    var3.add(var2);
                }

            }
        }
    }

    void deleteKey(Key var1) {
        if(var1 != null) {
            if(Application.showConfirmDialog("Вы действительно хотите удалить ключ?", "Удаление ключа") && var1.delete()) {
                ArrayListModel var2 = (ArrayListModel)this.getKeyTable().getList();
                var2.remove(var1);
                ArrayListModel var3 = (ArrayListModel)this.getKeyPermitTable().getList();
                var3.remove(var1);
            }

        }
    }

    void addKeyPermit() {
        Person var1 = (Person)this.getPersonTable().getSelection().getSelected();
        Key var2 = (Key)this.getKeyTable().getSelection().getSelected();
        if(var1.addKeyPermit(var2)) {
            ArrayListModel var3 = (ArrayListModel)this.getKeyPermitTable().getList();
            var3.add(var2);
        }

    }

    void deleteKeyPermit() {
        Person var1 = (Person)this.getPersonTable().getSelection().getSelected();
        Key var2 = (Key)this.getKeyPermitTable().getSelection().getSelected();
        if(var1.removeKeyPermit(var2)) {
            ArrayListModel var3 = (ArrayListModel)this.getKeyPermitTable().getList();
            var3.remove(var2);
        }

    }

    class KeyTableL implements TableModelListener {
        KeyTableL() {
        }

        public void tableChanged(TableModelEvent var1) {
            ConfigUIModel.this.getKeyPermitTable().fireTableDataChanged();
        }
    }
}

