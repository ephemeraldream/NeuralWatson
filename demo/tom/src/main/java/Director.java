public class Director extends Approver {

    Director(String name){
        super(name);
    }

    @Override
    void processRequest(PurchaseRequest request) {
        if (request.getAmount() >= 10000){
            successor.processRequest(request);
        }
        else{
            System.out.println("Approved.");
        }
    }
}
