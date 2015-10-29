package handwriting.learners.decisiontree;

import java.util.concurrent.ArrayBlockingQueue;

import handwriting.core.SampleData;
import search.core.Duple;
import search.core.Triple;

public class DTTrainer {
	private ArrayBlockingQueue<Double> progress;
	private DTSampleData baseData;
	private double currentProgress, tick;
	
	public DTTrainer(SampleData data, ArrayBlockingQueue<Double> progress) throws InterruptedException {
		baseData = new DTSampleData(data);
		this.progress = progress;
		this.currentProgress = 0;
		progress.put(currentProgress);
		this.tick = 1.0 / data.numDrawings();
	}
	
	public DTNode train() throws InterruptedException {
		return train(baseData);
	}
	
	private DTNode train(DTSampleData data) throws InterruptedException {
		if (data.numLabels() == 1 ) {
			DTLeaf leaf = new DTLeaf(data.getLabelFor(0));
			currentProgress += tick;
			progress.put(currentProgress);
			return leaf;
			// Update the progress bar
		} else {
			// TODO: Create an interior node
			// Use recursion to create the children of that node
            Triple<Integer, Integer, Duple<DTSampleData, DTSampleData>> bestFeature = getBestFeature(data);
            int featureX = bestFeature.getFirst();
            int featureY = bestFeature.getSecond();
            DTSampleData offBranch = bestFeature.getThird().getSecond();
            DTSampleData onBranch = bestFeature.getThird().getFirst();
            System.out.println("found best");
            DTInteriorNode interiorNode = new DTInteriorNode(featureX, featureY, train(onBranch), train(offBranch));
            return interiorNode;
		}
	}

    private Triple<Integer, Integer, Duple<DTSampleData,DTSampleData> > getBestFeature(DTSampleData data) {
        double bestGain = Integer.MIN_VALUE;
        Duple<DTSampleData, DTSampleData> bestSplittedSet = null;
        Triple<Integer, Integer, Duple<DTSampleData, DTSampleData>> best = null;

        for (int x = 0; x < data.getDrawingWidth(); x++) {
            for (int y = 0; y < data.getDrawingHeight(); y++) {
                Duple<DTSampleData, DTSampleData> splittedSet = data.splitOn(x, y);
                double gain = data.getGini() - (splittedSet.getFirst().getGini() + splittedSet.getSecond().getGini());
                if (bestGain < gain) {
                    bestGain = gain;
                    bestSplittedSet = splittedSet;
                    best = new Triple<>(x, y, bestSplittedSet);
                }
            }
        }
        if (best != null){
            return best;
        }
        else
            throw new IllegalStateException("No best feature found");
    }
}
