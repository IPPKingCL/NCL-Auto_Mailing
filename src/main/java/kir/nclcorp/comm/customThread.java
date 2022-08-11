package kir.nclcorp.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

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
@Service
class MailService {

    @Autowired
    private MailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yoongyu@nclworks.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }
}
