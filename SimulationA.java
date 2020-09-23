import java.util.*;

public class SimulationA{
  
  //used to moderate pending processes
  public PriorityQueue<Process> queue;
  //number of processes created by this simulation
  public int numberOfProcess;
  //burst time of each process in miliseconds
  public int burstTime;
  //time slice in miliseconds
  public int timeSlice;
  //overhead, measured in object count
  public int overhead;
  //arrival rate, measured as miliseconds between process arrivals
  public int arrivalRate;
  //probability of being high priority
  public double priorityMix;
  //used to store completed processes, so statistics can be collected later
  public ArrayList<Process> archive;
  
  //initializeabove fields and create processes
  public SimulationA(int numberOfProcess, int burstTime, int timeSlice, int overhead, int arrivalRate, double priorityMix){
    queue = new PriorityQueue<Process>(numberOfProcess);
    archive = new ArrayList<Process>(numberOfProcess);
    this.numberOfProcess = numberOfProcess;
    this.burstTime = burstTime;
    this.timeSlice = timeSlice;
    this.overhead = overhead;
    this.priorityMix = priorityMix;
    this.arrivalRate = arrivalRate;
    createProcesses();
  }
  
  //create the processes of each priority category and add them to the queue
  //to stagger arrival by 1 second, a delay variable is added
  public void createProcesses(){
    Process newProcess;
    for(int i = 0; i < numberOfProcess; i++){
      if(Math.random() < priorityMix)
        newProcess = new Process(1, burstTime, overhead, i*arrivalRate);
       else
         newProcess = new Process(2, burstTime, overhead, i*arrivalRate);
       queue.add(newProcess);
    }
  }
  
  //simulates the cpu
  public void simulate(){
    Process currentProcess;
    //retrieves the next process and runs it
    while(queue.peek() != null){
      updateQueue();
      currentProcess = queue.poll();
      currentProcess.run();
      //waits for the timeslice to expire
      pause();
      //if the process hasn't completed within its timeslice, it's interrupted and returned to the queue
      if(currentProcess.incomplete()){
        currentProcess.interrupt();
        queue.add(currentProcess);
      }
      //if it has completed, it is archived for future statistical reporting
      else{
        archive.add(currentProcess);
      }
    }
  }
  
  //basically sleep with exception-handling
  public void pause(){
    try{
      Thread.currentThread().sleep(timeSlice);
    }
    catch(InterruptedException e){}
  }
  
  //basically compiles the time statistics of each process
  //there are 5 different stats I keep track of: average response time, average burst time, average context switching time, average waiting time, average completion time
  public double[][] reportStats(){
    int numOfHighPriorityProcesses = numOfHighPriorityProcesses(archive);
    int[][] rawHighPriorityStats = new int[numOfHighPriorityProcesses][5];
    int[][] rawLowPriorityStats = new int[numberOfProcess-numOfHighPriorityProcesses][5];
    int[] temp;
    for(int i = 0, j=0; i + j < archive.size();){
      temp = archive.get(i+j).getStats();
      if(temp[temp.length-1]==1){
        rawHighPriorityStats[i]= temp;
        i++;
      }
      else{
        rawLowPriorityStats[j]= temp;
        j++;
      }
    }
    return new double[][]{average(rawHighPriorityStats),average(rawLowPriorityStats)};
  }
  
  //averages the time statistics of each process
  public double[] average(int[][] rawStats){
    int numberOfStats = rawStats[0].length;
    int numberOfProcess = rawStats.length;
    double[] avgStats = new double[numberOfStats];
    int sum;
    for(int i=0; i < numberOfStats; i++){
      sum = 0;
      for(int j=0; j<numberOfProcess; j++)
        sum += rawStats[j][i];
      avgStats[i] = sum/(double)numberOfProcess;
    }
    return avgStats;
  }
  
  //counts the number of high priority processes
  public int numOfHighPriorityProcesses(ArrayList<Process> archives){
    int count = 0;
    for(Process process : archives)
      if(process.getPRIORITY()==1)
        count++;
    return count;
  }
  
  //in case priorities have changed, update the queue
  public void updateQueue(){
    PriorityQueue<Process> newQueue = new PriorityQueue<Process>(numberOfProcess);
    while(!queue.isEmpty())
      newQueue.add(queue.remove());
    queue = newQueue;
  }
}