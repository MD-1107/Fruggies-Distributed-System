import java.rmi.*;

public interface Interface extends Remote {
	
	
	public String init() throws RemoteException;
	
	public String fruitChoice(int choice, int client_ID) throws RemoteException;
	public String fruitquantChoice(int choice, int client_ID) throws RemoteException;
    public String vegChoice(int choice, int client_ID) throws RemoteException;
	public String vegquantChoice(int choice, int client_ID) throws RemoteException;
	public String evaluate(int choice, int client_ID) throws RemoteException;
	public int fetchClientIDTime(int x, String clientRequestTime) throws RemoteException;

}
