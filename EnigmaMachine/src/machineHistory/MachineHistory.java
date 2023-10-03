package machineHistory;

import dto.MachineSetting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MachineHistory implements Serializable, Cloneable {

    private MachineSetting machineSetting;
    private List<ProcessedString> processedStringList;
    private ProcessedString currentProcessedString;

    public MachineHistory(MachineSetting machineSetting, List<ProcessedString> isEncodedStringList) {

        this.machineSetting = machineSetting;
        this.processedStringList = isEncodedStringList;
    }

    public MachineSetting getMachineSetting() {
        return machineSetting;
    }

    public List<ProcessedString> getProcessedStringList() {
        return processedStringList;
    }

    public ProcessedString getCurrentProcessedString() {
        return currentProcessedString;
    }

    public void setCurrentProcessedString(ProcessedString currentProcessedString) {
        this.currentProcessedString = currentProcessedString;
    }

    @Override
    public MachineHistory clone() {
        try {
            MachineHistory clone = (MachineHistory) super.clone();

            clone.processedStringList = new ArrayList<>(this.processedStringList);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
