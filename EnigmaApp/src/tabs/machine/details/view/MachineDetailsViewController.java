package tabs.machine.details.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import machine.details.MachineDetailsObject;

public class MachineDetailsViewController {
    @FXML private Label amountOfRotorsInUseValueLabel;
    @FXML private Label possibleAmountOfRotorsValueLabel;
    @FXML private Label amountOfReflectorsValueLabel;
    @FXML private Label amountOfIsEncodedMessagesValueLabel;


    public void setTextAmountOfRotorsInUseLabel(String amountOfRotorsInUseLabel) {
        this.amountOfRotorsInUseValueLabel.setText(amountOfRotorsInUseLabel);
    }

    public void setTextPossibleAmountOfRotorsLabel(String possibleAmountOfRotorsLabel) {
        this.possibleAmountOfRotorsValueLabel.setText(possibleAmountOfRotorsLabel);
    }

    public void setTextAmountOfReflectorsLabel(String amountOfReflectorsLabel) {
        this.amountOfReflectorsValueLabel.setText(amountOfReflectorsLabel);
    }

    public void setTextAmountOfIsEncodedMessagesLabel(String amountOfIsEncodedMessagesLabel) {
        this.amountOfIsEncodedMessagesValueLabel.setText(amountOfIsEncodedMessagesLabel);
    }


    public void updateMachineDetails(MachineDetailsObject machineDetails) {
        setTextAmountOfRotorsInUseLabel(machineDetails.getAmountOfRotorsInUse().toString());
        setTextPossibleAmountOfRotorsLabel(machineDetails.getPossibleAmountOfRotors().toString());
        setTextAmountOfReflectorsLabel(machineDetails.getAmountOfReflectors().toString());
        setTextAmountOfIsEncodedMessagesLabel(machineDetails.getAmountOfIsEncodedMessages().toString());
    }
}
