
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author NaveedPunjwani
 */
public class JobScheduler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            int choice;
            Scanner reader = new Scanner(System.in);
          
            do{
            	showMenu();
            choice = reader.nextInt();
            
            FileReader fileReader = new FileReader("jobs.txt");                 //reading from file "jobs.txt"
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<Job> jobQueue = fillJobsQueue(bufferedReader); //this function reads Jobs from file and initializes list of Jobs
                  
            bufferedReader.close();
            fileReader.close();
            switch(choice)
            {
                case 1:  
                  
                        FCFS fcfs = new FCFS(jobQueue);
                        fcfs.strategy();
                        break;
                        
                case 2: 
                 
                  
                        RR rr = new RR(jobQueue);
                        rr.strategy();     
                        break;
                        
                 case 3: 
                 
                       
                  
                        SPN spn = new SPN(jobQueue);
                        spn.strategy();     
                        break;
                        
                 case 4: 
                        
                  
                        SRT srt = new SRT(jobQueue);
                        srt.strategy();    
                        break;
                        
                  case 5: 
                        
                  
                        HRRN hrrn = new HRRN(jobQueue);
                hrrn.strategy();     
                        break;
                        
                        
                 case 6: 
                       
       
                        FD fd = new FD(jobQueue);
                fd.strategy();    
                        break;
                        
                 case 7: 
                        System.exit(0);
                        break;
            }
            
            
            }while(choice != 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Job> fillJobsQueue(BufferedReader bufferedReader) {
        try {
            String jobPCB = bufferedReader.readLine();                          // reading one job from file
           //System.out.println(jobPCB);
            String name;
            double arrivalTime, duration;
            StringTokenizer st;                                                 // string tokenizer to tokenize the line into name of job , arrival time and duration
            Job job;
            List<Job> jobQueue2 = new ArrayList<Job>();
            int i = 0;
            while (jobPCB != null) {                                            //read till the end of file

                st = new StringTokenizer(jobPCB, "\t");                         // tokenizing string with \t
                while (st.hasMoreTokens()) {                                    // read all the tokens from string
                    name = st.nextToken();                                      // First token is name of Job
                    arrivalTime = Double.parseDouble(st.nextToken());           // Second is arrival time , converting string into double
                    duration = Double.parseDouble(st.nextToken());              // Third token is duration
                    jobQueue2.add(i, new Job(name, arrivalTime, duration));     // adding job in job queue
                    i++;
                }
                jobPCB = bufferedReader.readLine();                             //reading next job
            }
            return jobQueue2;                                                   // returning list of jobs
        } catch (Exception e) {                                                 // catch IO Exception
            e.printStackTrace();
            return null;
        }

    }

    public static void showMenu() {
        System.out.println("\nWelcome to the Job Scheduler Program!");
        System.out.println("-------------------------------------");
        System.out.println("Please select the method you would like to employ or press 7 to exit :");
        System.out.println("1) FCFS");
        System.out.println("2) Round Robin");
        System.out.println("3) SPN");
        System.out.println("4) SRT");
        System.out.println("5) HRRN");
        System.out.println("6) Feedback");
        System.out.println("7) Exit");
    }

}

abstract class Scheduler {

    double time;                                                                // CPU time
    List<Job> jobsQueue;                                                        // list of jobs to schedule

    public Scheduler(List<Job> _jobsQueue) {                                    // constructor 
        jobsQueue = _jobsQueue;
        time = 0;
    }

    public void tick() {
        time++;
    }

    public abstract void strategy();                                            // Each sub class (Schedulling Algo) will define it in its own way

}

class Job {

    private String name;
    private double arrivalTime;
    private double duration;

    public Job(String _name, double _arrivalTime, double _duration) {           // parameterized constructor
        name = _name;
        arrivalTime = _arrivalTime;
        duration = _duration;

    }

    void run() {                                                                // job will start executing by calling this function
        for (int i = 0; i < duration; i++) {
            System.out.print(name);                                             //job prints its name while executing
        }
    }

    public String getName() {                                                   // getters
        return name;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setArrivalTime(double time) {                                   //setter
        arrivalTime = time;
    }

    public void passOneQuantum() {
        duration--;
    }
}

class FCFS extends Scheduler {

    public FCFS(List<Job> _jobQueue) {                                          // constructor takes job Queue as papamenter    
        super(_jobQueue);
    }

