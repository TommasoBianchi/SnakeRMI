import java.util.ArrayList;

public class SnakeController {
	private ArrayList<Point> points;
	private Point direction;
	private boolean justEat;
	private boolean isAlive;

	public SnakeController(int startX, int startY){
		this.points = new ArrayList<Point>();
		this.points.add(new Point(startX, startY));
		this.direction = new Point(1, 0);
		this.isAlive = true;
	}

	public void update(int maxX, int maxY){
		for (int i = points.size() - ((justEat) ? 2 : 1); i > 0; i--) {
			points.set(i, points.get(i - 1));
		}
		Point head = points.get(0);
		int nextHeadX = head.getX() + direction.getX();
		int nextHeadY = head.getY() + direction.getY();
		if(nextHeadX > maxX)
			nextHeadX = 0;
		else if(nextHeadX < 0)
			nextHeadX = maxX;
		if(nextHeadY > maxY)
			nextHeadY = 0;
		else if(nextHeadY < 0)
			nextHeadY = maxY;
		points.set(0, new Point(nextHeadX, nextHeadY));
		justEat = false;

		if(points.lastIndexOf(points.get(0)) > 0){
			isAlive = false;
		}
	}

	public ArrayList<Point> getPoints(){
		return points;
	}

	public Point getTail(){
		return points.get(points.size() - 1);
	}

	public Point getHead(){
		return points.get(0);
	}

	public void setDirection(int x, int y){
		if(x + direction.getX() != 0 && y + direction.getY() != 0)
			direction = new Point(x, y);
	}

	public void eat(){
		Point tail = getTail();
		points.add(new Point(tail.getX(), tail.getY()));
		justEat = true;
	}

	public boolean isAlive(){
		return isAlive;
	}
}