package decryption.manager;

import java.util.List;

public class TaskResult {
    private List<StringDecryptedCandidate> stringDecryptedCandidateList;
    private long milliSecondTimeTookToDecrypt;

    public TaskResult(List<StringDecryptedCandidate> stringDecryptedCandidateList, long milliSecondTimeTookToDecrypt){
        this.stringDecryptedCandidateList = stringDecryptedCandidateList;
        this.milliSecondTimeTookToDecrypt = milliSecondTimeTookToDecrypt;
    }

    public List<StringDecryptedCandidate> getStringDecryptedCandidateList() {
        return stringDecryptedCandidateList;
    }

    public long getMilliSecondTimeTookToDecrypt() {
        return milliSecondTimeTookToDecrypt;
    }
}
