package kir.nclcorp.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalTime;
import java.util.Properties;

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

    public customThread() {
        EmailService.instance.sendSimpleMessage("hjkwon0814@nclwork.com","Test","테스트 이메일입니다");
    }
}//customThread end
class sendMail implements Runnable {

    @Override
    public void run() {

    }
}
@Component
class EmailService {

    public static EmailService instance = new EmailService();
    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yoongyu@nclworks.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }
}
