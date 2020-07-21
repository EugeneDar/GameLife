package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private Spinner<Integer> spinner1;
    @FXML
    private Spinner<Integer> spinner2;
    @FXML
    private Text cursorPos;

    private Affine affine;

    private Simulation simulation;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {/*
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 600, 40);
        spinner1.setValueFactory(valueFactory1);

        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, 25);
        spinner2.setValueFactory(valueFactory2);


        affine = new Affine();
        affine.appendScale(800 / (fieldWidth * 1.0), 600 / (fieldHeight * 1.0));

        simulation = new Simulation(fieldHeight, fieldWidth);
        simulation.randomFilling(30);*/
    }



    public Controller() {
        int fieldHeight = 60;
        int fieldWidth = 80;

        canvas = new Canvas(600, 800);

        affine = new Affine();
        affine.appendScale(800 / (fieldWidth * 1.0), 600 / (fieldHeight * 1.0));

        simulation = new Simulation(fieldHeight, fieldWidth);
        simulation.randomFilling(30);
    }



    public void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setTransform(affine);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, 0, 600, 800);

        g.setFill(Color.BLUE);

        for (int x = 0; x < this.simulation.width; x++) {
            for (int y = 0; y < this.simulation.height; y++) {
                if (this.simulation.isAlive(x, y) == 1) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }

        g.setStroke(Color.GRAY);
        g.setLineWidth(0.05);
        for (int x = 0; x <= this.simulation.width; x++) {
            g.strokeLine(x, 0, x, simulation.height);
        }

        for (int y = 0; y <= this.simulation.height; y++) {
            g.strokeLine(0, y, simulation.width, y);
        }

    }



    public void setCursorPos (MouseEvent event) {
        try {
            Point2D point2D = affine.inverseTransform(event.getX(), event.getY());
            int x = (int) point2D.getX();
            int y = (int) point2D.getY();
            cursorPos.setText(String.format("Cursor: (%d, %d)", x, y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void mouseDraw (MouseEvent event)  {

        try {
            Point2D point2D = affine.inverseTransform(event.getX(), event.getY());
            int x = (int) point2D.getX();
            int y = (int) point2D.getY();
            if (event.isPrimaryButtonDown()) {
                simulation.setAlive(x, y);
            } else {
                simulation.setDead(x, y);
            }
            draw();
        } catch (Exception e) {
            System.out.println(event.getX() + " : " + event.getY());
            e.printStackTrace();
        }
    }



    private boolean pause = true;

    public void step (ActionEvent event) {
        simulation.step();
        draw();
    }

    public void start (ActionEvent event) {
        pause = false;

        Date start = new Date();
        while (!pause) {
            step(event);
        }
    }

    public void stop (ActionEvent event) {
        pause = true;
    }

}