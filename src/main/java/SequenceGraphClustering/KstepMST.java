package SequenceGraphClustering;//import scala.tools.cmd.gen.AnyVals;

import java.io.IOException;
import java.util.*;

/**
 * Created by yqb7 on 8/4/15.
 */
public class KstepMST {
    private double weight;  // weight of MST
    public Queue<Edge> mst = new Queue<Edge>();  // edges in MST
    public Set<Double> diff_weight_list = new HashSet<>();
    public ArrayList<Double> weight_list = new ArrayList<>();
    public Map<Double, ArrayList<Edge>> edgesDiffWeight = new HashMap<>();

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param G the edge-weighted graph
     */
    public KstepMST(EdgeWeightedGraph G) {
        // more efficient to build heap by passing array of edges
        MinPQ<Edge> pq = new MinPQ<Edge>();
        MinPQ<Double> weightPQ = new MinPQ<>();

        for (Edge e : G.edges) {
            pq.insert(e);
            diff_weight_list.add(e.weight());
        }
        /**/
        /** sort the weight*/
        for (Double w : diff_weight_list) {
            weightPQ.insert(w);
        }
        while (!weightPQ.isEmpty()) {
            weight_list.add(weightPQ.delMin());
        }
        /** end of sort the weight list*/
        /** initialize a hashmap variable to save the egdes with diff weight*/

        for (int i = 0; i < weight_list.size(); i++) {
            ArrayList<Edge> tmp = new ArrayList<>();
            edgesDiffWeight.put(weight_list.get(i), tmp);
        }

        /** separate the edges according to the distances*/
        while (!pq.isEmpty()) {
            Edge e = pq.delMin();
            edgesDiffWeight.get(e.weight()).add(e);
        }
        /** run greedy algorithm */
        UF uf = new UF(G.V);
        ArrayList<Integer> unVisitedNode = new ArrayList<>();
        int sameP = 0;

        for (int i = 0; i < weight_list.size(); i++) {
            //System.out.println("weight_list.get(i)) " +   weight_list.get(i));
            //System.out.println("uf.count() " + uf.count() + "____");
            if(uf.count() == 1) break;
            Queue<Edge> mst_tmp = new Queue<Edge>();


            /** method 2*/
            for (int j = 0; j < edgesDiffWeight.get(weight_list.get(i)).size(); j++) {
                Edge e = edgesDiffWeight.get(weight_list.get(i)).get(j);
                SeqInfor v = e.either();
                SeqInfor w = e.other(v);
                if (!uf.connected(v.seqIndex_uniqNode, w.seqIndex_uniqNode)) {
                    mst_tmp.enqueue(e);
                }
            } // end of for loop

            /** methods end */
            //System.out.println("mst_tmp " + mst_tmp.size());
            for(Edge eachE : mst_tmp){
                SeqInfor x = eachE.either();
                SeqInfor y = eachE.other(x);
                uf.union(x.seqIndex_uniqNode, y.seqIndex_uniqNode); //merge x and y components
                mst.enqueue(eachE);
            }
            sameP = uf.getparent(G.V);
            // check optimality conditions
            assert check(G);
        }

    }// end of while



    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> edges() {
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight() {
        return weight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // check total weight
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(total - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Edge e : edges()) {
            int v = e.either().seqIndex_uniqNode, w = e.other(e.either()).seqIndex_uniqNode;
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.edges()) {
            int v = e.either().seqIndex_uniqNode, w = e.other(e.either()).seqIndex_uniqNode;
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(G.V());
            for (Edge f : mst) {
                int x = f.either().seqIndex_uniqNode, y = f.other(f.either()).seqIndex_uniqNode;
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either().seqIndex_uniqNode, y = f.other(f.either()).seqIndex_uniqNode;
                if (!uf.connected(x, y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }


    /**
     * Unit tests the <tt>KstepKruskal</tt> data type.
     */
    public static void main(String[] args) throws IOException {

        long start_add = System.currentTimeMillis();
        TransformAllSeq tf = new TransformAllSeq(args[0], args[1], args[2], args[3], args[4]);
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

        Tools.printGML(args[5], mst_k.edges(), tf.seqDisFas_uniq);
        System.out.println("edgeCount = " + edgeCount);
        StdOut.printf("%.5f\n", mst_k.weight());
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in k-msp is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build  finished!");
    }
}