    public void strategy() {
        for (int i = 0; i < jobsQueue.size(); i++) {
            System.out.print("\n");                                             // to get the process in the next line
            for (int j = 0; j < time; j++) {                                    //printing spaces where process is not in the system or is waiting
                System.out.print(" ");
            }
            for (int j = 0; j < jobsQueue.get(i).getDuration(); j++) {          // printing process name where it is allocated CPU
                System.out.print(jobsQueue.get(i).getName());

            }
            time = time + jobsQueue.get(i).getDuration();                       // updating CPU time when process completes its execution
        }

    }

}

class SPN extends Scheduler {

    public SPN(List<Job> _jobQueue) {                                           // parameterized constructor
        super(_jobQueue);
    }

    public void strategy() {
        int[] actualProcessAllocationTime = new int[jobsQueue.size()];
        double[] processesAllocationTime = getProcessesCPUAllocationTime();        // holds information when processor will be allocated to each process
        for (int j = 0; j < jobsQueue.size(); j++) {
            System.out.println("");
            for (int k = 0; k < processesAllocationTime[j]; k++) {              //printing spaces where process is not in the system or is waiting
                System.out.print(" ");
            }
            for (int k = 0; k < jobsQueue.get(j).getDuration(); k++) {          // printing process name where it is allocated CPU
                System.out.print(jobsQueue.get(j).getName());
            }
        }
        // }

    }

    double[] getProcessesCPUAllocationTime() {                                  // return actual time when each processor is allocated to processes
        int smallestProcessIndex = 0;
        double[] processesAllocationTime = new double[jobsQueue.size()];
        for (int j = 0; j < jobsQueue.size(); j++) {                            // finding shortest process and finding time when processor will be allocated to it

            for (int i = 0; i < jobsQueue.size(); i++) {
                if (jobsQueue.get(i).getDuration() < jobsQueue.get(smallestProcessIndex).getDuration() && jobsQueue.get(i).getArrivalTime() <= time && jobsQueue.get(i).getArrivalTime() != -1) {
                    smallestProcessIndex = i;

                }
            }
            processesAllocationTime[smallestProcessIndex] = time;               // assigning time
            jobsQueue.get(smallestProcessIndex).setArrivalTime(-1);             // placing -1 in the arrival time of processes whose actual starting execution time is found
            time = time + jobsQueue.get(smallestProcessIndex).getDuration();    //updating CPU time when process completes its execution
            smallestProcessIndex = 1;
        }
        return processesAllocationTime;                                         //returning array
    }

}

class HRRN extends Scheduler {

    public HRRN(List<Job> _jobQueue) {                                          // parameterized constructor
        super(_jobQueue);
    }

    public void strategy() {
        int[] actualProcessAllocationTime = new int[jobsQueue.size()];

        double[] processesAllocationTime = getProcessesCPUAllocationTime();      // holds information when processor will be allocated to each process
        for (int j = 0; j < jobsQueue.size(); j++) {
            System.out.println("");
            for (int k = 0; k < processesAllocationTime[j]; k++) {              //printing spaces where process is not in the system or is waiting
                System.out.print(" ");
            }
            for (int k = 0; k < jobsQueue.get(j).getDuration(); k++) {          // printing process name where it is allocated CPU
                System.out.print(jobsQueue.get(j).getName());
            }
        }

    }

    double[] getProcessesCPUAllocationTime() {
        int maximumRatioProcessIndex;
        double waitingTime;
        double[] processesAllocationTime = new double[jobsQueue.size()];
        double[] processRatio = new double[jobsQueue.size()];
        for (int j = 0; j < jobsQueue.size(); j++) {

            for (int i = 0; i < jobsQueue.size(); i++) {
                if (jobsQueue.get(i).getArrivalTime() <= time && jobsQueue.get(i).getArrivalTime() != -1) {
                    waitingTime = time - jobsQueue.get(i).getArrivalTime();     // finding waiting time CPU current time - process arrival time
                    processRatio[i] = (waitingTime + jobsQueue.get(i).getDuration()) / jobsQueue.get(i).getDuration(); // calculating ratio R = (S+w)/s

                }
            }
            maximumRatioProcessIndex = findMaximumRatio(processRatio);          // finding process index having maximum ratio 
            processesAllocationTime[maximumRatioProcessIndex] = time;           // assigning time when this process will be allocated processor
            jobsQueue.get(maximumRatioProcessIndex).setArrivalTime(-1);         // processes whose time has been computed, assign -1 in their arrival time
            time = time + jobsQueue.get(maximumRatioProcessIndex).getDuration(); //updating CPU time when process completes its execution
            clear(processRatio);                                                // clearing process ratio array so that previous values can be cleared and maximum can be found accurately

        }
        return processesAllocationTime;
    }

