package com.patulus.taffystore.status;

import com.patulus.taffystore.call.Call;
import com.patulus.taffystore.customer.Customer;
import com.patulus.taffystore.linkedlist.LinkedList;

public class Stats {
    private LinkedList<Customer> customers;
    private LinkedList<Call> calls;
    private int svcDoneCustomerNum;
    private int svcDoneCallNum;
    private long customerWaitTime;
    private long customerServiceTime;
    private long callServiceTime;

    public Stats() {
        this.customers = new LinkedList<>();
        this.calls = new LinkedList<>();
        this.svcDoneCustomerNum = 0;
        this.svcDoneCallNum = 0;
        this.customerWaitTime = 0L;
        this.customerServiceTime = 0L;
        this.callServiceTime = 0L;
    }

    public void offerCustomer(Customer customer) throws Exception {
        this.customers.addLast(customer);

        if (customer.getIsDone()) {
            ++svcDoneCustomerNum;
            this.customerWaitTime += customer.getStartTime() - customer.getArriveTime();
            this.customerServiceTime += customer.getSvcTime();
        }
    }

    public void offerCall(Call call) throws Exception {
        this.calls.addLast(call);

        if (call.getIsDone()) {
            ++svcDoneCallNum;
            this.callServiceTime += call.getSvcTime();
        }
    }

    public Customer getCustomer(int index) throws Exception {
        return this.customers.get(index);
    }

    public Call getCall(int index) throws Exception {
        return this.calls.get(index);
    }

    public int getAvgWaitTime() {
        if (this.svcDoneCustomerNum == 0) return 0;
        return (int) ((double) this.customerWaitTime / this.svcDoneCustomerNum + 0.5);
    }

    public int getCustAvgSvcTime() {
        if (this.svcDoneCustomerNum == 0) return 0;
        return (int) ((double) this.customerServiceTime / this.svcDoneCustomerNum + 0.5);
    }

    public int getCallAvgSvcTime() {
        if (this.svcDoneCallNum == 0) return 0;
        return (int) ((double) this.callServiceTime / this.svcDoneCallNum + 0.5);
    }

    public int getCustomerSize() {
        return this.customers.size();
    }

    public int getSvcDoneCustomerNum() {
        return this.svcDoneCustomerNum;
    }

    public int getSvcDoneCallNum() {
        return this.svcDoneCallNum;
    }

    public int getCallSize() {
        return this.calls.size();
    }

    public long getCustomerWaitTime() {
        return this.customerWaitTime;
    }

    public long getCustomerServiceTime() {
        return this.customerServiceTime;
    }

    public long getCallServiceTime() {
        return this.callServiceTime;
    }
}
