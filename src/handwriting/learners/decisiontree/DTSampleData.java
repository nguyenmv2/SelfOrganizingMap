package handwriting.learners.decisiontree;

import handwriting.core.Drawing;
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
		//1 - sum(pi2) = Gini

		int totalLabel = this.numLabels();
		double sumLabel = 0.0;
		for (String label : this.allLabels()){
			sumLabel += Math.pow(numDrawingsFor(label) / totalLabel,2);
		}
		return 1 - sumLabel;
	}

	
	public Duple<DTSampleData,DTSampleData> splitOn(int x, int y) {
		DTSampleData on = new DTSampleData();
		DTSampleData off = new DTSampleData();
		// TODO: Add all elements with (x, y) set to "on"
		for (String label : this.allLabels()){
            for (int numLabel = 0; numLabel < this.numDrawingsFor(label); numLabel++){
				Drawing d = this.getDrawing(label,numLabel);
				if ( d.isSet(x,y))
					on.addDrawing(label, d);
				else
					off.addDrawing(label, d);
			}
		}
		//       Add all elements with (x, y) not set to "off"
        System.out.println(on.numLabels());
        return new Duple<>(on, off);
	}
}
