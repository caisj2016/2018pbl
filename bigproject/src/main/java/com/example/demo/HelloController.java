package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

/*	@Autowired
    private MailSender sender;*/


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("message", "Hello Springboot");
		return "index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index1() {
		return "sweat";
	}

	@RequestMapping(value = "/submit")
	public String submit(PersonForm personForm,Model model){

		return "index";
	}

	@RequestMapping(value = "/ajax")
	private  String kesang(){

		return "sweat";
	}


/*	@RequestMapping(value = "/mail")
	private void sendMail(PersonForm personForm,Model model) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(personForm.getEmail());
        msg.setTo("<メールアドレス>");
        msg.setSubject("テストメール");//タイトルの設定
        msg.setText("Spring Boot より本文送信"); //本文の設定

        this.sender.send(msg);
    }*/

}
