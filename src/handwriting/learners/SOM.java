package handwriting.learners;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import handwriting.learners.som.SOMPoint;
import handwriting.learners.som.SelfOrgMap;
import javafx.scene.canvas.Canvas;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by My_Nguyen on 10/13/15.
 */
public class SOM implements RecognizerAI {
    SelfOrgMap map;
    int iterations = 200, map_height = 10, map_width = 10;
    double learning_rate = 2.0;
    @Override
    public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws Exception {
        Drawing sample = data.getDrawing(0);
        double doneness = 0;
        map = new SelfOrgMap(map_height, map_width, sample.getWidth(), sample.getHeight());
        System.out.println(String.format("%d - %d" , sample.getWidth(), sample.getHeight()));
        map.setLearning_rate(learning_rate);
        map.setNum_iter(iterations);

        for (int k = 0; k < iterations; ++k) {
            for (String label : data.allLabels()) {
                System.out.println("Current Label: "+label);
                for (int i = 0; i < data.numDrawingsFor(label); ++i) {
                    Drawing current = data.getDrawing(label, i);
                    SOMPoint best = map.bestFor(current);
                    for (SOMPoint p : map.getAffectedPoints(best,map.radiusOfChange(k))){
                        map.setWeight(current, p, k);
                    }
                }
            }
            doneness += 1.0 / (iterations);
            System.out.println(doneness);
            progress.add(doneness);
        }

        for (String label : data.allLabels()){
            for (int k = 0; k < data.numDrawingsFor(label); ++k){
                Drawing s = data.getDrawing(label, k);
                map.setLabel(s, label);
            }
        }
        map.setOutstandingLabels();
    }

    @Override
    public String classify(Drawing d) {
        SOMPoint labelNode = null;
        try {
            labelNode = map.bestFor(d);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String result = map.getLabel(labelNode);
        return result == null ? "" : result;
    }

    @Override
    public void visualize(Canvas surface) {
        map.visualize(surface);
    }

}
