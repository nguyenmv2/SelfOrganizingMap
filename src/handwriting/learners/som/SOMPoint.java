package handwriting.learners.som;

import java.util.ArrayList;

public class SOMPoint {
	private int x, y;
	private double value;
    private double weight;

    public SOMPoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.value = 0.0;
	}
	
	public int x() {return x;}
	public int y() {return y;}
	
	public double distanceTo(SOMPoint other) {
		return distanceTo(other.x, other.y);
	}
	
	public double distanceTo(int x, int y) {
		return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
	}
	
	public int hashCode() {return x * 10000 + y;}
	public String toString() {return String.format("(%d,%d)", x, y);}
	public boolean equals(Object other) {
		if (other instanceof SOMPoint) {
			SOMPoint that = (SOMPoint)other;
			return this.x == that.x && this.y == that.y;
		} else {
			return false;
		}
	}

	public void setWeight(double value) {
		this.value = value;
	}

    public double getWeight() {
        return weight;
    }
}
