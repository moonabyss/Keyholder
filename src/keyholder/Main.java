package keyholder;

import keyholder.Application;
import keyholder.ApplicationError;
import keyholder.db.ConnectionParams;
import keyholder.db.DB;

public class Main {
    public Main() {
    }

    public static void main(String[] var0) {
        if(var0.length != 0) {
            try {
                if(!DB.connect((ConnectionParams)null)) {
                    return;
                }

                if(var0[0].equals("keyholder")) {
                    Application.RunKeyholder();
                } else if(var0[0].equals("keyholderConfig")) {
                    Application.RunKeyholderConfig();
                }
            } catch (ApplicationError var2) {
                Application.showApplicationError(var2);
                System.exit(0);
            }

        }
    }
}
