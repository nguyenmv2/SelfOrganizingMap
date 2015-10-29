package handwriting.learners;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

import handwriting.core.Drawing;
import handwriting.core.RecognizerAI;
import handwriting.core.SampleData;
import search.core.Histogram;

public class Bagger implements RecognizerAI {
	private ArrayList<RecognizerAI> bags;
	private Supplier<RecognizerAI> supplier;
	private int numBags;

	public final int numTrains =2;
	// For the "supplier" parameter, use the constructor; for example, 
	// b = new Bagger(DecisionTree::new, 30)
	public Bagger(Supplier<RecognizerAI> supplier, int numBags) {
		this.numBags = numBags;
		this.supplier = supplier;
		this.bags = new ArrayList<>();
	}

	@Override
	public void train(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		// TODO: Reset "bags" to be empty.  Then create "numBags" instances 
		// of whatever learner is being bagged.  For each of these instances,
		// recreate the training data by random sampling with replacement. 
		// Then train that instance using the rebuilt data.
		bags.clear();
		for (int i = 0; i < numBags; ++i){
			RecognizerAI instance = supplier.get();
			for (int t = 0; t < numTrains; t++){
				try {
					instance.train(getSampledData(data), progress);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			bags.add(instance);
		}
	}

	private SampleData getSampledData(SampleData data) {
		SampleData sampleData = new SampleData();
		Random generator = new Random();
		int bound = data.numDrawings();
		for (int i = 0; i < bound; ++i){
			int randomIndex = generator.nextInt(bound);
			String label = data.getLabelFor(randomIndex);
			Drawing drawing = data.getDrawing(randomIndex);
			sampleData.addDrawing(label, drawing);
		}
		return sampleData;
	}

	@Override
	public String classify(Drawing d) {
		// TODO: Use a Histogram (from search.core) to count the labels
		// returned by calling "classify(d)" on each learner.  Then
		// return the plurality winner.
		Histogram<String> counter = new Histogram<>();

		for (RecognizerAI instance : bags){
			String winner =instance.classify(d);
			counter.bump(winner);
		}
		return counter.getPluralityWinner();
	}

}
