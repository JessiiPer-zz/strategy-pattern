package payments.strategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PayByPaymentSlip implements PayStrategy{

    private final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private String dueDate;
    private boolean isValidPaymentSlip = false;

    @Override
    public boolean pay(int paymentAmount) {
        if(isValidPaymentSlip){
            System.out.println("Paying " + paymentAmount + " using Payment Slip.");
            return true;
        }
        return false;
    }

    @Override
    public void collectPaymentDetails() {
        try {
            System.out.print("Enter the validate date of the payment slip [dd/MM/yyyy]: ");
            dueDate = READER.readLine();
            if (verifyDateExpiration(dueDate)) {
                isValidPaymentSlip = true;
                System.out.println("Date verification has been successful.");
            } else {
                System.out.println("This date already expired!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean verifyDateExpiration(String dueDate) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateExpiration = LocalDate.parse(dueDate, format);

        if(dateExpiration.isAfter(LocalDate.now())){
            return true;
        }
        return false;
    }
}
