
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Test.reset();

        String skipListFile = "skip.data";
        String sourceListFile = "source.data";
        
        String trainFile = "train.data";
        String testFile = "test.data";
        
        //Test.booleanVectorTest(testFile);
        //Test.numericVectorTest(testFile);
        //Test.booleanVectorTest(trainFile);
        //Test.numericVectorTest(trainFile);
        
        DataHandler dh = new DataHandler(skipListFile, sourceListFile);       
               
        //ArrayList trainfBVL = dh.createBooleanVectorNLog(trainFile);
        //System.out.println("DBV for train is finished.");
        ArrayList trainNVL = dh.createNumericVector(trainFile);
        System.out.println("DNV for train is finished.");
        
        //ArrayList testBVL = dh.createBooleanVectorNLog(testFile);
        //System.out.println("DBV for test is finished.");
        ArrayList testNVL = dh.createNumericVector(testFile);       
        System.out.println("DNV for test is finished.");
        
        //HashMap hmBTrain = dh.cateorizeBooleanVector(trainfBVL);
        HashMap hmNTrain = dh.cateorizeNumericVector(trainNVL);
        //HashMap hmBTest = dh.cateorizeBooleanVector(testBVL);
        HashMap hmNTest = dh.cateorizeNumericVector(testNVL);
        System.out.println("Class categorization done.");
        
        //HashMap hmSTrainB = dh.booleanSummary(hmBTrain, trainfBVL.size());
        //System.out.println("boolean summary done.");
        
        HashMap hmSTrainN = dh.numericSummary(hmNTrain, trainNVL.size());
        System.out.println("numeric summary done.");
        
        System.out.println("===============================");
        
        Worker wrk = new Worker();
        int smoothingFactor = 1;
        //wrk.decideByBernoulliModel(hmSTrainB, testBVL, smoothingFactor);
        wrk.decideByMultinomialModel(hmSTrainN, testNVL, smoothingFactor);
        
        /*
        DocSummary bds;
        Iterator itr = hmSTrainN.keySet().iterator();
        Iterator itr2;
        ArrayList al;
        String str;
        String str2;
        while(itr.hasNext()){
            str = (String) itr.next(); 
            bds = (DocSummary) hmSTrainN.get(str);
            itr2 = bds.getDocCountMap().keySet().iterator();
            while(itr2.hasNext()){
                str2 = (String) itr2.next();
                System.out.println(str2+" "+bds.getCount(str2));
            }
            System.out.println(str+" "+bds.getTotalDoc());
            System.out.println("------------------");
        }
        */
    }
}
