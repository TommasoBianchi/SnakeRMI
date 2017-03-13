import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePlayer extends Remote {
	void disconnect() throws RemoteException;
	String helloWorld() throws RemoteException;
}