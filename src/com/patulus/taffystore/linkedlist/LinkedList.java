package com.patulus.taffystore.linkedlist;

import com.patulus.taffystore.node.Node;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private volatile int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    synchronized public T search(int index) throws Exception {
        if (this.isEmpty()) {
            throw new Exception("연결 리스트가 비어 있습니다.");
        }

        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        int i = 0;
        Node<T> findNode = null;
        // 인덱스가 tail에 더 가까우면 tail에서부터 탐색 시작
        if (index > this.size / 2) {
            findNode = this.tail;

            // 해당하는 인덱스까지 이동
            for (i = this.size - 1; i > index; --i) {
                findNode = findNode.getPrev();
            }
        // 인덱스가 head에 더 가까우면 head에서부터 탐색 시작
        } else {
            findNode = this.head;

            // 해당하는 인덱스까지 이동
            for (i = 0; i < index; ++i) {
                findNode = findNode.getNext();
            }
        }

        return findNode.getValue();
    }

    synchronized public void add(T value) throws Exception {
        this.addLast(value);
    }

    synchronized public void addFirst(T value) throws Exception {
        if (this.isFull()) {
            throw new Exception("연결 리스트가 가득 차 있습니다.");
        }

        Node<T> newNode = new Node<>(value);

        // 연결 리스트 처음에 추가하는 것이므로 newNode의 다음 노드로 head를 지정하고,
        // head가 null이 아니면 현재 head의 이전 노드로 newNode를 지정
        newNode.setNext(this.head);
        if (this.head != null) {
            this.head.setPrev(newNode);
        }

        // newNode를 head로 지정하고 크기 증가
        this.head = newNode;
        ++this.size;

        // 추가 전 연결 리스트가 빈 상태였다면 추가 후 tail을 newNode(현재 head)로 지정
        if (this.head.getNext() == null) {
            this.tail = this.head;
        }
    }

    synchronized public void addLast(T value) throws Exception {
        if (this.isFull()) {
            throw new Exception("연결 리스트가 가득 차 있습니다.");
        }

        Node<T> newNode = new Node<>(value);

        // 연결 리스트가 빈 상태이면 addFirst()로 노드를 추가
        if (this.isEmpty()) {
            this.addFirst(value);
            return;
        }

        // 연결 리스트 끝에 추가하는 것이므로 newNode의 다음 노드로 tail을 지정하고,
        // tail의 이전 노드로 newNode를 지정
        newNode.setPrev(this.tail);
        tail.setNext(newNode);

        // newNode를 head로 지정하고 크기 증가
        this.tail = newNode;
        ++this.size;
    }

    synchronized public T remove() throws Exception {
        return this.removeLast();
    }

    synchronized public T removeFirst() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("연결 리스트가 비어 있습니다.");
        }

        T res = this.head.getValue();
        Node<T> nextNode = this.head.getNext();

        if (nextNode != null) {
            nextNode.setPrev(null);
        }

        this.head = nextNode;
        --this.size;

        if (this.size == 0) {
            this.tail = null;
        }

        return res;
    }

    synchronized public T removeLast() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("연결 리스트가 비어 있습니다.");
        }

        T res = this.tail.getValue();
        Node<T> prevNode = this.tail.getPrev();

        if (prevNode != null) {
            prevNode.setNext(null);
        }

        this.tail = prevNode;
        --this.size;

        if (this.size == 0) {
            this.head = null;
        }

        return res;
    }

    synchronized public T get(int index) throws Exception {
        return this.search(index);
    }

    synchronized public int size() {
        return this.size;
    }

    synchronized public boolean isFull() {
        return false;
    }

    synchronized public boolean isEmpty() {
        return head == null;
    }
}
