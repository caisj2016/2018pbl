package com.example.demo;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class GoogleChart {

    /** Googleから画像を取得 */
    public ImageIcon get(Map<String, String> params) throws IOException {
		Object[] keys = params.keySet().toArray();
		String url = "http://chart.apis.google.com/chart?";
		for (int i = 0; i < keys.length; i++) {
			url += (i > 0) ? "&" : "";
			url += keys[i] + "=" + params.get(keys[i]);
		}
		
		byte[] data = httpGet(url);
		return new ImageIcon(data);
	}
	
	/** 指定したURLのデータを取得 */
	private byte[] httpGet(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		
		BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
		ByteArrayOutputStream bos  = new ByteArrayOutputStream();
        
    	while (true) {
        	byte[] buffer = new byte[1024 * 10];
        	int length = input.read(buffer);
        	if (length <= 0) break;
        	bos.write(buffer, 0, length);
        }
        input.close();
		connection.disconnect();

		byte[] result = bos.toByteArray();
		bos.close();
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		// Google Char APIについての解説サイト
		// http://www.ajaxtower.jp/googlechart/
		
		// パラメータ指定
		Map<String, String> params = new HashMap<String, String>();
		params.put("chs", "300x300");
		params.put("chd", "t:60,25,10,3,2");
		params.put("cht", "p");
		
		// 画像を取得
		GoogleChart gc = new GoogleChart();
		ImageIcon image = gc.get(params);
		
		// 取得した画像を表示
		JFrame frame = new JFrame();
		frame.add(new JLabel(image));
		frame.pack();
		frame.setVisible(true);
	}
}