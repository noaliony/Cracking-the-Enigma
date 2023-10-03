package machineHistory;

import java.io.Serializable;

public class ProcessedString implements Serializable {

    private String input;
    private String output;
    private int nanoSeconds;

    public ProcessedString(String input, String output, int nanoSeconds) {

        this.input = input;
        this.output = output;
        this.nanoSeconds = nanoSeconds;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public int getNanoSeconds() {
        return nanoSeconds;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setNanoSeconds(int nanoSeconds) {
        this.nanoSeconds = nanoSeconds;
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append("<");
        result.append(input);
        result.append("> --> <");
        result.append(output);
        result.append("> (");
        result.append(String.format("%,d nano-seconds", nanoSeconds));
        result.append(")");

        return result.toString();
    }
}
