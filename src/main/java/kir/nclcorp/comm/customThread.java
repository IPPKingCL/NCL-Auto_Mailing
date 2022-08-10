package kir.nclcorp.comm;

import java.time.LocalTime;

public class customThread implements Runnable {
    public static customThread instance = new customThread();

    @Override
    public void run() {
        boolean flag = false;

        while(true) {
            LocalTime localTime = LocalTime.now();

            if (localTime.getHour() == 7) {
                if (!flag) {
                    Thread mailThread = new Thread(new sendMail());
                    mailThread.start();
                    flag = true;
                }
            }
            else {
                flag = false;
            }
        }//while end
    }//run end
}//customThread end
class sendMail implements Runnable {

    @Override
    public void run() {

    }
}
