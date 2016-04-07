package keyholder.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import keyholder.db.DB;
import keyholder.db.KeyMapper;
import keyholder.db.Registry;
import keyholder.domain.Key;
import keyholder.domain.KeyMovement;

public class KeyMovementMapper {
    private static final String findAllSql = "SELECT ID, KeyID, Employee, PrevEmployee, Operation, Date FROM tblKeyMovementLog2";
    private static final String insertSql = "INSERT INTO tblKeyMovementLog2 VALUES(?,?,?,?,GETDATE())";
    private static final String deleteSql = "DELETE FROM tblKeyMovementLog2 WHERE Date <= ?";

    public KeyMovementMapper() {
    }

    public static ArrayList<KeyMovement> findAll() {
        ArrayList var0 = new ArrayList();
        PreparedStatement var1 = null;
        ResultSet var2 = null;

        try {
            var1 = DB.prepare("SELECT ID, KeyID, Employee, PrevEmployee, Operation, Date FROM tblKeyMovementLog2");
            var2 = var1.executeQuery();

            while(var2.next()) {
                var0.add(load(var2));
            }

            var2.close();
            var1.close();
            return var0;
        } catch (SQLException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static boolean insert(long var0, String var2, String var3, short var4) {
        try {
            PreparedStatement var5 = DB.prepare("INSERT INTO tblKeyMovementLog2 VALUES(?,?,?,?,GETDATE())");
            var5.setLong(1, var0);
            var5.setString(2, var2);
            var5.setString(3, var3);
            var5.setShort(4, var4);
            var5.execute();
            return true;
        } catch (SQLException var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAllLater(int var0) {
        try {
            Statement var1 = DB.createStatement();
            ResultSet var2 = var1.executeQuery("select getdate()");
            var2.next();
            Timestamp var3 = var2.getTimestamp(1);
            Calendar var4 = Calendar.getInstance();
            var4.setTime(var3);
            var4.add(2, -var0);
            Timestamp var5 = new Timestamp(var4.getTime().getTime());
            var2.close();
            var1.close();
            PreparedStatement var6 = DB.prepare("DELETE FROM tblKeyMovementLog2 WHERE Date <= ?");
            var6.setTimestamp(1, var5);
            var6.execute();
            var6.close();
            return true;
        } catch (SQLException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    private static KeyMovement load(ResultSet var0) throws SQLException {
        KeyMovement var1 = null;
        long var2 = var0.getLong(1);
        var1 = Registry.getKeyMovement(var2);
        if(var1 != null) {
            return var1;
        } else {
            Key var4 = KeyMapper.find(var0.getLong(2));
            String var5 = var0.getString(3);
            String var6 = var0.getString(4);
            short var7 = var0.getShort(5);
            Timestamp var8 = var0.getTimestamp(6);
            var1 = new KeyMovement(var2, var4, var5, var6, var7, var8);
            Registry.put(var1);
            return var1;
        }
    }
}

