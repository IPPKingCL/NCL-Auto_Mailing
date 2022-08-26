package kir.nclcorp.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThreadPool {
    public List<NamedThread> threadList = new ArrayList<>();
    public List<HashMap<String, String>> mapList = new ArrayList<>();


}

class NamedThread extends Thread {
    public String name;

    @Override
    public void run() {
        while (true) {
            Runtime.getRuntime().gc();
            long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            System.out.println(name + " is alive use "+usedMemory/1024+"KB");
            try {
                sleep(1000);
            }
            catch (Exception e) {
                System.out.println(name + " is dead");

                break;
            }
        }
    }
    public NamedThread(String name) {
        this.name = name;
    }
}
