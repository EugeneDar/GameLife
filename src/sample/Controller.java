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
    public Canvas canvas;
    @FXML
    private Spinner<Integer> spinner1;
    @FXML
    private Spinner<Integer> spinner2;
    @FXML
    private Text cursorPos;

    public Affine affine;

    public Simulation simulation;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            spinner1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 600,60));
            spinner2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50,30));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Controller() {

        int fieldHeight = 60;
        int fieldWidth = 80;

        canvas = new Canvas(600, 800);

        affine = new Affine();
        affine.appendScale(800 / (fieldWidth * 1.0), 600 / (fieldHeight * 1.0));

        simulation = new Simulation(fieldHeight, fieldWidth);
        simulation.randomFilling(30);
        draw();
    }



    public void draw() {
        // choose Canvas
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setTransform(affine);

        // fill all Rectangle
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, 0, 600, 800);

        // choose color
        g.setFill(Color.BLUE);
        // paint cells
        for (int x = 0; x < this.simulation.width; x++) {
            for (int y = 0; y < this.simulation.height; y++) {
                if (this.simulation.isAlive(x, y) == 1) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }

        // paint lines (#)
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



    public void mouseDraw (MouseEvent event) throws InterruptedException {

        boolean pauseMode = pause;

        // pause AppThread
        stop();

        // paint cell
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

        // start AppThread
        if (!pauseMode) {
            pause = false;
            thread = new AppThread(this);
        }
    }

    public volatile boolean pause = true;
    AppThread thread = null;

    public void step (ActionEvent event) {
        if (pause) {
            simulation.step();
            draw();
        }
    }

    public void start (ActionEvent event) {
        pause = false;
        thread = new AppThread(this);
    }

    public void stop (ActionEvent event) {
        pause = true;
    }
    public void stop () {
        try {
            pause = true;
            if (thread != null) {
                thread.join();
            }
        } catch (Exception e) {
            System.out.println("Exception ");
        }
    }

    public void reset () throws InterruptedException {

        // pause AppThread
        stop();

        // get
        int density = spinner2.getValue();
        int fieldHeight = spinner1.getValue();
        int fieldWidth = fieldHeight * 4 / 3;
        System.out.println("resolution : " + fieldHeight + " density : " + density);

        affine = new Affine();
        // height : width = 3 : 4
        // resolution = height
        affine.appendScale(800 / (fieldWidth * 1.0), 600 / (fieldHeight * 1.0));

        // create simulation
        simulation = new Simulation(fieldHeight, fieldWidth);
        simulation.randomFilling(density);
        draw();
    }

}