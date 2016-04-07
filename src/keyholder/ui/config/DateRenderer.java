package keyholder.ui.config;

import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableCellRenderer;

class DateRenderer extends DefaultTableCellRenderer {
    SimpleDateFormat formatter;

    public DateRenderer() {
    }

    public void setValue(Object var1) {
        if(this.formatter == null) {
            this.formatter = new SimpleDateFormat("dd.MM.yy  HH:mm:ss");
        }

        this.setText(var1 == null?"":this.formatter.format(var1));
    }
}
