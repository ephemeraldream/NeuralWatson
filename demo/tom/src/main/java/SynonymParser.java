
import java.io.*;
import java.util.*;

import org.json.simple.parser.ParseException;

public class SynonymParser {

    public static void main(String[] args) throws IOException, ParseException {
        HashMap<String, String[]> synonyms = new HashMap<>();
        Scanner scanner = new Scanner(new File("another_data/synonyms.csv"));
        scanner.useDelimiter(",");
        boolean trigger = false;
        while (scanner.hasNext()){
            if (!trigger){
                trigger = true;
                scanner.nextLine();
            }
            String toParse = scanner.nextLine();
            List<String> splitted = new LinkedList<String>(List.of(toParse.split(",")));
            splitted.remove(1);
            synonyms.put(splitted.get(0), splitted.get(1).split(";|\\|"));

            int iasdf = 32;



        }
                try {
            FileOutputStream myFileOutStream
                = new FileOutputStream(
                    "another_data/parsedSynonyms.txt");

            ObjectOutputStream myObjectOutStream
                = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(synonyms);

            // closing FileOutputStream and
            // ObjectOutputStream
            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

