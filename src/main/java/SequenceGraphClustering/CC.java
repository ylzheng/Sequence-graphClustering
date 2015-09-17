package SequenceGraphClustering; /**
 * Created by yqb7 on 7/30/15.
 */

import java.io.IOException;
import java.util.ArrayList;

/**
 *  The <tt>CC</tt> class represents a data type for
 *  determining the connected components in an undirected graph.
 *  The <em>id</em> operation determines in which connected component
 *  a given vertex lies; the <em>connected</em> operation
 *  determines whether two vertices are in the same connected component;
 *  the <em>count</em> operation determines the number of connected
 *  components; and the <em>size</em> operation determines the number
 *  of vertices in the connect component containing a given vertex.

 *  The <em>component identifier</em> of a connected component is one of the
 *  vertices in the connected component: two vertices have the same component
 *  identifier if and only if they are in the same connected component.

 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>id</em>, <em>count</em>, <em>connected</em>,
 *  and <em>size</em> operations take constant time.
 *  <p>
 *  For additional documentation, see <a href="/algs4/41graph">Section 4.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class CC {
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    /**
     * Computes the connected components of the undirected graph <tt>G</tt>.
     * @param G the graph
     */
    public CC(Graph G) {
        marked = new boolean[G.V];
        id = new int[G.V];
        size = new int[G.V];
        for (int v = 0; v < G.V; v++) {
            //if (!marked[v]) {
            //if (marked[v] == true) {
            if (marked[v] == false) { /**same result if (!marked[v]) */

                dfs(G, v);
                count++;
            }
        }
    }

    /** depth-first search */
    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (SeqInfor w : G.adj[v]) {
            if (!marked[w.seqIndex_uniqNode]) {
                dfs(G, w.seqIndex_uniqNode);
            }
        }
    }

    /**
     * Returns the component id of the connected component containing vertex <tt>v</tt>.
     * @param v the vertex
     * @return the component id of the connected component containing vertex <tt>v</tt>
     */
    public int id(int v) {
        return id[v];
    }

    /**
     * Returns the number of vertices in the connected component containing vertex <tt>v</tt>.
     * @param v the vertex
     * @return the number of vertices in the connected component containing vertex <tt>v</tt>
     */
    public int size(int v) {
        return size[id[v]];
    }

    /**
     * Returns the number of connected components.
     * @return the number of connected components
     */
    public int count() {
        return count;
    }

    /**
     * Are vertices <tt>v</tt> and <tt>w</tt> in the same connected component?
     * @param v one vertex
     * @param w the other vertex
     * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the same
     *     connected component, and <tt>false</tt> otherwise
     */
    public boolean connected(int v, int w) {
        return id(v) == id(w);
    }

    /**
     * Are vertices <tt>v</tt> and <tt>w</tt> in the same connected component?
     * @param v one vertex
     * @param w the other vertex
     * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the same
     *     connected component, and <tt>false</tt> otherwise
     * @deprecated Use connected(v, w) instead.
     */
    public boolean areConnected(int v, int w) {
        return id(v) == id(w);
    }


    /**
     * Unit tests the <tt>CC</tt> data type.
     */
    public static void main(String[] args) throws IOException {
        //In in = new In(args[0]);
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
        /** find out the connected component*/
        CC cc = new CC(gp);

        // number of connected components
        int M = cc.count();
        System.out.println(M + " components");

        // compute list of vertices in each connected component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[M];
        for (int i = 0; i < M; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < gp.V; v++) {
            components[cc.id(v)].enqueue(v);
        }

        // print results
        for (int i = 0; i < M; i++) {
            for (int v : components[i]) {
                System.out.println(v + " ");
            }
            System.out.println();
        }
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in building graph is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build graph component finished!");
    }
}
