package keyholder.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import keyholder.ApplicationError;
import keyholder.db.ConnectionParams;

class FileStoredParams implements ConnectionParams {
    String fileName;
    Properties properties;

    FileStoredParams(String var1) throws FileNotFoundException, IOException {
        this.fileName = var1;
        this.properties = new Properties();
        this.properties.load(new FileInputStream(var1));
    }

    public String getHostname() {
        return this.properties.getProperty("host", "localhost");
    }

    public String getPort() {
        return this.properties.getProperty("port", "1433");
    }

    public String getUser() {
        return this.properties.getProperty("login", "sa");
    }

    public String getPassword() {
        return this.properties.getProperty("password", "");
    }

    public String getDatabase() {
        return this.properties.getProperty("database", "NextAccess");
    }

    public long getPointNumber() throws ApplicationError {
        String var1 = this.properties.getProperty("point_number", "11");

        try {
            return Long.parseLong(var1);
        } catch (NumberFormatException var3) {
            throw new ApplicationError("В " + this.fileName + " параметр point_number емеет не числовое значение: " + var1);
        }
    }
}

