
#include <Wire.h>
#include<Adafruit_MCP4725.h>
Adafruit_MCP4725 dac;

int data[10];
volatile boolean flag = 1;



void setup() {

  dac.begin(0x60);
  dac.setVoltage(0, false);
  delay(3000);
  Serial.begin(115200);
  Serial.setTimeout(1000);

  pinMode(2, INPUT_PULLUP);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  attachInterrupt(0, myIterrupt, CHANGE);
}


void myIterrupt()
{
  flag = 0;
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  // dac.setVoltage(0, false);
}


void pars_parameters()
{

  char str[30];
  int count = 0;
  int amount = Serial.readBytesUntil(';', str, 30);
  str[amount] = NULL;
  char* offset = str;

  while (true)
  {
    data[count++] = atoi(offset);
    offset = strchr (offset, ',');
    if (offset) offset++;
    else break;
  }

}




void square()
{
  digitalWrite(5, HIGH);
  int Uamp = 0;
  int Timp = 0;
  int T = 0;
  // Serial.println("Выбрали прямоугольный режим");

  flag = 1;
  while (flag)
  {
    if (Serial.available())
    {
      pars_parameters();
      /*
        Serial.print(data[0]);
        Serial.print(data[2]);
        Serial.print(data[1]);
        Serial.print(data[3]);
      */
      Uamp = data[0] * data[3];
      Timp = data[1];
      T = data[2];
    }
    dac.setVoltage(int(float(Uamp) * 0.82), false);
    digitalWrite(6, HIGH);
    delay(Timp);
    digitalWrite(6, LOW);
    dac.setVoltage(0, false);
    delay(T - Timp);

  }
  flag = 1;
}




void Const()
{
  digitalWrite(5, HIGH);
  // Serial.println("Выбрали постоянный режим");
  int Uamp = 0;
  flag = 1;
  while (flag)
  {
    if (Serial.available())
    {
      pars_parameters();

   /*   Serial.print(data[0]);
      Serial.print(data[1]);
      Serial.print(data[2]);
      Serial.print(data[3]);*/

      Uamp = data[0] * data[1];
      //Serial.print(Uamp);
    }

    dac.setVoltage(int(float(Uamp) * 0.82), false);

    if (data[1] == 1)
      digitalWrite(6, HIGH);
    else
      digitalWrite(6, LOW);

  }
  flag = 1;
  dac.setVoltage(0, false);
}


void single()
{
  digitalWrite(5, HIGH);
  // Serial.println("Выбрали однократный режим");

  int Uamp = 0;
  int Timp = 0;
  boolean sflag = 0;
  flag = 1;
  while (flag)
  {
    if (Serial.available())
    {
      pars_parameters();
      /*
        Serial.print(data[0]);
        Serial.print(data[2]);
        Serial.print(data[1]);
        Serial.print(data[3]);
      */
      Uamp = data[0];
      Timp = data[1];
      sflag = 1;
    }
    if (sflag)
    {
      dac.setVoltage(int(float(Uamp) * 0.82), false);
      digitalWrite(6, HIGH);
      delay(Timp);
      dac.setVoltage(0, false);
      digitalWrite(6, LOW);
      sflag = 0;
    }

  }
  flag = 1;
}




void rise()
{
  digitalWrite(5, HIGH);
  // Serial.println("Выбрали нарастающий режим");
  int Uamp1 = 0;
  int Uamp2 = 0;
  long int T = 0;
  boolean sflag = 0;
  float k = 0;
  flag = 1;
  while (flag)
  {
    if (Serial.available())
    {
      pars_parameters();
      /*
        Serial.print(data[0]);
        Serial.print(data[2]);
        Serial.print(data[1]);
        Serial.print(data[3]);
      */
      Uamp1 = data[0];
      Uamp2 = data[1];
      T = data[2];
      sflag = 1;
    }

    if (sflag)
    {
      digitalWrite(6, HIGH);
      dac.setVoltage(int(float(Uamp1) * 0.82), false);

      k = float(Uamp2 - Uamp1) / float(T);
      for (long int i = 0; i <= T; i++)
      {
        delay(1);
        dac.setVoltage(int(float(Uamp1) + k * i), false);

      }
      sflag = 0;
      digitalWrite(6, LOW);
    }

  }
  flag = 1;
  dac.setVoltage(0, false);
}





void choose_mode(char a)
{

  switch (a)
  {
    case '1':
      square();
      break;
    case '2':
      Const();
      break;
    case '3':
      single();
      break;
    case '4':
      rise();
      break;

  }

}




void loop() {


  if (Serial.available())
  {
    char key = Serial.read();
    //Serial.println(key);
    //Serial.println(key);
    choose_mode(key);
  }

}
