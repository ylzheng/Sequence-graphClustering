package SequenceGraphClustering;

import java.io.IOException;
import java.util.*;

/**
 * Created by yqb7 on 7/21/15.
 */
public class SeqLinkedList{
    ListNode head;
    public SeqLinkedList(){
        head = null;
    }

    public void addANode(ArrayList<OutputFile> addData, int distance, int listNodeIndex){

        head = new ListNode(addData, head, distance, listNodeIndex);
    }

    public void showList(){
        ListNode position = head;
        int counter = 0;
        while (position != null){
            counter = counter + position.getData().size();
            System.out.println("distance is " + position.distance + " : " +
                    position.getData().size());
            position = position.getLink();
        }
        System.out.println("counter is " + counter);
    }

    public int length(){
        int count = 0;
        ListNode position = head;
        while(position != null){
            count++;
            position = position.getLink();
        }
        return count;
    }



    public void deleteHeadNode(){
        if(head != null){
            head = head.getLink();
        }
        else{
            System.out.println("Deleting from an empty list.");
            System.exit(0);
        }
    }

    /**public boolean onList(ArrayList<OutputFile> target){
        return find(target) != null;
    }*/

    public ListNode find(OutputFile target){
        boolean found = false;
        ListNode position = head;
        //while((position != null) && !found){
        while((position != null) && found == false){
            if(position.distance == target.seqDistance)
                found = true;
            else
                position = position.getLink();
        }
        /*if(found == false){
            return null;
        }*/
        return position;
    }

    /** Sort the Linkedlist node with distance value is smallest and return*/
    public ListNode sort(ListNode head){

        if (head == null || head.link == null){
            return head;
        }
        ListNode newHead = new ListNode(head.seqPairs, null,
                head.listNodeIndex, head.distance);
        ListNode pointer = head.link;
        /** loop through each element in the list*/
        while (pointer != null ){
            //System.out.println(pointer.distance);
            /**insert this element to the new list*/
            ListNode innerPointer = newHead;
            ListNode next = pointer.link;
            if (pointer.distance <= newHead.distance) {
                ListNode oldHead = newHead;
                newHead = pointer;
                newHead.link = oldHead;
            } else {
                while (innerPointer.link != null) {
                    if (pointer.distance > innerPointer.distance &&
                            pointer.distance <= innerPointer.link.distance) {
                        ListNode oldNext = innerPointer.link;
                        innerPointer.link = pointer;
                        pointer.link = oldNext;
                    }

                    innerPointer = innerPointer.link;
                }

                if (innerPointer.link == null && pointer.distance > innerPointer.distance) {
                    innerPointer.link = pointer;
                    pointer.link = null;
                }
            }

            // finally
            pointer = next;
        }
        System.out.println("finish linkedlist node sort");
        return newHead;
    }
    /*public void sortByDistance(){
        ListNode prePos = head;
        ListNode currentPos = prePos.link;

        int counter = 0;
        while(currentPos != null){
            counter++;
            System.out.println("counter sort " + counter);
            ListNode inerPointer = head;
            ListNode nextPos = currentPos.link;
            int inerCounter = 0;
            while(inerCounter < counter){
                if(inerPointer.distance > currentPos.distance){
                    ListNode oldNext = currentPos.link;
                    currentPos.link = inerPointer;
                    inerPointer = currentPos;
                }
                inerPointer = inerPointer.link;
                inerCounter++;
            }
            *//*if(currentPos.distance < prePos.distance){
                ListNode tmpPos = currentPos;
                currentPos = prePos ;
                prePos = tmpPos;
                prePos.link = currentPos;
                currentPos.link = nextPos;
            }*//*
            currentPos = nextPos;
        }
    }*/

    /**Not sure this method will be used, and there is prob in this method becuase the data
    * type is changed into ArrayList<Item> in the ListNode class*/
    private ArrayList<ArrayList<OutputFile>>  toArrayList(){
        ArrayList<ArrayList<OutputFile>> list = new ArrayList<>();
        ListNode position = head;
        while(position != null){
            list.add(position.getData());
            position = position.getLink();
        }
        return list;
    }

    public static void printList(ListNode x) {
        while (x != null) {
            //System.out.print(x.link.distance + "++ ");
            System.out.println("listNodeIndex " + x.listNodeIndex +
                    "  distance " + x.distance);
            x = x.link;
        }
        System.out.println();
    }

    public static void printlistUpdataListIndex(ListNode x) {
        int counter = 0;
        while(x != null){
            x.listNodeIndex = counter;
            System.out.println("listNodeIndex " + x.listNodeIndex +
                    "  distance " + x.distance);
            x = x.link;
            counter++;
        }
    }

    /**Build the linkedList dataStructure and separate the */
    public static void main(String[] args) throws IOException {
        long start_add = System.currentTimeMillis();
        TransformAllSeq tf = new TransformAllSeq(args[0], args[1], args[2], args[3], args[4]);
        SeqLinkedList sll = new SeqLinkedList();
        int nodeCounter = 0;
        for(int i = 0; i < tf.outOfFilePair.size(); i ++){
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
        //sll.showList();
        sll.printList(sll.head);
        sll.head = sll.sort(sll.head);
        System.out.println(sll.length());
        long end_add = System.currentTimeMillis();
        System.out.print("The time used in building linkedlist is: ");
        Tools.printStat(start_add, end_add);
        System.out.println("build linkedList finished!");
    }

}
