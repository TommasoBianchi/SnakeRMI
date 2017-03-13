import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.stage.Stage;  
import javafx.scene.shape.Rectangle; 
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent; 
import javafx.scene.Node;
import java.rmi.RemoteException;

public class WorldView {
	private int rowCount;
	private int colCount;
	private Rectangle[][] cells;
	private Scene scene;
	private double leftRightMarginPercentage = 0.1;
	private double topDownMarginPercentage = 0.1;
	private RemoteWorld remoteWorld;
  private int playerID;

	public WorldView(RemoteWorld remoteWorld, Stage stage, int playerID) throws RemoteException {
   		this.remoteWorld = remoteWorld;
   		this.rowCount = remoteWorld.getWidth();
   		this.colCount = remoteWorld.getHeight();
      this.playerID = playerID;

     	Group group = new Group(); 
     	ObservableList<Node> list = group.getChildren(); 
     	scene = new Scene(group, stage.getWidth(), stage.getHeight());   

     	cells = new Rectangle[rowCount][colCount];
     	double leftRightMargin = scene.getWidth() * leftRightMarginPercentage;
     	double topDownMargin = scene.getHeight() * topDownMarginPercentage;
     	double cellWidth = (scene.getWidth() - rowCount + 1 - 2 * leftRightMargin) / rowCount;
     	double cellHeight = (scene.getHeight() - colCount + 1 - 2 * topDownMargin) / colCount;
     	double cellSize = (cellWidth > cellHeight) ? cellHeight : cellWidth;
     	leftRightMargin = (scene.getWidth() - rowCount * (cellSize + 1)) / 2;
     	topDownMargin = (scene.getHeight() - colCount * (cellSize + 1)) / 2;
      CellState[][] world = remoteWorld.getWorld();
     	for (int x = 0; x < rowCount; x++) {
        	for (int y = 0; y < colCount; y++) {
            	Rectangle cell = new Rectangle(leftRightMargin + x * (cellSize + 1), topDownMargin + y * (cellSize + 1),
                                 cellSize, cellSize);
            	cell.setFill(world[x][y].getColor());
             	list.add(cell);
             	cells[x][y] = cell;
         	}
      	} 

      	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
         	@Override
         	public void handle(KeyEvent event) {
              try {
                remoteWorld.sendKeypress(event, playerID);
              }
              catch(RemoteException e){
                System.out.println(e);
              }
         	}
      	});
	}

	public Scene getScene(){
		return scene;
	}

	public void update() throws RemoteException {
    CellState[][] world = remoteWorld.getWorld();
		for (int x = 0; x < rowCount; x++) {
    	for (int y = 0; y < colCount; y++) {
       	cells[x][y].setFill(world[x][y].getColor());
     	}
  	} 
	}
}