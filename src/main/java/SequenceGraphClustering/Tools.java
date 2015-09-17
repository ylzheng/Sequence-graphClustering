package SequenceGraphClustering;//import org.apache.spark.api.java.JavaRDD;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yqb7 on 6/24/15.
 */
public class Tools {

    /**find the second _'s position, since the file name is HOC_P02_1b_1_2249*/
    public static String[] getFileElement(String line) {
        String[] lineElement = line.split("_");
        return lineElement;
    }
    /**separate the seqInfor arraylist according to the
     * fas file name*/
    public static void getFasFileInfor(String fasFileName, ArrayList<SeqInfor> seqs){
        ArrayList<SeqInfor> seqSameFas = new ArrayList<SeqInfor>();
        for(int i = 0; i < seqs.size(); i++){
            if (seqs.get(i).patientfileName.equals(fasFileName)){
                seqSameFas.add(seqs.get(i));
            }
        }
    }
    /**find out the number of patients in the file*/
    public static ArrayList<String> getAllFasFileName(String[] content){
        ArrayList<String> fasNameList = new ArrayList<String>();
        for(int i = 0; i < content.length; i= i+6){
            //System.out.println("Tools.java i= " + i);
            String[] sequence_head_element = new String[(content.length+1)/6];
            String sequence = "";
            for(int j = i ; j <= i + 4; j++){
                if (content[j].contains(">")){
                    sequence_head_element = Tools.getFileElement(content[j]);
                }
                else if(!content[j].equals("\n")){
                    sequence = sequence.concat(content[j]);
                }
            }
            String fasFileName = sequence_head_element[0].
                    substring(1,sequence_head_element[0].length()).
                    concat("_" + sequence_head_element[1]);
            boolean foundSameName = false;
            for(int j = 0; j < fasNameList.size(); j++){
                if (fasNameList.get(j).equals(fasFileName)) {
                    foundSameName = true;
                }
            }
            if(foundSameName == false){
                fasNameList.add(fasFileName);
            }
        }
        return fasNameList;
    }

