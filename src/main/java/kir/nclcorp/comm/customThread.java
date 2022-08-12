package kir.nclcorp.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class customThread implements Runnable {
    MailService emailService;

    public customThread(MailService mailService) {
        this.emailService = mailService;
    }

    @Override
    public void run() {
        boolean flag = false;

        while (true) {
            LocalTime localTime = LocalTime.now();

            if (localTime.getHour() == 7) {
                if (!flag) {
                    Thread mailThread = new Thread(new sendMail(emailService));
                    mailThread.start();
                    flag = true;
                }
            } else {
                flag = false;
            }
        }//while end
    }//run end


}//customThread end

class sendMail implements Runnable {
    MailService emailService;

    public sendMail(MailService mailService) {
        this.emailService = mailService;
    }

    @Override
    public void run() {
        LocalDate now = LocalDate.now();
        now = now.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = now.format(formatter);

        List<Map<String, String>> list97 = WebScraping.doScrape(date, "97");
        List<Map<String, String>> list98 = WebScraping.doScrape(date, "98");
        List<Map<String, String>> list99 = WebScraping.doScrape(date, "99");
        List<Map<String, String>> list100 = WebScraping.doScrape(date, "100");
        List<Map<String, String>> list101 = WebScraping.doScrape(date, "101");

        Map<String, Integer> map97 = calcCount(list97);
        Map<String, Integer> map98 = calcCount(list98);
        Map<String, Integer> map99 = calcCount(list99);
        Map<String, Integer> map100 = calcCount(list100);
        Map<String, Integer> map101 = calcCount(list101);


        String content = "";
        content += "대종로네거리의 " + date + "의 센서 값 수";
        content += mapToString(map97);
        content += "\n\n가장교오거리의 " + date + "의 센서 값 수";
        content += mapToString(map98);
        content += "\n\n화암네거리의 " + date + "의 센서 값 수";
        content += mapToString(map99);
        content += "\n\n도룡삼거리의 " + date + "의 센서 값 수";
        content += mapToString(map100);
        content += "\n\n읍내삼거리의 " + date + "의 센서 값 수";
        content += mapToString(map101);
        content += "<table border=\"1\">" +
                "<th>테이블</th>" +
                "<th>만들기</th>" +
                "<tr>" +
                "<td>첫번째 칸</td>" +
                "<td>두번째 칸</td>" +
                "</tr>" +
                "<tr>" +
                "<td>첫번째 칸</td>" +
                "<td>두번째 칸</td>" +
                "</tr>" +
                "</table>";


        emailService.sendSimpleMessage("yoongyu@nclworks.com", "대전지역의 " + date + " 센서값", content);
    }

    public Map<String, Integer> calcCount(List<Map<String, String>> list) {
        Map<String, Integer> map = new HashMap<>();

        int zero = 0;
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int seven = 0;
        int eight = 0;
        int nine = 0;
        int ten = 0;
        int eleven = 0;
        int twelve = 0;
        int thirteen = 0;
        int fourteen = 0;
        int fifteen = 0;
        int sixteen = 0;
        int seventeen = 0;
        int eighteen = 0;
        int nineteen = 0;
        int twenty = 0;
        int twentyOne = 0;
        int twentyTwo = 0;
        int twentyThree = 0;

        for (Map<String, String> varMap : list) {
            switch (varMap.get("센서시간").substring(11, 13)) {
                case "00": zero++; break;
                case "01": one++; break;
                case "02": two++; break;
                case "03": three++; break;
                case "04": four++; break;
                case "05": five++; break;
                case "06": six++; break;
                case "07": seven++; break;
                case "08": eight++; break;
                case "09": nine++; break;
                case "10": ten++; break;
                case "11": eleven++; break;
                case "12": twelve++; break;
                case "13": thirteen++; break;
                case "14": fourteen++; break;
                case "15": fifteen++; break;
                case "16": sixteen++; break;
                case "17": seventeen++; break;
                case "18": eighteen++; break;
                case "19": nineteen++; break;
                case "20": twenty++; break;
                case "21": twentyOne++; break;
                case "22": twentyTwo++; break;
                case "23": twentyThree++; break;
            }
        }

        map.put("00", zero);
        map.put("01", one);
        map.put("02", two);
        map.put("03", three);
        map.put("04", four);
        map.put("05", five);
        map.put("06", six);
        map.put("07", seven);
        map.put("08", eight);
        map.put("09", nine);
        map.put("10", ten);
        map.put("11", eleven);
        map.put("12", twelve);
        map.put("13", thirteen);
        map.put("14", fourteen);
        map.put("15", fifteen);
        map.put("16", sixteen);
        map.put("17", seventeen);
        map.put("18", eighteen);
        map.put("19", nineteen);
        map.put("20", twenty);
        map.put("21", twentyOne);
        map.put("22", twentyTwo);
        map.put("23", twentyThree);

        return map;
    }

    public String mapToString(Map<String, Integer> map) {
        String temp = "\n";
        temp +=
                "00 : " + map.get("00") + "\n" +
                        "01 : " + map.get("01") + "\n" +
                        "02 : " + map.get("02") + "\n" +
                        "03 : " + map.get("03") + "\n" +
                        "04 : " + map.get("04") + "\n" +
                        "05 : " + map.get("05") + "\n" +
                        "06 : " + map.get("06") + "\n" +
                        "07 : " + map.get("07") + "\n" +
                        "08 : " + map.get("08") + "\n" +
                        "09 : " + map.get("09") + "\n" +
                        "10 : " + map.get("10") + "\n" +
                        "11 : " + map.get("11") + "\n" +
                        "12 : " + map.get("12") + "\n" +
                        "13 : " + map.get("13") + "\n" +
                        "14 : " + map.get("14") + "\n" +
                        "15 : " + map.get("15") + "\n" +
                        "16 : " + map.get("16") + "\n" +
                        "17 : " + map.get("17") + "\n" +
                        "18 : " + map.get("18") + "\n" +
                        "19 : " + map.get("19") + "\n" +
                        "20 : " + map.get("20") + "\n" +
                        "21 : " + map.get("21") + "\n" +
                        "22 : " + map.get("22") + "\n" +
                        "23 : " + map.get("23") + "\n";
        return temp;
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
