package handwriting.learners;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;


public class MultiLayer_1 implements RecognizerAI

{
    int num_iter = trainingIter(),
            num_output = numOutput(),
            num_hidden = numHidden(),
            num_input  = numInput();
    double trainRate = trainingRate();
    ArrayList<String> labels;
    MultiLayer perceptron;

    public MultiLayer_1(){
        this.perceptron = new MultiLayer(num_input, num_hidden, num_output);
    }

    @Override
    public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
        labels = new ArrayList<>(data.allLabels());
        double prog = 0;
        for (int iter = 0; iter < num_iter; iter++) {
            for (String label : labels) {
                for (int i = 0; i < data.numDrawingsFor(label); i++) {
                    Drawing drawing = data.getDrawing(label, i);
                    double[] inputs = getInputs(drawing);
                    int expected = labels.indexOf(label);
                    double[] out = {expected};
                    perceptron.train(inputs, out, trainRate);
                }
                perceptron.updateWeights();
                prog += 1.0 / num_iter;
                progress.add(prog);
            }
        }
    }


    @Override
    public String classify(Drawing d) {
        double[] in = getInputs(d);
        double[] results = perceptron.compute(in);
        return labels.get(results[0] >= 0.5 ? 1 : 0);
    }



    public double[] getInputs(Drawing drawing){
        double[] inputs = new double[drawing.getWidth() * drawing.getHeight()];
        for (int i = 0; i < drawing.getHeight(); i++){
            for (int j = 0; j < drawing.getWidth(); j++){
                int index = i*(drawing.getWidth()) + j;
                inputs[index] = drawing.isSet(i, j) ? 1.0 : 0.0;
            }
        }
        return inputs;
    }
    private ArrayList<Drawing> getDrawingsFromLabel(String label, SampleData data){
        ArrayList<Drawing> result = new ArrayList<>();
        for (int i =0; i < data.numDrawingsFor(label); i++){
            result. add(data.getDrawing(label, i));
        }
        return result;
    }

    private double trainingRate(){
        return 0.02;
    }
    private int trainingIter() {
        return 2000;
    }
    private int numOutput(){
        return 1;
    }
    private int numHidden(){
        return 40;
    }
    private int numInput(){
        return 1600;
    }
}
