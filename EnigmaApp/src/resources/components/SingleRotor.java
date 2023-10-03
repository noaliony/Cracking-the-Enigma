package resources.components;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.app.MainController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import static javafx.geometry.Pos.CENTER;

public class SingleRotor extends VBox {

    private final Integer rotorID;
    private final int IMAGE_SIZE = 65;
    private final String IMAGE_URL = "/resources/images/Rotor.png";

    public SingleRotor(int rotorID) throws IOException {
        this.rotorID = rotorID;
        setUpSingleRotor();
    }

    private void setUpSingleRotor() throws IOException {
        URL imageURL = getClass().getResource(IMAGE_URL);
        InputStream inputStream = Objects.requireNonNull(imageURL).openStream();
        ImageView image = new ImageView(new Image(inputStream));
        Label rotorIDLabel = new Label(rotorID.toString());

        image.setFitWidth(IMAGE_SIZE);
        image.setFitHeight(IMAGE_SIZE);
        rotorIDLabel.setAlignment(CENTER);
        getChildren().add(image);
        getChildren().add(rotorIDLabel);
    }

}
