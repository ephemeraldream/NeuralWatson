import opennlp.tools.tokenize.Tokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.io.IOException;

public class Queueing {
    Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
    Analyzer analyzer = new StandardAnalyzer();
    FSDirectory index = FSDirectory.open(new File("Indexation").toPath());

    public Queueing() throws IOException {
    }


    public float QueueByOne(String Q, String A) throws IOException, ParseException {
            float MRR = 0;
            String tokenizedQuery = Arrays.toString(tokenizer.tokenize(Q));
            tokenizedQuery = tokenizedQuery.substring(1, tokenizedQuery.length()-1);
            Query totalQuery = new QueryParser("info", analyzer).parse(tokenizedQuery);
            IndexReader indexReader = DirectoryReader.open(this.index);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            indexSearcher.setSimilarity(new LMDirichletSimilarity());
            TopDocs documents = indexSearcher.search(totalQuery, 9);
            ScoreDoc[] matches =  documents.scoreDocs;
            if (matches.length == 0){
                return 0;
            }
            int i = 1;
            for (ScoreDoc match : matches){
                Document document = indexSearcher.doc(match.doc);
                if (A.contains(document.get("docid"))){
                    MRR = (float)1/i;
                    return MRR;
                }
                i++;
            }

            return 0;


    }

    public static void main(String[] args) throws IOException {
        Queueing meta = new Queueing();
        HashMap<String, String> map;
        float total = 0;
        ArrayList<Float> MRR = new ArrayList<>();
        try{
            FileInputStream file = new FileInputStream("serealizedQA.txt");
            ObjectInputStream in = new ObjectInputStream(file);
            map = (HashMap<String, String>) in.readObject();
            int count = 0;
            for (String Q: map.keySet()){
                String A = map.get(Q);
                if (meta.QueueByOne(Q,A) != 0) {
                    total += meta.QueueByOne(Q, A);
                    count++;
                }
            }
            System.out.println(total/100);
            System.out.println(count);


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }





}






















