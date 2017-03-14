import javafx.scene.paint.Paint;
import java.lang.Math;
import javafx.scene.input.KeyEvent; 
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class WorldController implements RemoteWorld {
 	private int rowCount;
 	private int colCount;
 	private CellState[][] world;
 	private ArrayList<SnakeController> snakes = new ArrayList<SnakeController>();
  private ArrayList<RemotePlayer> players = new ArrayList<RemotePlayer>();
 	private boolean isRunning;

 	private int fruitAmount = 0;

 	public WorldController(int rowCount, int colCount){
 		this.rowCount = rowCount;
 		this.colCount = colCount;
 		this.world = new CellState[rowCount][colCount];

 		for (int x = 0; x < rowCount; x++) {
 			for (int y = 0; y < colCount; y++) {
 				world[x][y] = CellState.EMPTY;
 			}
 		}
	
   	this.isRunning = true;
 	}

  public void stop(){
    isRunning = false;
 	}

 	public void update(){
 		if(isRunning){
      for(int i = 0; i < snakes.size(); i++){
        SnakeController snake = snakes.get(i);
        if(snake == null)
          continue;

  		 	Point snakeTail = snake.getTail();
  	  	world[snakeTail.getX()][snakeTail.getY()] = CellState.EMPTY;

  	  	snake.update(rowCount - 1, colCount - 1);

  	  	Point snakeHead = snake.getHead();
  	  	if(world[snakeHead.getX()][snakeHead.getY()] == CellState.FRUIT){
  	    	snake.eat();
  	    	fruitAmount--;
  	     	world[snakeHead.getX()][snakeHead.getY()] = CellState.EMPTY;
  	  	}
        else if(world[snakeHead.getX()][snakeHead.getY()] == CellState.SNAKE){
          snake.die();
        }

  	  	for(Point p : snake.getPoints())
  	  		world[p.getX()][p.getY()] = CellState.SNAKE;

  	  	if(Math.random() < probabilityToSpawnFruit()){
  	  		int randX = (int)(Math.random() * rowCount);
  	  		int randY = (int)(Math.random() * colCount);
  	  		if(world[randX][randY] == CellState.EMPTY){
  	  			world[randX][randY] = CellState.FRUIT;
  	  			fruitAmount++;
  	  		}
  	  	}
      }

      for(int i = 0; i < snakes.size(); i++){
        if(snakes.get(i) != null && snakes.get(i).isAlive() == false){
          removePlayer(i);
        }
      }
		}      
 	}

  private double probabilityToSpawnFruit(){
    return 0.5 / ((fruitAmount + 1) * (fruitAmount + 1));
  }

 	public boolean isRunning(){
 		return isRunning;
 	}

  // Remote methods
  public CellState[][] getWorld() {
    return world;
  }

  public int getWidth(){
    return rowCount;
  }

  public int getHeight(){
    return colCount;
  }

  public void sendKeypress(KeyEvent event, int playerID) {
    SnakeController snake = snakes.get(playerID);
    if(snake == null) return;
    switch (event.getCode()) {
      case UP:
      case W:    
        snake.setDirection(0, -1);
        break;
      case DOWN: 
      case S:       
        snake.setDirection(0, 1);
        break;
      case LEFT:   
      case A:     
        snake.setDirection(-1, 0);
        break;
      case RIGHT:  
      case D:      
        snake.setDirection(1, 0);
        break;
    }
  }
  
  public int addPlayer() {
    snakes.add(new SnakeController(rowCount / 2, colCount / 2));
    int playerID = snakes.size() - 1;
    System.out.println("Player " + playerID + " connected");
    return playerID;
  }

  public void removePlayer(int playerID) {
    SnakeController snake = snakes.get(playerID);
    if(snake == null)
      return;

    try {
      for(Point p : snake.getPoints())
        world[p.getX()][p.getY()] = CellState.EMPTY;
      snakes.set(playerID, null);
      players.get(playerID).disconnect();
      players.set(playerID, null);
      System.out.println("Player " + playerID + " disconnected");
    }
    catch (Exception e){
      System.err.println(e);
    }    
  }

  public String helloWorld(){
    return "Hello World";
  }

  public void bindPlayer(int playerID) throws RemoteException {
    if(playerID > players.size())
      throw new RemoteException();
    else
      try {
        if(playerID < players.size())
          players.set(playerID, (RemotePlayer) LocateRegistry.getRegistry().lookup("RemotePlayer" + playerID));
        else
          players.add((RemotePlayer) LocateRegistry.getRegistry().lookup("RemotePlayer" + playerID));
        System.out.println("Test connection, player " + playerID + " says: " + players.get(playerID).helloWorld());
      }
      catch (Exception e){
        System.err.println(e);
      }
  }
}