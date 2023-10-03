package machineHistory;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;

import java.io.Serializable;

public class ProcessedString extends Node implements Serializable {

    private String input = "";
    private String output = "";
    private long nanoSeconds = 0L;

    public ProcessedString(){;}

    public ProcessedString(String input, String output, long nanoSeconds) {

        this.input = input;
        this.output = output;
        this.nanoSeconds = nanoSeconds;
    }

    @Override
    protected NGNode impl_createPeer() {
        return null;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        return null;
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        return false;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public long getNanoSeconds() {
        return nanoSeconds;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setNanoSeconds(long nanoSeconds) {
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

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return null;
    }
}
