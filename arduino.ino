#include <ESP8266WiFi.h>
#include <Milkcocoa.h>
/////////////////////////////////////////////
#include "DHT.h"
#define DHTTYPE DHT22   // DHT 22
#define PI 3.141592653589

/************************* WiFi Access Point *********************************/
// Wi-Fi SSID
#define WLAN_SSID       "Test001"
// Wi-Fi PASSWORD
#define WLAN_PASS       "hashimoto"
/************************* Your Milkcocoa Setup *********************************/

#define MILKCOCOA_APP_ID      "vuejb91il2k"
#define MILKCOCOA_DATASTORE   "test"

/************* Milkcocoa Setup (you don't need to change this!) ******************/

#define MILKCOCOA_SERVERPORT  1883

/************ Global State (you don't need to change this!) ******************/

// Create an ESP8266 WiFiClient class to connect to the MQTT server.
WiFiClient client;

const char MQTT_SERVER[] PROGMEM    = MILKCOCOA_APP_ID ".mlkcca.com";
const char MQTT_CLIENTID[] PROGMEM  = __TIME__ MILKCOCOA_APP_ID;

Milkcocoa milkcocoa = Milkcocoa(&client, MQTT_SERVER, MILKCOCOA_SERVERPORT, MILKCOCOA_APP_ID, MQTT_CLIENTID);
//-------------------------------------------------------------------------

const int DHTPin = 15; //nodemcu pind8
DHT dht(DHTPin, DHTTYPE);

// Wifi setup
void setupWiFi() {
  Serial.println(); Serial.println();
  Serial.print("Connecting to ");
  Serial.println(WLAN_SSID);

  WiFi.begin(WLAN_SSID, WLAN_PASS);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

//---------------------------------------------------
void setup() {
  Serial.begin(115200);
  dht.begin();
  setupWiFi();
  // Milkcocoaへデータがsendされたか監視
  milkcocoa.on(MILKCOCOA_DATASTORE, "push", onSend);
};

//---------------------------------------------------
void loop() {
  milkcocoa.loop();
  // データ格納用のオブジェクトです
  DataElement elem = DataElement();
  float tmp = vh();
  // 以下のようにkey, valueを設定していきます。

  if (tmp > 0 && tmp < 99999.0){
  elem.setValue("tmp", tmp*100);

    // データストア名を指定して、データをpushもしくはsendします
    milkcocoa.push(MILKCOCOA_DATASTORE, &elem);

  }
  delay(1000);

};
//---------------------------------------------------

void onSend(DataElement *pelem) {

};

float vh() {
  float tem; //Temperature
  float hum; //Humidity
  float vh; //absolute humidity
  float r = 3.3; // デバイスの半径
  float a = 5; // デバイスの高さ
  float s; //デバイスの面積
  float v; //デバイスの容積
  float m; //1平方センチ発汗量
  float e = 0.07;
  s = PI * r * r;
  v = s * a;
  tem = dht.readTemperature();
  hum = dht.readHumidity();
  vh = 217 * (pow(6.1078, (7.5 * tem / (tem + 237.3)))) / (tem + 273.15) * hum / 100;
  m = vh * v / s * e;
  return m;
}
