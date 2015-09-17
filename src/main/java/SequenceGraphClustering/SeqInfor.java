package SequenceGraphClustering;

import java.util.ArrayList;

/**
 * Created by yqb7 on 6/26/15.
 */
public class SeqInfor {
    public int seqIndex_uniqNode = -1; //unique node number
    public String patientfileName;
    public String genotype;
    public int sequenceIndex; // read in from the fas file
    public int repeatNumber;
    public String sequence;
    public ArrayList<String> sameSeqs;
    public String fullSeqName;
    public int group = 0; //file1 group = 1; file2 group = 2; for the shared seq group = -1;
    SeqInfor(int nodeIndex, String fileName, String gP, int seqIndex,
             int repeatTimes, String seq, ArrayList<String> sameSeq){
        this.seqIndex_uniqNode = nodeIndex;
        this.patientfileName = fileName;
        this.sequenceIndex = seqIndex;
        this.genotype = gP;
        this.repeatNumber = repeatTimes;
        this.sequence = seq;
        this.sameSeqs = sameSeq;
        fullSeqName = patientfileName + "_" + genotype + "_" + sequenceIndex
                + "_" + repeatNumber;
    }
}
