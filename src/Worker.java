
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Worker {

    public Worker() {

    }

    public void decideByBernoulliModel(HashMap train, ArrayList test, int smoothing) {
        Iterator itr = test.iterator();
        Iterator itr2;

        DocumentBooleanVector tempDBV;
        DocSummary tempBDS;

        String str;
        String str2;
        String topic;
        double max;
        double tempProb;
        // for each document in the test file
        int count = 0;
        int misClass = 0;
        while (itr.hasNext()) {
            tempDBV = (DocumentBooleanVector) itr.next();

            max = Integer.MIN_VALUE;
            topic = "notopic";

            // going through the classes summarized from train
            itr2 = train.keySet().iterator();
            while (itr2.hasNext()) {
                str2 = (String) itr2.next();
                tempBDS = (DocSummary) train.get(str2);

                //getting prob for each class with the test doc
                tempProb = getPosteriorProbability(tempBDS, tempDBV, smoothing);
                if (max < tempProb) {
                    max = tempProb;
                    topic = str2;
                }
            }
            //System.out.println(topic);
            //System.out.println(tempDBV.getTopic());
            // matches
            if (topic.equals(tempDBV.getTopic())) {
                Test.csvB(topic + ", " + tempDBV.getTopic() + ", 1");
            } else {
                misClass++;
                Test.csvB(topic + ", " + tempDBV.getTopic() + ", 0");
            }
            System.out.println(++count);
        }
        
        Test.csvN(" , , ");
        Test.csvN(" , , ");
        Test.csvN("Total docs ,"+test.size()+" , ");
        Test.csvN("Misclassified ,"+misClass+" , ");
        Test.csvN("Accuracy ,"+(((test.size() - misClass) * 100.0) / test.size())+" , ");
    }

    public void decideByMultinomialModel(HashMap train, ArrayList test, int smoothing) {

        HashMap totalWordByClass = getWordCountMapByClass(train);
        int totalVocab = getTotalVocabulary(train);
                
        Iterator itr = test.iterator();
        Iterator itr2;

        DocumentNumericVector tempDNV;
        DocSummary tempDS;

        String str;
        String str2;
        String topic;
        double max;
        double tempProb;
        // for each document in the test file
        int count = 0;
        int misClass = 0;
        while (itr.hasNext()) {
            tempDNV = (DocumentNumericVector) itr.next();

            max = Integer.MIN_VALUE;
            topic = "notopic";

            // going through the classes summarized from train
            itr2 = train.keySet().iterator();
            while (itr2.hasNext()) {
                str2 = (String) itr2.next(); //class
                tempDS = (DocSummary) train.get(str2);

                //getting prob for each class with the test doc
                tempProb = getPosteriorProbability(tempDS, tempDNV, 
                        (int) totalWordByClass.get(str2), totalVocab, smoothing);
                if (max < tempProb) {
                    max = tempProb;
                    topic = str2;
                }
            }
            //System.out.println(topic);
            //System.out.println(tempDBV.getTopic());
            // matches
            if (topic.equals(tempDNV.getTopic())) {
                Test.csvN(topic + ", " + tempDNV.getTopic() + ", 1");
            } else {
                misClass++;
                Test.csvN(topic + ", " + tempDNV.getTopic() + ", 0");
            }
            System.out.println(++count);
        }
        
        Test.csvN(" , , ");
        Test.csvN(" , , ");
        Test.csvN("Total docs ,"+test.size()+" , ");
        Test.csvN("Misclassified ,"+misClass+" , ");
        Test.csvN("Accuracy ,"+(((test.size() - misClass) * 100.0) / test.size())+" , ");
    }

    private double getPosteriorProbability(DocSummary tempBDS, DocumentBooleanVector tempDBV, int sm) {
        Iterator itr = tempBDS.getDocCountMap().keySet().iterator();
        String str;
        double prob = 0.0;
        while (itr.hasNext()) {
            str = (String) itr.next();
            if (tempDBV.containsWord(str)) {
                prob = prob + Math.log(tempBDS.getCount(str) * 1.0 / (tempBDS.getTotalDoc() + sm));
            } else {
                prob = prob + Math.log(1 - (tempBDS.getCount(str) * 1.0 / (tempBDS.getTotalDoc() + sm)));
            }
        }
        prob = prob + Math.log(tempBDS.getPriorProbability());
        return prob;
    }

    private double getPosteriorProbability(DocSummary tempDS, DocumentNumericVector tempDNV, 
            int totalWordInClass, int totalVocab, int sm) {
        
        Iterator itr = tempDNV.getNumericVector().keySet().iterator();
        String str;
        double prob = 0.0;
        while (itr.hasNext()) {
            str = (String) itr.next();      // words in the doc
            prob = prob + Math.log(((tempDS.getCount(str) * 1.0) + sm) / (totalWordInClass + totalVocab));                    
        }
        prob = prob + Math.log(tempDS.getPriorProbability());
        return prob;
    }

    public HashMap getWordCountMapByClass(HashMap train) {
        HashMap totalWordByClass = new HashMap();
        String str;
        String str2;
        int totalWordCount;
        DocSummary tempDS = null;
        Iterator itr = train.keySet().iterator();
        Iterator itr2;
        // for each class
        while (itr.hasNext()) {
            str = (String) itr.next();  // class
            tempDS = (DocSummary) train.get(str);

            totalWordCount = 0;
            itr2 = tempDS.getDocCountMap().keySet().iterator();
            // for each word
            while (itr2.hasNext()) {
                str2 = (String) itr2.next();     // word
                totalWordCount = totalWordCount + tempDS.getCount(str2);
            }

            // map the count with class name
            totalWordByClass.put(str, totalWordCount);
        }
        return totalWordByClass;
    }

    public int getTotalVocabulary(HashMap hm) {
        String str;
        Iterator itr = hm.keySet().iterator();
        DocSummary ds;

        Set vocabSet = new HashSet();
        Set tempSet;
        while (itr.hasNext()) {
            str = (String) itr.next();
            ds = (DocSummary) hm.get(str);
            tempSet = ds.getDocCountMap().keySet();
            vocabSet.addAll(tempSet);
        }
        return vocabSet.size();
    }
}
