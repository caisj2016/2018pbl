package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mlkcca.client.MilkcocoaException;

@Controller
public class HelloController {
	@ModelAttribute
	PersonForm setupForm() {
		return new PersonForm();
	}

	@Autowired
	private ApiService apiService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("message", "Hello PBL");
		return "index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index1(Model model) {
		model.addAttribute("personForm", new PersonForm());
		return "sweat";
	}

	@RequestMapping(value = "/submit")
	public String submit(PersonForm personForm, Model model) {
		// 計算ロジック
		double bsa = Math.pow(Double.valueOf(personForm.getHeight()), 0.725)
				* Math.pow(Double.valueOf(personForm.getWeight()), 0.425) * 0.007184; // 体表面積
		model.addAttribute("bsa", bsa);
		model.addAttribute("drink", null);
		model.addAttribute("wc", String.valueOf(getWc(personForm)));
		return "sweat";
	}

	@RequestMapping(value = "/kesang")
	@ResponseBody
	public List<String> kesang(Model model, @RequestParam("height") String height, @RequestParam("weight") String weight,
			@RequestParam("datakey") String datakey) throws InterruptedException, JSONException, MilkcocoaException {
		List<String> list = new ArrayList<String>();
		if (!"".equals(height) && !"".equals(weight)) {
			String level = apiService.getLevel(height, weight, datakey);
    		list.add(String.valueOf(apiService.getdrink(height, weight, level)));
    		list.add(level);
		}
		return list;
	}

	@RequestMapping(value = "/mail")
	public String sendMail(PersonForm personForm, Model model, @RequestParam("mail") String mail) {
		apiService.sentmail(mail);
		return "sweat";
	}

	private int getWc(PersonForm personForm) {
		int wc;
		int age = Integer.valueOf(personForm.getAge());
		int m = Integer.valueOf(personForm.getWeight());
		if (age < 20) {
			wc = m * 35;
		} else if (age >= 20 && age <= 60) {
			wc = m * 33;
		} else {
			wc = m * 30;
		}
		return wc;
	}

}
