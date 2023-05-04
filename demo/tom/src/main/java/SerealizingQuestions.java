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
        ArrayList<String> Qs = new ArrayList<>();
        ArrayList<String> As = new ArrayList<>();

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
                Qs.add(Q);
                As.add(A);
            }
        }
        try {
            FileOutputStream myFileOutStream
                = new FileOutputStream(
                    "serealizedQ.txt");

            ObjectOutputStream myObjectOutStream
                = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(Qs);

            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
                try {
            FileOutputStream myFileOutStream
                = new FileOutputStream(
                    "serealizedA.txt");

            ObjectOutputStream myObjectOutStream
                = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(As);

            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    }


