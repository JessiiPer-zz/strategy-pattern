package payments;

import payments.order.Order;
import payments.strategies.PayByCreditCard;
import payments.strategies.PayByPayPal;
import payments.strategies.PayByPaymentSlip;
import payments.strategies.PayStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * World first console e-commerce application.
 */
public class Demo {

    private static Map<Integer, Integer> priceOnProducts = new HashMap<>();
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Order order = new Order();
    private static PayStrategy strategy;

    static {
        priceOnProducts.put(1, 2200);
        priceOnProducts.put(2, 1850);
        priceOnProducts.put(3, 1100);
        priceOnProducts.put(4, 890);
    }

    public static void main(String[] args) throws IOException {
        while (!order.isClosed()) {
            getChoise();
            PayStrategy payStrategy =  getStrategy();
            proccessOrderPayment(payStrategy);
            handlePayment();

        }
    }

    private static void getChoise() throws IOException {
        int cost;

        String continueChoice;
        do {
            System.out.print("Please, select a product:" + "\n" +
                    "1 - Mother board" + "\n" +
                    "2 - CPU" + "\n" +
                    "3 - HDD" + "\n" +
                    "4 - Memory" + "\n");
            int choice = Integer.parseInt(reader.readLine());
            cost = priceOnProducts.get(choice);
            System.out.print("Count: ");
            int count = Integer.parseInt(reader.readLine());
            order.setTotalCost(cost * count);
            System.out.print("Do you wish to continue selecting products? Y/N: ");
            continueChoice = reader.readLine();
        } while (continueChoice.equalsIgnoreCase("Y"));

    }


    private static void handlePayment() throws IOException {
        System.out.print("Pay " + order.getTotalCost() + " units or Continue shopping? P/C: ");
        String proceed = reader.readLine();
        if (proceed.equalsIgnoreCase("P")) {
            // Finally, strategy handles the payment.
            if (strategy.pay(order.getTotalCost())) {
                System.out.println("Payment has been successful.");
            } else {
                System.out.println("FAIL! Please, check your data.");
            }
            order.setClosed();
        }
    }

    private static PayStrategy getStrategy() throws IOException {
        if (strategy == null) {
            System.out.println("Please, select a payment method:" + "\n" +
                    "1 - PalPay" + "\n" +
                    "2 - Credit Card" + "\n"+
                    "3 - Payment Slip");
            String paymentMethod = reader.readLine();

            // Client creates different strategies based on input from user,
            // application configuration, etc.
            switch(paymentMethod){
                case "1":  strategy = new PayByPayPal(); break;
                case "2":  strategy = new PayByCreditCard(); break;
                case "3":  strategy = new PayByPaymentSlip(); break;
            }
        }
        return strategy;
    }

    private static void proccessOrderPayment(PayStrategy payStrategy) {
        // Order object delegates gathering payment data to strategy object,
        // since only strategies know what data they need to process a
        // payment.
        order.processOrder(strategy);
    }
}
