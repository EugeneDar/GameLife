package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Spinner;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Canvas canvas;
    @FXML
    Spinner<Integer> spinner123;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
