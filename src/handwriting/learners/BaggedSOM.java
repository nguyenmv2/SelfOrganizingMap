package handwriting.learners;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by My_Nguyen on 10/27/15.
 */
public class BaggedSOM implements RecognizerAI{
    public final int numBags = 2;
    Bagger bagger = new Bagger(SOM::new, numBags);
    @Override
    public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws Exception {
        bagger.train(data, progress);
    }

    @Override
    public String classify(Drawing d) {
        return bagger.classify(d);
    }
}
