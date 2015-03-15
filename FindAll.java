import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindAll {
    static String fTrain;
    static String fTest;
    static String fModel;
    static String fOut;
    static String fTestV;
    public static void main(String[] args) throws Exception{
        // write your code here
        String featType = args[0];//= args[0];//00
        String trainNum = args[1]; //= args[1];//1
        String testNum = "a";//= args[2];//a
        String validNum = "b";
        File fout = new File("./"+ featType + "/"+featType + "." + trainNum + "." + testNum + ".result");
        File foutV = new File("./"+ featType + "/"+featType + "." + trainNum + "." + validNum + ".result");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout)));
        BufferedWriter writerV = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(foutV)));

        fTrain = "./" + featType + "/" + featType + "." + trainNum + ".in";
        fTest = "./" + featType + "/" + featType + "." + trainNum + "." + testNum + ".test";
        fTestV = "./" + featType + "/" + featType + "." + trainNum + "." + validNum + ".test";
        fModel = "./" + featType + "/" + featType + "." + trainNum + ".model";
        fOut = "./" + featType + "/" + featType + "." + trainNum + "." + testNum + ".out";
        System.out.print(fTrain);
        writer.write(fTrain+"\n");
        writer.flush();
        writerV.write(fTrain+"\n");
        writerV.flush();
        testLoss(0.001, writer, writerV);
        testLoss(0.01, writer, writerV);
        testLoss(0.1, writer, writerV);
        testLoss(1, writer, writerV);
        for(double i=10; i<=210; i+=20){
            testLoss(i,writer, writerV);
        }
        writer.flush();
        writer.close();
        writerV.flush();
        writeV.close();
    }

/*
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
*/



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
                    if(i%1000==0)
                        System.out.println(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void testLoss(double c, BufferedWriter writer, BufferedWriter writerV) throws Exception{
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
        double result = (result1>=0)? result1: result2;
        System.out.println(c+"\t"+result);
        writer.write(c+"\t"+result+"\n");
        stdout.close();
        stderr.close();

        command = "./svm_tool/svm_hmm_classify "+fTestV+" "+fModel+" "+fOut;
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
        stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        Future<Double> stdoutFuture2 = executorService.submit(new ParseLoss(stdout));
        Future<Double> stderrFuture2 = executorService.submit(new ParseLoss(stderr));
        result1 = stdoutFuture2.get();
        result2 = stderrFuture2.get();
        result = (result1>=0)? result1: result2;
        System.out.println(c+"\t"+result);
        writerV.write(c+"\t"+result+"\n");
        writer.flush();
        writerV.flush();
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
                    if(!line.contains(KEY)){
                        line = reader.readLine();
                        continue;
                    }
                    int idx = line.indexOf(KEY);
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
