String end = String("E");
String split = String("S");
String start = String("F");
String temp;
String n;
int steps[3];
int tsteps[3] = {-1000,-1000,-1000};
int const numSteppers = 3;
int const stepPins[numSteppers] = {2,4,6};
int const dirPins[numSteppers] = {3,5,7};
int const limitPins[numSteppers] = {8,9,10};
float const speed = 0.1;
int limit[3] = {0,0,0};
int counter;

void getCommand(){
  while(true){
      temp = String((char)serialRead());
       
      if (temp.equals(split)){
        //Serial.print("s");
        steps[counter] = n.toInt();
        //Serial.print(steps[counter]==tsteps[counter]);
        n = "";
        counter += 1;
      }
      else if (temp.equals(start)){
        //Serial.print("f");
        counter =0;
        n = "";
      }
      else if (temp.equals(end)){
        break;
      }
      else{
        n.concat(temp);
      }
    }
    Serial.print("z");
}

char serialRead()
{
  char in;
  // Loop until input is not -1 (which means no input was available)
  while ((in = Serial.read()) == -1) {}
  return in;
}


void setup() {
  Serial.begin(9600);
  Serial.flush();
  for(int i = 0; i < numSteppers; i++){
    pinMode(dirPins[i], OUTPUT); 
    pinMode(stepPins[i], OUTPUT);
    pinMode(limitPins[i], INPUT);
  } 
}

void loop() {
  
    if(Serial.available()>0){    
      //Serial.print("a");
      getCommand(); 
      //Serial.print(steps[0]);
      //Serial.print("b");
      //Serial.print(steps[1]);
      //Serial.print("c");
      //Serial.print(steps[2]);
      Serial.print("d");

      moveDelta(steps[0],steps[1],steps[2],speed);
    } 
    
  }
  
void moveDelta(int steps1, int steps2, int steps3, float s){
   int numSteps1[numSteppers] = {steps1, steps2, steps3};
   int numSteps[numSteppers];
   
   for (int i = 0; i < numSteppers; i++){
     
     int dir = (numSteps1[i] < 0)? HIGH:LOW;
     numSteps[i] = abs(numSteps1[i]);
     digitalWrite(dirPins[i], dir);
   }
   
   float usDelay = (1/s) * 70;
   
   while(numSteps[0] > 0 || numSteps[1] > 0 || numSteps[2] > 0){
     
     for (int i = 0; i<numSteppers; i++){
       limit[i] = digitalRead(limitPins[i]);
       if (limit[i] && (numSteps1[i] >0)){
         numSteps[i]=0;
         digitalWrite(stepPins[i], LOW);
         
       }
     }
   
     for(int i = 0; i < numSteppers; i++){
       
       if (numSteps[i] > 0){
         digitalWrite(stepPins[i], HIGH);
       }   
     }
     
     delayMicroseconds(usDelay);
     
     for(int i = 0; i < numSteppers; i++){
       
       if (numSteps[i] > 0){
         digitalWrite(stepPins[i], LOW);
         numSteps[i] --;
       }     
     }
     
     delayMicroseconds(usDelay);
   }
}