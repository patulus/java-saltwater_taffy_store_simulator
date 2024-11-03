package com.patulus.taffystore.simulate;

public class SimulationConfig {
    /** 시뮬레이션 할 일 수 (일) */
    private int days;

    /** 점원의 수 */
    private int nServer;
    /** 점원의 하루 근로 시간 (분) */
    private int workingTime;
    /** 최소 서비스 시간 */
    private int minSvcTime;
    /** 최대 서비스 시간 */
    private int maxSvcTime;
    /** 최소 전화 서비스 시간 */
    private int minCallSvcTime;
    /** 최대 전화 서비스 시간 */
    private int maxCallSvcTime;

    /** 손님의 평균 방문 간격 */
    private int avgArrivalCycle;
    /** VIP 등장 확률 */
    private double probVip;
    /** 전화가 올 확률 */
    private double probCall;

    public SimulationConfig(int days, int nServer, int workingTime, int avgArrivalCycle, int minSvcTime,
                            int maxSvcTime, int minCallSvcTime, int maxCallSvcTime, double probVip, double probCall) {
        this.days = days;
        this.nServer = nServer;
        this.workingTime = workingTime;
        this.avgArrivalCycle = avgArrivalCycle;
        this.minSvcTime = minSvcTime;
        this.maxSvcTime = maxSvcTime;
        this.minCallSvcTime = minCallSvcTime;
        this.maxCallSvcTime = maxCallSvcTime;
        this.probVip = probVip;
        this.probCall = probCall;
    }

    public int getDays() {
        return days;
    }

    public int getNServer() {
        return nServer;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public int getAvgArrivalCycle() {
        return avgArrivalCycle;
    }

    public int getMinSvcTime() {
        return minSvcTime;
    }

    public int getMaxSvcTime() {
        return maxSvcTime;
    }

    public int getMinCallSvcTime() {
        return minCallSvcTime;
    }

    public int getMaxCallSvcTime() {
        return maxCallSvcTime;
    }

    public double getProbVip() {
        return probVip;
    }

    public double getProbCall() {
        return probCall;
    }
}
