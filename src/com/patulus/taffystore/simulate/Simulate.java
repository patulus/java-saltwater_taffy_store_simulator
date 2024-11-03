package com.patulus.taffystore.simulate;

import com.patulus.taffystore.call.Call;
import com.patulus.taffystore.customer.Customer;
import com.patulus.taffystore.customer.CustomerType;
import com.patulus.taffystore.deque.Deque;
import com.patulus.taffystore.server.Server;
import com.patulus.taffystore.status.Stats;

import java.util.Random;

public class Simulate {
    /** 일자별 통계 저장 리스트 */
    private final Stats[] statistics;

    Server[] servers;
    Thread[] serverThreads;

    /** 일반 손님 대기열 */
    private final Deque<Customer> dequeForNormal;
    /** VIP 손님 대기열 */
    private final Deque<Customer> dequeForVip;
    /** 전화 대기열 */
    private final Deque<Call> dequeForCall;

    /** 시뮬레이션 실행 후 지난 일 수 (일) */
    private int elapsedDays;
    /** 시뮬레이션 할 일 수 (일) */
    private final int days;
    /** 하루 동안 지난 시간 */
    private int elapsedTime;

    /** 점원의 수 */
    private final int nServer;
    /** 점원의 하루 근로 시간 (분) */
    private final int workingTime;
    /** 최소 서비스 시간 */
    private final int minSvcTime;
    /** 최대 서비스 시간 */
    private final int maxSvcTime;
    /** 최소 전화 서비스 시간 */
    private final int minCallSvcTime;
    /** 최대 전화 서비스 시간 */
    private final int maxCallSvcTime;

    /** 손님의 평균 방문 간격 */
    private final int avgArrivalCycle;
    /** VIP 등장 확률 */
    private final double probVip;
    /** 전화가 올 확률 */
    private final double probCall;

    /** 무작위 수 추첨을 위한 Random 객체 참조 */
    private final Random random;

    public Simulate(int days, int nServer, int workingTime, int avgArrivalCycle, int minSvcTime, int maxSvcTime, int minCallSvcTime, int maxCallSvcTime, double probVip, double probCall) {
        this.statistics = new Stats[days + 1];
        for (int i = 0; i < days + 1; ++i) {
            this.statistics[i] = new Stats();
        }

        this.servers = new Server[nServer];
        this.serverThreads = new Thread[nServer];

        this.dequeForNormal = new Deque<>();
        this.dequeForVip = new Deque<>();
        this.dequeForCall = new Deque<>();

        this.elapsedDays = 1;
        this.days = days;
        this.elapsedTime = 1;

        this.nServer = nServer;
        this.workingTime = workingTime;
        this.minSvcTime = minSvcTime;
        this.maxSvcTime = maxSvcTime;
        this.minCallSvcTime = minCallSvcTime;
        this.maxCallSvcTime = maxCallSvcTime;

        this.avgArrivalCycle = avgArrivalCycle;
        this.probVip = probVip;
        this.probCall = probCall;

        this.random = new Random();
    }

    public Simulate(int days, int nServer) {
        this(days, nServer, 480, 4, 1, 10, 1, 3, 0.01, 0.05);
    }

