package handwriting.learners.decisiontree;

import handwriting.core.Drawing;

/**
 * Created by My_Nguyen on 10/22/15.
 */
public class DTInteriorNode implements DTNode{


    //Position of the feature
    private int x;
    private int y;
    private DTNode leftBranch, rightBranch;

    public DTInteriorNode(int x, int y, DTNode left, DTNode right){
        this.x = x;
        this.y = y;
        this.leftBranch = left;
        this.rightBranch = right;

    }

    @Override
    public String classify(Drawing d) {
        if ( d.isSet(x, y)){
            System.out.println("left");
            return this.leftBranch.classify(d);
        } else {
            System.out.println("right");
            return this.rightBranch.classify(d);
        }
    }

}
