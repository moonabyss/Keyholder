package keyholder.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import keyholder.db.DB;
import keyholder.db.Registry;
import keyholder.domain.Person;

public class PersonMapper {
    private static final String findAllPersonSql = "SELECT colAccountNumber, colCardNumber,colName, colSurname, colMiddleName,colStatus, colWorkPhone, colPhotoFile FROM tblAllPersons";
    private static final String findPersonSql = "SELECT colAccountNumber, colCardNumber,colName, colSurname, colMiddleName,colStatus, colWorkPhone, colPhotoFile FROM tblAllPersons WHERE colAccountNumber = ?";
    private static final String findPersonWithPermitSql = "SELECT colAccountNumber, colCardNumber,colName, colSurname, colMiddleName,colStatus, colWorkPhone, colPhotoFile FROM tblAllPersons WHERE colAccountNumber IN  (SELECT PersonID FROM tblKeyPermit2 WHERE KeyID = ? )";

    public PersonMapper() {
    }

    public static Person findPerson(long var0) {
        if(var0 == 0L) {
            return null;
        } else {
            Person var2 = Registry.getPerson(var0);
            if(var2 != null) {
                return var2;
            } else {
                PreparedStatement var3 = null;
                ResultSet var4 = null;

                try {
                    var3 = DB.prepare("SELECT colAccountNumber, colCardNumber,colName, colSurname, colMiddleName,colStatus, colWorkPhone, colPhotoFile FROM tblAllPersons WHERE colAccountNumber = ?");
                    var3.setLong(1, var0);
                    var4 = var3.executeQuery();
                    var4.next();
                    var2 = loadPerson(var4);
                    var4.close();
                    var3.close();
                    return var2;
                } catch (SQLException var6) {
                    var6.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static ArrayList<Person> findAllPersons() {
        ArrayList var0 = new ArrayList();
        PreparedStatement var1 = null;
        ResultSet var2 = null;

        try {
            var1 = DB.prepare("SELECT colAccountNumber, colCardNumber,colName, colSurname, colMiddleName,colStatus, colWorkPhone, colPhotoFile FROM tblAllPersons");
            var2 = var1.executeQuery();

            while(var2.next()) {
                var0.add(loadPerson(var2));
            }

            var2.close();
            var1.close();
            return var0;
        } catch (SQLException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private static Person loadPerson(ResultSet var0) throws SQLException {
        long var1 = var0.getLong(1);
        Person var3 = Registry.getPerson(var1);
        if(var3 != null) {
            return var3;
        } else {
            long var4 = var0.getLong(2);
            String var6 = var0.getString(3);
            String var7 = var0.getString(4);
            String var8 = var0.getString(5);
            String var9 = var0.getString(6);
            String var10 = var0.getString(7);
            String var11 = var0.getString(8);
            var3 = new Person(var1, var4, var6, var7, var8, var9, var10, var11);
            Registry.put(var3);
            return var3;
        }
    }

    public static ArrayList findPersonsWithKeyPermit(long var0) {
        ArrayList var2 = new ArrayList();
        PreparedStatement var3 = null;
        ResultSet var4 = null;

        try {
            var3 = DB.prepare("SELECT colAccountNumber, colCardNumber,colName, colSurname, colMiddleName,colStatus, colWorkPhone, colPhotoFile FROM tblAllPersons WHERE colAccountNumber IN  (SELECT PersonID FROM tblKeyPermit2 WHERE KeyID = ? )");
            var3.setLong(1, var0);
            var4 = var3.executeQuery();

            while(var4.next()) {
                var2.add(loadPerson(var4));
            }

            var4.close();
            var3.close();
            return var2;
        } catch (SQLException var6) {
            var6.printStackTrace();
            return null;
        }
    }
}

