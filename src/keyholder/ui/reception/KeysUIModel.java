package keyholder.ui.reception;

import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.value.ValueModel;
import idb.commands.Command;
import idb.commands.CommandContext;
import idb.models.fieldaccessors.Field;
import idb.models.fieldaccessors.FieldFactory;
import idb.models.table.TableModel;
import java.util.ArrayList;
import keyholder.domain.Key;
import keyholder.domain.KeyManager;

public class KeysUIModel extends Model {
    CommandContext commands;
    String state = "show_all";
    ArrayListModel listModel;

    public KeysUIModel() {
    }

    public ArrayListModel getKeyListModel() {
        if(this.listModel != null) {
            return this.listModel;
        } else {
            ArrayList var1 = Key.findAll();
            this.listModel = new ArrayListModel(var1);
            return this.listModel;
        }
    }

    public TableModel getKeyTableModel() {
        Field[] var1 = FieldFactory.createFieldList(Key.class, new String[]{"number;Номер", "person;Выдано"});
        return new TableModel(var1, this.getKeyListModel());
    }

    public CommandContext getCommands() {
        if(this.commands != null) {
            return this.commands;
        } else {
            this.commands = new CommandContext();
            Command var1 = new Command("refresh_keys") {
                public void execute() {
                    KeyManager.reloadKeys();
                    KeysUIModel.this.setState(KeysUIModel.this.getState());
                }
            };
            this.commands.addLocalCommand(var1);
            return this.commands;
        }
    }

    public String getState() {
        return this.state;
    }

    public void setState(String var1) {
        ArrayListModel var2 = this.getKeyListModel();
        ArrayList var3;
        if(var1.equals("show_all")) {
            var3 = Key.findAll();
        } else if(var1.equals("show_handed")) {
            var3 = KeyManager.selectHandedKey();
        } else {
            var3 = KeyManager.selectFreeKey();
        }

        var2.clear();
        var2.addAll(var3);
        String var4 = this.state;
        this.state = var1;
        this.firePropertyChange("state", var4, this.state);
    }

    ValueModel getStateValue() {
        BeanAdapter var1 = new BeanAdapter(this, true);
        return var1.getValueModel("state");
    }
}
