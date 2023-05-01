import opennlp.tools.lemmatizer.SimpleLemmatizer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;


import static java.lang.System.exit;

public class IndexGenerator {
    StandardAnalyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;



    public void totalIndexation() throws IOException {
        IndexWriter w = null;
        String file = "Indexation";
        Directory index = FSDirectory.open(Paths.get(file));
        try {
            w = new IndexWriter(index, config);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        File dir = new File("data");
        File[] files = dir.listFiles();
        if (files != null) {
            int numDocument = 0;
            for (File f : files) {
                buildIndex(f, index, w);
                numDocument++;
                System.out.println("Finished File: " + numDocument);
            }
            w.close();
        }
    }
    public Directory buildIndex(File file, Directory index, IndexWriter w) throws FileNotFoundException {
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
                        Document doc = new Document();
                        doc.add(new TextField("info", info.toString(), Field.Store.YES));
                        doc.add(new StringField("docid", title, Field.Store.YES));
                        w.addDocument(doc);
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

        return index;

    }







}

