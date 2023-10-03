package machineHistory;

import machine.MachineSetting;

import java.io.Serializable;
import java.util.List;

public class MachineHistory implements Serializable {

    private MachineSetting machineSetting;
    private List<ProcessedString> processedStringList;

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
}
