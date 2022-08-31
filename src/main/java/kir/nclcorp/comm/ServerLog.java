package kir.nclcorp.comm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServerLog extends JFrame {

    public boolean SaveSwitch = false;
    public static ServerLog instance = new ServerLog();

    public List<String> list = new ArrayList<String>();

    public JButton makeBtn = new JButton("쓰레드 만들기");
    public JTextField textField = new JTextField(3);
    public JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JTextArea area = new JTextArea(10,20);
    public List<Thread> threadList = new ArrayList<>();

    public ServerLog() {
        setSize(500, 500); //크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        setTitle("서버 GUI");

        setLayout(new BorderLayout()); //배치 관리자 설정

        //컴포넌트 생성 및 추가
        area.setSize(300,300);

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        northPanel.add(makeBtn);
        northPanel.add(textField);
        westPanel.add(new JLabel("쓰레드 목록"));
        westPanel.setSize(500,30);

        c.add(area, BorderLayout.CENTER);


        c.add(northPanel, BorderLayout.NORTH);
        c.add(westPanel, BorderLayout.SOUTH);

        System.out.println("Manager Thread has started");

        Thread t = new Thread(new ManagerFile());
        Thread t2 = new Thread(new DisPlayLog());

        makeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new CheckThread(Integer.parseInt(textField.getText())));
                threadList.add(t);
                t.start();
                JButton b = new JButton(textField.getText());
                b.setSize(50,20);
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        t.interrupt();
                        westPanel.remove(b);
                        threadList.remove(t);
                    }
                });
                westPanel.add(b);
            }
        });

        t.start();
        t2.start();

        setVisible(true);

    }

    public void WriteLog(String timeStamp, String content) {
        list.add(timeStamp + " : " + content);
    }

}

class ManagerFile implements Runnable {

    @Override
    public void run() {
        System.out.println("Automatic log saving thread has started");

        while (true) {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm");
            int formatedNow = Integer.parseInt(now.format(formatter));
            if (formatedNow % 2 == 0) {
                if (!ServerLog.instance.SaveSwitch) {
                    Thread t = new Thread(new FileOut(ServerLog.instance.list));
                    t.start();
                    ServerLog.instance.SaveSwitch = true;
                }
            } else {
                ServerLog.instance.SaveSwitch = false;

            }
        }
    }
}


class FileOut implements Runnable {
    private List<String> list;

    @Override
    public void run() {
        synchronized (list) {
            System.out.println("Auto Save doing");
            try {

                File file = new File("C:\\\\Users\\\\NCL-NT-0162\\\\Desktop\\\\log.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = null;
                fw = new FileWriter(file, true);

                BufferedWriter writer = new BufferedWriter(fw);
                for (String s: list) {
                    writer.write(s+"\n");
                }
                list.clear();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Save Done");
        }
    }


    public FileOut(List<String> list) {
        this.list = list;
    }
}

class DisPlayLog implements Runnable {
    @Override
    public void run() {
        while(true) {
            ServerLog.instance.area.setText("");
            synchronized (ServerLog.instance.list) {
                for (String s : ServerLog.instance.list) {
                    ServerLog.instance.area.append(s + "\n");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class CheckThread implements Runnable {
    private int checkTime;
    public CheckThread(int checkTime) {
        this.checkTime = checkTime;
    }
    boolean flag = false;
    @Override
    public void run() {
        while(true) {
            if (LocalTime.now().getSecond() %checkTime == 0) {
                if (!flag) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                    String nowString = formatter.format(now);
                    synchronized (ServerLog.instance.list) {
                        ServerLog.instance.list.add("지금은 " + nowString + ", "+checkTime+"의 배수 초입니다");
                    }
                    flag = true;
                }
            }
            else {
                flag = false;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
