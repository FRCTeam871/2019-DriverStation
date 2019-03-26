package com.team871.newStuff;

/**
 * @author T3Pfaffe on 3/7/2019.
 * @project DriverStation
 */
public class TimedLoopThread implements Runnable{

    private Runnable runnable;
    private long rate;
    private boolean doDebugPrint;


    /**
     * Will take a runnable and run it inside of a loop at the rate given.
     * @param runnable what will be looped
     * @param rate how many times this should run *per second*
     */
    public TimedLoopThread(Runnable runnable, long rate){
        this.runnable = runnable;
        this.rate = rate;

        this.doDebugPrint = false;
    }

    public void doDebugPrint(boolean doDebugPrint){
        this.doDebugPrint = doDebugPrint;
    }

    @Override
    public void run() {
        long startT;
        while(true) {
            startT = System.currentTimeMillis();

            runnable.run();

            try {
                final long sleepMillis = ((1000/rate) - (System.currentTimeMillis() - startT));
                Thread.sleep(Math.max(sleepMillis, 0));
                if (doDebugPrint)
                    System.out.println("Met target loop rate of " + rate + "hz (" + 1000/rate + "ms) by: " + sleepMillis +"ms");
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupt on an instance of TimedLoopThread from: (" + this.runnable.toString() + ") ");
            }
        }
    }
}
