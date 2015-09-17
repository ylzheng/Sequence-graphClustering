package SequenceGraphClustering;

/**
 * Created by yqb7 on 6/30/15.
 */
public class OutputFile implements Comparable<OutputFile> {
    public int seqDistance;
    public SeqInfor file1, file2;
    public String symbolForFalseSame;
    /**if the two sequences are real same, do nothing
     * if the two sequences are false same, give a "*" to this sequence pair*/
    OutputFile(SeqInfor f1, SeqInfor f2, int distance, String falseSameSymbol){
        this.seqDistance = distance;
        this.file1 = f1;
        this.file2 = f2;
        this.symbolForFalseSame = falseSameSymbol;
    }

    @Override
    public int compareTo(OutputFile o) {
        int comparedSize = o.seqDistance;
        if (this.seqDistance > comparedSize) {
            return 1;
        } else if (this.seqDistance == comparedSize) {
            return 0;
        } else {
            return -1;
        }
    }
}
