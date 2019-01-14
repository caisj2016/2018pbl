package com.example.demo;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.junit.Test;

import com.mlkcca.client.DataElement;
import com.mlkcca.client.DataElementValue;
import com.mlkcca.client.DataStore;
import com.mlkcca.client.DataStoreEventListener;
import com.mlkcca.client.MilkCocoa;
import com.mlkcca.client.MilkcocoaException;
import com.mlkcca.client.Streaming;
import com.mlkcca.client.StreamingListener;

public class MilkcocoService {
		@Test
	    public void content() throws InterruptedException, JSONException, MilkcocoaException {
	        MilkCocoa milkCocoa = new MilkCocoa("vuejb91il2k.mlkcca.com");
	        DataStore dataStore = milkCocoa.dataStore("message");
	        Streaming streaming = dataStore.streaming();
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
	        dataStore.addDataStoreEventListener(new DataStoreEventListener() {
	            @Override
	            public void onPushed(DataElement dataElement) {
	                System.out.print("onPushed:");
	                System.out.println(dataElement.getValue());
	            }

	            @Override
	            public void onSetted(DataElement dataElement) {

	            }

	            @Override
	            public void onSended(DataElement dataElement) {
	                System.out.print("onSended:");
	                // System.out.println(dataElement.getValue());
	            }

	            @Override
	            public void onRemoved(DataElement dataElement) {

	            }
	        });
	        dataStore.on("push");
	        dataStore.on("send");

	        while(true) {
	            Thread.sleep(500);
	            Date date = new Date();
	            DataElementValue params = new DataElementValue();
	             params.put("message", date.getTime());
	           dataStore.push(params);

	        }


	    }
}