    int findMaximumRatio(double[] processesRatio) {                             // returns maximum ratio by using linear search
        int maximumRatioProcessIndex = 0;
        for (int i = 0; i < jobsQueue.size(); i++) {
            if (processesRatio[i] > processesRatio[maximumRatioProcessIndex]) {
                maximumRatioProcessIndex = i;

            }

        }
        return maximumRatioProcessIndex;
    }

    void clear(double[] array) {                                                // clearing process ratio array so that previous values can be cleared and maximum can be found accurately
        for (int i = 0; i < jobsQueue.size(); i++) {
            array[i] = 0;
        }
    }
}

class SRT extends Scheduler {

    int[] execution = new int[findSize()];
    Queue<Integer> readyQueue = new LinkedList<Integer>();                      // creating queue
    int i = 0, processID, k = 0;

    public SRT(List<Job> _jobQueue) {
        super(_jobQueue);
    }

    public void strategy() {
        readyQueue.add(i);                                                      // adding first process in it i.e. 0 is for process A
        while (readyQueue.size() != 0) {
            try {
                processID = readyQueue.remove();                                // removing process from queue , allocating 1 sec of CPU to it
                tick();                                                         // CPU time = CPU time + i.e. quantum = 1
                jobsQueue.get(processID).passOneQuantum();                      // decrease one execution time as it has executed in 1sec
                execution[k] = processID;                                       // storing its ID in execution array to keep record at which time process has executed
                k++;
                if (i + 1 < jobsQueue.size() && time == jobsQueue.get(i + 1).getArrivalTime()) { // checking if the next process has shorter remaining time 

                    if (jobsQueue.get(i + 1).getDuration() > jobsQueue.get(processID).getDuration() && jobsQueue.get(processID).getDuration() > 0) {
                      
                        while (jobsQueue.get(processID).getDuration() > 0) {    // if not so current process is executed completely
                           
                            tick();
                            jobsQueue.get(processID).passOneQuantum();                      // decrease one execution time as it has executed in 1sec
                            execution[k] = processID;                                       // storing its ID in execution array to keep record at which time process has executed
                            k++;
                        }
                        readyQueue.add(i + 1);                                      // If there is some other process arrived then place it iun ready queue
                        i++;

                    } else {
                        int ID = readyQueue.remove();
                        readyQueue.add(i + 1);                                      // If so then new process has moved in the ready queue and current process ahs moved after it
                        readyQueue.add(ID);
                    }
                    readyQueue.add(i + 1);                                      
                    i++;
                } else if (jobsQueue.get(processID).getDuration() > 0) {               // If its execution time is not completed then again place it in ready queue
                    readyQueue.add(processID);
                }

            } catch (Exception e) {
                System.out.println("No such element exception");
            }
        }
        readExecution();                                                        // reading execution array
    }

    private int findSize() {                                                    //// execution array has the size equal to sum of all the duration times
        double sum = 0;                                                         // of processes. because quantum  = 1 thats this size is selected
        for (int i = 0; i < jobsQueue.size(); i++) {
            sum = sum + jobsQueue.get(i).getDuration();
        }
        return (int) Math.round(sum);
    }

    private boolean isFilled(int size) {
        boolean flag = false;
        for (int i = 0; i < size && !flag; i++) {
            if (execution[i] == 0) {
                flag = false;
            }
        }
        return flag;
    }

    void readExecution() {
        for (int j = 0; j < jobsQueue.size(); j++) {                            // for each process , inner function will called
            readProcessExecution(j);
        }

    }

    private void readProcessExecution(int processID) {                          // at indexes where it finds process ID , prints process name else prints space
        for (int j = 0; j < execution.length; j++) {
            if (execution[j] == processID) {
                System.out.print(jobsQueue.get(processID).getName());
            } else {
                System.out.print(" ");
            }
        }
        System.out.print("\n");                                                 // to get the process in the next line
    }

}

class RR extends Scheduler {

    int[] execution = new int[findSize()];
    Queue<Integer> readyQueue = new LinkedList<Integer>();                      // creating queue
    int i = 0, processID, k = 0;

    public RR(List<Job> _jobQueue) {
        super(_jobQueue);
    }

