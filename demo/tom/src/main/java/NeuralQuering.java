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
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.util.*;

import java.io.IOException;

public class NeuralQuering {
    Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
    Analyzer analyzer = new StandardAnalyzer();
    FSDirectory index = FSDirectory.open(new File("Indexation").toPath());

    public NeuralQuering() throws IOException {
    }


    public float QueueWithIn(String Q, String A, HashMap<String, String[]> synonyms, Similarity scoreFunction, int qs, int useW2V, String w2v) throws IOException, ParseException {

            ArrayList<Float> allMRRs = new ArrayList<>();
            ArrayList<Integer> allCounts = new ArrayList<>();
            float MRR = 0;

            String[] tokensQ = tokenizer.tokenize(Q);
            ArrayList<String> plainSynonyms = new ArrayList<>();
            ArrayList<ArrayList<String>>  permutedWithSynonyms = new ArrayList<>();
            for (String str : tokensQ){
                if (synonyms.containsKey(str)){
                    plainSynonyms.add(synonyms.get(str)[0]);
                    for (int i = 0; i < synonyms.get(str).length; i++) {
                        ArrayList<String> temp = new ArrayList<>();
                        String[] poij = synonyms.get(str);
                        Collections.addAll(temp, tokensQ);
                        int index = temp.indexOf(str);
                        temp.set(index, synonyms.get(str)[i]);
                        permutedWithSynonyms.add(temp);
                    }


                }
                else{
                    plainSynonyms.add(str);
                }
                permutedWithSynonyms.add(plainSynonyms);
            }
            int trigger = 0;
            for (ArrayList synonymQueue: permutedWithSynonyms) {

                String[] toTokenize = new String[synonymQueue.size()];
                for (int i = 0; i < toTokenize.length; i++) {
                    toTokenize[i] = (String) synonymQueue.get(i);
                }
                String stringToken = Arrays.toString(toTokenize);
                stringToken = stringToken.substring(1, stringToken.length() - 1);
                Query totalQuery = new QueryParser("info", analyzer).parse(stringToken);
                IndexReader indexReader = DirectoryReader.open(this.index);
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                indexSearcher.setSimilarity(new LMDirichletSimilarity());
                TopDocs documents = indexSearcher.search(totalQuery, 9);
                ScoreDoc[] matches = documents.scoreDocs;
                if (matches.length == 0) {
                    continue;
                }
                else {
                    int i = 1;
                    int count = 0;
                    for (ScoreDoc match : matches) {
                        Document document = indexSearcher.doc(match.doc);
                        if (A.contains(document.get("docid"))) {
                            count++;
                            MRR = (float) 1 / i;

                            allMRRs.add(MRR);
                        }
                        i++;
                    }
                    allCounts.add(count);
                    if (qs != -1){
                        trigger++;
                    }
                    if (qs == 0 || qs == trigger) {
                        break;
                    }
                }
            }
        if (useW2V == 1){
            w2v = w2v.replaceAll("[^a-zA-Z0-9 ]", "");
            String stringToken = Arrays.toString(w2v.split(" "));
            stringToken = stringToken.substring(1, stringToken.length() - 1);
            Query totalQuery = new QueryParser("info", analyzer).parse(stringToken);
            IndexReader indexReader = DirectoryReader.open(this.index);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            indexSearcher.setSimilarity(new LMDirichletSimilarity());
            TopDocs documents = indexSearcher.search(totalQuery, 9);
            ScoreDoc[] matches = documents.scoreDocs;
            if (matches.length == 0) {
                return 0;
            }
            else {
                int i = 1;
                int count = 0;
                for (ScoreDoc match : matches) {
                    Document document = indexSearcher.doc(match.doc);
                    if (A.contains(document.get("docid"))) {
                        count++;
                        MRR = (float) 1 / i;
                        allMRRs.add(MRR);
                    }
                    i++;
                }
            }
        }
        float topMRR = 0;
        for (int i = 0; i < allMRRs.size(); i++) {
            if (allMRRs.get(i) > topMRR){
                topMRR = allMRRs.get(i);
            }
        }

            return topMRR;


    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello! I'm QA Watson System!");
        System.out.println("Firstly, please, choose the similarity function to work with!");
        System.out.println("Press 1 to stay on default one.");
        System.out.println("Press 2 to choose Boolean.");
        System.out.println("Press 3 to choose Jelinek Mercer Model");
        Scanner s = new Scanner(System.in);
        Similarity scoreFunction = null;
        int sw = s.nextInt();
        if (sw == 1){
            scoreFunction = null;
        }
        if (sw == 2){
            scoreFunction = new BooleanSimilarity();
        }
        if (sw == 3){
            scoreFunction = new LMDirichletSimilarity();
        }

        System.out.println("Choose, please, how many synonym Queries you want me to work on? Press -1 if all of them.");
        System.out.println("You can choose from 1 to 100.");
        int syns = s.nextInt();
        System.out.println("Do you want to use Word2Vec? Press 1 to include.");
        int one  = s.nextInt();
        File fileSynonyms = new File("W2VGeneratedSynonyms.txt");
        Scanner reader = new Scanner(fileSynonyms);
        ArrayList<String> synonymQs = new ArrayList<>();
        while (reader.hasNext()){
            String data = reader.nextLine();
            synonymQs.add(data);
        }



        NeuralQuering meta = new NeuralQuering();
        ArrayList<String> Qs;
        ArrayList<String> As;
        HashMap<String, String[]> symMap;
        float total = 0;
        ArrayList<Float> MRR = new ArrayList<>();
        try{
            FileInputStream fileQ = new FileInputStream("serealizedQ.txt");
            ObjectInputStream inQ = new ObjectInputStream(fileQ);

            FileInputStream fileA = new FileInputStream("serealizedA.txt");
            ObjectInputStream inA = new ObjectInputStream(fileA);
            FileInputStream symFile = new FileInputStream("parsedSynonyms.txt");
            ObjectInputStream symIn = new ObjectInputStream(symFile);
            symMap = (HashMap<String, String[]>) symIn.readObject();
            Qs = (ArrayList<String>) inQ.readObject();
            As = (ArrayList<String>) inA.readObject();
            int count = 0;
            int loopCount =0;
            for (String Q: Qs){
                String A = As.get(loopCount);
                float mrr = meta.QueueWithIn(Q,A,symMap,scoreFunction, syns, one, synonymQs.get(loopCount));
                if (mrr != 0) {
                    total += mrr;
                    count++;
                }
                loopCount++;
            }
            System.out.println("Your MRR is: " + total/100);
            System.out.println("Caught documents: " + count + "/100");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }





}
