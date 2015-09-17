package SequenceGraphClustering;

import java.util.ArrayList;

/**
 * Created by yqb7 on 6/29/15.
 */
public class FasFileInfor {
    ArrayList<SeqInfor> seqs = new ArrayList<SeqInfor>();
    String patientfileName;
    int fasFileSeqNumber;
    FasFileInfor(String fName, ArrayList<SeqInfor> sequenceInFile){
        this.seqs = sequenceInFile;
        this.patientfileName = fName;
    }
    public int getFileSeqNumber(){
        return fasFileSeqNumber = seqs.size();
    }
}
