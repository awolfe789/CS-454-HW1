// Alec Wolfe
// CS 454
// HW 1
// Dinning Philosopher problem

// run instructions:
// javac Philosopher.java
// java Philosopher
// then enter number of philosophers



import java.util.concurrent.Semaphore;
import java.util.Scanner;

// Philosopher class
// Holds all logic for progra
  class Philosopher extends Thread{
    Semaphore leftChopstick;
    int leftChopstickNum;
    Semaphore rightChopstick;
    int rightChopstickNum;
    Semaphore notAll;
    int philNum;
    int totalEatings;
    public boolean notDone;

    // Constructor
    Philosopher(Semaphore left, int leftNum, Semaphore right, int rightNum,Semaphore nAll, int num){
      this.leftChopstick = left;
      this.rightChopstick = right;
      this. notAll = nAll;
      this.philNum = num;
      this.totalEatings = 0;
      this.notDone = true;
      this.leftChopstickNum = leftNum;
      this.rightChopstickNum = rightNum;
    }

    // Philosopher aquires semaphores that represent each chopstick
    private void AquireChopstiks() throws InterruptedException {
      System.out.println("Philosopher #" + philNum + " is hungry");
      notAll.acquire();
      leftChopstick.acquire();
      System.out.println("Philosopher #" + philNum + " Picks up left Chopstick  #" + leftChopstickNum);
      rightChopstick.acquire();
      System.out.println("Philosopher #" + philNum + " Picks up right Chopstick #" + rightChopstickNum);
    }
    
    // Philosopher releases semaphores that represent each chopstick
    private void ReleaseChopsticks() throws InterruptedException {
      System.out.println("Philosopher #" + philNum + " Puts down left Chopstick #" + leftChopstickNum);
      leftChopstick.release();
      
      System.out.println("Philosopher #" + philNum + " Puts down right Chopstick #" + rightChopstickNum);
      rightChopstick.release();
      
      notAll.release();
      totalEatings++;
    }

    // Philosopher thinks for a somewhat random amount of time
    private void think() throws InterruptedException{
      System.out.println("Philosopher #" + philNum + " thinks...");
      Thread.sleep((int) (Math.random()*20 +500)); // think at most .52 seconds
      
    }
    // Philosopher eats for a somewhat random amount of time
    private void eat() throws InterruptedException{
      System.out.println("Philosopher #" + philNum + " is eating");
      Thread.sleep((int) (Math.random()*20 + 500)); // eat for at most .52 seconds  
    }
    // run
    // a Philosopher will think, then pick up chopsticks, then eat then put down chopsticks
    @Override
    public void run(){
      try{
        while(notDone){
        think();
        AquireChopstiks();
        eat();
        ReleaseChopsticks();
        }
      }
      catch (InterruptedException e) {
        System.out.println("something went wrong");
      }
    }
  

    public static void main(String[] args) {
      
      Scanner scan = new Scanner(System.in);
      System.out.print("Enter the number of Philosophers: ");
      int numPhil = scan.nextInt();
      Semaphore notAll = new Semaphore(numPhil - 1); // dont let every person pick up a chopstick at the same time
      Semaphore[] Chopsticks = new Semaphore[numPhil];
      Philosopher[] Phils = new Philosopher[numPhil];
      for(int i = 0; i< numPhil; i++){ // create chopsticks
        Chopsticks[i] = new Semaphore(1); // only allow one thread to hold chopstick at a time
      }
      for(int i = 0; i< numPhil - 1; i++){ // create philosophers
        Phils[i] = new Philosopher(Chopsticks[i], i, Chopsticks[i+1], i+1, notAll,  i);
        Phils[i].start();
      }
      // create last philosopher so its a circle
      Phils[numPhil-1] = new Philosopher(Chopsticks[numPhil - 1], numPhil - 1,Chopsticks[0], 0, notAll, numPhil-1);
      Phils[numPhil-1].start();
        
      try {
        Thread.sleep(10000); // let trial run for 10 seconds
        for(int i = 0; i< numPhil; i++){
          Phils[i].notDone = false; // this will stop the while loop for the philosopher
        }
        Thread.sleep(3000); // Give it 3 seconds to wrap up
      }
       
      catch (InterruptedException e) {
        System.out.println("interupted");
      }
      System.out.println("********** Results ************");
      for(int i = 0; i< numPhil; i++){
        
        System.out.println("Philosopher #"+ i +" total times eating "  + Phils[i].totalEatings + "");
      }
    }
}