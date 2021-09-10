package com.company;

import java.util.List;

public class Customer extends Thread {

    volatile List<Cashier> cashiers;
    int taskQty;
    Cashier currentCashier;
    volatile boolean isServed;
    int service_time = 0;

    public Customer(String name, List<Cashier> cashiers, int taskQty) {
        super(name);
        this.cashiers = cashiers;
        this.taskQty = taskQty;
        isServed = false;
        start();
    }

    public int getTaskQty() {
        return taskQty;
    }

    public void setServed(boolean isServed) {
        this.isServed = isServed;
    }

    @Override
    public void run() {
        int shortestQueueId = findShortestQueue(0);
        currentCashier = cashiers.get(shortestQueueId);
        currentCashier.enqueueCustomer(this);

        while (!isServed){
            if (this != currentCashier.getCustomerQueue().peek()) {
                Cashier oldCashier = null;
                synchronized (this) {
                    Cashier bufferCashier = findFreeQueue();
                    if (bufferCashier != null) {
                        oldCashier = currentCashier;
                        currentCashier = bufferCashier;
                    }
                }
                if (oldCashier != null) {
                    oldCashier.dequeueCustomer(this);
                    currentCashier.enqueueCustomer(this);
                }
            }
        }
        System.out.println(getTaskQty() + " items of " + Thread.currentThread().getName() + " was served by " + currentCashier +
                ". Service time: " + service_time + " milliseconds.");
    }

    private int findShortestQueue(int shortestQueueId) {
        int shortestQueueLength = Integer.MAX_VALUE;
        for (int i = 0; i < cashiers.size(); i++) {
            int currentQueueLength = cashiers.get(i).getCustomerQueue().size();
            if (currentQueueLength == 0) {
                shortestQueueId = i;
                break;
            } else {
                if (currentQueueLength < shortestQueueLength) {
                    shortestQueueId = i;
                    shortestQueueLength = currentQueueLength;
                }
            }
        }
        return shortestQueueId;
    }
    private Cashier findFreeQueue () {
        for (int i = 0; i < cashiers.size(); i++) {
            int currentQueueLength = cashiers.get(i).getCustomerQueue().size();
            if (currentQueueLength == 0) {
                return cashiers.get(i);
            }
        }
        return null;
    }

    public void setService_time(int service_time) {
        this.service_time = service_time;
    }

    public int getService_time() {
        return service_time;
    }

    @Override
    public String toString() {
        return getName();
    }
}