package SequenceGraphClustering;

/**
 * Created by yqb7 on 7/22/15.
 */
public class SeqPairAnalyzerApp {
   /* public static final JavaPairRDD<Integer, Iterable<OutputFile>> separateSeqByDis(
            JavaRDD<OutputFile> outputSeqPairs){
        JavaPairRDD<Integer, Iterable<OutputFile>> seqPairs =
        outputSeqPairs.mapToPair(lines -> new Tuple2<>(lines.seqDistance, lines)).groupByKey();

        return seqPairs;
    }

    *//*public static final buildGraph(){
        Graph

    }*//*


    public static void main(String[] args) throws IOException{
        String f1 = "/scicomp/home/yqb7/allData/hepatitis/kstep/small_test/HOC_P02_1b_aligned.fas";
        String f2 = "/scicomp/home/yqb7/allData/hepatitis/kstep/small_test/dist_vector.txt";
        String f3 = "/scicomp/home/yqb7/allData/hepatitis/kstep/small_test/output_aligned1.txt";
        String f4 = "/scicomp/home/yqb7/allData/hepatitis/kstep/small_test/trueSame1.txt";
        String f5 = "/scicomp/home/yqb7/allData/hepatitis/kstep/small_test/falseSame1.txt";
        TransformAllSeq a = new TransformAllSeq(f1, f2, f3, f4, f5);
        SparkConf conf = new SparkConf().setAppName("A seq dataset analysis");
        JavaSparkContext sa = new JavaSparkContext(conf);
        JavaRDD<OutputFile> outputSeqPairs = sa.parallelize(a.outOfFilePair);
        JavaPairRDD<Integer, Iterable<OutputFile>> seqPairs =
                SeqPairAnalyzerApp.separateSeqByDis(outputSeqPairs);
        JavaRDD<Integer> keyList = seqPairs.keys();
        //seqPairs.foreach(rdd -> );
        System.out.println("..s1..");

    }*/
}

