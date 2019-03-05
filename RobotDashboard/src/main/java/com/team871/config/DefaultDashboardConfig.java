package com.team871.config;

import com.team871.config.Style.ColorMode;
import edu.wpi.first.networktables.NetworkTableInstance;


public class DefaultDashboardConfig implements IDashboardConfig {

  private final ColorMode colorMode;
  private NetworkTableInstance networkTableInstance;

  /**
   * A dashboard configuration filled with default values.
   * ID is set to "GUI" and team number 871 by default
   **/
  public DefaultDashboardConfig() {
    this("GUI_CLIENT", 871);
  }

  /**
   * A dashboard configuration filled with default values.
   * @param teamNumber FRC team number to communicate on network.
   */
  public DefaultDashboardConfig(int teamNumber) {
    this("GUI_CLIENT", teamNumber);
  }

  /**
   * A dashboard configuration filled with default values.
   *
   * @param networkID  the identity recognized by NetworkTables
   * @param teamNumber the team number recognized by NetworkTables
   */
  public DefaultDashboardConfig(String networkID, int teamNumber) {
    colorMode = new ColorMode(true);

    networkTableInstance = NetworkTableInstance.getDefault();
    networkTableInstance.setNetworkIdentity(networkID);
    networkTableInstance.startClientTeam(teamNumber);
  }

  @Override
  public ColorMode getColorMode() {
    return colorMode;
  }

  @Override
  public NetworkTableInstance getNetworkTableInstance() {
    return networkTableInstance;
  }

  @Override
  public int getInitialWidth() {
    return 1280;
  }

  @Override
  public int getInitialHeight() {
    return 720;
  }
}
