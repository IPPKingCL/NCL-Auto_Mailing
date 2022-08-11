package kir.nclcorp.comm;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {
	
	private customThread ct = customThread.instance;
	@Autowired
	MailService emailService;

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

		return "home";
	}
}
