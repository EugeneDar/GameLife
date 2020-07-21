package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Canvas canvas;

    private Affine affine;

    private Simulation simulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Controller() {

        int fieldHeight = 60;
        int fieldWidth = 80;

        canvas = new Canvas(600, 800);

        affine = new Affine();
        affine.appendScale(800 / (fieldWidth * 1.0), 600 / (fieldHeight * 1.0));

        simulation = new Simulation(fieldHeight, fieldWidth);

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

    public void step (ActionEvent event) {
        simulation.step();
        draw();
    }

}