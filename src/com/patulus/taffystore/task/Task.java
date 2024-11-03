package com.patulus.taffystore.task;

import com.patulus.taffystore.server.Server;

public abstract class Task {
    /** 고객 번호 */
    protected int custNum;
    /** 도착 시각 */
    protected long arriveTime;
    /** 담당 점원 */
    protected Server server;
    /** 서비스 시작 시각 */
    protected long startTime;
    /** 서비스 예정 시간 */
    protected int svcTime;
    /** 서비스 실제 종료 시간 */
    protected int elapsedSvcTime;
    /** 서비스 완료 여부 */
    protected boolean isDone;

    public Task(int arriveTime, int svcTime) {
        this.custNum = -1;
        this.arriveTime = arriveTime;
        this.server = null;
        this.startTime = -1;
        this.svcTime = svcTime;
        this.elapsedSvcTime = -1;
        this.isDone = false;
    }

    public void setCustNum(int custNum) {
        this.custNum = custNum;
    }

    public int getCustNum() {
        return custNum;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getSvcTime() {
        return svcTime;
    }

    public void setElapsedSvcTime(int elapsedSvcTime) {
        this.elapsedSvcTime = elapsedSvcTime;
    }

    public long getElapsedSvcTime() {
        return this.elapsedSvcTime;
    }

    public void svcDone() {
        this.isDone = true;
    }

    public boolean getIsDone() {
        return this.isDone;
    }
}
