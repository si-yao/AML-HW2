import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by szeyiu on 3/15/15.
 */
public class Report {
    static String featType;
    public static void main(String[] args) throws Exception{
        featType = args[0];//= args[0];//00
        String prefix = "./"+featType+"/"+featType+".";
        List<List<Double>> A = new ArrayList<List<Double>>();
        List<List<Double>> B = new ArrayList<List<Double>>();
        for(int i=1; i<=10; ++i){
            A.add(new ArrayList<Double>());
            B.add(new ArrayList<Double>());
            List<Double> alst = A.get(i-1);
            List<Double> blst = B.get(i-1);
            B.add(new ArrayList<Double>());
            File fa = new File(prefix+i+".a.result");
            File fb = new File(prefix+i+".b.result");
            BufferedReader ra = new BufferedReader(new InputStreamReader(new FileInputStream(fa)));
            BufferedReader rb = new BufferedReader(new InputStreamReader(new FileInputStream(fb)));
            String la, lb;
            ra.readLine();
            rb.readLine();
            la = ra.readLine();
            lb = rb.readLine();
            while(la!=null){
                alst.add(Double.valueOf(la.split("\\t")[1]));
                blst.add(Double.valueOf(lb.split("\\t")[1]));
                la = ra.readLine();
                lb = rb.readLine();
            }
            ra.close();
            rb.close();
        }
        double[] aRst = new double[10];
        double[] bRst = new double[10];
        Arrays.fill(aRst,0.0);
        Arrays.fill(bRst,0.0);
        for(int i=1; i<=10; ++i){
            List<Double> alst = A.get(i-1);
            int minIdx = 0;
            double min = alst.get(minIdx);
            for(int j=0; j<alst.size(); ++j){
                if(min>alst.get(j)){
                    minIdx = j;
                    min = alst.get(j);
                }
            }
            bRst[i-1] = B.get(i-1).get(minIdx);
            List<Double> blst = B.get(i-1);
            minIdx = 0;
            min = blst.get(minIdx);
            for(int j=0; j<blst.size(); ++j){
                if(min > blst.get(j)){
                    minIdx = j;
                    min = blst.get(j);
                }
            }
            aRst[i-1] = A.get(i-1).get(minIdx);
        }

        System.out.println("A loss\tB loss");
        for(int i=0; i<10; ++i){
            System.out.print(aRst[i] + "\t" + bRst[i] + "\n");
        }

    }
}
