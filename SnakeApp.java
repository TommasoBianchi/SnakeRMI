import javafx.application.Application; 
import javafx.application.Application.Parameters; 
import javafx.stage.Stage;  
import java.util.List;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import javafx.concurrent.Task;
import javafx.application.Platform;

public class SnakeApp extends Application implements RemotePlayer { 
   private RemoteWorld remoteWorld;
   private WorldView worldView;
   private int playerID;
   private boolean isRunning = true;

   @Override     
   public void start(Stage primaryStage) throws Exception {   
      Parameters params = getParameters();
      List<String> list = params.getRaw();

      String host = (list.size() < 1) ? "127.0.0.1" : list.get(0);
      try {
         Registry registry = LocateRegistry.getRegistry(host);
         remoteWorld = (RemoteWorld) registry.lookup("RemoteWorld");
         playerID = remoteWorld.addPlayer();
         RemotePlayer remotePlayer = (RemotePlayer)UnicastRemoteObject.exportObject(this, 0);
         registry.bind("RemotePlayer" + playerID, remotePlayer);
         remoteWorld.bindPlayer(playerID);
         System.out.println("Connected as player " + playerID);
         System.out.println("Test connection, server says: " + remoteWorld.helloWorld());
      } 
      catch (Exception e) {
         System.err.println("Client exception: " + e.toString());
         e.printStackTrace();
         return;
      }

      primaryStage.setMaximized(true);       
      primaryStage.show();      

      worldView = new WorldView(remoteWorld, primaryStage, playerID);  
      
      primaryStage.setTitle("Snake"); 
      primaryStage.setScene(worldView.getScene()); 

      startGame();
   }    

   @Override
   public void stop() throws Exception {
      remoteWorld.removePlayer(playerID);
      isRunning = false;
   }


   private void startGame(){
      try {
         worldView.update();
      }
      catch (Exception e) {
         System.out.println(e);
         return;
      }

      SnakeApp snakeApp = this;

      Task<Void> task = new Task<Void>() {
         @Override 
         public Void call() throws Exception {
            while(snakeApp.isRunning){
               try {
                  worldView.update();
                  Thread.sleep(75);
               }
               catch (Exception e) {
                  System.out.println(e);
                  break;
               }
            }

            snakeApp.exit();
            return null;
        }
      };
      new Thread(task).start();
   }

   private void exit(){
      Platform.exit();
      System.exit(0);
   }

   public void run(String[] args){
      launch(args);
   }

   // Remote methods    
   public void disconnect() {
      System.out.println("Server disconnected me");
      try {
         LocateRegistry.getRegistry().unbind("RemotePlayer" + playerID);
      }
      catch (Exception e){
        System.err.println(e);
      }

      isRunning = false;
      //Platform.exit();
      //System.exit(0);
   }

   public String helloWorld(){
      return "Hello World";
   }
} 