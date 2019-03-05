package com.team871.config.Style;

import javafx.scene.paint.Color;

/**
 * @author T3Pfaffe on 1/19/2019.
 * @project DriverStation
 */
public class ColorModeController {
    private static Boolean hasInit;
    private static ColorMode colorMode;

    /**
     * Allows for the control of a ColorMode configuration
     * Can only be created once to control one ColorMode.
     *
     * @param colorMode
     */
    public ColorModeController(ColorMode colorMode) {
        if (hasInit == null) {
            hasInit = false;
        } else if (!hasInit) {
            hasInit = true;
        } else {
            System.out.println("ERROR!! ColorModeController instantiated twice, will use already assigned ColorModeObject!");
            return;
        }


        ColorModeController.colorMode = colorMode;
    }

    /**
     * @param newPrimaryColor the new primary color
     */
    public void setPrimaryColor(Color newPrimaryColor) {
        setPrimaryColor(newPrimaryColor);
    }

    /**
     * will invert the base color(dark or light).
     */
    public void changeBaseColor() {
        changeBaseColor();
    }

    public void update(){
        changeBaseColor();
        changeBaseColor();
    }

}
