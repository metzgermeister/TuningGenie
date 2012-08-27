 package org.test.sample;

public class Example1 implements Runnable {

    public void doSmthng(){

        //tuneAbleParam name=first start=2 stop=3
        int first = 1;

        //tuneAbleParam name=second start=-1 stop=1
        int second = 77;

    }

    @Override
    public void run() {
        System.out.println("run lola");
    }
}
