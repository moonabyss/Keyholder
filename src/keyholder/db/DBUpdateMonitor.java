package keyholder.db;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import keyholder.ApplicationError;
import keyholder.db.ConnectionParams;
import keyholder.db.DB;

public class DBUpdateMonitor {
    static final String dbTimestampSql = "select @@dbts";
    static final String dbTableCountSql = "select count(*) from tblAllRegistrations";
    static final String dbTableTimestampSql = "select colAccountNumber,colSystemID from tblAllRegistrations where colPointNumber = ? and colSystemID = ( select max(colSystemID) from tblAllRegistrations )";
    long pointNumber = 0L;
    long lastDBTimestamp = 0L;
    long lastRegistratonTableCount = 0L;
    long lastRegistratonTimestamp = 0L;
    long lastPersonID = 0L;

    public DBUpdateMonitor(ConnectionParams var1) throws ApplicationError {
        this.pointNumber = var1.getPointNumber();
        this.lastDBTimestamp = this.getDBTimestamp();
        this.lastRegistratonTableCount = this.getRegistratonTableCount();
        this.lastRegistratonTimestamp = this.getRegistratonTableTimestamp();
    }

    public boolean hasNewRegistration() throws ApplicationError {
        long var1 = this.getDBTimestamp();
        if(var1 == this.lastDBTimestamp) {
            return false;
        } else {
            this.lastDBTimestamp = var1;
            long var3 = this.getRegistratonTableCount();
            if(var3 == this.lastRegistratonTableCount) {
                return false;
            } else {
                this.lastRegistratonTableCount = var3;
                long var5 = this.getRegistratonTableTimestamp();
                if(var5 == this.lastRegistratonTimestamp) {
                    return false;
                } else {
                    this.lastRegistratonTimestamp = var5;
                    return true;
                }
            }
        }
    }

    public long getLastPersonID() {
        return this.lastPersonID;
    }

    public long getDBTimestamp() throws ApplicationError {
        try {
            long var1 = 0L;
            PreparedStatement var3 = DB.prepare("select @@dbts");
            ResultSet var4 = var3.executeQuery();
            var4.next();
            BigInteger var5 = new BigInteger(var4.getBytes(1));
            var1 = var5.longValue();
            var3.close();
            var4.close();
            return var1;
        } catch (SQLException var6) {
            if(var6.getSQLState().equals("HY010")) {
                throw new ApplicationError(new String[]{"Соединение с MSSQL сервером разорвано.", "Проверьте соединение и перезапустите программу."});
            } else {
                var6.printStackTrace();
                return 0L;
            }
        }
    }

    public long getRegistratonTableCount() throws ApplicationError {
        try {
            long var1 = 0L;
            PreparedStatement var3 = DB.prepare("select count(*) from tblAllRegistrations");
            ResultSet var4 = var3.executeQuery();
            var4.next();
            var1 = var4.getLong(1);
            var3.close();
            var4.close();
            return var1;
        } catch (SQLException var5) {
            if(var5.getSQLState().equals("HY010")) {
                throw new ApplicationError(new String[]{"Соединение с MSSQL сервером разорвано.", "Проверьте соединение и перезапустите программу."});
            } else {
                var5.printStackTrace();
                return 0L;
            }
        }
    }

    public long getRegistratonTableTimestamp() throws ApplicationError {
        try {
            long var1 = 0L;
            PreparedStatement var3 = DB.prepare("select colAccountNumber,colSystemID from tblAllRegistrations where colPointNumber = ? and colSystemID = ( select max(colSystemID) from tblAllRegistrations )");
            var3.setLong(1, this.pointNumber);
            ResultSet var4 = var3.executeQuery();
            if(!var4.next()) {
                return this.lastRegistratonTimestamp;
            } else {
                this.lastPersonID = var4.getLong(1);
                BigInteger var5 = new BigInteger(var4.getBytes(2));
                var1 = var5.longValue();
                var3.close();
                var4.close();
                return var1;
            }
        } catch (SQLException var6) {
            if(var6.getSQLState().equals("HY010")) {
                throw new ApplicationError(new String[]{"Соединение с MSSQL сервером разорвано.", "Проверьте соединение и перезапустите программу."});
            } else {
                var6.printStackTrace();
                return 0L;
            }
        }
    }
}

