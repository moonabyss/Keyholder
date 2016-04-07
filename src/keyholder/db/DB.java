package keyholder.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import keyholder.ApplicationError;
import keyholder.db.ConnectionParams;
import keyholder.db.DBUpdateMonitor;
import keyholder.db.FileStoredParams;

public class DB {
    static Connection connection;
    static DBUpdateMonitor dbMonitor;

    public DB() {
    }

    public static boolean connect(ConnectionParams var0) throws ApplicationError {
        if(var0 == null) {
            var0 = createDefaultConnectionParams();
        }

        try {
            String var1 = "jdbc:jtds:sqlserver://" + var0.getHostname() + ":" + var0.getPort() + "/" + var0.getDatabase();
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(var1, var0.getUser(), var0.getPassword());
            if(connection == null) {
                return false;
            } else {
                dbMonitor = new DBUpdateMonitor(var0);
                return true;
            }
        } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
            return false;
        } catch (SQLException var4) {
            String var2 = var4.getSQLState();
            if(var2.equals("08S01")) {
                throw new ApplicationError("Invalid host IP or port number");
            } else {
                throw new ApplicationError(var4.getMessage());
            }
        }
    }

    public static DBUpdateMonitor getDbMonitor() {
        return dbMonitor;
    }

    public static PreparedStatement prepare(String var0) throws SQLException {
        return connection.prepareStatement(var0);
    }

    public static Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    private static ConnectionParams createDefaultConnectionParams() throws ApplicationError {
        try {
            return new FileStoredParams("config.ini");
        } catch (FileNotFoundException var1) {
            throw new ApplicationError("Не найден файл config.ini");
        } catch (IOException var2) {
            throw new ApplicationError("Файл config.ini поврежден");
        }
    }
}

