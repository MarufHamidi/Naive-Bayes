
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Skiper {

    public HashMap skipHashMap;
    public HashMap sourceHashMap;

    public Skiper(String skip, String source) {
        skipHashMap = new HashMap();
        sourceHashMap = new HashMap();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(skip)));
            String w;
            while ((w = br.readLine()) != null) {
                skipHashMap.put(w, w);
            }
            
            br.close();
            
            br = new BufferedReader(new FileReader(new File(source)));
            while ((w = br.readLine()) != null) {
                sourceHashMap.put(w, w);
            }
            br.close();
        } catch (Exception ex) {
            Logger.getLogger(Skiper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                System.out.println("Exception finally");
            }
        }
    }

    public boolean skip(String str) {
        return skipHashMap.containsValue(str);
    }

    public boolean source(String str) {
        return sourceHashMap.containsValue(str);
    }
    
    /*
    public static void main(String[] args) {
        Skiper skiper = new Skiper("skip.data", "source.data");
        System.out.println(skiper.source("times"));
    }
    */
}
