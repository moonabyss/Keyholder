package keyholder.ui.reception;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import idb.commands.CommandManager;
import idb.components.VokiJButton;
import idb.models.table.TableHeader;
import idb.models.table.TableHeaderModel;
import idb.models.table.TableModel;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import keyholder.ui.reception.PersonUIModel;

public class PersonDlg extends JDialog {
    PersonUIModel model;
    JLabel photo;
    JLabel fio;
    JLabel status;
    JLabel phone;
    JTable keyTable;
    VokiJButton handButton;
    VokiJButton handInButton;
    VokiJButton handOverButton;
    JButton closeButton;

    public PersonDlg(PersonUIModel var1, JFrame var2) {
        super(var2, false);
        this.model = var1;
        this.initComponents();
        this.buildPanel();
        this.bindComponents();
    }

    private void initComponents() {
        this.photo = new JLabel();
        this.photo.setVerticalAlignment(0);
        this.photo.setHorizontalAlignment(0);
        this.fio = new JLabel();
        this.status = new JLabel();
        this.phone = new JLabel();
        this.fio.setFont(new Font("Tahoma", 1, 14));
        this.keyTable = new JTable();
        CommandManager.setCommandContext(this.keyTable, this.model.getCommands());
        this.handButton = new VokiJButton(this.model.getCommands().getCommand("hand"), true);
        this.handInButton = new VokiJButton(this.model.getCommands().getCommand("handin"), true);
        this.handOverButton = new VokiJButton(this.model.getCommands().getCommand("handover"), true);
        this.handButton.setVerticalTextPosition(3);
        this.handButton.setHorizontalTextPosition(0);
        this.handInButton.setVerticalTextPosition(3);
        this.handInButton.setHorizontalTextPosition(0);
        this.handOverButton.setVerticalTextPosition(3);
        this.handOverButton.setHorizontalTextPosition(0);
        this.closeButton = new JButton("Закрыть");
        this.setResizable(false);
        this.setTitle("Выдача/сдача ключей");
    }

    private void buildPanel() {
        FormLayout var1 = new FormLayout("200dlu, 5dlu, f:p", "p,2dlu,  p, 10dlu , p, 10dlu, p, p:grow, p");
        PanelBuilder var2 = new PanelBuilder(var1);
        CellConstraints var3 = new CellConstraints();
        var2.addTitle("Права сотрудника на получение ключей", var3.xy(1, 1));
        var2.add(new JScrollPane(this.keyTable), var3.xywh(1, 3, 1, 7));
        var2.add(this.handButton, var3.xy(3, 3));
        var2.add(this.handInButton, var3.xy(3, 5));
        var2.add(this.handOverButton, var3.xy(3, 7));
        var2.add(this.closeButton, var3.xy(3, 9));
        JPanel var4 = var2.getPanel();
        var1 = new FormLayout("l:340px, 14dlu, left:p", "p, 2dlu,  f:450px:grow, 5dlu,  p, 5dlu, p, 5dlu, p");
        var2 = new PanelBuilder(var1);
        var2.addTitle("Сотрудник", var3.xy(1, 1));
        var2.add(this.photo, var3.xy(1, 3));
        var2.add(this.fio, var3.xy(1, 5));
        var2.add(this.status, var3.xy(1, 7));
        var2.add(this.phone, var3.xy(1, 9));
        var2.add(var4, var3.xywh(3, 1, 1, 9));
        var2.setBorder(Borders.DLU14_BORDER);
        this.add(var2.getPanel());
    }

    private void bindComponents() {
        Bindings.bind(this.fio, this.model.getModel("FIO"));
        Bindings.bind(this.status, this.model.getModel("status"));
        Bindings.bind(this.phone, this.model.getModel("workPhone"));
        this.setCloseAction();
        TableModel var1 = this.model.getKeyTable();
        this.keyTable.setModel(var1);
        this.keyTable.setColumnModel(var1.getColumnModel());
        this.keyTable.setSelectionModel(var1.getSelection());
        this.keyTable.setTableHeader(new TableHeader(new TableHeaderModel(var1)));
        this.model.addPropertyChangeListener("bean", new PersonDlg.PhotoPathL());
    }

    private void setCloseAction() {
        KeyStroke var1 = KeyStroke.getKeyStroke(27, 0, false);
        AbstractAction var2 = new AbstractAction() {
            public void actionPerformed(ActionEvent var1) {
                PersonDlg.this.setVisible(false);
            }
        };
        this.getRootPane().getInputMap(2).put(var1, "ESCAPE");
        this.getRootPane().getActionMap().put("ESCAPE", var2);
        this.closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                PersonDlg.this.setVisible(false);
            }
        });
    }

    protected ImageIcon createImageIcon(String var1) {
        if(var1 != null) {
            ImageIcon var2 = new ImageIcon(var1);
            if(var2.getIconHeight() > 0) {
                return var2;
            }
        }

        URL var3 = this.getClass().getResource("/resource/no_photo.png");
        return new ImageIcon(var3);
    }

    protected Icon resizeImage(ImageIcon var1) {
        Image var2 = var1.getImage();
        int var3 = var2.getWidth((ImageObserver)null);
        int var4 = var2.getHeight((ImageObserver)null);
        if(var3 <= 340 && var4 <= 450) {
            return var1;
        } else {
            double var5 = 1.0D;
            if(var4 > 450) {
                var5 = 450.0D / (double)var4;
            } else if(var3 > 340) {
                var5 = 340.0D / (double)var3;
            }

            int var7 = (int)(var5 * (double)var3);
            int var8 = (int)(var5 * (double)var4);
            Image var9 = var2.getScaledInstance(var7, var8, 4);
            return new ImageIcon(var9);
        }
    }

    class PhotoPathL implements PropertyChangeListener {
        PhotoPathL() {
        }

        public void propertyChange(PropertyChangeEvent var1) {
            String var2 = (String)PersonDlg.this.model.getValue("photoFile");
            if(var2 != null) {
                var2 = var2.trim();
            }

            ImageIcon var3 = PersonDlg.this.createImageIcon(var2);
            PersonDlg.this.photo.setIcon(PersonDlg.this.resizeImage(var3));
        }
    }
}
