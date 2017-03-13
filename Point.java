public class Point {
	private int x;
	private int y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public Point add(Point other){
		return new Point(this.x + other.x, this.y + other.y);
	}

	@Override
	public String toString(){
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object other){
		if (other == null) return false;
    	if (other == this) return true;
   	 	if (!(other instanceof Point)) return false;
   	 	Point p = (Point)other;
   	 	return p.x == this.x && p.y == this.y;
	}
}