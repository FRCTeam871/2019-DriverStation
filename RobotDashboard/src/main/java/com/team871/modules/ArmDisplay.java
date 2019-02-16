package com.team871.modules;

import com.team871.util.data.IData;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

/**
 * @author T3Pfaffe on 2/15/2019.
 * @project DriverStation
 */
public class ArmDisplay extends VBox {

    private GraphicsContext graphicsContext;
    private Canvas canvas;

    public ArmDisplay(){
        super();

        canvas = new Canvas();
       //this.setPrefHeight(400);
       canvas.setWidth(250);
       canvas.setHeight(250);
       graphicsContext = canvas.getGraphicsContext2D();
       graphicsContext.scale(.75,.75);
       graphicsContext.save();

       this.getChildren().add(canvas);



    }

    public void initialize(IData<Double> readAngle1, IData<Double> readAngle2, IData<Double> readAngle3){



        AnimationTimer anim = new AnimationTimer() {

            @Override
            public void handle(long now) {

                graphicsContext.save();

                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setFill(Color.TRANSPARENT);

                graphicsContext.save();
                graphicsContext.translate(canvas.getWidth() /2, (canvas.getHeight() / 2));


                // draw arm

                int length1 = 70;
                int length2 = 70;

                int size = 20;

                double angle1 = readAngle1.getValue();
                double angle2 = readAngle2.getValue();
                double angle3 = readAngle3.getValue() * 90.0;


                drawArm(graphicsContext, length1, length2, size, angle1, angle2, angle3, false, Color.TRANSPARENT, Color.TRANSPARENT);

                graphicsContext.restore();
            }
        };

        anim.start();
    }

    private void drawArm(GraphicsContext graphicsContext, int length1, int length2, int size, double angle1, double angle2, double angle3, boolean drawLimitCircles, Paint jointFill, Paint connectorFill) {
        graphicsContext.save();

        graphicsContext.setLineDashes(5);

        double endX = length1 * Math.cos(Math.toRadians(angle1)) + length2 * Math.cos(Math.toRadians(angle1 + angle2));
        double endY = length1 * Math.sin(Math.toRadians(angle1)) + length2 * Math.sin(Math.toRadians(angle1 + angle2));

        if (drawLimitCircles) {

            graphicsContext.setStroke(Color.LIGHTGRAY);
            //System.out.println("1");
            graphicsContext.strokeLine(0, 0, endX, endY);
            //System.out.println("2");

            graphicsContext.strokeOval(-length1, -length1, length1 * 2, length1 * 2);
            graphicsContext.strokeOval(-(length1 + length2), -(length1 + length2), (length1 + length2) * 2, (length1 + length2) * 2);

            graphicsContext.setLineDashes(0);
            graphicsContext.strokeOval(-(length1 + length2 + size), -(length1 + length2 + size), (length1 + length2 + size) * 2, (length1 + length2 + size) * 2);

            graphicsContext.setLineDashes(5);
            graphicsContext.translate(length1 * Math.cos(Math.toRadians(angle1)), length1 * Math.sin(Math.toRadians(angle1)));
            graphicsContext.rotate(angle1);

            graphicsContext.strokeOval(-length2, -length2, length2 * 2, length2 * 2);

            graphicsContext.translate(length2 * Math.cos(Math.toRadians(angle2)), length2 * Math.sin(Math.toRadians(angle2)));

            graphicsContext.strokeOval(-size, -size, size * 2, size * 2);
        }

        graphicsContext.restore();

        //System.out.println(lastA1);


        graphicsContext.setStroke(Color.BLACK);


        graphicsContext.setStroke(connectorFill);
        graphicsContext.setLineWidth(size);
        graphicsContext.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1)), (size / 2) * Math.sin(Math.toRadians(angle1)), (length1 - size / 2) * Math.cos(Math.toRadians(angle1)), (length1 - size / 2) * Math.sin(Math.toRadians(angle1)));

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1 + 90)), (size / 2) * Math.sin(Math.toRadians(angle1 + 90)), length1 * Math.cos(Math.toRadians(angle1)) + (size / 2) * Math.cos(Math.toRadians(angle1 + 90)), length1 * Math.sin(Math.toRadians(angle1)) + (size / 2) * Math.sin(Math.toRadians(angle1 + 90)));
        graphicsContext.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1 - 90)), (size / 2) * Math.sin(Math.toRadians(angle1 - 90)), length1 * Math.cos(Math.toRadians(angle1)) + (size / 2) * Math.cos(Math.toRadians(angle1 - 90)), length1 * Math.sin(Math.toRadians(angle1)) + (size / 2) * Math.sin(Math.toRadians(angle1 - 90)));

        graphicsContext.setFill(jointFill);
        graphicsContext.fillOval(-size / 2, -size / 2, size, size);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.strokeOval(-size / 2, -size / 2, size, size);

        graphicsContext.save();
        graphicsContext.translate(length1 * Math.cos(Math.toRadians(angle1)), length1 * Math.sin(Math.toRadians(angle1)));
        graphicsContext.rotate(angle1);

        graphicsContext.setStroke(Color.BLACK);


        graphicsContext.setStroke(connectorFill);
        graphicsContext.setLineWidth(size);
        graphicsContext.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2)), (size / 2) * Math.sin(Math.toRadians(angle2)), (length2 - size / 2) * Math.cos(Math.toRadians(angle2)), (length2 - size / 2) * Math.sin(Math.toRadians(angle2)));

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2 + 90)), (size / 2) * Math.sin(Math.toRadians(angle2 + 90)), length2 * Math.cos(Math.toRadians(angle2)) + (size / 2) * Math.cos(Math.toRadians(angle2 + 90)), length1 * Math.sin(Math.toRadians(angle2)) + (size / 2) * Math.sin(Math.toRadians(angle2 + 90)));
        graphicsContext.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2 - 90)), (size / 2) * Math.sin(Math.toRadians(angle2 - 90)), length2 * Math.cos(Math.toRadians(angle2)) + (size / 2) * Math.cos(Math.toRadians(angle2 - 90)), length1 * Math.sin(Math.toRadians(angle2)) + (size / 2) * Math.sin(Math.toRadians(angle2 - 90)));

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeOval(-size / 2, -size / 2, size, size);
        graphicsContext.setFill(jointFill);
        graphicsContext.fillOval(-size / 2, -size / 2, size, size);

        graphicsContext.translate(length2 * Math.cos(Math.toRadians(angle2)), length2 * Math.sin(Math.toRadians(angle2)));

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeOval(-size / 2, -size / 2, size, size);
        graphicsContext.setFill(jointFill);
        graphicsContext.fillOval(-size / 2, -size / 2, size, size);

        graphicsContext.rotate(-angle1 + angle3);
        graphicsContext.translate(20, 0);

        graphicsContext.strokeArc(-size / 2, -size / 2, size, size, 180 - 90, 180, ArcType.OPEN);

        //graphicsContext
        //.strokeOval(-size / 2, -size / 2, size, size);
        graphicsContext.restore();
        //graphicsContext
        //.restore();

    }


}
