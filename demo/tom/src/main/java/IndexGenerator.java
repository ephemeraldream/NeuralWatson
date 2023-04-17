

import java.io.*;
import java.util.*;
import static java.lang.System.exit;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.index.IndexWriterConfig;


public class IndexGenerator {
    StandardAnalyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    Properties properties = new Properties();



    public void totalIndexation() throws IOException {
        IndexWriter w = null;
        Directory index = new ByteBuffersDirectory();
        try {
            w = new IndexWriter(index, config);
        } catch (IOException e) {
            e.printStackTrace();
            exit(1);
        }
        properties.setProperty("annotations", "tokenize,pos,lemma");
        File dir = new File("indexed-data");
        File[] files = dir.listFiles();
        if (files != null) {
            int i = 0;
            for (File f : files) {
                buildIndex(f, index, w);
                i++;
                System.out.println("Finished File: " + i);
            }
            w.close();
        }
    }
    public Directory buildIndex(File file, Directory index, IndexWriter w){
        return null;
    }









}

