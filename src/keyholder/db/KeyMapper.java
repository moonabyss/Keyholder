package keyholder.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import keyholder.db.DB;
import keyholder.db.PersonMapper;
import keyholder.db.Registry;
import keyholder.domain.Key;
import keyholder.domain.Person;

public class KeyMapper {
    private static final String findAllStatement = "SELECT ID, RoomNumber, PersonID FROM tblKey2 ";
    private static final String findStatement = "SELECT ID, RoomNumber, PersonID FROM tblKey2 WHERE ID = ?";
    private static final String findForPersonStatement = "SELECT ID, RoomNumber, PersonID FROM tblKey2 WHERE ID IN ( SELECT KeyID FROM tblKeyPermit2 WHERE PersonID = ? )";
    private static final String updateStatement1 = "UPDATE tblKey2 set PersonID = ?, Date = NULL  WHERE ID = ?";
    private static final String updateStatement2 = "UPDATE tblKey2 set PersonID = NULL, Date = NULL  WHERE ID = ?";
    private static final String updateStatement3 = "UPDATE tblKey2 set RoomNumber = ?, Date = NULL  WHERE ID = ?";
    private static final String deleteStatement = "DELETE FROM tblKey2 WHERE ID = ?";
    private static final String insetKeyPermitStatement = "INSERT INTO tblKeyPermit2 VALUES(?,?)";
    private static final String deleteKeyPermitStatement = "DELETE FROM tblKeyPermit2 WHERE PersonID = ? AND KeyID = ? ";

    public KeyMapper() {
    }

    public static Key find(long var0) {
        Key var2 = Registry.getKey(var0);
        if(var2 != null) {
            return var2;
        } else {
            PreparedStatement var3 = null;
            ResultSet var4 = null;

            try {
                var3 = DB.prepare("SELECT ID, RoomNumber, PersonID FROM tblKey2 WHERE ID = ?");
                var3.setLong(1, var0);
                var4 = var3.executeQuery();
                var4.next();
                var2 = load(var4);
                var4.close();
                var3.close();
                return var2;
            } catch (SQLException var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }

    public static ArrayList<Key> findAll() {
        ArrayList var0 = new ArrayList();
        PreparedStatement var1 = null;
        ResultSet var2 = null;

        try {
            var1 = DB.prepare("SELECT ID, RoomNumber, PersonID FROM tblKey2 ");
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

    public static void reloadAll() {
        PreparedStatement var0 = null;
        ResultSet var1 = null;

        try {
            var0 = DB.prepare("SELECT ID, RoomNumber, PersonID FROM tblKey2 ");
            var1 = var0.executeQuery();

            while(var1.next()) {
                reload(var1);
            }

            var1.close();
            var0.close();
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    public static ArrayList<Key> findKeysWhere(String var0) {
        var0 = "WHERE " + var0;
        ArrayList var1 = new ArrayList();

        try {
            Statement var2 = DB.createStatement();
            ResultSet var3 = var2.executeQuery("SELECT ID, RoomNumber, PersonID FROM tblKey2 " + var0);

            while(var3.next()) {
                var1.add(load(var3));
            }

            var3.close();
            var2.close();
            return var1;
        } catch (SQLException var4) {
            var4.printStackTrace();
            return var1;
        }
    }

    public static ArrayList<Key> findForPerson(long var0) {
        ArrayList var2 = new ArrayList();
        PreparedStatement var3 = null;
        ResultSet var4 = null;

        try {
            var3 = DB.prepare("SELECT ID, RoomNumber, PersonID FROM tblKey2 WHERE ID IN ( SELECT KeyID FROM tblKeyPermit2 WHERE PersonID = ? )");
            var3.setLong(1, var0);
            var4 = var3.executeQuery();

            while(var4.next()) {
                var2.add(load(var4));
            }

            var4.close();
            var3.close();
            return var2;
        } catch (SQLException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    private static Key load(ResultSet var0) throws SQLException {
        long var1 = var0.getLong(1);
        Key var3 = Registry.getKey(var1);
        if(var3 != null) {
            return var3;
        } else {
            String var4 = var0.getString(2);
            long var5 = var0.getLong(3);
            Person var7 = null;
            if(var5 != 0L) {
                var7 = PersonMapper.findPerson(var5);
            }

            var3 = new Key(var1, var4, var7);
            Registry.put(var3);
            return var3;
        }
    }

    private static void reload(ResultSet var0) throws SQLException {
        long var1 = var0.getLong(1);
        Key var3 = Registry.getKey(var1);
        if(var3 != null) {
            String var4 = var0.getString(2);
            long var5 = var0.getLong(3);
            Person var7 = null;
            if(var5 != 0L) {
                var7 = PersonMapper.findPerson(var5);
            }

            var3.number = var4;
            var3.person = var7;
        }
    }

    public static boolean insertKey(Key var0) {
        if(var0.getID() != 0L) {
            return false;
        } else {
            try {
                Statement var1 = DB.createStatement();
                var1.execute("INSERT INTO tblKey2 (RoomNumber) values(\'" + var0.getNumber() + "\')");
                ResultSet var2 = var1.executeQuery("SELECT @@IDENTITY");
                var2.next();
                long var3 = var2.getLong(1);
                var0.setID(var3);
                var1.close();
                var2.close();
                Registry.put(var0);
                return true;
            } catch (SQLException var5) {
                var5.printStackTrace();
                return false;
            }
        }
    }

    public static boolean deleteKey(Key var0) {
        PreparedStatement var1 = null;
        Object var2 = null;

        try {
            var1 = DB.prepare("DELETE FROM tblKey2 WHERE ID = ?");
            var1.setLong(1, var0.getID());
            var1.execute();
            var1.close();
            Registry.remove(var0);
            return true;
        } catch (SQLException var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static boolean updateKey(Key var0, Person var1) {
        PreparedStatement var2 = null;
        Object var3 = null;

        try {
            if(var1 != null) {
                var2 = DB.prepare("UPDATE tblKey2 set PersonID = ?, Date = NULL  WHERE ID = ?");
                var2.setLong(1, var1.getID());
                var2.setLong(2, var0.getID());
            } else {
                var2 = DB.prepare("UPDATE tblKey2 set PersonID = NULL, Date = NULL  WHERE ID = ?");
                var2.setLong(1, var0.getID());
            }

            var2.execute();
            var2.close();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean updateKeyNumber(Key var0, String var1) {
        PreparedStatement var2 = null;
        Object var3 = null;

        try {
            var2 = DB.prepare("UPDATE tblKey2 set RoomNumber = ?, Date = NULL  WHERE ID = ?");
            var2.setString(1, var1);
            var2.setLong(2, var0.getID());
            var2.execute();
            var2.close();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean insertKeyPermit(long var0, long var2) {
        PreparedStatement var4 = null;
        Object var5 = null;

        try {
            var4 = DB.prepare("INSERT INTO tblKeyPermit2 VALUES(?,?)");
            var4.setLong(1, var0);
            var4.setLong(2, var2);
            var4.execute();
            var4.close();
            return true;
        } catch (SQLException var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public static boolean deleteKeyPermit(long var0, long var2) {
        PreparedStatement var4 = null;
        Object var5 = null;

        try {
            var4 = DB.prepare("DELETE FROM tblKeyPermit2 WHERE PersonID = ? AND KeyID = ? ");
            var4.setLong(1, var0);
            var4.setLong(2, var2);
            var4.execute();
            var4.close();
            return true;
        } catch (SQLException var7) {
            var7.printStackTrace();
            return false;
        }
    }
}

