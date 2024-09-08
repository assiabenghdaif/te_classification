package fsm.miaad.models;

public class FastaEntry {
    private final String sequenceID;
    private final String sequence;

    public FastaEntry(String sequenceID, String sequence) {
        this.sequenceID = sequenceID;
        this.sequence = sequence;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public String getSequence() {
        return sequence;
    }
}
