package kir.nclcorp.comm;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
//import org.jsoup.nodes.Elements;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.Array;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class WebScraping {
    //private final ObjectMapper objectMapper;
    //private final ResourceLoader resourceLoader;

    public static List<Map<String, String>> doScrape(String date, String seq) {
        String URL = "http://sensor.iothouse.kr:7999/view/monitor.do?group_id=DAEJEON&sensor_seq="+seq+"&search_date="+date+"&search_time=&search_use_yn=Y";
        Document doc = null;
        try{
            doc = (Document) Jsoup.connect(URL).get();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        Elements elements1 = doc.select("tbody > tr > td > p >span");
        Elements elements2 = doc.select("tbody > tr > td > p >strong");

        String[] arr = elements1.toString().split("<span>");

        int lastLen = arr.length/16;

        List<Map<String, String>> list = new ArrayList<Map<String,String>>();

        for(int i=0; i<lastLen; i++){
            Map<String,String> map = new HashMap<String,String>();

            map.put("센서시간",elements2.get((i*16)).text());
            map.put("등록시간",elements2.get((i*16)+1).text());
            map.put("PM10",elements2.get((i*16)+2).text());
            map.put("PM2.5",elements2.get((i*16)+3).text());
            map.put("PM1.0",elements2.get((i*16)+4).text());
            map.put("HUMI",elements2.get((i*16)+5).text());
            map.put("TEMP",elements2.get((i*16)+6).text());
            map.put("CO2",elements2.get((i*16)+7).text());
            map.put("VOC",elements2.get((i*16)+8).text());
            map.put("CO",elements2.get((i*16)+9).text());
            map.put("HCHO",elements2.get((i*16)+10).text());
            map.put("NO2",elements2.get((i*16)+11).text());
            map.put("O3",elements2.get((i*16)+12).text());
            map.put("SO2",elements2.get((i*16)+13).text());
            map.put("WD",elements2.get((i*16)+14).text());
            map.put("WS",elements2.get((i*16)+15).text());

            list.add(map);


        }
        return list;
    }
}
