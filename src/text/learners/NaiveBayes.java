package text.learners;

import search.core.Histogram;
import text.core.Sentence;
import text.core.TextLearner;

import java.util.HashMap;
import java.util.HashSet;

public class NaiveBayes implements TextLearner {
	HashSet<String> vocabSet;
	HashMap<String, Histogram<String>> wordCountByLabel;
	public NaiveBayes(){
		vocabSet = new HashSet<>();
		wordCountByLabel = new HashMap<>();
	}
	@Override
	public void train(Sentence words, String lbl) {
		// Count up the relevant values
		Histogram<String> onCurrentLabel = wordCountByLabel.getOrDefault(lbl, new Histogram<>());
		for (String word : words){
			if (!vocabSet.contains(word)){
				vocabSet.add(word);
			}
			onCurrentLabel.bump(word);
		}
	}

	@Override
	public String classify(Sentence words) {
		// Use the counted values for classification.
		double bestProb = 0.0;
		String bestLabel = "";
		for (String label : wordCountByLabel.keySet()){
			double prob = Double.MIN_VALUE;
			for (String word : words){
				double probWord = calProbOf(word, wordCountByLabel.get(label));
				if (prob == Double.MIN_VALUE){
					prob = probWord;
				}
				prob *= probWord;
			}
			if (bestProb < prob) {
				bestProb = prob;
				bestLabel = label;
			}
		}
		if (!"".equals(bestLabel)){
			return bestLabel;
		} else {
			throw new IllegalStateException("No Label Found");
		}

	}

	public double calProbOf(String word, Histogram<String> wordCounts){
		double probWordInLabel = (wordCounts.getCountFor(word) +1) / (wordCounts.getTotalCounts() + vocabSet.size());
		return probWordInLabel;
	}
}