    public void strategy() {
        readyQueue.add(i);                                                      // adding first process in it i.e. 0 is for process A
        while (readyQueue.size() != 0) {
            try {
                processID = readyQueue.remove();                                // removing process from queue , allocating 1 sec of CPU to it
                tick();                                                         // CPU time = CPU time + i.e. quantum = 1
                if (i + 1 < jobsQueue.size() && time == jobsQueue.get(i + 1).getArrivalTime()) {
                    readyQueue.add(i + 1);                                      // If there is some other process arrived then place it iun ready queue
                    i++;
                }
                jobsQueue.get(processID).passOneQuantum();                      // decrease one execution time as it has executed in 1sec
                execution[k] = processID;                                       // storing its ID in execution array to keep record at which time process has executed
                k++;
                if (jobsQueue.get(processID).getDuration() > 0) {               // If its execution time is not completed then again place it in ready queue
                    readyQueue.add(processID);
                }
            } catch (Exception e) {
                System.out.println("No such element exception");
            }
        }
        readExecution();                                                        // reading execution array
    }

    private int findSize() {                                                    //// execution array has the size equal to sum of all the duration times
        double sum = 0;                                                         // of processes. because quantum  = 1 thats this size is selected
        for (int i = 0; i < jobsQueue.size(); i++) {
            sum = sum + jobsQueue.get(i).getDuration();
        }
        return (int) Math.round(sum);
    }

    private boolean isFilled(int size) {
        boolean flag = false;
        for (int i = 0; i < size && !flag; i++) {
            if (execution[i] == 0) {
                flag = false;
            }
        }
        return flag;
    }

    void readExecution() {
        for (int j = 0; j < jobsQueue.size(); j++) {                            // for each process , inner function will called
            readProcessExecution(j);
        }

    }

    private void readProcessExecution(int processID) {                          // at indexes where it finds process ID , prints process name else prints space
        for (int j = 0; j < execution.length; j++) {
            if (execution[j] == processID) {
                System.out.print(jobsQueue.get(processID).getName());
            } else {
                System.out.print(" ");
            }
        }
        System.out.print("\n");                                                 // to get the process in the next line
    }

}

class FD extends Scheduler {

    int[] execution = new int[findSize()];
    Queue<Integer> readyQueue1 = new LinkedList<Integer>();
    Queue<Integer> readyQueue2 = new LinkedList<Integer>();
    Queue<Integer> readyQueue3 = new LinkedList<Integer>();
    int i = 0, processID, k = 0;

    public FD(List<Job> _jobQueue) {
        super(_jobQueue);
    }

    public void strategy() {
        readyQueue1.add(i);                                                      // adding first process in it i.e. 0 is for process A
        while (readyQueue1.size() != 0) {
            try {
                processID = readyQueue1.remove();                                // removing process from queue , allocating 1 sec of CPU to it
                tick();                                                         // CPU time = CPU time + i.e. quantum = 1
                if (i + 1 < jobsQueue.size() && time == jobsQueue.get(i + 1).getArrivalTime()) {
                    readyQueue1.add(i + 1);                                      // If there is some other process arrived then place it iun ready queue
                    i++;
                }
                jobsQueue.get(processID).passOneQuantum();                      // decrease one execution time as it has executed in 1sec
                execution[k] = processID;                                       // storing its ID in execution array to keep record at which time process has executed
                k++;
                if (jobsQueue.get(processID).getDuration() > 0) {               // If its execution time is not completed then again place it in ready queue
                    readyQueue1.add(processID);
                }
            } catch (Exception e) {
                System.out.println("No such element exception");
            }
        }
        readExecution();                                                        // reading execution array
    }

    private int findSize() {
        double sum = 0;
        for (int i = 0; i < jobsQueue.size(); i++) {                            // execution array has the size equal to sum of all the duration times
            // of processes. because quantum  = 1 thats this size is selected
            sum = sum + jobsQueue.get(i).getDuration();
        }
        return (int) Math.round(sum);
    }

    private boolean isFilled(int size) {
        boolean flag = false;
        for (int i = 0; i < size && !flag; i++) {
            if (execution[i] == 0) {
                flag = false;
            }
        }
        return flag;
    }

    void readExecution() {
        for (int j = 0; j < jobsQueue.size(); j++) {                            // for each process , inner function will called
            readProcessExecution(j);
        }

    }

    private void readProcessExecution(int processID) {                          // at indexes where it finds process ID , prints process name else prints space
        for (int j = 0; j < execution.length; j++) {
            if (execution[j] == processID) {
                System.out.print(jobsQueue.get(processID).getName());
            } else {
                System.out.print(" ");
            }
        }
        System.out.print("\n");                                                 // to get the process in the next line
    }

}
