import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SerealizingQuestions {

    public static void main(String[] args) throws FileNotFoundException {
        SerealizingQuestions q = new SerealizingQuestions();
        q.serealize();
    }



    public void serealize() throws FileNotFoundException {
        File questions = new File("questions.txt");
        Scanner sc = new Scanner(questions);
        HashMap<String, String> toSerealize = new HashMap<>();

        int i = 1;
        String Q ="";
        String A="";
        while(sc.hasNextLine()) {
            if (i == 1 || i == 0) {
                i++;
                sc.nextLine();
            }
            else if (i == 2) {
                Q = sc.nextLine();
                Q = Q.replaceAll("[^a-zA-Z0-9 ]", "");
                Q = Q.toLowerCase();
                i++;
            }
            else if (i == 3) {
                A = sc.nextLine();
                A = A.replaceAll("[^a-zA-Z0-9 ]", "");
                A = A.toLowerCase();
                i = 0;
            }
            toSerealize.put(Q,A);
        }
        try {
            FileOutputStream myFileOutStream
                = new FileOutputStream(
                    "serealizedQA.txt");

            ObjectOutputStream myObjectOutStream
                = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(toSerealize);

            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    }


