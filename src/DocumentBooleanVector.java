import java.util.HashMap;


public class DocumentBooleanVector {
    
    public int hd;
    public String topic;
    public HashMap booleanVector;
    
    public DocumentBooleanVector(String t){
        hd = -1;
        topic = t;
        booleanVector = new HashMap();
    }

    public int getHd() {
        return hd;
    }

    public void setHd(int hd) {
        this.hd = hd;
    }    
    
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public HashMap getBooleanVector() {
        return booleanVector;
    }

    public void setBooleanVector(HashMap booleanVector) {
        this.booleanVector = booleanVector;
    }
    
    public boolean containsWord(String str){
        return booleanVector.containsKey(str);
    }
    
    public void addWord(String str){
        booleanVector.put(str, true);
    }
}
