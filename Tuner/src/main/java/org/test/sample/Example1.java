package org.test.sample;

public class Example1 implements Runnable {
    public Example1(){
        System.out.println("Example1 instance  was created");
    }

    public void doSmthng(){
        //tuneAbleParam name=findMe start=2 stop=20
        int findMe = 1;

    }

    @Override
    public void run() {
        System.out.println("run lola 2");
    }
}
