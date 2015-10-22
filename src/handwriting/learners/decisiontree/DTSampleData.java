package handwriting.learners.decisiontree;

import handwriting.core.SampleData;
import search.core.Duple;

public class DTSampleData extends SampleData {
	public DTSampleData() {super();}
	
	public DTSampleData(SampleData src) {
		for (int i = 0; i < src.numDrawings(); i++) {
			this.addDrawing(src.getLabelFor(i), src.getDrawing(i));
		}
	}
	
	public double getGini() {
		// TODO: Implement Gini coefficient for this set
		return 0.0;
	}
	
	public Duple<DTSampleData,DTSampleData> splitOn(int x, int y) {
		DTSampleData on = new DTSampleData();
		DTSampleData off = new DTSampleData();
		// TODO: Add all elements with (x, y) set to "on"
		//       Add all elements with (x, y) not set to "off"
		return new Duple<>(on, off);
	}
}
