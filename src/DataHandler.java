
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DataHandler {

    public Skiper skiper;
    public BufferedReader br;
    public String[] strArr;
    public String str;

    public DataHandler(String skp, String src) {
        skiper = new Skiper(skp, src);
    }

    public void readDocument(String fileName) {
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            while ((str = br.readLine()) != null) {
                strArr = getWords(str);

                str = getTopic();
                if (str != null) {
                    // got the topic no need to go further
                    System.out.println((++count) + str);
                    continue;
                }

                //others words in a line
                for (int i = 0; i < strArr.length; i++) {
                    if (consider(strArr[i])) {
                        System.out.print(strArr[i] + " ");
                    }
                }
                System.out.println("");
            }
        } catch (Exception ex) {
            System.out.println("Exception in readDocument - " + ex);
        }
    }

    public ArrayList createBooleanVector(String fileName) {
        ArrayList documentVectors = new ArrayList();
        DocumentBooleanVector tempDBV = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            while ((str = br.readLine()) != null) {
                strArr = getWords(str);

                str = getTopic();
                if (str != null) {
                    if (tempDBV != null) {
                        documentVectors.add(tempDBV);
                    }
                    tempDBV = new DocumentBooleanVector(str);
                    continue;
                }

                for (int i = 0; i < strArr.length; i++) {
                    if (consider(strArr[i]) && !tempDBV.containsWord(strArr[i])) {
                        tempDBV.addWord(strArr[i]);
                    }
                }
            }

            // EOF reached - add the last vector - return
            if (tempDBV != null) {
                documentVectors.add(tempDBV);
            }
            return documentVectors;
        } catch (Exception ex) {
            System.out.println("Exception in readDocument - " + ex);
            return null;
        }
    }

    public ArrayList createBooleanVectorNLog(String docFile) {
        System.out.println("Boolean vector with log running ...");

        ArrayList v = createBooleanVector(docFile);
        Test.output("Boolean vector for file : " + docFile);
        Test.output("Total news : " + v.size());
        Test.output("");
        Test.output("****************************");
        Test.output("");
        DocumentBooleanVector dbv;
        Iterator itr = v.iterator();
        Iterator itr2;
        int count = 0;
        while (itr.hasNext()) {
            dbv = (DocumentBooleanVector) itr.next();
            Test.output("Topic#" + (++count) + " : " + dbv.getTopic());
            Test.output("---------Words---------");
            itr2 = dbv.getBooleanVector().keySet().iterator();
            String s;
            while (itr2.hasNext()) {
                s = (String) itr2.next();
                Test.output(s + " - " + dbv.getBooleanVector().get(s));
            }
            Test.output("");
            Test.output("****************************");
            Test.output("");
        }
        System.out.println("See 'output.txt'.");
        return v;
    }

    public ArrayList createNumericVector(String fileName) {
        ArrayList documentVectors = new ArrayList();
        DocumentNumericVector tempDNV = null;
        try {
            br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            while ((str = br.readLine()) != null) {
                strArr = getWords(str);

                str = getTopic();
                if (str != null) {
                    if (tempDNV != null) {
                        tempDNV.setTotalWord(count);
                        documentVectors.add(tempDNV);
                    }
                    tempDNV = new DocumentNumericVector(str);
                    count = 0;
                    continue;
                }

                for (int i = 0; i < strArr.length; i++) {
                    if (consider(strArr[i])) {
                        count++;
                        if (tempDNV.containsWord(strArr[i])) {
                            tempDNV.updateKeyValue(strArr[i]);
                        } else {
                            tempDNV.addWord(strArr[i]);
                        }
                    }
                }
            }

            // EOF reached - add the last vector - return
            if (tempDNV != null) {
                tempDNV.setTotalWord(count);
                documentVectors.add(tempDNV);
            }
            return documentVectors;
        } catch (Exception ex) {
            System.out.println("Exception in readDocument - " + ex);
            return null;
        }
    }

    public ArrayList createNumericVectorNLog(String docFile) {
        System.out.println("Numeric vector with log running ...");

        ArrayList v = createNumericVector(docFile);
        Test.output("Numeric vector for file : " + docFile);
        Test.output("Total news : " + v.size());
        Test.output("");
        Test.output("****************************");
        Test.output("");
        DocumentNumericVector dnv;
        Iterator itr = v.iterator();
        Iterator itr2;
        int count = 0;
        while (itr.hasNext()) {
            dnv = (DocumentNumericVector) itr.next();
            Test.output("Topic#" + (++count) + " : " + dnv.getTopic());
            Test.output("---------Words---------");
            itr2 = dnv.getNumericVector().keySet().iterator();
            String s;
            while (itr2.hasNext()) {
                s = (String) itr2.next();
                Test.output(s + " - " + dnv.getNumericVector().get(s));
            }
            Test.output("");
            Test.output("****************************");
            Test.output("");
        }
        System.out.println("See 'output.txt'.");
        return v;
    }

    public HashMap cateorizeBooleanVector(ArrayList<DocumentBooleanVector> vector) {
        HashMap docs = new HashMap();
        DocumentBooleanVector tempdbv;
        ArrayList<DocumentBooleanVector> tempAL;

        Iterator itr = vector.iterator();
        while (itr.hasNext()) {
            tempdbv = (DocumentBooleanVector) itr.next();
            if (docs.containsKey(tempdbv.getTopic())) {
                tempAL = (ArrayList<DocumentBooleanVector>) docs.get(tempdbv.getTopic());
                tempAL.add(tempdbv);
                docs.put(tempdbv.getTopic(), tempAL);
            } else {
                tempAL = new ArrayList<>();
                tempAL.add(tempdbv);
                docs.put(tempdbv.getTopic(), tempAL);
            }
        }
        return docs;
    }

    public HashMap cateorizeNumericVector(ArrayList<DocumentNumericVector> vector) {
        HashMap docs = new HashMap();
        DocumentNumericVector tempdbv;
        ArrayList<DocumentNumericVector> tempAL;

        Iterator itr = vector.iterator();
        while (itr.hasNext()) {
            tempdbv = (DocumentNumericVector) itr.next();
            if (docs.containsKey(tempdbv.getTopic())) {
                tempAL = (ArrayList<DocumentNumericVector>) docs.get(tempdbv.getTopic());
                tempAL.add(tempdbv);
                docs.put(tempdbv.getTopic(), tempAL);
            } else {
                tempAL = new ArrayList<>();
                tempAL.add(tempdbv);
                docs.put(tempdbv.getTopic(), tempAL);
            }
        }
        return docs;
    }

    // skips all numeric values, punctuation (except in word hyphen)
    public String[] getWords(String s) {
        String[] arr;
        s = s.trim().toLowerCase();

        s = s.replaceAll("[^ a-zA-Z.\\-]", " ")
                .replaceAll("[.]", "")
                .replaceAll(" {2,}", " ");
        arr = s.split(" ");
        return arr;
    }

    public boolean isBlankLine(String[] arr) {
        return (arr.length == 1 && arr[0].length() == 0);
    }

    public String getTopic() throws IOException {
        // 1st blank
        if (isBlankLine(strArr)) {
            // read next line - and it is not null
            if ((str = br.readLine()) != null) {
                strArr = getWords(str);
                // 2nd blank line
                if (isBlankLine(strArr)) {
                    while (true) {
                        if ((str = br.readLine()) == null) {
                            // EOF reached - return 
                            return null;
                        }
                        strArr = getWords(str);
                        // skip the succeeding blank lines
                        if (isBlankLine(strArr)) {
                            continue;
                        }
                        // some text found at last - this is the topic - return it
                        // if the topic contains space seperated string it will protect that
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < strArr.length; i++) {
                            sb.append(strArr[i] + " ");
                        }
                        return sb.toString().trim();
                    }
                }
                // only one blank - not the topic - our read line won't be
                // lost as it is read in a global variable
                // go back and process that - return
            }
            // EOF reached - return
            return null;
        }
        // not blank - return
        return null;
    }

    // considered words -- 
    // length more than 1 -- single lettered words are skipped
    // word not in the skip.data
    // word not in the source.data
    public boolean consider(String str) {
        return str.length() > 1 && !skiper.skip(str) && !skiper.source(str);
    }

    public HashMap booleanSummary(HashMap hmBTrain, int total) {
        HashMap summary = new HashMap();
        DocSummary tempBDS;

        String tempKey;
        String tempKey2;
        ArrayList tempAL;
        DocumentBooleanVector tempDBV;
        HashMap tempHM;
        int tempCount;

        Iterator itr = hmBTrain.keySet().iterator();
        Iterator itr2;
        // for each class
        while (itr.hasNext()) {

            tempKey = (String) itr.next();
            tempAL = (ArrayList) hmBTrain.get(tempKey);

            tempBDS = new DocSummary();
            tempBDS.setTotalDoc(tempAL.size());
            tempBDS.setPriorProbability(tempAL.size() * 1.0 / total);
            tempHM = new HashMap();

            // for each document vector
            for (int i = 0; i < tempAL.size(); i++) {
                tempDBV = (DocumentBooleanVector) tempAL.get(i);
                itr2 = tempDBV.getBooleanVector().keySet().iterator();
                while (itr2.hasNext()) {
                    tempKey2 = (String) itr2.next();
                    if (tempHM.containsKey(tempKey2)) {
                        tempCount = (int) tempHM.get(tempKey2);
                        tempCount++;
                        tempHM.put(tempKey2, tempCount);
                    } else {
                        tempHM.put(tempKey2, 1);
                    }
                }
            }

            tempBDS.setDocCountMap(tempHM);
            summary.put(tempKey, tempBDS);
        }

        return summary;
    }

    public HashMap numericSummary(HashMap hmNTrain, int total) {
        HashMap summary = new HashMap();
        DocSummary tempDS;

        String tempKey;
        String tempKey2;
        ArrayList tempAL;
        DocumentNumericVector tempDNV;
        HashMap tempHM;
        int tempCount;

        Iterator itr = hmNTrain.keySet().iterator();
        Iterator itr2;
        // for each class
        while (itr.hasNext()) {

            tempKey = (String) itr.next();
            tempAL = (ArrayList) hmNTrain.get(tempKey);

            tempDS = new DocSummary();
            tempDS.setTotalDoc(tempAL.size());
            tempDS.setPriorProbability(tempAL.size() * 1.0 / total);
            tempHM = new HashMap();

            // for each document vector
            for (int i = 0; i < tempAL.size(); i++) {
                tempDNV = (DocumentNumericVector) tempAL.get(i);
                itr2 = tempDNV.getNumericVector().keySet().iterator();
                while (itr2.hasNext()) {
                    tempKey2 = (String) itr2.next();
                    if (tempHM.containsKey(tempKey2)) {
                        tempCount = (int) tempHM.get(tempKey2);
                        tempCount = (int) (tempCount + tempDNV.getWordCount(tempKey2));
                        tempHM.put(tempKey2, tempCount);
                    } else {
                        tempCount = tempDNV.getWordCount(tempKey2);
                        tempHM.put(tempKey2, tempCount);
                    }
                }
            }

            tempDS.setDocCountMap(tempHM);
            summary.put(tempKey, tempDS);
        }

        return summary;
    }
}
