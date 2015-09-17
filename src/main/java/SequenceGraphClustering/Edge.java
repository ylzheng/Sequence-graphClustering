package SequenceGraphClustering; /**
 * Created by yqb7 on 7/13/15.
 */

/*************************************************************************
 *  Compilation:  javac Edge.java
 *  Execution:    java Edge
 *
 *  Immutable weighted Edge.
 *
 *************************************************************************/

/**
 *  The <tt>Edge</tt> class represents a weighted Edge in an
 *  {@link EdgeWeightedGraph}. Each Edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the Edge and
 *  the weight. The natural order for this data type is by
 *  ascending order of weight.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Edge implements Comparable<Edge> {

    private final SeqInfor v;
    private final SeqInfor w;
    private final int weight;

    /**
     * Initializes an Edge between vertices <tt>v/tt> and <tt>w</tt> of
     * the given <tt>weight</tt>.
     * param v one vertex
     * param w the other vertex
     * param weight the weight of the Edge
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *    is a negative integer
     * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
     */

    public Edge(SeqInfor v, SeqInfor w, int weight) {
        if (v == null) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (w == null) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /**
     * Returns the weight of the Edge.
     * @return the weight of the Edge
     */
    public double weight() {
        return weight;
    }

    /**
     * Returns either endpoint of the Edge.
     * @return either endpoint of the Edge
     */
    public SeqInfor either() {
        return v;
    }

    /**
     * Returns the endpoint of the Edge that is different from the given vertex
     * (unless the Edge represents a self-loop in which case it returns the same vertex).
     * @param vertex one endpoint of the Edge
     * @return the endpoint of the Edge that is different from the given vertex
     *   (unless the Edge represents a self-loop in which case it returns the same vertex)
     * @throws java.lang.IllegalArgumentException if the vertex is not one of the endpoints
     *   of the Edge
     */
    public SeqInfor other(SeqInfor vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Compares two edges by weight.
     * @param that the other Edge
     * @return a negative integer, zero, or positive integer depending on whether
     *    this Edge is less than, equal to, or greater than that Edge
     */
    public int compareTo(Edge that) {
        if      (this.weight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return +1;
        else                                    return  0;
    }

    /**
     * Returns a string representation of the Edge.
     * @return a string representation of the Edge
     */
    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }

    /**
     * Unit tests the <tt>Edge</tt> data type.
     */
    public static void main(String[] args) {
        //Edge e = new Edge(12, 23, 3.14);
        //System.out.println(e);
    }
}
