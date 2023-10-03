package tabs.encryptdecrypt.history.configuration.history.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import machineHistory.ProcessedString;

import java.util.List;

public class ConfigurationHistoryView extends HBox {
    private Button lessOrMoreViewButton = new Button("Less");
    private VBox configurationsViewVBox;
    private Label configurationTitleLabel;
    private ListView<String> processedStringViewListView;

    public ConfigurationHistoryView(String ConfigurationTextTitle){
        lessOrMoreViewButton.setMnemonicParsing(false);
        lessOrMoreViewButton.setOnAction(event -> lessOrMoreViewButtonAction());
        configurationTitleLabel = new Label(ConfigurationTextTitle);
        configurationTitleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        configurationTitleLabel.setUnderline(true);
        processedStringViewListView = new ListView<>();
        configurationsViewVBox = new VBox(configurationTitleLabel, processedStringViewListView);
        configurationsViewVBox.setSpacing(15);
        setSpacing(15);
        setMinWidth(USE_PREF_SIZE);
        getChildren().addAll(lessOrMoreViewButton, configurationsViewVBox);
        turnOnLessOrMoreViewButton();
    }


    public String getTextConfigurationTitleLabel() {
        return configurationTitleLabel.getText();
    }

    private void lessOrMoreViewButtonAction() {
        if (lessOrMoreViewButton.getText().equals("More")){
            turnOnLessOrMoreViewButton();
        } else {
            turnOffLessOrMoreViewButton();
        }
    }

    public void turnOffLessOrMoreViewButton(){
        lessOrMoreViewButton.setText("More");
        processedStringViewListView.setVisible(false);
    }

    private void turnOnLessOrMoreViewButton(){
        lessOrMoreViewButton.setText("Less");
        processedStringViewListView.setVisible(true);
    }

    public void addProcessedString(ProcessedString processedString){
        processedStringViewListView.getItems().add(processedString.toString());
    }
}
