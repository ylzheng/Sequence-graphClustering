package SequenceGraphClustering;

import java.util.ArrayList;

/**
 * Created by yqb7 on 7/21/15.
 */
public class ListNode {
    int listNodeIndex;
    int distance;
    ArrayList<OutputFile> seqPairs;
    ListNode link;
    public ListNode(){
        link = null;
        seqPairs = null;
    }

    public ListNode(ArrayList<OutputFile> newData, ListNode linkValue,
                    int dist, int listNodeID){
        seqPairs = newData;
        link = linkValue;
        distance = dist;
        listNodeIndex = listNodeID;
    }

    public void setData(ArrayList<OutputFile> newData)
    {
        seqPairs = newData;
    }

    public ArrayList<OutputFile> getData()
    {
        return seqPairs;
    }

    public void setLink(ListNode newLink){
        link = newLink;
    }

    public ListNode getLink(){
        return link;
    }

    /*@Override
    public int compareTo(ListNode o) {
        int comparedSize = o.distance;
        if (this.distance > comparedSize) {
            return 1;
        } else if (this.distance == comparedSize) {
            return 0;
        } else {
            return -1;
        }
    }*/
}

