package com.team871.config.network;


import com.team871.util.data.BinaryDataValue;
import com.team871.util.data.IData;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * @author TP-Laptop on 1/5/2019.
 * @project Robotics-Dashboard-Code
 *
 * Template that sets up an agreement of the data
 *  available on the NetworkTables.
 * Will *attempt* in insure that both the server
 *  and client are working on an agreed set of
 *  variables.
 */
public abstract class AbstractNetConfig {

  public final NetworkTableInstance networkTableInstance;
  public final String SERVER_VERSION_KEY = "SERVER_VERSION";
  public final String CLIENT_VERSION_KEY = "CLIENT_VERSION";
  public final String IS_RED_TEAM_KEY  = "TODO";
  public final String MATCH_NUMBER_KEY = "TODO";
  public final String GAME_TIME_KEY    = "TODO";

  public final String networkIdentity;
  public IData<Boolean> isConected;

  private Thread checkVersionThread;

  public AbstractNetConfig(boolean isClient, NetworkTableInstance instance, String VERSION_VAL) {
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    this.networkTableInstance = instance;

    if(isClient) {
      networkIdentity = "client";
      instance.setNetworkIdentity(networkIdentity);
    }
    else {
      networkIdentity = "server";
      instance.setNetworkIdentity(networkIdentity);
      instance.getEntry(SERVER_VERSION_KEY).setString(VERSION_VAL);
    }

    boolean isConnecedtL = (instance.isConnected());
    this.isConected = new BinaryDataValue(isConnecedtL);

    Runnable checkVersionTask = () ->{
      try {
        while(!instance.isConnected())
          Thread.sleep(250);
      } catch (InterruptedException e) {
//        e.printStackTrace();
      }

      System.out.println(" ");
      if (!instance.getEntry(SERVER_VERSION_KEY).getString("null").equals(VERSION_VAL)) {
        if(isClient) {
          System.out.println("WARNING!! NetworkTableKey versions are not the same. \nThis may make the robot and any clients incompatible.");
          System.out.println("Server(robot) Version: " + instance.getEntry(SERVER_VERSION_KEY).getString("[Not_Found]"));
          System.out.println("       Client Version: " + VERSION_VAL);
        }else
          System.out.println("ERROR!! NetworkTables were not set correctly, clients will most likely not work in conjunction with this server");
      }else{
        System.out.println("NetworkTable connection started with no found problems");
        System.out.println( networkIdentity + " broadcasting with version: " + VERSION_VAL);
      }
      this.isConected = new BinaryDataValue(instance.isConnected());
      System.out.println(" ");
      Thread.currentThread().stop();
    };

    checkVersionThread = new Thread(checkVersionTask);
    checkVersionThread.setDaemon(true);
    checkVersionThread.start();
    //Fun fact #3064: you cant set Daemeon to true if you already started the thread.

    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }



  public NetworkTableInstance getInstance(){
    return networkTableInstance;
  }

  public abstract NetworkTable getTable();

  private void close(){
    if(checkVersionThread.isAlive()){
      checkVersionThread.interrupt();
    }
  }

}