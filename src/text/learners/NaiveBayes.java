package text.learners;

import text.core.Sentence;
import text.core.TextLearner;

public class NaiveBayes implements TextLearner {

	@Override
	public void train(Sentence words, String lbl) {
		// Count up the relevant values
	}

	@Override
	public String classify(Sentence words) {
		// Use the counted values for classification.
		return "";
	}
}
