package com.patulus.taffystore.node;

public class Node<T> {

    private Node<T> prev;
    private final T value;
    private Node<T> next;

    public Node(T value) {
        this.prev = null;
        this.value = value;
        this.next = null;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node<T> getPrev() {
        return this.prev;
    }

    public T getValue() {
        return this.value;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getNext() {
        return this.next;
    }

}
