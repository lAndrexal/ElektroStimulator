
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>

/* Set these to your desired credentials. */
const char *ssid = "ElektroStimul1";
const char *password = "012345678";

boolean flagIntr = 1;

String start;

ESP8266WebServer server(80);

/* Just a little test message.  Go to http://192.168.4.1 in a web browser
   connected to this access point to see it.
*/




/*void params() // функция приема данных с сервера
  {
  String message = "Число параметров:";
  message += server.args();      // получить количество параметров
  message += "\n";               // переход на новую строку

  for (int i = 0; i < server.args(); i++)
  {
    message += server.argName(i) + ": ";      // получить имя параметра
    message += server.arg(i) + "\n";          // получить значение параметра
  }
  Serial.println(message);
  server.send(200);
  }*/


void params() // функция приема данных с сервера
{
  String message = "";
  for (int i = 0; i < server.args() - 1; i++)
  {
    message += server.arg(i) + ",";          // получить значение параметра
  }
  message += server.arg(server.args() - 1) + ";";
  Serial.print(message);
  server.send(200);
}



void Resume()
{
  //Serial.println("Вернулись в меню выбора");
  flagIntr = !flagIntr;
  digitalWrite(D2, flagIntr);
  server.send(200);
}

void square()
{
  //Serial.println("Выбрали прямоугольный режим");
  Serial.print(1);
  server.send(200);
}

void Const()
{
  //Serial.println("Выбрали постоянный режим");
  Serial.print(2);
  server.send(200);
}

void single()
{
  //Serial.println("Выбрали однократный режим");
  Serial.print(3);
  server.send(200);
}

void rise()
{
  //Serial.println("Выбрали нарастающий режим");
  Serial.print(4);
  server.send(200);
}


void send_data() // функция отправки данных на сервер
{
  int timer = millis() / 1000;
  String key = String(timer);
  //String key = value;
  server.send(200, "text/plane", key);
}


void setup() {
  delay(1000);
  Serial.begin(115200);

  Serial.println();
  // Serial.print("Configuring access point...");
  /* You can remove the password parameter if you want the AP to be open. */
  WiFi.softAP(ssid, password);
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(D2, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH); //LED OFF
  IPAddress myIP = WiFi.softAPIP();

  // Serial.print("AP IP address: ");
  // Serial.println(myIP);
  // server.on("/", handleRoot);
  server.on("/parameters", params);
  server.on("/resume", Resume);
  server.on("/square", square);
  server.on("/Const", Const);
  server.on("/single", single);
  server.on("/rise", rise);
  server.begin();
  Serial.println("HTTP server started");
}



void loop() {
  server.handleClient();
}
