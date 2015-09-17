package SequenceGraphClustering;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yqb7 on 8/3/15.
 */
public class KstepKruskal {
    private double weight;  // weight of MST
    private Queue<Edge> mst = new Queue<Edge>();  // edges in MST

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public KstepKruskal(EdgeWeightedGraph G) {
        // more efficient to build heap by passing array of edges
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }

        // run greedy algorithm
        UF uf = new UF(G.V);
        int counter = 0;
        while (mst.size() < G.V - 1) {
        //while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();
            counter++;
            if(e.weight() == 1) System.out.println(e.weight() + " " + counter);
            SeqInfor v = e.either();
            SeqInfor w = e.other(v);
            if (!uf.connected(v.seqIndex_uniqNode, w.seqIndex_uniqNode)) { // v-w does not create a cycle
                uf.union(v.seqIndex_uniqNode, w.seqIndex_uniqNode);  // merge v and w components
                mst.enqueue(e);  // add edge e to mst
                weight += e.weight();
            }
            if(e.weight() == 2){
                System.out.println("!");
            }
        }

        // check optimality conditions
        assert check(G);
    }

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
        KstepKruskal mst = new KstepKruskal(EG);
        int edgeCount = 0;
        for (Edge e : mst.edges()) {
            StdOut.println(e.weight());
            edgeCount++;
        }
        System.out.println("edgeCount = " + edgeCount);
        StdOut.printf("%.5f\n", mst.weight());
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in building linkedlist is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build linkedList finished!");

    }
}
