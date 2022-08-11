package kir.nclcorp.comm;

import java.text.DateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {
	
	private customThread ct = customThread.instance;
	@Autowired
	MailService emailService;

	@Autowired
	ExcelService excelService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate );

		System.out.println("안녕@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

		emailService.sendSimpleMessage("qudqud97@nclworks.com","Test","테스트 이메일입니다.");


		return "home";
	}
	@RequestMapping(value = "/scrap", method = RequestMethod.GET)
	public String scrap(Locale locale, Model model) {
		WebScraping wc = new WebScraping();

		wc.doScrape("2022-08-08","97");
		return "home";
	}

	@RequestMapping(value = "/scrap/exceltest/{date}/{seq}", method = RequestMethod.GET)
	public String scrapIntoExcel(Model model, @PathVariable("date") String date, @PathVariable("seq") String seq) {
		WebScraping wc = new WebScraping();
		List<Map<String, String>> mapList = new ArrayList<>();
		mapList = wc.doScrape(date,seq);
		excelService.insertToExcel(mapList);
		return "home";
	}
}
