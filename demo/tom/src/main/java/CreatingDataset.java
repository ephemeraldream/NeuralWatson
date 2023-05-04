import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import opennlp.tools.tokenize.SimpleTokenizer;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.System.exit;



public class CreatingDataset {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        FileWriter myWriter = new FileWriter("dataset.txt");

    public CreatingDataset() throws IOException {
    }

    public void totalDataset() throws IOException {
        String file = "Indexation";
        File dir = new File("data");
        File[] files = dir.listFiles();
        if (files != null) {
            int numDocument = 0;
            for (File f : files) {
                datasetCreation(f);
                numDocument++;
                System.out.println("Finished File: " + numDocument);
            }
        }
    }
    public void datasetCreation(File file) throws IOException {
        FileReader source = new FileReader(file);
        BufferedReader reader = new BufferedReader(source);
        String str;
        String title = "";
        StringBuilder info = new StringBuilder();
        boolean start = true;


        try {
            while ((str = reader.readLine()) != null) {
                if (str.equals("")) {
                    continue;
                }
                if (str.contains("[[") && str.contains("]]")){
                    if(str.contains("[[File:") || str.contains("[[Media:") || str.contains("[[Image:") || str.contains("</ref>")) {
                        continue;
                    }
                    if(!start){
                        myWriter.write(info.toString());

                        title = "";
                        info.setLength(0);
                    }
                    str = str.substring(2);
                    str = str.substring(0, str.length() - 2);
                    str = str.replaceAll("[^a-zA-Z0-9 ]", "");
                    str = str.toLowerCase();
                    title = str;
                    start = false;
                    continue;
                }
                if (str.contains("==")) {
                    str = str.replaceAll("[^a-zA-Z0-9 ]", "");
                    continue;
                }
                if (str.contains("CATEGORIES:")) {
                    str = str.substring(11);
                    String[] splits = str.split(",");
                    for (String split : splits) {
                        split = split.replaceAll("[^a-zA-Z0-9 ]", "");
                    }
                }
                else {
                    str = str.replaceAll("[^a-zA-Z0-9 ]", "");
                    if(str.equals("") || !str.matches(".*\\w.*")){
                        continue;
                    }
                    str = str.toLowerCase();
                    String procInfo = Arrays.toString(tokenizer.tokenize(str));
                    procInfo = procInfo.substring(1, procInfo.length()-1);
                    info.append(" ");
                    info.append(procInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }


    }

    public static void main(String[] args) throws IOException {
        CreatingDataset d = new CreatingDataset();
        d.totalDataset();
    }

}
