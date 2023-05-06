import java.io.IOException;

public class Main {

}




abstract class Approver {
    String name;
    Approver successor;

    Approver(String name){
        this.name = name;

    }
    void setSuccessor(Approver successor){
        this.successor = successor;
    }
    abstract void processRequest(PurchaseRequest request);
}

