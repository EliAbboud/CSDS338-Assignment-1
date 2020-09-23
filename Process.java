import java.util.*;

public class Process extends Thread implements Comparable<Process>{
  //this process's priority. 1 is high, 2 is low
  public final int PRIORITY;
  //when the process was created
  public int arrivalTimestamp;
  //when this process first recieved cpu time 
  public int responseTimestamp;
  //when this process entirely finished execution
  public int completionTimestamp;
  //miliseconds spent context switching
  public int contextSwitchingTime;
  //miliseconds of work to do upon arrival
  public int burstTime;
  //miliseconds of work left
  public int workLeft;
  //simulates RAM storage
  public Object[] storage;
  //simulates cpu register
  public Object[] cpuRegister;
  
  //initialize above fields
  public Process(int priority, int burstTime, int overhead, int delay){
    PRIORITY = priority;
    this.burstTime = burstTime;
    workLeft = burstTime;
    arrivalTimestamp =  (int)System.currentTimeMillis()+delay;
    responseTimestamp = -1;
    completionTimestamp = -1;
    //made up data to simulate context-switching
    storage = new Object[overhead]; 
    Arrays.fill(storage,new Object());
  }
  
  @Override
  public void run(){
    //if first time running, record arrivalTimestamp
    if (responseTimestamp==-1)
      responseTimestamp = (int)System.currentTimeMillis();
    //timestamp for the begining of the current cpu timeslice
    int start = (int)System.currentTimeMillis();
    //move data onto the cpu register
    contextSwitch(null, false);
    try{
    //sleeping simulates work
    Thread.currentThread().sleep(workLeft);
    //record completion time
    completionTimestamp = (int)System.currentTimeMillis();
    }
    //an InterruptedException is thrown if pre-empted
    catch(InterruptedException e){
      System.out.println("I was interrupted");
      //if pre-empted, context switch off the cpu register
      contextSwitch(start, true);
    }
  }
  
  public boolean incomplete(){
    return completionTimestamp == -1;
  }
  
  //to simulate context switching, data is moved back and forth between the storage and cpuRegister fields
  //timestamps are used to track context-switching time
  public void contextSwitch(Integer start, boolean switchOff){
      int contextSwitchingTimestamp = (int)System.currentTimeMillis();
      if(switchOff){
        workLeft = workLeft + start - (int)System.currentTimeMillis();
        storage = cpuRegister.clone();
      }
      else
        cpuRegister= storage.clone();
      contextSwitchingTime += (int)System.currentTimeMillis() - contextSwitchingTimestamp;
  }
  
  //return priority
  public int getPRIORITY(){
    return PRIORITY;
  }
  
   //return arrivalTimestamp
  public int getArrivalTimestamp(){
    return arrivalTimestamp;
  }
  
  //implemented to enable usage of a PriorityQueue
  @Override
  public int compareTo(Process process){
    //it hasn't officially arrived yet, it's sent straight to back of the queue
    if(getArrivalTimestamp() > (int)System.currentTimeMillis())
      return Integer.MAX_VALUE;
    //the highest priority process always come first
    else if(getPRIORITY() != process.getPRIORITY())
      return getPRIORITY() - process.getPRIORITY();
    //if threads have the same priority, they are sorted based on arrival time
    else
      return getArrivalTimestamp() - process.getArrivalTimestamp();
  }
  
  //Returns all the time statistics for this process
  public int[] getStats(){
    int responseTime = responseTimestamp - arrivalTimestamp;
    int completionTime = completionTimestamp - arrivalTimestamp;
    int timeWaiting = completionTime - contextSwitchingTime - burstTime;
    return new int[]{responseTime, burstTime, contextSwitchingTime, timeWaiting, completionTime, getPRIORITY()};
  }
  
}