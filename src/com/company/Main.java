package com.company;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Cashier> cashiers = new LinkedList<>();
        List<Customer> customers = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            cashiers.add(new Cashier("Cashier "+ i));
        }

        for (int i = 0; i < 80; i++) {
            customers.add(new Customer("Customer " + i, cashiers, 1 +  (int) (9 * Math.random())));
        }

        for (Customer customer : customers){
            customer.join();
        }

        System.out.println("All customers have been served");
    }
}
