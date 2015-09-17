package SequenceGraphClustering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yqb7 on 6/26/15.
 */
public class SeqCollections {
    /** This class will do the following tasks
     * 1. read in the sequence name, count number and
     * 2. put the seq infor into FasFileInfor format: FasFileInfor*/
    ArrayList<SeqInfor> seqDifFas = new ArrayList<SeqInfor>();
    HashMap<String, FasFileInfor> fasCollect = new HashMap<String, FasFileInfor>();
    //HashSet<String> fasNameList = new HashSet<String>();/**hold the unique fas file name*/
    ArrayList<String> fasNameList = new ArrayList<String>();
    HashMap<String, Integer> fas_groupInfor = new HashMap<>();
    String[] content;
    SeqCollections(String fasFileDir) throws IOException {
        //String content1 = new Scanner(new File(fasFileDir));
        String content1 = new String(Files.readAllBytes(Paths.get(fasFileDir)));
        if(content1.contains("\r")){
            content = content1.split("\r\n");
        }else{
            content = content1.split("\n");
        }
        //String[] vectorContent = new String(Files.readAllBytes(Paths.get(distanceVectorDir))).split("\n");
        /**input fas file */
        /**1. split according to "\n"
         * 2. split according to "_"*/
        fasNameList = Tools.getAllFasFileName(content);/**hold the unique fas file name*/

        /**Create hash structure which can hold the file name as key and save other information*/
        int fileCounter = 0;
        for (String eachFasName : fasNameList){
            fileCounter++;
            ArrayList<SeqInfor> tmp = new ArrayList<SeqInfor>();
            fasCollect.put(eachFasName, new FasFileInfor(eachFasName, tmp));
            fas_groupInfor.put(eachFasName, fileCounter);
        }
        for(int i = 0; i < content.length; i= i+6){
            //System.out.println("i= " + i);
            String[] sequence_head_element = new String[(content.length+1)/6];
            String sequence = "";
            if(content[1].length() > 60){
                for(int j = i ; j <= i + 4; j++){
                    if (content[j].contains(">")){
                        sequence_head_element = Tools.getFileElement(content[j]);
                    }
                    else if(!content[j].equals("\n")){
                        sequence = sequence.concat(content[j]);
                    }
                }
            }else{
                for(int j = i ; j <= i + 5; j++){
                    if (content[j].contains(">")){
                        sequence_head_element = Tools.getFileElement(content[j]);
                    }
                    else if(!content[j].equals("\n")){
                        sequence = sequence.concat(content[j]);
                    }
                }
            }
            int seqIndex_node = -1; // this varialble will be reassigned in the unique step in transformAllSeq
            String fasFileName = sequence_head_element[0].
                    substring(1,sequence_head_element[0].length()).
                    concat("_" + sequence_head_element[1]);
            ArrayList<String> sameSeqName = new ArrayList<String>();
            SeqInfor tmpSeq = new SeqInfor(seqIndex_node, fasFileName,
                    sequence_head_element[2],
                    Integer.parseInt(sequence_head_element[3]),
                    Integer.parseInt(sequence_head_element[4]),
                    sequence,
                    sameSeqName);
            tmpSeq.group = fas_groupInfor.get(tmpSeq.patientfileName);
            seqDifFas.add(tmpSeq);
            for(String eachKey : fasCollect.keySet()){
                if (tmpSeq.patientfileName.equals(eachKey)){
                    /*System.out.println("eachKey " + eachKey + " " +
                            "tmpSeq.patientfileName " + tmpSeq.patientfileName);*/
                    fasCollect.get(eachKey).seqs.add(tmpSeq);
                }
            }
        }
        int size = 0;
        for(String eachName: fasCollect.keySet()){
            size = size + fasCollect.get(eachName).getFileSeqNumber();
            //System.out.println("each fasfile " + fasCollect.get(eachName).seqs.size());
        }
        //System.out.println("size: " + size );
        /**input distance vector file*/

    }

    public static void main(String[] args) throws IOException {
        SeqCollections ob = new SeqCollections(args[0]);
    }
}
