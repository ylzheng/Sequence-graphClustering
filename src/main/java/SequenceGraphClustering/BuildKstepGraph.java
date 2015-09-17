package SequenceGraphClustering; /**
 * Created by yqb7 on 7/21/15.
 */

import java.io.IOException;
import java.util.ArrayList;

/** This class is used to add all seqs data into */
public class BuildKstepGraph {
    public BuildKstepGraph(String f0, String f1, String f2, String f3, String f4, String f5)
            throws IOException {
        long start_add = System.currentTimeMillis();
        TransformAllSeq tf = new TransformAllSeq(f0, f1, f2, f3, f4);
        SeqLinkedList sll = new SeqLinkedList();
        int nodeCounter = 0;
        for(int i = 0; i < tf.outOfFilePair.size(); i++){
            //System.out.println("i-- " + i);
            ArrayList<OutputFile> tmpNodeData = new ArrayList<>();
            if (sll.find(tf.outOfFilePair.get(i)) == null){
                //System.out.println("if- ");
                sll.addANode(tmpNodeData, tf.outOfFilePair.get(i).seqDistance, nodeCounter);
                nodeCounter++;
                sll.find(tf.outOfFilePair.get(i)).getData().add(tf.outOfFilePair.get(i));
            }else {
                //System.out.println("else -- " );
                sll.find(tf.outOfFilePair.get(i)).getData().add(tf.outOfFilePair.get(i));
            }
        }
        EdgeWeightedGraph EG = new EdgeWeightedGraph(tf.seqDisFas_uniq, sll);
        KstepMST mst_k = new KstepMST(EG);
        int edgeCount = 0;
        /*for (Edge e : mst_k.edges()) {
            *//*StdOut.println("mst " + e.either().seqIndex_uniqNode + " " + e.other(e.either()).seqIndex_uniqNode
                 + " " + e.weight());*//*
            edgeCount++;
        }*/
        Tools.printEdge(mst_k.edges(), f5);
        System.out.println("edgeCount = " + edgeCount);
        StdOut.printf("%.5f\n", mst_k.weight());
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in building linkedlist is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build linkedList finished!");
    }
    /**test*/
    public static void main(String[] args) throws IOException {
        BuildKstepGraph kspGraph = new BuildKstepGraph(args[0], args[1], args[2], args[3], args[4], args[5]);
    }


}
