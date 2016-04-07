package keyholder;

import java.awt.EventQueue;
import keyholder.Application;
import keyholder.ApplicationError;
import keyholder.db.DB;

class DBMonitoringThread extends Thread {
    static long personID = 0L;

    DBMonitoringThread() {
    }

    public void run() {
        try {
            while(true) {
                if(DB.getDbMonitor().hasNewRegistration()) {
                    personID = DB.getDbMonitor().getLastPersonID();
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            Application.showPerson(DBMonitoringThread.personID);
                        }
                    });
                }

                sleep(1000L);
            }
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        } catch (final ApplicationError var3) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    Application.showApplicationError(var3);
                    System.exit(1);
                }
            });
        }

    }
}
