public class Main{
  //takes five command line arguments: number of processes, burst time (miliseconds), timeslice (miliseconds),overhead (# of objects), arrival rate (miliseconds), priority mix
  public static void main(String arg[]){
    //replace SimulationA with B and vice versa to change
    SimulationB simulation = new SimulationB(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),Integer.parseInt(arg[3]),Integer.parseInt(arg[4]),Double.parseDouble(arg[5]));
    simulation.simulate();
    //prints out some basic descriptive info
    System.out.println("-------------Simulation B-----------------");
    System.out.println("Simulation created "+arg[0]+" processes, each with a burst time of "+arg[1]+" miliseconds and an overhead of "+arg[3] +" objects");
    System.out.println("Arrival Rate: There is a " +arg[4]+ " milisecond delay between process arrival");
    System.out.println("Priority Mix: The probability of creating a high priority process is "+Double.parseDouble(arg[5])*100+"%.");
    System.out.println("Smulation utilized a time slice of "+arg[2]+" miliseconds.");
    //conducts simulation
    double[][] stats = simulation.reportStats();
    //prints out detailed statistics
    printStats("high", stats[0]);
    printStats("low", stats[1]);
  }
  
  //prints detailed time statistics
  public static void printStats(String priority, double[] stats){
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    System.out.println("The average response time for "+ priority +"-priority processes is: " + Math.round(stats[0]) + " miliseconds");
    System.out.println("The average burst time for "+ priority +"-priority processes is: " + Math.round(stats[1]) + " miliseconds");
    System.out.println("The average context switching time for "+ priority +"-priority processes is: " + Math.round(stats[2]) + " miliseconds");
    System.out.println("The average waiting time for "+ priority +"-priority processes is: " + Math.round(stats[3]) + " miliseconds");
    System.out.println("The average completion time for "+ priority +"-priority processes is: " + Math.round(stats[4]) + " miliseconds");
    System.out.println((double)Math.round(stats[2]/stats[4]*100*100)/100 + "% of the completion time was spent context switching per "+ priority +"-priority process on average");
    System.out.println((double)Math.round(stats[3]/stats[4]*100*100)/100 + "% of the completion time was spent waiting per "+ priority +"-priority process on average");
    System.out.println((double)Math.round(stats[1]/stats[4]*100*100)/100 + "% of the completion time was spent doing work per "+ priority +"-priority process on average");
  }
}