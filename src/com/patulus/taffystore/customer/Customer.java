package com.patulus.taffystore.customer;

import com.patulus.taffystore.task.Task;

public class Customer extends Task {

    /** 고객 유형 */
    private CustomerType custType;

    public Customer(int arriveTime, int svcTime) {
        super(arriveTime, svcTime);
        this.custType = CustomerType.CUST_NORMAL;
    }

    public void setNormalCustomer() {
        this.setCustType(CustomerType.CUST_NORMAL);
    }

    public void setVipCustomer() {
        this.setCustType(CustomerType.CUST_VIP);
    }

    private void setCustType(CustomerType custType) {
        this.custType = custType;
    }

    public CustomerType getCustType() {
        return this.custType;
    }
}
