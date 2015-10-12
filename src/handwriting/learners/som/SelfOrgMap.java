package handwriting.learners.som;

import handwriting.core.Drawing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SelfOrgMap {
	private int drawingWidth, drawingHeight;
	// Representation data type
	
	public SelfOrgMap(int width, int height, int dWidth, int dHeight) {
		this.drawingWidth = dWidth;
		this.drawingHeight = dHeight;
		/* TODO: Initialize your representation here */
	}
	
	// TODO: Fix these two methods
	public int getWidth() {return -1;}
	public int getHeight() {return -1;}
	
	public int getDrawingWidth() {return drawingWidth;}
	public int getDrawingHeight() {return drawingHeight;}
	
	public SOMPoint bestFor(Drawing example) {
		// TODO: Return the best matching node for "example"
		return new SOMPoint(0, 0);
	}
	
	public boolean isLegal(SOMPoint point) {
		return point.x() >= 0 && point.x() < getWidth() && point.y() >= 0 && point.y() < getHeight();
	}
	
	public void train(Drawing example) {
		/* TODO: Train your SOM using "example" */
	}
	
	public Color getFillFor(int x, int y, SOMPoint node) {
		/* TODO: Return the correct color for pixel (x, y) at node */
		return new Color(0, 0, 0, 1.0);
	}

	public void visualize(Canvas surface) {
		final double cellWidth = surface.getWidth() / getWidth();
		final double cellHeight = surface.getHeight() / getHeight();
		final double pixWidth = cellWidth / getDrawingWidth();
		final double pixHeight = cellHeight / getDrawingHeight();
		GraphicsContext g = surface.getGraphicsContext2D();
		for (int x = 0; x < getWidth(); x++) { 
			for (int y = 0; y < getHeight(); y++) {
				SOMPoint cell = new SOMPoint(x, y);
				for (int x1 = 0; x1 < getDrawingWidth(); x1++) {
					for (int y1 = 0; y1 < getDrawingHeight(); y1++) {
						g.setFill(getFillFor(x1, y1, cell));
						g.fillRect(cellWidth * x + pixWidth * x1, cellHeight * y + pixHeight * y1, pixWidth, pixHeight);			
					}
				}
			}
		}
	}
}
