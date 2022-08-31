package kir.nclcorp.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getPackage().getName());

    private static String API_URL = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getMsrstnList";
    private static String AIRKOREA_API_SERVICE_KEY = "s3FCx6soPjML6rIQUv7s9zFT222uWpkTQ6V1XaJbpXjIXE16aAHt06hXMtU7x5cikt2nk6HU%2BdybfSSzPy3juA%3D%3D";
    private List<AirKoreaEnvVO> parsingAirKoreaEnvVOList;

    private List<AirKoreaEnvVO> inputDataAirKoreaEnvVoList = new ArrayList<AirKoreaEnvVO>();

    @Autowired
    ExcelService excelService;


    public void callAirkoreaAPI(String location) {

        // API URL 생성
        String urlStr = makeAPIUrl(location);
        logger.error("---------[AIRKOREA API URL STR] : {}", urlStr);

        // API 호출
        String dataStr = callAPI(urlStr);

        // 데이터 파싱 XML
        List<AirKoreaEnvVO> airkoreaEnvVO = parsingDataXml(dataStr);

        // 데이터 저장
        excelService.locationInsertToExcel(airkoreaEnvVO, location);
    }

    // API URL 생성
    private String makeAPIUrl(String location) {
        StringBuilder urlBuilder = new StringBuilder(API_URL);

        try {
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + AIRKOREA_API_SERVICE_KEY);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + 1);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + 100);
            urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
//			urlBuilder.append("&" + URLEncoder.encode("stationName", "UTF-8") + "="+ URLEncoder.encode(airKoreaInfoVO.getMonitoring(), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("addr", "UTF-8") + "="+ URLEncoder.encode(location, "UTF-8"));
//			urlBuilder.append("&" + URLEncoder.encode("dataTerm", "UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8"));
//            urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.3", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("---------[AIRKOREA API URL 생성 에러 - UnsupportedEncodingException] error : {}", e);
            return null;
        } catch (Exception e) {
            logger.error("---------[AIRKOREA API URL 생성 에러 - Exception] error : {}", e);
            return null;
        }

        return urlBuilder.toString();
    }

    // API 호출
    private String callAPI(String urlStr) {
        URL url;
        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setConnectTimeout(3000); // 상대방 서버통신 오류로 인해 접속 지연시 강제 로 timeout 처리
            conn.setReadTimeout(3000); // 상대방 서버에서 응답이 오지 않은경우 timeout 처리

            BufferedReader rd;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            conn.disconnect();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("---------[AIRKOREA CALL API 에러 - IOException] error : {}", e);
            return null;
        } catch (Exception e) {
            logger.error("---------[AIRKOREA CALL API 에러 - Exception] error : {}", e);
            return null;
        }

        return sb.toString();
    }

    private List<AirKoreaEnvVO> parsingDataXml(String dataStr) {
        try {
            parsingAirKoreaEnvVOList = new ArrayList<AirKoreaEnvVO>();
            InputSource inputSource = new InputSource(new StringReader(dataStr));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputSource);
            document.getDocumentElement().normalize(); // 정규화

            Element root = document.getDocumentElement();
            NodeList items = root.getElementsByTagName("items").item(0).getChildNodes();
            System.out.println("NodeListSize = " + items.getLength());
            for(int i=0; i< items.getLength(); i++) {
                Node node = items.item(i);
                System.out.println("nodeType = " + node.getNodeType());
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    AirKoreaEnvVO airKoreaEnvVO = new AirKoreaEnvVO();
                    airKoreaEnvVO.setDmX(getTagValue("dmX", (Element) node));
                    airKoreaEnvVO.setDmY(getTagValue("dmY", (Element) node));
                    airKoreaEnvVO.setMangName(getTagValue("mangName", (Element) node));
                    airKoreaEnvVO.setStationName(getTagValue("stationName", (Element) node));
                    airKoreaEnvVO.setAddr(getTagValue("addr", (Element) node));
                    System.out.println("airkorea dmx= " + airKoreaEnvVO.getDmX());
                    System.out.println("airkorea dmy= " + airKoreaEnvVO.getDmY());
                    System.out.println("airkorea addr= " + airKoreaEnvVO.getAddr());
                    System.out.println("airkorea station= " + airKoreaEnvVO.getStationName());
                    System.out.println("airkorea mangname= " + airKoreaEnvVO.getMangName());
                    parsingAirKoreaEnvVOList.add(airKoreaEnvVO);
                }
                System.out.println("parsingAirkoreaEnvVOList size = " + parsingAirKoreaEnvVOList.size());
            }

            return parsingAirKoreaEnvVOList;
        } catch (Exception e) {
            logger.error("---------[AIRKOREA API Parsing 에러 - Exception] error : {}", e);
        }
        return null;
    }

    private String getTagValue(String sTag, Element element) {
        try{
            String result = element.getElementsByTagName(sTag).item(0).getTextContent();
            return result;
        } catch(NullPointerException e){
            return "";
        } catch(Exception e){
            return "";
        }
    }

}
