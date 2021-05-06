package classes;

public class Stack {
    Object[] stackArray;
    int maxSize;
    int top;

    public Stack(int maxSize) {
        this.maxSize = maxSize;
        stackArray = new Object[maxSize];
        top = -1;
    }

    public void push(Object a) {
        if (isFull())
            System.out.println("Stack overflow");
        else {
            top++;
            stackArray[top] = a;
        }
    }

    public Object peek() {
        if (isEmpty())
            return null;
        else
            return stackArray[top];
    }

    public Object pop() {
        if (isEmpty())
            return null;
        else {
            Object data = stackArray[top];
            top--;
            return data;
        }
    }

    public Boolean isEmpty() {
        return top == -1;
    }

    public Boolean isFull() {
        return (top + 1 == stackArray.length);
    }

    public int size() {
        return top + 1;
    }

}
