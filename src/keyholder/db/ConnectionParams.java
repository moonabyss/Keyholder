package keyholder.db;

import keyholder.ApplicationError;

public interface ConnectionParams {
    String getHostname() throws ApplicationError;

    String getPort() throws ApplicationError;

    String getUser() throws ApplicationError;

    String getPassword() throws ApplicationError;

    String getDatabase() throws ApplicationError;

    long getPointNumber() throws ApplicationError;
}
