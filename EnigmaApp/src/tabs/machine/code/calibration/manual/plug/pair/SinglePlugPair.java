package tabs.machine.code.calibration.manual.plug.pair;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

public class SinglePlugPair extends HBox {

    private Label plugOneLabel = new Label();
    private Label plugTwoLabel = new Label();
    private Button deletePlugPairButton = new Button("Delete");
    private String plugOneCharacter;
    private String plugTwoCharacter;

    public SinglePlugPair(String plugOneCharacter, String plugTwoCharacter){
        plugOneLabel.setText(plugOneCharacter);
        plugTwoLabel.setText(plugTwoCharacter);
        plugOneLabel.setMinWidth(30);
        plugTwoLabel.setMinWidth(30);
        getChildren().addAll(plugOneLabel, plugTwoLabel, deletePlugPairButton);
        setSpacing(10);
        deletePlugPairButton.setMnemonicParsing(false);
        this.plugOneCharacter = plugOneCharacter;
        this.plugTwoCharacter = plugTwoCharacter;
        setMinWidth(USE_PREF_SIZE);
    }

    public void setDeletePlugPair(EventHandler<ActionEvent> deletePlugPairEvent) {
        deletePlugPairButton.setOnAction(deletePlugPairEvent);
    }

    public String getPlugOneCharacter() {
        return plugOneCharacter;
    }

    public String getPlugTwoCharacter() {
        return plugTwoCharacter;
    }



}
