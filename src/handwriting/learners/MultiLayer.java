package handwriting.learners;

public class MultiLayer extends PerceptronNet {
    private Perceptron inputToHidden, hiddenToOutput;
    
    // Invariant: 
    //   inputToHidden.numOutputNodes() == hiddenToOutput.numInputNodes()
    
    public int numInputNodes() {return inputToHidden.numInputNodes();}
    public int numHiddenNodes() {return inputToHidden.numOutputNodes();}
    public int numOutputNodes() {return hiddenToOutput.numOutputNodes();}
    
    public Perceptron getHiddenLayer() {return inputToHidden;}
    public Perceptron getOutputLayer() {return hiddenToOutput;}
    
    public MultiLayer(int numIn, int numHid, int numOut) {
        this(new Perceptron(numIn, numHid), new Perceptron(numHid, numOut));
    }
    
    public MultiLayer(Perceptron hidden, Perceptron output) {
    	checkArgs(hidden.numOutputNodes(), output.numInputNodes(), "output");
        inputToHidden = hidden;
        hiddenToOutput = output;
    }
    
    public double[] compute(double[] inputs) {
    	checkCompute(inputs);
        return hiddenToOutput.compute(inputToHidden.compute(inputs));
    }
    
    public void updateWeights() {
        inputToHidden.updateWeights();
        hiddenToOutput.updateWeights();
    }
    
    protected void backpropagate(double[] inputs, double rate) {
        for(int hidden_idx =0; hidden_idx < hiddenToOutput.numInputNodes(); hidden_idx++){
            double error =0.0;
            double input = inputToHidden.output(hidden_idx);
            for ( int output_idx =0; output_idx < hiddenToOutput.numOutputNodes(); output_idx++){
                double weight = hiddenToOutput.getWeightFromTo(hidden_idx, output_idx);
                error += weight * gradient(inputToHidden.output(hidden_idx)) * hiddenToOutput.error(output_idx);
            }
            inputToHidden.setError(hidden_idx, error);
        }
        inputToHidden.addToWeightDeltas(inputs, rate);

    }

    public void train(double[] inputs, double[] targets, double rate) {
    	checkTrain(inputs, targets, rate);
        hiddenToOutput.train(getHiddenLayer().compute(inputs), targets, rate);
        backpropagate(inputs, rate);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Input to Hidden\n");
        sb.append(inputToHidden);
        sb.append("\nHidden to Output\n");
        sb.append(hiddenToOutput);
        return sb.toString();
    }
}
