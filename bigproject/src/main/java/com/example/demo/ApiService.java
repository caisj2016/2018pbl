package com.example.demo;

import java.util.ArrayList;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.mlkcca.client.DataElement;
import com.mlkcca.client.DataElementValue;
import com.mlkcca.client.DataStore;
import com.mlkcca.client.DataStoreEventListener;
import com.mlkcca.client.MilkCocoa;
import com.mlkcca.client.MilkcocoaException;
import com.mlkcca.client.Streaming;
import com.mlkcca.client.StreamingListener;

@Service
public class ApiService {
	@Autowired
	private MailSender sender;

	public String getLevel(String height, String weight, String datakey)
			throws InterruptedException, JSONException, MilkcocoaException {
		String keys[] = datakey.split(",");
		int size = keys.length;
		double maxtotel = 0;
		if (size < 30) {
			return "0";
		} else {
			int sizehead = size - 30;
			for (int i = sizehead; i < size; i++) {
				maxtotel = +Double.valueOf(keys[i]);
			}
		}
		int m = Integer.valueOf(weight);
		int h; // 身長
		int age = Integer.valueOf(height);
		double s_skin;
		String level = "0";
		if (age < 20) {
			s_skin = m * 35 - m * 20 - m * 10;
		} else if (age >= 20 && age <= 60) {
			s_skin = m * 33 - m * 18 - m * 8.5;
		} else {
			s_skin = m * 30 - m * 16 - m * 7;
		}

		double s_total = maxtotel * 2 * 15;

		if (s_total <= s_skin / 96 && s_total < 0.01 * m / 96) {
			output("0");
		} else if (s_total > s_skin / 96 || (s_total >= 0.01 * m / 96 && s_total <= 0.02 * m / 96)) {
			output("1");
			level = "1";
		} else if (s_total >= 0.02 * m / 96 && s_total <= 0.03 * m / 96) {
			output("2");
			level = "2";
		} else {
			output("3");
			level = "3";
		}
		return level;
	}

	public double getdrink(String height, String weight, String level) {
		int m = Integer.valueOf(weight);
		int h; // 身長
		int age = Integer.valueOf(height);
		double drink;
		double s_skin;
		if (age < 20) {
			s_skin = m * 35 - m * 20 - m * 10;
			if (level.equals("1")) {
				drink = s_skin * 0.6;
			} else if (level.equals("2")) {
				drink = s_skin * 1.1;
			} else if (level.equals("3")) {
				drink = s_skin * 1.45;
			} else {
				drink = 0;
			}
		} else if (age >= 20 && age <= 60) {
			s_skin = m * 33 - m * 18 - m * 8.5;
			if (level.equals("1")) {
				drink = s_skin * 0.55;
			} else if (level.equals("2")) {
				drink = s_skin * 1.05;
			} else if (level.equals("3")) {
				drink = s_skin * 1.35;
			} else {
				drink = 0;
			}
		} else {
			s_skin = m * 30 - m * 16 - m * 7;
			if (level.equals("1")) {
				drink = s_skin * 0.5;
			} else if (level.equals("2")) {
				drink = s_skin;
			} else if (level.equals("3")) {
				drink = s_skin * 1.25;
			} else {
				drink = 0;
			}
		}
		return drink;
	}

	public void sentmail(String mail) {
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setFrom("caisjtest@gmail.com");
		msg.setTo(mail);
		msg.setSubject("テストメール");// タイトルの設定
		msg.setText("Spring Boot より本文送信"); // 本文の設定
		this.sender.send(msg);
	}

	private void output(String level) throws InterruptedException, JSONException, MilkcocoaException {
		// MilkCocoa milkCocoa = new MilkCocoa("vuejb91il2k.mlkcca.com");
		MilkCocoa milkCocoa = new MilkCocoa("vuejb91il2k.mlkcca.com");
		DataStore dataOutputStore = milkCocoa.dataStore("test2");
		Streaming streaming = dataOutputStore.streaming();
		streaming.size(1);
		streaming.addStreamingListener(new StreamingListener() {
			@Override
			public void onData(ArrayList<DataElement> arrayList) {
				for (DataElement element : arrayList) {
					System.out.print("onData:");
					System.out.println(element.getValue());
				}
			}

			@Override
			public void onError(Exception e) {
				System.out.print("onError:");
				e.printStackTrace();
			}
		});

		streaming.next();
		dataOutputStore.addDataStoreEventListener(new DataStoreEventListener() {
			@Override
			public void onPushed(DataElement dataElement) {
			}

			@Override
			public void onSetted(DataElement dataElement) {

			}

			@Override
			public void onSended(DataElement dataElement) {
				// System.out.println(dataElement.getValue());
			}

			@Override
			public void onRemoved(DataElement dataElement) {

			}
		});
		dataOutputStore.on("send");

		DataElementValue params = new DataElementValue();
		params.put("level", level);
		System.out.println("sent level :"+level);
		dataOutputStore.send(params);


	}
}
