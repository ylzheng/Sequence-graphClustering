package SequenceGraphClustering; /**
 * Created by yqb7 on 7/13/15.
 */

import java.io.IOException;
import java.util.ArrayList;

/**
 *  The <tt>EdgeWeightedGraph</tt> class represents an edge-weighted
 *  graph of vertices named 0 through <em>V</em> - 1, where each
 *  undirected edge is of type {@link Edge} and has a real-valued weight.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the edges incident to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which
 *  is a vertex-indexed array of @link{Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the edges incident to a given vertex, which takes
 *  time proportional to the number of such edges.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class EdgeWeightedGraph {
    int V; //Vertex
    int E; // Edge
    Bag<Edge>[] adj;
    ArrayList<Edge> edges = new ArrayList<>();


    /**
     * Initializes an empty edge-weighted graph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws java.lang.IllegalArgumentException if <tt>V</tt> < 0
     */
    public EdgeWeightedGraph( ArrayList<SeqInfor> seq_uniq, SeqLinkedList sll ) {
        if (seq_uniq.size() < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = seq_uniq.size();
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
        ListNode x = sll.sort(sll.head);
        int totoalEdges = 0;
        int addedEdges = 0;

        while (x != null ){
            //System.out.println(x.distance);
            totoalEdges = totoalEdges + x.seqPairs.size();
            for(int i = 0; i < x.seqPairs.size(); i++){
                if(x.seqPairs.get(i).file1.seqIndex_uniqNode != -1 &&
                        x.seqPairs.get(i).file2.seqIndex_uniqNode != -1 &&
                        x.distance == 0) {
                    System.out.println("!");
                }
                if((x.seqPairs.get(i).file1.seqIndex_uniqNode != -1 &&
                        x.seqPairs.get(i).file2.seqIndex_uniqNode != -1) &&
                        x.distance != 0) {
                    Edge e = new Edge(x.seqPairs.get(i).file1, x.seqPairs.get(i).file2, x.distance);
                    edges.add(e);
                    addEdge(e);
                    addedEdges++;
                }


            }
            /*if(x.distance == 1){
            }*/
            x = x.link;
        }
        System.out.println("EdgeWeightedGraph Total edge " + totoalEdges);
        System.out.println("Total add edge " + addedEdges);
    }

    public EdgeWeightedGraph(In in) {
    }

    /**in the edge list, choose the chosen distance to add as a edges*/



    /**
     * Returns the number of vertices in the edge-weighted graph.
     * @return the number of vertices in the edge-weighted graph
     */
    public int V()
    {
        return V;
    }

    /**
     * Returns the number of edges in the edge-weighted graph.
     * @return the number of edges in the edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(SeqInfor v) {
        if (v.seqIndex_uniqNode < -1 || v.seqIndex_uniqNode >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " " +
                    "is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge <tt>e</tt> to the edge-weighted graph.
     * @param e the edge
     * @throws java.lang.IndexOutOfBoundsException unless both endpoints are between 0 and V-1
     */
    public void addEdge(Edge e) {
        SeqInfor v = e.either();
        SeqInfor w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v.seqIndex_uniqNode].add(e);
        adj[w.seqIndex_uniqNode].add(e);
        E++;
    }

    /**
     * Returns all edges in the edge-weighted graph.
     * To iterate over the edges in the edge-weighted graph, use foreach notation:
     * <tt>for (Edge e : G.edges())</tt>.
     * @return all edges in the edge-weighted graph as an Iterable.
     */
    /** this method has problem and not used! */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        int counter = 0;
        for (int i = 0; i < V; i++) {
            int selfLoops = 0;
            for (Edge e : adj[i]) {
                /*System.out.println(" no compared ~~~~~~ i " + i+ " : " + e.other(e.either()).seqIndex_uniqNode
                        + " : " + e.either().seqIndex_uniqNode );*/
                if (e.other(e.either()).seqIndex_uniqNode > e.either().seqIndex_uniqNode ) {
                    list.add(e);
                    counter++;
                    /*System.out.println("i " + i+ " : " + e.other(e.either()).seqIndex_uniqNode
                                    + " : " + e.either().seqIndex_uniqNode );*/
                }
                // only add one copy of each self loop (self loops will be consecutive)
                else if (e.other(e.either()).seqIndex_uniqNode == e.either().seqIndex_uniqNode) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
            //System.out.println("selfLoops  "  + selfLoops);
            System.out.println("Counter Edge  "  + counter);
        }
        return list;
    }

    /**
     * Returns the edges incident on vertex <tt>v</tt>.
     * @return the edges incident on vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public Iterable<Edge> adj(SeqInfor v) {
        validateVertex(v);
        return adj[v.seqIndex_uniqNode];
    }

    /**
     * Returns the degree of vertex <tt>v</tt>.
     * @return the degree of vertex <tt>v</tt>
     * @param v the vertex
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public int degree(SeqInfor v) {
        validateVertex(v);
        return adj[v.seqIndex_uniqNode].size();
    }



    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *   followed by the <em>V</em> adjacency lists of edges
     */
    /*public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < size; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }*/

    /**
     * Unit tests the <tt>EdgeWeightedGraph</tt> data type.
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
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in building linkedlist is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build linkedList finished!");

    }
}