    /**get file pairs from the fas file name list*/
    public static ArrayList<String> getFilePairs(ArrayList<String> fasName){
        int sum = 0;
        for(int i = 1; i <= fasName.size(); i++){
            sum = sum + i;
        }
        ArrayList<String> filePair = new ArrayList<String>();
        for(int i = 0; i < fasName.size(); i++){
            for(int j = i; j < fasName.size(); j++){
                filePair.add(fasName.get(i) + " " + fasName.get(j));
            }
        }
        return filePair;
    }
    /**output the file into a folder*/
    public static void printout(String outputFile, ArrayList<OutputFile> outputStuff) throws IOException {
        FileWriter outputStream = null;
        try{
            outputStream = new FileWriter(new File(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert outputStream != null;
        for (OutputFile eachElement : outputStuff){
            outputStream.write(eachElement.file1.patientfileName + " "
            + eachElement.file1.genotype + " "
                    +eachElement.file1.sequenceIndex + " "
            + eachElement.file1.repeatNumber + " "
            + ","
            + eachElement.file2.patientfileName + " "
                    + eachElement.file2.genotype + " "
                    + eachElement.file2.sequenceIndex + " "
                    + eachElement.file2.repeatNumber + " "
            + eachElement.seqDistance + " "
                    + eachElement.symbolForFalseSame + "\n");
        }
        outputStream.close();
    }

    /**output the trueSame and falseSame sequence*/
    public static void printoutSeq(String outputFile, ArrayList<OutputFile> outputStuff) throws IOException {
        FileWriter outputStream = null;
        try{
            outputStream = new FileWriter(new File(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert outputStream != null;
        for (OutputFile eachElement : outputStuff){
            outputStream.write(eachElement.file1.patientfileName + " "
                    + eachElement.file1.genotype + " "
                    +eachElement.file1.sequenceIndex + " "
                    + eachElement.file1.repeatNumber + " "
                    + ","
                    + eachElement.file2.patientfileName + " "
                    + eachElement.file2.genotype + " "
                    + eachElement.file2.sequenceIndex + " "
                    + eachElement.file2.repeatNumber + " "
                    + eachElement.seqDistance + " "
                    + eachElement.symbolForFalseSame + "\n");
            /*outputStream.write(eachElement.file1.sequence + "\n"
                    + eachElement.file2.sequence + "\n");*/
        }
        outputStream.close();
    }
    /** check if the seq from the two different are real same or not*/
    public static boolean checkIfTrueSame(int i, int j, String[] vectorDis, int counter){
        int counter_row = 0;
        int sum_counter_row = 0;
        boolean checkIfTrueSame = true;
        for(int k = i-j-1; k >= 1; k--) {
            counter_row++;
            sum_counter_row = sum_counter_row + (i - counter_row);
            int vecDis_row = Integer.parseInt(vectorDis[counter + k].split("\t")[1]);
            int vecDis_col = Integer.parseInt(vectorDis[counter - sum_counter_row].split("\t")[1]);
            /*System.out.println("counter " + counter + " vesDis_row " + vecDis_row + " " + "vesDis_col "
                    + vecDis_col + " "
                    + "rowDisVec " + (counter + k)
                    + " colDisVec " + (counter - sum_counter_row));*/
            if (vecDis_row != vecDis_col) {
                checkIfTrueSame = false;
                break;
            }
        }
        return checkIfTrueSame;
    }
    /** classify trueSameSeqs*/
    public static HashMap<String, ArrayList<String>>  classifySeq(ArrayList<TrueSameSeq> trueSameSeq){
        HashMap<String, ArrayList<String>> seqAndSameSeqs = new HashMap<String, ArrayList<String>>();
        for(int i = 0; i < trueSameSeq.size(); i++){
            ArrayList<String> sameSeq = new ArrayList<String>();
            for(int j = 0; j < trueSameSeq.size(); j++){
                if(trueSameSeq.get(j).first.fullSeqName.equals(
                        trueSameSeq.get(i).first.fullSeqName)){
                    sameSeq.add(trueSameSeq.get(j).second.fullSeqName);
                }
            }
            if(!seqAndSameSeqs.containsKey(trueSameSeq.get(i).first.fullSeqName)){
                seqAndSameSeqs.put(trueSameSeq.get(i).first.fullSeqName, sameSeq);
            }
        }
        return seqAndSameSeqs;
    }
    /**Check if the seq is in the shared list.*/
    public static boolean chekIfHavaSharedSeq(SeqInfor a,HashMap<String, ArrayList<String>> seqWithSameSeqList ){
        boolean sharedSeq = false;
        for(String key: seqWithSameSeqList.keySet()){
            for(String eachSeq : seqWithSameSeqList.get(key)){
                if (a.fullSeqName.equals(eachSeq)) {
                    sharedSeq = true;
                    break;
                }
            }
        }
        return sharedSeq;
    }
    /** print out the running time*/
    public static void printStat(long start, long end) {
        double totalTime = (double)(end - start)/1000.0D;
        System.out.println("running totalTime: " + totalTime + " s");
    }

    /** print out edges */
    public static void printEdge (Iterable<Edge> mst_e, String output_link) throws IOException {
        FileWriter outputStream = null;
        try{
            outputStream = new FileWriter(new File(output_link));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        assert outputStream != null;
        for(Edge e : mst_e)
        {
            SeqInfor v = e.either();
            SeqInfor w = e.other(v);
            Double dis = e.weight();
            outputStream.write(v.seqIndex_uniqNode + " " + w.seqIndex_uniqNode + " " +
                    dis + "\n");
        }
        outputStream.close();
    }

    /**print out gml file*/
    public static void printGML(String outputgml, Iterable<Edge> mst_e, ArrayList<SeqInfor> seq_uniq)
    throws IOException {
        FileWriter outputStream = null;
        try{
            outputStream = new FileWriter(new File(outputgml));
        } catch (IOException exc){
            exc.printStackTrace();
        }
        int i = 0;
        assert outputStream != null;
        outputStream.write("Creator " + '"' + "seq nodes" + '"' + "\n"
                + "graph" + "\n" + "[" + "\n" + "  " + "undirected 0" + "\n" );
        for(SeqInfor singleNode : seq_uniq){
            i++;
            outputStream.write("  " + "node [" + "\n");
            outputStream.write("    " + "id " + (singleNode.seqIndex_uniqNode+1) + "\n" +
                                "    " + "label " + '"'  + singleNode.fullSeqName);
            if(singleNode.sameSeqs.size() > 0){
                for(String sameSeq : singleNode.sameSeqs){
                    outputStream.write("/" + sameSeq + "/" );
                }
            }
            outputStream.write('"' + "\n" + "    " + "value 0" + "\n" + "  " + "]" + "\n");
        }
        for(Edge e : mst_e)
        {
            SeqInfor v = e.either();
            SeqInfor w = e.other(v);
            Double dis = e.weight();
            outputStream.write("  " + "edge [" + "\n" );
            outputStream.write("    " + "source " + (v.seqIndex_uniqNode+1) + "\n"
                    + "    " + "target " +  (w.seqIndex_uniqNode+1) + "\n" + "]\n");
        }
        outputStream.close();

    }
}