    public void run() {
        int i;

        Stats stats;
        Customer customer;
        Call call;
        int svcTime = 0;
        int totCust = -1;
        int totCall = -1;
        int doneCust = -1;
        int doneCall = -1;
        int avgWaitTime = -1;
        int custAvgSvcTime = -1;
        int callAvgSvcTime = -1;

        for (this.elapsedDays = 1; this.elapsedDays <= this.days; ++this.elapsedDays) {
            for (i = 0; i < this.nServer; ++i) {
                servers[i] = new Server(this, i + 1, this.dequeForNormal, this.dequeForVip, this.dequeForCall);
                serverThreads[i] = new Thread(servers[i]);
                serverThreads[i].start();
            }

            this.elapsedTime = 1;

            while (this.elapsedTime <= this.workingTime) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

                // 무작위 수가 손님의 평균 방문 간격이 되면 손님 객체를 생성
                if (this.random.nextInt(this.avgArrivalCycle) + 1 == this.avgArrivalCycle) {
                    // 손님의 서비스 수행 시간 설정
                    svcTime = this.random.nextInt(this.minSvcTime - 1, this.maxSvcTime) + 1;

                    customer = new Customer(this.elapsedTime, svcTime);
                    customer.setCustNum(Deque.takeNum());
                    customer.setArriveTime(this.elapsedTime);

                    // 설정된 VIP 등장 확률로 손님을 VIP 손님으로 지정
                    // 손님을 유형에 맞는 대기열에 추가
                    // TODO: 우선 순위 큐로 관리해 두 대기열을 통합, 고객별 우선 순위 지정
                    if (this.random.nextDouble() <= this.probVip) {
                        customer.setVipCustomer();
                        try {
                            this.dequeForVip.offer(customer);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        try {
                            this.dequeForNormal.offer(customer);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }

                    synchronized (Server.class) {
                        System.out.println(this.elapsedDays + "일 " + customer.getArriveTime() + "분 : " + (customer.getCustType() == CustomerType.CUST_VIP ? "VIP " : "일반 ") + customer.getCustNum() + "번 고객이 입장했습니다.");
                    }
                } else {
                    // 설정된 전화 확률로 전화를 무작위 수 번호의 점원에게 지정
                    if (this.random.nextDouble() <= this.probCall) {
                        svcTime = this.random.nextInt(this.minCallSvcTime - 1, this.maxCallSvcTime) + 1;
                        call = new Call(this.elapsedTime, svcTime);
                        call.setCustNum(Deque.takeNum());

                        try {
                            this.dequeForCall.offer(call);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }

                ++this.elapsedTime;
            }

            try {
                for (i = 0; i < this.avgArrivalCycle + this.avgArrivalCycle + 10; ++i) {
                    Thread.sleep(20);
                    ++this.elapsedTime;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            Deque.resetNum();

            stats = this.statistics[this.elapsedDays];
            // 모든 큐에서 객체를 빼내 리스트에 저장
            while (!this.dequeForVip.isEmpty()) {
                try {
                    stats.offerCustomer(this.dequeForVip.poll());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            while (!this.dequeForNormal.isEmpty()) {
                try {
                    stats.offerCustomer(this.dequeForNormal.poll());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            while (!this.dequeForCall.isEmpty()) {
                try {
                    stats.offerCall(this.dequeForCall.poll());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

            // 정보 객체에서 통계 작업을 진행
            totCust = stats.getCustomerSize();
            totCall = stats.getCallSize();
            doneCust = stats.getSvcDoneCustomerNum();
            doneCall = stats.getSvcDoneCallNum();
            avgWaitTime = stats.getAvgWaitTime();
            custAvgSvcTime = stats.getCustAvgSvcTime();
            callAvgSvcTime = stats.getCallAvgSvcTime();

            // 정보를 출력
            System.out.println("[" + this.elapsedDays+ "일] " + "전체 고객: " + totCust + " 전체 전화: " + totCall + " 완료 고객: " + doneCust + " 완료 전화: " + doneCall + " 평균 대기 시간: " + avgWaitTime + " 평균 방문 고객 서비스 시간: " + custAvgSvcTime + " 평균 전화 고객 서비스 시간: " + callAvgSvcTime);
        }

        totCust = 0;
        totCall = 0;
        doneCust = 0;
        doneCall = 0;
        avgWaitTime = 0;
        long totCustWaitTime = -1;
        long totCustSvcTime = -1;
        long totCallSvcTime = -1;
        custAvgSvcTime = 0;
        callAvgSvcTime = 0;

        for (i = 1; i <= this.days; ++i) {
            totCust += this.statistics[i].getCustomerSize();
            totCall += this.statistics[i].getCallSize();
            doneCust += this.statistics[i].getSvcDoneCustomerNum();
            doneCall += this.statistics[i].getSvcDoneCallNum();
            totCustWaitTime += this.statistics[i].getCustomerWaitTime();
            totCustSvcTime += this.statistics[i].getCustomerServiceTime();
            totCallSvcTime += this.statistics[i].getCallServiceTime();
        }
        avgWaitTime = (int)((double) totCustWaitTime / doneCust + 0.5);
        custAvgSvcTime = (int)((double) totCustSvcTime / doneCust + 0.5);
        callAvgSvcTime = (int)((double) totCallSvcTime / doneCall + 0.5);
        System.out.println("[전체] " + "전체 고객: " + totCust + " 전체 전화: " + totCall + " 완료 고객: " + doneCust + " 완료 전화: " + doneCall + " 평균 대기 시간: " + avgWaitTime + " 평균 방문 고객 서비스 시간: " + custAvgSvcTime + " 평균 전화 고객 서비스 시간: " + callAvgSvcTime);

    }

    synchronized public void offerCustomer(Customer customer) throws Exception {
        this.statistics[this.elapsedDays].offerCustomer(customer);
    }

    synchronized public void offerCall(Call call) throws Exception {
        this.statistics[this.elapsedDays].offerCall(call);
    }

    synchronized public int getWorkingTime() {
        return this.workingTime;
    }

    synchronized public int getElapsedDays() {
        return this.elapsedDays;
    }

    synchronized public int getElapsedTime() {
        return this.elapsedTime;
    }

    public int getDays() {
        return this.days;
    }

    public Stats getStats(int day) {
        if (day < 1 || day > days) {
            return null;
        }

        return this.statistics[day];
    }

    public String getTotalSummary() {
        int totCust = 0;
        int totCall = 0;
        int doneCust = 0;
        int doneCall = 0;
        long totCustWaitTime = 0;
        long totCustSvcTime = 0;
        long totCallSvcTime = 0;

        for (int i = 1; i <= this.days; ++i) {
            totCust += this.statistics[i].getCustomerSize();
            totCall += this.statistics[i].getCallSize();
            doneCust += this.statistics[i].getSvcDoneCustomerNum();
            doneCall += this.statistics[i].getSvcDoneCallNum();
            totCustWaitTime += this.statistics[i].getCustomerWaitTime();
            totCustSvcTime += this.statistics[i].getCustomerServiceTime();
            totCallSvcTime += this.statistics[i].getCallServiceTime();
        }

        int avgWaitTime = doneCust > 0 ? (int) ((double) totCustWaitTime / doneCust + 0.5) : 0;
        int custAvgSvcTime = doneCust > 0 ? (int) ((double) totCustSvcTime / doneCust + 0.5) : 0;
        int callAvgSvcTime = doneCall > 0 ? (int) ((double) totCallSvcTime / doneCall + 0.5) : 0;

        String summary = String.format(
                "전체 고객 수: %d\n전체 전화 수: %d\n완료된 고객 수: %d\n완료된 전화 수: %d\n평균 대기 시간: %d분\n" +
                        "평균 방문 고객 서비스 시간: %d분\n평균 전화 고객 서비스 시간: %d분",
                totCust, totCall, doneCust, doneCall, avgWaitTime, custAvgSvcTime, callAvgSvcTime
        );

        return summary;
    }

    public String getDaySummary(int day) throws Exception {
        if (day < 1 || day > days) {
            throw new Exception("잘못된 일 수입니다.");
        }

        Stats stats = this.statistics[day];

        int totCust = stats.getCustomerSize();
        int totCall = stats.getCallSize();
        int doneCust = stats.getSvcDoneCustomerNum();
        int doneCall = stats.getSvcDoneCallNum();
        int avgWaitTime = stats.getAvgWaitTime();
        int custAvgSvcTime = stats.getCustAvgSvcTime();
        int callAvgSvcTime = stats.getCallAvgSvcTime();

        String summary = String.format(
                "[%d일차 요약]\n전체 고객 수: %d\n전체 전화 수: %d\n완료된 고객 수: %d\n완료된 전화 수: %d\n평균 대기 시간: %d분\n" +
                        "평균 방문 고객 서비스 시간: %d분\n평균 전화 고객 서비스 시간: %d분",
                day, totCust, totCall, doneCust, doneCall, avgWaitTime, custAvgSvcTime, callAvgSvcTime
        );

        return summary;
    }
}