package SequenceGraphClustering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by yqb7 on 6/30/15.
 */
public class DistanceVectorFile {
    int totalSeqNumber;
    String[] vectorContent;
    DistanceVectorFile(String distanceVectorDir) throws IOException {
        vectorContent = new String(Files.readAllBytes(Paths.get(distanceVectorDir))).split("\n");
        totalSeqNumber = vectorContent.length/6;
    }

    public static void main(String[] args){

    }
}
