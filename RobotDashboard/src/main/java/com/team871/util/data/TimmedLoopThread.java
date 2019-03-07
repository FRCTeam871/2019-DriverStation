package com.team871.util.data;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class TimmedLoopThread implements Runnable{

    private Runnable runnable;
    private long rate;


    /**
     * Will take a runnable and run it inside of a loop at the rate given.
     * @param runnable what will be looped
     * @param rate how many times this should run *per second*
     */
    public TimmedLoopThread(Runnable runnable, long rate){
        this.runnable = runnable;
        this.rate = rate;
    }

    @Override
    public void run() {
        long startT;
        while(true) {
            startT = System.currentTimeMillis();

            runnable.run();

            try {
                final long sleepMillis = (rate - (System.currentTimeMillis() - startT));
                Thread.sleep(Math.max(sleepMillis, 0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
