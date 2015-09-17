package SequenceGraphClustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yqb7 on 7/2/15.
 */
public class TransformAllSeq {
    /**This class is used to correlated all seqs with the distance vectors*/
    public ArrayList<OutputFile> outOfFilePair = new ArrayList<OutputFile>();
    public ArrayList<OutputFile> trueSame = new ArrayList<OutputFile>();
    public ArrayList<OutputFile> falseSame = new ArrayList<OutputFile>();
    public ArrayList<TrueSameSeq> trueSameSeqList = new ArrayList<TrueSameSeq>();
    public String[] vectorDis;
    public HashMap<String, ArrayList<String>> seq_sameSeqs = new HashMap<String, ArrayList<String>>();
    public ArrayList<SeqInfor> seqDisFas_uniq = new ArrayList<SeqInfor>();
    public TransformAllSeq(String fasfileDir, String vectorDisDir, String outputFile,
                    String trueSameOutput, String falseSameOutput)
            throws IOException {
        /**Step 1*************************************/
        System.out.println("Step1. sequence information");
        SeqCollections fas = new SeqCollections(fasfileDir);
        /**Step 2*************************************/
        System.out.println("Step2. get distance vector ");
        DistanceVectorFile vecDis = new DistanceVectorFile(vectorDisDir);
        vectorDis = vecDis.vectorContent;
        /**Step 3 correlate the seq and distance vector information*/
        System.out.println("Step3. correlate seq information and distance vector");
        int counter = 0;
        for (int i = 1; i < fas.seqDifFas.size(); i++){
            String secondSeqFileName = fas.seqDifFas.get(i).patientfileName;
            for(int j = 0; j < i; j ++){
                String[] vectorDisLine = vectorDis[counter].split("\t");
                int distance = Integer.parseInt(vectorDisLine[1]);

                String firstSeqFileName = fas.seqDifFas.get(j).patientfileName;
                /**case 1. the two seqs belong to the same file, and the distance between them is 0,
                 * then the symbol * will be given*/
                if(firstSeqFileName.equals(secondSeqFileName)){
                    //System.out.println("case 1 found!");
                    if(distance == 0){
                        outOfFilePair.add(new OutputFile(fas.seqDifFas.get(j),
                                fas.seqDifFas.get(i),
                                distance,
                                "*" ));
                        counter++;
                    }
                    else{
                        outOfFilePair.add(new OutputFile(fas.seqDifFas.get(j),
                                fas.seqDifFas.get(i),
                                distance,
                                "" ));
                        counter++;
                    }
                }
                /**case 2. the two seqs belong to different files, and the distance between them is 0
                 * */
                if(!firstSeqFileName.equals(secondSeqFileName)){
                    //System.out.println("case 2 found! counter " + counter );
                    /**to confirm if the two seq are real same or not,
                     * check the corresponding vector values in the row and column*/
                    if(distance == 0){
                        boolean checkIfTrueSame = Tools.checkIfTrueSame(i, j, vectorDis, counter);
                        if(checkIfTrueSame == true){


                            outOfFilePair.add(new OutputFile(fas.seqDifFas.get(j),
                                    fas.seqDifFas.get(i),
                                    distance,
                                    "trueSame"));
                            trueSame.add(new OutputFile(fas.seqDifFas.get(j),
                                    fas.seqDifFas.get(i),
                                    distance,
                                    "trueSame"));
                            trueSameSeqList.add(new TrueSameSeq(fas.seqDifFas.get(j), fas.seqDifFas.get(i)));
                            counter++;
                        }
                        if(checkIfTrueSame == false){
                            outOfFilePair.add(new OutputFile(fas.seqDifFas.get(j),
                                    fas.seqDifFas.get(i),
                                    distance,
                                    "falseSame" ));
                            falseSame.add(new OutputFile(fas.seqDifFas.get(j),
                                    fas.seqDifFas.get(i),
                                    distance,
                                    "falseSame" ));
                            counter++;
                        }
                    }
                    else{
                        outOfFilePair.add(new OutputFile(fas.seqDifFas.get(j),
                                fas.seqDifFas.get(i),
                                distance,
                                "" ));
                        counter++;
                    }
                }
            }
        }
        Tools.printout(outputFile, outOfFilePair);
        Tools.printoutSeq(trueSameOutput, trueSame);
        Tools.printoutSeq(falseSameOutput, falseSame);
        /** unique the seqDifFas into seqDifFas_uniq and give value to the SeqInfor.sameSeqs
         * seqDisFas_uniq hold all node information*/
        seq_sameSeqs = Tools.classifySeq(trueSameSeqList);
        boolean checkSharedSeq = false;
        int nodeCounter = 0;
        for(SeqInfor eachSeq: fas.seqDifFas){
            /** case 1, the seqs having same seqs */
            if(seq_sameSeqs.containsKey(eachSeq.fullSeqName)){
                eachSeq.seqIndex_uniqNode = nodeCounter;
                eachSeq.group = 3; //if the seq have the same seq then the group number is assigned to 3
                eachSeq.sameSeqs = seq_sameSeqs.get(eachSeq.fullSeqName);
                seqDisFas_uniq.add(eachSeq);
                nodeCounter++;
            }
            /** case 2, the seqs not having same seqs, need to check if this seq is the shared
             * seq of case 1*/
            else{
                checkSharedSeq = Tools.chekIfHavaSharedSeq(eachSeq, seq_sameSeqs);
                if(checkSharedSeq == false){
                    eachSeq.seqIndex_uniqNode = nodeCounter;
                    seqDisFas_uniq.add(eachSeq);
                    nodeCounter++;
                }
            }
        }
        /**Get all possible edges and delete all the shared sequence reads from the
         * edges set , outOfFilePair contains all the possible edges */
    }
    /**test */
    public static void main(String[] args) throws IOException {
        long start_add = System.currentTimeMillis();
        TransformAllSeq ob = new TransformAllSeq(args[0], args[1], args[2], args[3], args[4]);
        long end_add = System.currentTimeMillis();
        System.out.print("The time used is : ");
        Tools.printStat(start_add, end_add);
    }
}
