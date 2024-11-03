package com.patulus.taffystore.deque;

import com.patulus.taffystore.node.Node;

public class Deque<T> {
    // 다음 고객에게 부여될 고객 번호
    private static volatile int cusCntr = 1;
    // 대기열 첫 번째 고객
    private Node<T> front;
    // 대기열 마지막 고객
    private Node<T> rear;
    // 대기열에 있는 고객의 수
    private volatile int nWaiter;

    public Deque() {
        front = null;
        rear = null;
        nWaiter = 0;
    }

    synchronized public void offer(T value) throws Exception {
        this.offerLast(value);
    }

    synchronized public void offerFirst(T value) throws Exception {
        if (this.isFull()) {
            throw new Exception("큐가 가득 차 있습니다.");
        }

        Node<T> newNode = new Node<>(value);
        newNode.setNext(front);

        // 덱 처음에 넣으므로 newNode를 front의 이전 노드로 지정
        if (front != null) {
            front.setPrev(newNode);
        }

        // front를 newNode로 지정
        front = newNode;

        // 덱이 빈 상태였다면 rear도 newNode로 지정
        if (front.getNext() == null) {
            rear = front;
        }

        ++nWaiter;
    }

    synchronized public void offerLast(T value) throws Exception {
        if (this.isFull()) {
            throw new Exception("큐가 가득 차 있습니다.");
        }

        // 덱이 비어 있다면 offerFirst()를 실행
        if (this.isEmpty()) {
            this.offerFirst(value);
            return;
        }

        Node<T> newNode = new Node<>(value);

        // 덱 마지막에 넣으므로 newNode를 rear 다음 노드로 지정
        rear.setNext(newNode);
        newNode.setPrev(rear);
        rear = newNode;

        ++nWaiter;
    }

    synchronized public T poll() throws Exception {
        return this.pollFront();
    }

    synchronized public T pollFront() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("큐가 비어 있습니다.");
        }

        T res = front.getValue();
        Node<T> nextNode = front.getNext();

        // 덱의 원소가 하나 이상일 때
        // 첫 번째 노드를 지우는 것이므로 front 다음 노드의 이전 노드를 null로 지정
        if (nextNode != null) {
            nextNode.setPrev(null);
        }
        // front를 다음 노드로 지정
        front = nextNode;

        // 이후 덱에 아무 것도 없으면 rear를 null로 지정
        // front는 nextNode에 의해 null로 지정됨
        if (this.isEmpty()) {
            rear = null;
        }

        --nWaiter;
        return res;
    }

    synchronized public T pollLast() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("큐가 비어 있습니다.");
        }

        T res = rear.getValue();
        Node<T> prevNode = rear.getPrev();

        // 마지막 노드의 이전 노드가 있을 때
        // 마지막 노드 이전 노드의 다음 노드는 없으므로 null로 지정
        if (prevNode != null) {
            prevNode.setNext(null);
        }
        // 마지막 노드를 마지막 노드 이전 노드로 지정
        rear = prevNode;

        // 이후 덱에 아무 것도 없으면 front를 null로 지정
        // rear는 prevNode에 의해 null로 지정됨
        if (this.isEmpty()) {
            front = null;
        }

        --nWaiter;
        return res;
    }

    synchronized public boolean isFull() {
        return false;
    }

    synchronized public boolean isEmpty() {
        return front == null;
    }

    synchronized public T getFront() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("큐가 비어 있습니다.");
        }

        return front.getValue();
    }

    synchronized public T getRear() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("큐가 비어 있습니다.");
        }

        return rear.getValue();
    }

    synchronized public int getNWaiter() {
        return nWaiter;
    }

    synchronized public static int takeNum() {
        return cusCntr++;
    }

    synchronized public static void resetNum() {
        cusCntr = 1;
    }

    synchronized public static int getTotCust() {
        return cusCntr - 1;
    }

}
