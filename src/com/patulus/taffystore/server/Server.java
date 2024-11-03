package com.patulus.taffystore.server;

import com.patulus.taffystore.call.Call;
import com.patulus.taffystore.customer.Customer;
import com.patulus.taffystore.customer.CustomerType;
import com.patulus.taffystore.deque.Deque;
import com.patulus.taffystore.simulate.Simulate;

public class Server implements Runnable {
    private Simulate simulate;
    private Deque<Customer> dequeForNormal;
    private Deque<Customer> dequeForVip;
    private Deque<Call> dequeForCall;

    /** 점원 번호 */
    private int serverNum;
    /** 점원 상태 */
    private ServerState state;

    /** 상대한 손님의 수 */
    private int totCustomers;
    /** 상대한 손님들의 총 서비스 시간 */
    private long totSvcTime;

    public Server(Simulate simulate, int serverNum, Deque<Customer> dequeForNormal, Deque<Customer> dequeForVip, Deque<Call> dequeForCall) {
        this.simulate = simulate;
        this.dequeForNormal = dequeForNormal;
        this.dequeForVip = dequeForVip;
        this.dequeForCall = dequeForCall;

        this.serverNum = serverNum;
        this.state = ServerState.REST;

        this.totCustomers = 0;
        this.totSvcTime = 0;
    }

    @Override
    public void run() {
        try {
            Customer customer = null;
            long svcTime = -1;

            while (true) {
                try {
                    if (!this.dequeForVip.isEmpty()) {
                        customer = this.dequeForVip.poll();
                    } else {
                        customer = this.dequeForNormal.poll();
                    }
                } catch (Exception ex) {
                    Thread.sleep(10);
                }

                if (customer != null) {
                    this.state = ServerState.RUNNING;
                    customer.setServer(this);
                    customer.setStartTime(this.simulate.getElapsedTime());
                    synchronized (Server.class) {
                        System.out.println(this.simulate.getElapsedDays() + "일 " + this.simulate.getElapsedTime() + "분 : " + (customer.getCustType() == CustomerType.CUST_VIP ? "VIP " : "일반 ") + customer.getCustNum() + "번 고객의 서비스가 " + customer.getServer().serverNum + "번 창구에서 " + customer.getSvcTime() + "분 동안 진행됩니다.");
                    }

                    svcTime = customer.getSvcTime();
                    while (true) {
                        if (this.simulate.getElapsedTime() >= customer.getStartTime() + svcTime) {
                            break;
                        }

                        svcTime += processCall();

                        Thread.sleep(2);
                    }
                    customer.setElapsedSvcTime((int) (this.simulate.getElapsedTime() - customer.getStartTime()));
                    synchronized (Server.class) {
                        System.out.println(this.simulate.getElapsedDays() + "일 " + this.simulate.getElapsedTime() + "분 : " + (customer.getCustType() == CustomerType.CUST_VIP ? "VIP " : "일반 ") + customer.getCustNum() + "번 고객의 서비스가 " + customer.getServer().serverNum + "번 창구에서 " + customer.getElapsedSvcTime() + "분 동안 진행되었습니다.");
                    }

                    ++this.totCustomers;
                    this.totSvcTime += customer.getSvcTime();
                    customer.svcDone();

                    // 서비스 완료 고객을 Simulate에 저장
                    this.simulate.offerCustomer(customer);

                    customer = null;
                    this.state = ServerState.IDLE;
                } else {
                    processCall();
                    this.state = ServerState.IDLE;
                }

                if (this.simulate.getElapsedTime() >= this.simulate.getWorkingTime()) {
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private long processCall() {
        if (!dequeForCall.isEmpty()) {
            try {
                Call call = dequeForCall.poll();
                this.state = ServerState.RUNNING;

                call.setServer(this);
                call.setStartTime(this.simulate.getElapsedTime());
                synchronized (Server.class) {
                    System.out.println(this.simulate.getElapsedDays() + "일 " + this.simulate.getElapsedTime() + "분 : " + call.getCustNum() + "번 전화 서비스가 " + call.getServer().serverNum + "번 창구에서 " + call.getSvcTime() + "분 동안 진행됩니다.");
                }

                while (true) {
                    if (this.simulate.getElapsedTime() >= call.getStartTime() + call.getSvcTime()) {
                        break;
                    }

                    Thread.sleep(2);
                }

                call.setElapsedSvcTime((int) (this.simulate.getElapsedTime() - call.getStartTime()));
                synchronized (Server.class) {
                    System.out.println(this.simulate.getElapsedDays() + "일 " + this.simulate.getElapsedTime() + "분 : " + call.getCustNum() + "번 전화 서비스가 " + call.getServer().serverNum + "번 창구에서 " + call.getElapsedSvcTime() + "분 동안 진행되었습니다.");
                }

                ++this.totCustomers;
                this.totSvcTime += call.getSvcTime();
                call.svcDone();

                // 서비스 완료 전화를 Simulate에 저장
                this.simulate.offerCall(call);

                return call.getSvcTime();
            } catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }

        return 0;
    }

    public int getServerNum() {
        return serverNum;
    }

    public int getTotCustomers() {
        return this.totCustomers;
    }

    public long getTotSvcTime() {
        return this.totSvcTime;
    }
}