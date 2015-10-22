package handwriting.learners.som;

import handwriting.core.Drawing;

/**
 * Created by My_Nguyen on 10/12/15.
 */
public class SOMNode {
    private SOMPoint[][] points;
    private int drawingHeight, drawingWidth;
    private int coordX, coordY;
    private String label;

    public int getDrawingHeight() {
        return drawingHeight;
    }

    public int getDrawingWidth() {
        return drawingWidth;
    }

    /*
            X and Y is the starting coordinate of the drawing
            dX and dY is the dimension ( width x height ) of the drawing
         */
    public SOMNode(int x, int y, int dX, int dY){
        this.drawingHeight= dY;
        this.drawingWidth = dX;
        this.coordX = x;
        this.coordY = y;

        points = new SOMPoint[dX][dY];
        for (int i =0; i < dX; i++ ){
            for (int j=0; j< dY; j++){
                // i = width
                // j = height
                points[i][j] = new SOMPoint(x+i, y+j);
                points[i][j].setWeight(0.0);
            }
        }

    }
    public SOMPoint[][] getPoints(){
        return points;
    }

    public void setWeight(double weight) {
        for ( int x = 0; x < drawingWidth; x++){
            for (int y =0; y < drawingHeight; y++){
                points[x][y].setWeight(weight);
            }
        }
    }

    public double calculateDistToDrawing(Drawing drawing) {
        double distance = 0.0;
        for (int x = 0; x < this.drawingWidth; x++){
            for (int y=0; y< this.drawingHeight; y++){
                distance += Math.pow((drawing.isSet(x, y) ? 1 : 0) -points[x][y].getWeight(),2);
            }
        }
        return distance;
    }

    public double calculateDistToNode(SOMNode other) {
        double distance = 0.0;
        for (int x = 0; x< this.drawingWidth; x++){
            for (int y=0; y < this.drawingHeight; y++){
                distance += Math.pow(this.points[x][y].x() - other.points[x][y].x(),2) + Math.pow(this.points[x][y].y() - other.points[x][y].y(),2);

            }
        }
        return distance;
    }

    public double getWeight() {
        return points[0][0].getWeight();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
