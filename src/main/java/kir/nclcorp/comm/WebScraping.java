package kir.nclcorp.comm;

import org.jsoup.nodes.Document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jsoup.Jsoup;
public class WebScraping {
    //private final ObjectMapper objectMapper;
    //private final ResourceLoader resourceLoader;

    public static List<Map<String, String>> doScrape() {
        String URL = "http://sensor.iothouse.kr:7999/view/monitor.do?group_id=DAEJEON&sensor_seq=97&search_date=2022-08-01&search_time=&search_use_yn=Y";
        Document doc = null;
        try{
            doc = (Document) Jsoup.connect(URL).get();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        System.out.println(doc);
        return new ArrayList<>();
    }
}
