import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import javafx.concurrent.Task;

public class SnakeServer {
 	private int worldWidth = 40;
 	private int worldHeight = 40;
 	private WorldController worldController;
 	private boolean isRunning = true;

	public static void main(String args[]) {    
		new SnakeServer(args);
    }

    public SnakeServer(String[] args){
    	if(args.length >= 2){
		    try {
		    	worldWidth = Integer.parseInt(args[0]);
		        worldHeight = Integer.parseInt(args[1]);
		        System.out.println("World size set to " + worldWidth + "x" + worldHeight);
		    }
		    catch (NumberFormatException e){
		        System.out.println("Default to a " + worldWidth + "x" + worldHeight + " world");
		    }
		}

        try {
            worldController = new WorldController(worldWidth, worldHeight);
            RemoteWorld remoteWorld = (RemoteWorld)UnicastRemoteObject.exportObject(worldController, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RemoteWorld", remoteWorld);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

        startGame();
    }

    private void startGame(){
        System.out.println("Game started");      	
    	while(isRunning){
           	try {
              	worldController.update();
              	Thread.sleep(75);
           	}
           	catch (Exception e) {
              	System.out.println(e);
              	break;
           	}
        }
   	}
}