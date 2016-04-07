package keyholder.ui.reception;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.value.ValueHolder;
import idb.commands.Command;
import idb.commands.CommandContext;
import idb.models.SelectionInList;
import idb.models.fieldaccessors.Field;
import idb.models.fieldaccessors.FieldFactory;
import idb.models.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import keyholder.domain.Key;
import keyholder.domain.KeyManager;
import keyholder.domain.Person;
import keyholder.ui.reception.KeysUIModel;

public class PersonUIModel extends PresentationModel {
    KeysUIModel keyModel;
    private TableModel keyTable;
    private CommandContext commands;

    public PersonUIModel(KeysUIModel var1) {
        super(new ValueHolder((Object)null, true));
        this.keyModel = var1;
        this.addPropertyChangeListener("bean", new PersonUIModel.BeanListener());
    }

    CommandContext getCommands() {
        if(this.commands != null) {
            return this.commands;
        } else {
            this.commands = new CommandContext();
            Command var1 = new Command("hand") {
                public void execute() {
                    if(KeyManager.handKey(PersonUIModel.this.getCurrentKey(), PersonUIModel.this.getCurrentPerson())) {
                        PersonUIModel.this.getKeyTable().updateRecord(PersonUIModel.this.getCurrentKey());
                    }

                    PersonUIModel.this.updateUI();
                }
            };
            this.commands.addLocalCommand(var1);
            var1 = new Command("handin") {
                public void execute() {
                    if(KeyManager.handInKey(PersonUIModel.this.getCurrentKey(), PersonUIModel.this.getCurrentPerson())) {
                        PersonUIModel.this.getKeyTable().updateRecord(PersonUIModel.this.getCurrentKey());
                    }

                    PersonUIModel.this.updateUI();
                }
            };
            this.commands.addLocalCommand(var1);
            var1 = new Command("handover") {
                public void execute() {
                    if(KeyManager.handOverKey(PersonUIModel.this.getCurrentKey(), PersonUIModel.this.getCurrentPerson())) {
                        PersonUIModel.this.getKeyTable().updateRecord(PersonUIModel.this.getCurrentKey());
                    }

                    PersonUIModel.this.updateUI();
                }
            };
            this.commands.addLocalCommand(var1);
            this.updateUI();
            return this.commands;
        }
    }

    TableModel getKeyTable() {
        if(this.keyTable == null) {
            Field[] var1 = FieldFactory.createFieldList(Key.class, new String[]{"number; Ключ", "person;Выдано"});
            ArrayListModel var2 = new ArrayListModel();
            this.keyTable = new TableModel(var1, var2);
            this.keyTable.setSelection(new SelectionInList(0));
            this.keyTable.getSelection().addListSelectionListener(new PersonUIModel.TableSelectionL());
        }

        return this.keyTable;
    }

    public void updateUI() {
        Key var1 = (Key)this.getKeyTable().getSelection().getSelected();
        Person var2 = this.getCurrentPerson();
        int var3 = this.keyModel.getKeyListModel().indexOf(var1);
        if(var3 >= 0) {
            this.keyModel.getKeyListModel().fireContentsChanged(var3);
        }

        Command var4 = this.getCommands().getCommand("hand");
        Command var5 = this.getCommands().getCommand("handin");
        Command var6 = this.getCommands().getCommand("handover");
        var4.setEnabled(false);
        var5.setEnabled(false);
        var6.setEnabled(false);
        if(var1 != null && var2 != null) {
            if(var1.getPerson() == Person.GUARDS) {
                var4.setEnabled(true);
            } else if(var1.getPerson() == var2) {
                var5.setEnabled(true);
            } else {
                var6.setEnabled(true);
            }

        }
    }

    private Person getCurrentPerson() {
        return (Person)this.getBean();
    }

    private Key getCurrentKey() {
        return (Key)this.getKeyTable().getSelection().getSelected();
    }

    class TableSelectionL implements ListSelectionListener {
        TableSelectionL() {
        }

        public void valueChanged(ListSelectionEvent var1) {
            PersonUIModel.this.updateUI();
        }
    }

    class BeanListener implements PropertyChangeListener {
        BeanListener() {
        }

        public void propertyChange(PropertyChangeEvent var1) {
            Person var2 = (Person)PersonUIModel.this.getBean();
            if(var2 != null) {
                ArrayList var3 = var2.getKeys();
                ArrayListModel var4 = (ArrayListModel)PersonUIModel.this.getKeyTable().getList();
                var4.clear();
                var4.addAll(var3);
            }
        }
    }
}
