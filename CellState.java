import javafx.scene.paint.Paint;

public enum CellState {
	EMPTY(Paint.valueOf("#111")),
	SNAKE(Paint.valueOf("#888")),
	FRUIT(Paint.valueOf("#FFF"));

	private Paint color;

	CellState(Paint color){
		this.color = color;
	}

	public Paint getColor(){
		return color;
	}
}