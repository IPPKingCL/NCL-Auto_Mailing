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

		emailService.sendSimpleMessage("yoongyu@nclworks.com","Test","테스트 이메일입니다.");


		return "home";
	}
	@RequestMapping(value = "/scrap", method = RequestMethod.GET)
	public String scrap(Locale locale, Model model) {
		WebScraping wc = new WebScraping();
		wc.doScrape("2022-08-08", "97");
		return "home";
	}
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Locale locale, Model model) {
		Thread t = new Thread(new sendMail(emailService));
		t.start();
		return "home";
	}

	@RequestMapping(value = "/excel/{date}", method = RequestMethod.GET)
	public String scrapToExcel(@PathVariable("date") String date) {
		WebScraping wc = new WebScraping();
		sendMail calc = new sendMail(emailService);
		Integer seq = 97;

		List<Map<String,String>> list97 = wc.doScrape(date,"97");
		List<Map<String,String>> list98 = wc.doScrape(date,"98");
		List<Map<String,String>> list99 = wc.doScrape(date,"99");
		List<Map<String,String>> list100 = wc.doScrape(date,"100");
		List<Map<String,String>> list101 = wc.doScrape(date,"101");

		Map<String, Integer> map97 = calc.calcCount(list97);
		System.out.println(map97);
		excelService.insertToExcel(map97, date, seq);
		Map<String, Integer> map98 = calc.calcCount(list98);
		System.out.println(map98);
		excelService.insertToExcel(map98, date , seq+1);
		Map<String, Integer> map99 = calc.calcCount(list99);
		System.out.println(map99);
		excelService.insertToExcel(map99, date , seq+2);
		Map<String, Integer> map100 = calc.calcCount(list100);
		System.out.println(map100);
		excelService.insertToExcel(map100, date , seq+3);
		Map<String, Integer> map101 = calc.calcCount(list101);
		System.out.println(map101);
		excelService.insertToExcel(map101, date , seq+4);

		return "home";
	}
}
