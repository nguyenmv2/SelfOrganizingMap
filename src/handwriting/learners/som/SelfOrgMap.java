package handwriting.learners.som;

import handwriting.core.Drawing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class SelfOrgMap {
	private int drawingWidth, drawingHeight, width, height;
    private SOMNode[][] inputNodes;

    public void setNum_iter(int num_iter) {
        this.num_iter = num_iter;
    }

    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    public int getNum_iter() {
        return num_iter;
    }

    public double getLearning_rate() {
        return learning_rate;
    }

    private int num_iter;
    private double learning_rate;
    private double[][][][] maps;
    private String[][] labels;
	// Representation data type

	public SelfOrgMap(int width, int height, int dWidth, int dHeight) {
		this.drawingWidth = dWidth;
		this.drawingHeight = dHeight;
        this.width = width;
        this.height = height;
        maps = new double[width][height][drawingWidth][drawingHeight];
        labels = new String[width][height];
        for ( int x=0; x< width; x++){
            for(int y=0; y<height; y++){
                for( int dX=0; dX < drawingWidth; dX++){
                    for(int dY =0; dY < drawingHeight; dY++){
                        maps[x][y][dX][dY] = Math.random();
                    }
                }

            }
        }
	}
	
	public int getWidth() {return this.width;}
	public int getHeight() {return this.height;}
	
	public int getDrawingWidth() {return drawingWidth;}
	public int getDrawingHeight() {return drawingHeight;}
	
	public SOMPoint bestFor(Drawing example) throws Exception {
        SOMPoint closest = null;
        double closestDist =Double.MAX_VALUE;
        for(int x=0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                double distanceFromNodeToDrawing = distanceFromTo(maps[x][y], example);
                if (closestDist > distanceFromNodeToDrawing) {
                    closestDist = distanceFromNodeToDrawing;
                    closest = new SOMPoint(x,y);
                }
            }
        }
        if (closest != null) return closest;
        else
            throw new Exception("Couldn't find the closest node");
	}

    private double distanceFromTo(double[][] thisNode, double[][] otherNode){
        double distance = 0.0;
        for (int x=0; x < getDrawingWidth(); x++){
            for (int y=0; y < getDrawingHeight(); y++){
                distance += Math.pow(thisNode[x][y] - otherNode[x][y], 2);
            }
        }
        return distance;
    }
    private double distanceFromTo(double[][] node, Drawing d) {
        double distance = 0.0;
        for ( int x=0; x < getDrawingWidth(); x++){
            for (int y=0; y < getDrawingHeight(); y++){
                distance += Math.pow(node[x][y] - (d.isSet(x, y)?1:0),2);
            }
        }
        return distance;
    }

    public int mapRadius(){
        return Math.max(this.width, this.height) /2;
    }

    public double timeConstant(){
        return num_iter / Math.log(mapRadius());
    }
    /*
        radiusOfNeighborhood = mapRadius * exp(-currentIteration / timeConstant)
     */
    public double radiusOfChange(int cur_iter){
        return mapRadius() * Math.exp(-cur_iter / timeConstant());
    }
    /*
        learning rate is affected by the number of iteration. Lower near the end
        new_rate = old_rate * exp(-current_iteration / timeConstant)
     */
    public double currentLearningRate(int cur_iter){
        return learning_rate * Math.exp(-cur_iter / timeConstant());
    }
    /*
        The closer the node to the winning node, the more significant the change of weight
     */
    public double nodeInfluence(int cur_iter, double distToWinningNode){
        return Math.exp(-Math.pow(-distToWinningNode, 2) / 2 * Math.pow(mapRadius(),2));
    }
	public boolean isLegal(SOMPoint point) {
		return point.x() >= 0 && point.x() < getWidth() && point.y() >= 0 && point.y() < getHeight();
	}

    public double scalingFactor(double disFromBestToCurNode, double radiusOfNeighbor){
        return Math.exp( -disFromBestToCurNode / 2 * Math.pow(radiusOfNeighbor,2));
    }
	
	public void train(Drawing example, int cur_iter) throws Exception {
		/* TODO: Train your SOM using "example" */
            SOMPoint winningPoint = bestFor(example);
            setWeight(example, winningPoint, cur_iter);
            ArrayList<SOMPoint> affectedPoints = getAffectedPoints(winningPoint, radiusOfChange(cur_iter));
            for (SOMPoint p : affectedPoints){
                setWeight(example, p, cur_iter);
            }

	}

    public void setWeight(Drawing d, SOMPoint point, int cur_iter){
        for (int dx=0; dx < getDrawingWidth(); dx++){
            for (int dy=0; dy < getDrawingHeight(); dy++){
                double oldWeight = maps[point.x()][point.y()][dx][dy];
                double newWeigth = oldWeight + currentLearningRate(cur_iter) * (d.isSet(dx,dy)?1:0 - maps[point.x()][point.y()][dx][dy]);
                maps[point.x()][point.y()][dx][dy] = newWeigth;
            }
        }
    }
    public ArrayList<SOMPoint> getAffectedPoints(SOMPoint winningPoint, double areaOfInfluence){
        ArrayList<SOMPoint> affectedPoints = new ArrayList<>();
        for (int x =0; x < this.width; x++){
            for (int y=0; y < this.height; y++){
                SOMPoint p = new SOMPoint (x,y);
                if (p.distanceTo(winningPoint) <= areaOfInfluence){
                    affectedPoints.add(p);
                }
            }
        }
        return affectedPoints;
    }
	public Color getFillFor(int x, int y, SOMPoint point) {
        double value = maps[point.x()][point.y()][x][y];
        if ( value < 0) value = 0.0;
        else if (value > 1) value = 1.0;
        return new Color (value, value, value, 1.0);
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
    public void setLabel(Drawing d, String label) throws Exception {
        SOMPoint bestPoint = bestFor(d);
        labels[bestPoint.x()][bestPoint.y()] = label;
    }
    public String getLabel(SOMPoint point) {
       return labels[point.x()][point.y()];
    }

    public void setOutstandingLabels() throws Exception {
        for (int x=0; x < getWidth(); x++){
            for (int y=0; y < getHeight(); y++){
                if ( labels[x][y] == null) {
                    //No label here
                    //Look for a similar node and take the label from it

                    SOMPoint mostSimilar = findSimilarPoint(maps[x][y]);
                    String label = labels[mostSimilar.x()][mostSimilar.y()];
                    labels[x][y] = label;
                }
            }
        }
    }

    private SOMPoint findSimilarPoint(double[][] curPoint) throws Exception {
        double closestDist = Double.MAX_VALUE;
        SOMPoint best = null;
        for ( int x=0; x < getWidth(); x++){
            for(int y=0; y < getHeight(); y++){
                double dist = distanceFromTo(curPoint, maps[x][y]);
                if ( dist < closestDist && labels[x][y] != null) {
                    closestDist = dist;
                    best = new SOMPoint(x,y);
                }
            }
        }
        if (best != null) return best;
        else
            throw new Exception("Couldn't find closest node");
    }
}
