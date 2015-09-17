package SequenceGraphClustering;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yqb7 on 6/30/15.
 */
public class Transform {
    ArrayList<OutputFile> outOfFilePair = new ArrayList<OutputFile>();
    Transform(String fasfileDir, String vectorDisDir, String outputFile) throws IOException {
        /**Step 1*************************************/
        System.out.println("Step1. group sequence information");
        SeqCollections fas = new SeqCollections(fasfileDir);
        ArrayList<String> fasFilePair = Tools.getFilePairs(fas.fasNameList);
        /**Step 2*************************************/
        System.out.println("Step2. get distance vector ");
        DistanceVectorFile vecDis = new DistanceVectorFile(vectorDisDir);
        String[] vectorDis = vecDis.vectorContent;
        int counter = 0;
        for (int i = 0; i < fasFilePair.size(); i++){/**this loop is to iterate
         the file pairs created from the fas file name list */
            String[] filePairName = fasFilePair.get(i).split(" ");
            String[] vectorDisLine = vectorDis[counter].split("\t");
            int distance = Integer.parseInt(vectorDisLine[1]);
            /** two case, 1. is the same file sequence comparison
             * 2. the different file sequence comparison*/
            /** case 1 same file */
            if(filePairName[0].equals(filePairName[1])){
                for(int j = 1; j < fas.fasCollect.get(filePairName[1]).getFileSeqNumber(); j++ ){
                    /** this loop is used to iterate the second file in the file pair*/
                    for(int k = 0; k < j; k++){/**his loop is
                     used to iterate the first file in the file pair*/
                        if (distance == 0){
                            outOfFilePair.add(new OutputFile(fas.fasCollect.get(filePairName[0]).seqs.get(k),
                                    fas.fasCollect.get(filePairName[1]).seqs.get(j),
                                    Integer.parseInt(vectorDisLine[1]), "*" ));
                            counter++;
                        }else if (distance != 0){
                            outOfFilePair.add(new OutputFile(fas.fasCollect.get(filePairName[0]).seqs.get(k),
                                    fas.fasCollect.get(filePairName[1]).seqs.get(j),
                                    Integer.parseInt(vectorDisLine[1]), "" ));
                            counter++;
                        }
                        /** OutputFile(SeqInfor f1, SeqInfor f2, int distance) */

                        System.out.println("Counter " + counter);
                    }
                }
            }
            /** case 2 different files */
            else {
                for(int j = 0; j < fas.fasCollect.get(filePairName[1]).getFileSeqNumber(); j++ ){
                    /** this loop is used to iterate the second file in the file pair*/
                    for(int k = 0; k < fas.fasCollect.get(filePairName[0]).getFileSeqNumber(); k++){/**this loop is
                     used to iterate the first file in the file pair*/
                        //String[] vectorDisLine = vectorDis[counter].split("\t");
                        /** OutputFile(SeqInfor f1, SeqInfor f2, int distance) */
                        if(distance == 0){

                        }
                        outOfFilePair.add(new OutputFile(fas.fasCollect.get(filePairName[0]).seqs.get(k),
                                fas.fasCollect.get(filePairName[1]).seqs.get(j),
                                Integer.parseInt(vectorDisLine[1]), "+"));
                        counter++;
                        System.out.println("Counter " + counter);
                    }
                }
            }
        }
        Tools.printout(outputFile, outOfFilePair);
    }
    /**test */
    public static void main(String[] args) throws IOException {
        Transform ob = new Transform(args[0], args[1], args[2]);
    }
}
