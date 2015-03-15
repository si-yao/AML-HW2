import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindC {
    static String fTrain;
    static String fTest;
    static String fModel;
    static String fOut;
    public static void main(String[] args) throws Exception{
        // write your code here
        String featType = args[0];//00
        String trainNum = args[1];//1
        String testNum = args[2];//a
        fTrain = "./"+featType+"/"+featType+"."+trainNum+".in";
        fTest = "./"+featType+"/"+featType+"."+trainNum+"."+testNum+".test";
        fModel = "./"+featType+"/"+featType+"."+trainNum+".model";
        fOut = "./"+featType+"/"+featType+"."+trainNum+"."+testNum+".out";
        List<Double> rst = findMin();
        System.out.println("\n****************************\nFIND OPTIMUM C: " + rst.get(0) + "\nLOSS WITH OPT C: "+rst.get(1)+"\n");
    }

    public static List<Double> findMin() throws Exception{
        double low = 0.001;
        double high = 200;
        double mid = (low+high)/2;
        //double Alow = testLoss(low);
        //double Ahigh = testLoss(high);
        double Amid = testLoss(mid);

        while((mid-low)/mid > 0.05) {
            double mid0 = mid - 0.05*mid;
            double Amid0 = testLoss(mid0);
            if(Amid0 > Amid){
                low = mid;
                //Alow = Amid;
            } else {
                high = mid;
                //Ahigh = Amid;
            }
            mid = (low+high)/2;
            Amid = testLoss(mid);
        }
        List<Double> rst = new ArrayList<Double>();
        rst.add(mid);
        rst.add(Amid);
        return rst;
    }



    public static void train(double c) throws Exception{
        String command = "./svm_tool/svm_hmm_learn -c "+c+" -e 0.1 "+fTrain+" "+fModel;
        System.out.println(command);
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        Thread stdoutT = new Thread(new Consume(stdout));
        stdoutT.start();
        Thread stderrT = new Thread(new Consume(stderr));
        stderrT.start();
        process.waitFor();
    }

    private static class Consume implements Runnable{
        BufferedReader reader;
        public Consume(BufferedReader reader){
            this.reader = reader;
        }
        @Override
        public void run(){
            try {
                String line = reader.readLine();
                int i = 0;
                while (line!= null){
                    i ++;
                    if(i%200==0)
                        System.out.println(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static double testLoss(double c) throws Exception{
        train(c);
        String command = "./svm_tool/svm_hmm_classify "+fTest+" "+fModel+" "+fOut;
        System.out.println(command);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        Future<Double> stdoutFuture = executorService.submit(new ParseLoss(stdout));
        Future<Double> stderrFuture = executorService.submit(new ParseLoss(stderr));
        double result1 = stdoutFuture.get();
        double result2 = stderrFuture.get();
        return (result1>=0)? result1: result2;
    }

    private static class ParseLoss implements Callable<Double>{
        BufferedReader reader;
        public ParseLoss(BufferedReader reader){
            this.reader = reader;
        }
        @Override
        public Double call(){
            double result = -1;
            try {
                String KEY = "Average loss per token: ";
                String line = reader.readLine();
                while(line!=null){
                    System.out.println(line);
                    if(!line.contains(KEY)){
                        line = reader.readLine();
                        continue;
                    }
                    int idx = line.indexOf(KEY);
                    System.out.println("\n*******GET LOSS*******\n"+line);
                    result = Double.valueOf(line.substring(idx+KEY.length()));
                    line = reader.readLine();
                }
            } catch (Exception  e){
                e.printStackTrace();
            }
            return result;
        }
    }


}
