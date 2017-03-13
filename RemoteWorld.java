import java.rmi.Remote;
import java.rmi.RemoteException;
import javafx.scene.input.KeyEvent; 

public interface RemoteWorld extends Remote {
	CellState[][] getWorld() throws RemoteException;
	void sendKeypress(KeyEvent event, int playerID) throws RemoteException;
	int addPlayer() throws RemoteException;
	void removePlayer(int playerID) throws RemoteException;
	int getWidth() throws RemoteException;
	int getHeight() throws RemoteException;	
	String helloWorld() throws RemoteException;
	void bindPlayer(int playerID) throws RemoteException;
}