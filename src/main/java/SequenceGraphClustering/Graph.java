package SequenceGraphClustering;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yqb7 on 7/14/15.
 */
public class Graph {
    int V;
    int E;
    Bag<SeqInfor>[] adj;
    double weight;
    //Bag<Integer>[] adj;
    //int distance = 0;

    /**
     * Initializes an empty graph with <tt>V</tt> vertices and seqpair number of edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    public Graph(ArrayList<SeqInfor> seq_uniq, SeqLinkedList sll) {

        if (seq_uniq.size() < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = seq_uniq.size();
        this.E = 0;
        adj = (Bag<SeqInfor>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<SeqInfor>();
        }
        ListNode x = sll.sort(sll.head);
        Queue<Edge> mst = new Queue<Edge>();  // edges in MST
        ConnectedComponet cc = new ConnectedComponet(this.V);
        int totoalEdges = 0;
        int addedEdges = 0;
        while (x != null){
            //System.out.println(x.distance);
            totoalEdges = totoalEdges + x.seqPairs.size();
            for(int i = 0; i < x.seqPairs.size(); i++){
                /*if(x.distance == 1){
                    mst.enqueue(x.seqPairs.get(i));
                }*/
                if(x.seqPairs.get(i).file1.seqIndex_uniqNode != -1 &&
                        x.seqPairs.get(i).file2.seqIndex_uniqNode != -1) {
                    addEdge(x.seqPairs.get(i).file1, x.seqPairs.get(i).file2, x.distance);
                    addedEdges++;
                    mst.enqueue(new Edge(x.seqPairs.get(i).file1,
                            x.seqPairs.get(i).file2, x.seqPairs.get(i).seqDistance));
                }
            }
            /*int counter = 0;
            while (counter < this.V) {
                //while (!pq.isEmpty() && mst.size() < G.V() - 1) {
                adj[counter].seqInfor.
                if (!cc.connected(v.seqIndex_uniqNode, w.seqIndex_uniqNode)) { // v-w does not create a cycle
                    cc.union(v.seqIndex_uniqNode, w.seqIndex_uniqNode);  // merge v and w components
                    mst.enqueue(e);  // add edge e to mst
                    weight += e.weight();
                }
            }*/
            x = x.link;
        }
        System.out.println("Graph Total edge " + totoalEdges);
        System.out.println("Graph add edge i " + addedEdges);
    }

    /**
     * Adds the undirected edge v-w to the graph.
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */
    public void addEdge(SeqInfor v, SeqInfor w, int dis) {
        E++;
        adj[v.seqIndex_uniqNode].add(w, v, dis);
        adj[w.seqIndex_uniqNode].add(v, w, dis);
    }

    public void checkEmptyNode(Graph G){
        int emptyNodeCounter = 0;
        int counter = 0;
        while(counter < G.V){
            if (G.adj[counter].size() == 0){
                emptyNodeCounter++;
            }
            counter++;
        }
        System.out.println("emptyNodeCounter: "  + emptyNodeCounter);
    }

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
        Graph gp = new Graph(tf.seqDisFas_uniq, sll);
        gp.checkEmptyNode(gp);
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in building linkedlist is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build linkedList finished!");
    }
}
