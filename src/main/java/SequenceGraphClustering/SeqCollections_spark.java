package SequenceGraphClustering;

/**
 * Created by yqb7 on 6/24/15.
 */
public class SeqCollections_spark {
/** This class will do the following tasks
 * 1. */
    /*public static void main(String[] args){
        ArrayList<String> fileNameList = new ArrayList<String>();
        String fasfileCotent;
        String vectorDistance;
        int[] seqNumber = new int [fileNameList.size()];
        SparkConf conf = new SparkConf().setAppName("seqCount");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> input = sc.textFile("/scicomp/home/yqb7/" +
                "allData/hepatitis/kstep/small_test/HOC_P02_1b.fas").cache();
        JavaRDD<String> words = input.flatMap(
                new FlatMapFunction<String, String>() {
                    public Iterable<String> call(String x) throws Exception {
                        return Arrays.asList(x.split("\n"));
                    }});


        *//*SeqCollections_spark(String fasFileDir, String distanceVectorDir) throws IOException {
            String content = new String(Files.readAllBytes(Paths.get(fasFileDir)));
            BufferedReader reader2 = new BufferedReader(new FileReader(new File(distanceVectorDir)));

        }*//*
    }*/


    /*public static void main(String[] args) throws IOException {
        SeqCollections_spark ob = new SeqCollections_spark(args[0], args[1]);
    }*/
}