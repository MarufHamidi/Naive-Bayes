
import java.util.HashMap;


/**
 *
 * @author Maruf
 */
public class DocSummary {
    
    public int totalDoc;
    public double priorProbability;
    public HashMap docCountMap;

    public DocSummary() {
        docCountMap = new HashMap();
    }   
    
    public DocSummary(int totalDoc, double priorProbability, HashMap docCountMap) {
        this.totalDoc = totalDoc;
        this.priorProbability = priorProbability;
        this.docCountMap = docCountMap;
    }

    public int getTotalDoc() {
        return totalDoc;
    }

    public void setTotalDoc(int totalDoc) {
        this.totalDoc = totalDoc;
    }

    public double getPriorProbability() {
        return priorProbability;
    }

    public void setPriorProbability(double priorProbability) {
        this.priorProbability = priorProbability;
    }

    public HashMap getDocCountMap() {
        return docCountMap;
    }

    public void setDocCountMap(HashMap docCountMap) {
        this.docCountMap = docCountMap;
    } 
    
    public boolean containsWord(String str){
        return docCountMap.containsKey(str);
    }
    
    public int getCount(String str){
        if(!docCountMap.containsKey(str)){
            return 0;
        }
        else{
            return (int) docCountMap.get(str);
        }
    }    
}
