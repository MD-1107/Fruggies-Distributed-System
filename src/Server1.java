 import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.sql.*;

class ClientNode{
	int clientID;
	// String timeStamp;
	boolean fruitMutexLock=true;
	boolean vegetableMutexLock;
}

public class Server1 extends UnicastRemoteObject implements Interface {
	String fruits = "";
	int fruitIndex = 0;
	int vegIndex = 0;
	String fruits_quant = "";
	String veg = "";
	String veg_quant = "";
	String state = "";
	double base = 0.0;
	double val = 0.0;
	int client_no = 0;
	HashMap<Integer, Integer> tempMap = new HashMap<>();
	HashMap<Integer, String> clientTimeMap = new HashMap<>();
	HashMap<Integer, ClientNode> clientNodeMap = new HashMap<>();
	HashMap<Integer, String> clientFruitMap = new HashMap<>();
	HashMap<Integer, String> clientVegMap = new HashMap<>();
	HashMap<Integer, String> clientFruitQuantMap = new HashMap<>();
	HashMap<Integer, String> clientVegQuantMap = new HashMap<>();
	// PriorityQueue<ClientNode> queue = new PriorityQueue<>((a, b) -> a.timeStamp.compareTo(b.timeStamp));
	boolean fruitMutexLock = false;

	ClientNode current;


	static Connection connection =null;

	static Connection connection1 =null;

	static Connection connection2 =null;
	
	public static void databaseLog()
	{
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/localdb",
                "root", "Maanu1107%");
			
				connection1 = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/localdb1",
					"root", "Maanu1107%");
						
				connection2 = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/localdb2",
					"root", "Maanu1107%");
			 
            // mydb is database
            // mydbuser is name of database
            // mydbuser is password of database
 
            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet_fruit;
			ResultSet resultSet_vegetable;
			//ResultSet resultSet1;
			//ResultSet resultSet2;

            resultSet_fruit = statement.executeQuery(
                "select * from FRUITS2");
            int indexf;
            String namef;
            Integer quantityf;
            while (resultSet_fruit.next()) {
                indexf = resultSet_fruit.getInt("fruit_index");
                namef = resultSet_fruit.getString("fruit_name").trim();
                quantityf = resultSet_fruit.getInt("fruit_quantity_avl");
                System.out.println("FRUIT INDEX : " + indexf
                                   + " FRUIT NAME : " + namef
                                   + " FRUIT QUANTITY: "+quantityf);
				
				Data.FRUITS.get(indexf).set(2, quantityf.toString());
				
            }

			resultSet_vegetable = statement.executeQuery(
                "select * from VEG2");
            int indexv;
            String namev;
            Integer quantityv;
            while (resultSet_vegetable.next()) {
                indexv = resultSet_vegetable.getInt("veg_index");
                namev = resultSet_vegetable.getString("veg_name").trim();
                quantityv = resultSet_vegetable.getInt("veg_quantity_avl");
                System.out.println("VEGETABLE INDEX : " + indexv
                                   + " VEGETABLE NAME : " + namev
                                   + " VEGETABLE QUANTITY: "+quantityv);
				
				Data.VEG.get(indexv).set(2, quantityv.toString());
				
            }
			//  // create the java mysql update preparedstatement
			//  String query = "update FRUITS set fruit_quantity_avl=? where fruit_index=?";
			//  PreparedStatement preparedStmt = connection.prepareStatement(query);
			//  preparedStmt.setInt(1,12);
			//  preparedStmt.setInt(2,0);
			//  // execute the java preparedstatement
			//  preparedStmt.executeUpdate();
            // resultSet.close();
            // statement.close();
            // connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }

		System.out.println("The server is connected to the database");
	}
	// 1. iska order usme dikh rha hai, mapping karo(done)
	// 2. TimeStamp ke order mein u'll the cs unlock
	public Server1() throws RemoteException {
		super();

	}

	public String init() throws RemoteException {
		
		String res = "\n\nSelect:\n";

		

		for (int i = 0; i < Data.FRUITS.size(); i++) {
			res += i + ": " + Data.FRUITS.get(i).get(0) + " (PRICE PER KG: Rs." + Data.FRUITS.get(i).get(1) + ") qty: "
					+ Data.FRUITS.get(i).get(2) + "\n";

		}

		res += ">>> ";
		return res;
	}

	public int fetchClientIDTime(int x, String clientRequestTime) {
		client_no++;
		System.out.println("\n\nThe client ID is: " + (x + client_no) + "\nRequest made at: " + clientRequestTime);
		ClientNode client = new ClientNode();
		client.clientID = (x + client_no);
		// client.timeStamp = clientRequestTime;

		clientNodeMap.put(x + client_no, client);
		return x + client_no;
	}

	public String fruitChoice(int choice, int client_ID) throws RemoteException {
		if (!(choice > -1 && choice < Data.FRUITS.size())) {
			return "Invalid choice\n>>> ";
		}

		if (fruitMutexLock == false) {
			fruitMutexLock = true;
		} else
			return "locked";

		fruitIndex = choice;
		fruits = Data.FRUITS.get(choice).get(0);
		clientFruitMap.put(client_ID, fruits);
		base = Double.parseDouble(Data.FRUITS.get(choice).get(1));

		String res;

		res = "\n------------------------------------------------------------------------------------------------\n";
		res += "Current Choice: " + clientFruitMap.get(client_ID) + " | Rs." + String.format("%.2f", base) + "\n";
		res += "------------------------------------------------------------------------------------------------\n\n";

		res += "Select:\n";

		Integer curr = 0;
		for (Integer i = 0; i < Data.FRUITS_QUANT.size(); i++) {
			if (Data.FRUITS_QUANT.get(i).get(0).equals(fruits)) {
				tempMap.put(curr, i);
				res += curr + ": " + Data.FRUITS_QUANT.get(i).get(1) + " \n";

				curr += 1;
			}

			// for (Integer key : tempMap.keySet()) {

			// System.out.println(key+" "+tempMap.get(key));

			// }
		}
		// System.out.println("The fruit chosen"+ fruits);

		res += ">>> ";
		return res;
	}

	public String fruitquantChoice(int choice, int client_ID) throws RemoteException {
		if (tempMap.get(choice) == null
				|| !(tempMap.get(choice) > -1 && tempMap.get(choice) < Data.FRUITS_QUANT.size())) {
			return "Invalid choice\n>>> ";
		}

		fruits_quant = Data.FRUITS_QUANT.get(tempMap.get(choice)).get(1); //index that user will enter 0, 1, 2
		val = base + Double.parseDouble(Data.FRUITS_QUANT.get(tempMap.get(choice)).get(2));

		// System.out.println("Choijhgfdjhg: "+fruitIndex);

		Integer x = Integer.parseInt(Data.FRUITS.get(fruitIndex).get(2));
		x = x - (fruits_quant.charAt(0) - '0');

		try{
		// create the java mysql update preparedstatement
		System.out.println("To update the quantity of fruits in the database");
		String query = "update FRUITS set fruit_quantity_avl=? where fruit_index=?";
		PreparedStatement preparedStmt = connection.prepareStatement(query);
		preparedStmt.setInt(1,x);
		preparedStmt.setInt(2,fruitIndex);
		// execute the java preparedstatement
		System.out.println("The fruits quantity changed: "+x+"\nThe index is: "+choice+"\nThe fruit index is: "+fruitIndex);
		preparedStmt.executeUpdate();

		// create the java mysql update preparedstatement
		System.out.println("To update the quantity of fruits1 in the database");
		String query1 = "update FRUITS1 set fruit_quantity_avl=? where fruit_index=?";
		PreparedStatement preparedStmt1 = connection1.prepareStatement(query1);
		preparedStmt1.setInt(1,x);
		preparedStmt1.setInt(2,fruitIndex);
		// execute the java preparedstatement
		System.out.println("The fruits quantity changed: "+x+"\nThe index is: "+choice+"\nThe fruit index is: "+fruitIndex);
		preparedStmt1.executeUpdate();

		// create the java mysql update preparedstatement
		System.out.println("To update the quantity of fruits1 in the database");
		String query2 = "update FRUITS2 set fruit_quantity_avl=? where fruit_index=?";
		PreparedStatement preparedStmt2 = connection2.prepareStatement(query2);
		preparedStmt2.setInt(1,x);
		preparedStmt2.setInt(2,fruitIndex);
		// execute the java preparedstatement
		System.out.println("The fruits quantity changed: "+x+"\nThe index is: "+choice+"\nThe fruit index is: "+fruitIndex);
		preparedStmt2.executeUpdate();


		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		System.out.println("fruit quant chosen: "+fruits_quant+" x: "+x);
		clientFruitQuantMap.put(client_ID, fruits_quant);
		if (x < 0) {
			// System.out.println("Insufficient Inventory :(\n");
			val = 0;
			String res ="Insufficient Inventory :(\n";
			res = "\n------------------------------------------------------------------------------------------------\n";
			res += "Current Choice: " + clientFruitMap.get(client_ID) + ", " + "0kg" + " | Rs. 0 \n";
			res += "------------------------------------------------------------------------------------------------\n\n";

			res += "Select:\n";

			for (int i = 0; i < Data.VEG.size(); i++) {
				res += i + ": " + Data.VEG.get(i).get(0) + " (PRICE PER KG: Rs." + Data.VEG.get(i).get(1)+ ") qty: "
				+ Data.VEG.get(i).get(2) + "\n";
				
			}

			res += ">>> ";
			return res;
		}

		// System.out.println("fruits_quant: "+fruits_quant);
		// System.out.println((int)fruits_quant.charAt(0));
		// String mod_quant =
		// (Integer.parseInt(Data.FRUITS.get(tempMap.get(choice)).get(2))-Integer.parseInt(fruits_quant));
		
		Data.FRUITS.get(fruitIndex).set(2, x.toString());

		String res;

		res = "\n------------------------------------------------------------------------------------------------\n";
		res += "Current Choice: " + clientFruitMap.get(client_ID) + ", " + clientFruitQuantMap.get(client_ID) + " | Rs."
				+ String.format("%.2f", val) + "\n";
		res += "------------------------------------------------------------------------------------------------\n\n";

		res += "Select:\n";

		for (int i = 0; i < Data.VEG.size(); i++) {
			res += i + ": " + Data.VEG.get(i).get(0) + " (PRICE PER KG: Rs." + Data.VEG.get(i).get(1)+ ") qty: "
			+ Data.VEG.get(i).get(2) + "\n";

		}

		res += ">>> ";
		return res;
	}

	public String vegChoice(int choice, int client_ID) throws RemoteException {
		fruitMutexLock = false;
		if (!(choice > -1 && choice < Data.VEG.size())) {
			return "Invalid choice\n>>> ";
		}

		vegIndex = choice;
		veg = Data.VEG.get(choice).get(0);
		base = Double.parseDouble(Data.VEG.get(choice).get(1));
		clientVegMap.put(client_ID, veg);

		String res;

		res = "\n------------------------------------------------------------------------------------------------\n";
		res += "Current Choice: " + clientVegMap.get(client_ID) + " | Rs." + String.format("%.2f", base) + "\n";
		res += "------------------------------------------------------------------------------------------------\n\n";

		res += "Select veg quant:\n";

		Integer curr = 0;
		for (Integer i = 0; i < Data.VEG_QUANT.size(); i++) {
			if (Data.VEG_QUANT.get(i).get(0).equals(veg)) {
				tempMap.put(curr, i);
				res += curr + ": " + Data.VEG_QUANT.get(i).get(1) + " \n";

				curr += 1;
			}
		}

		res += ">>> ";
		return res;

	}

	public String vegquantChoice(int choice, int client_ID) throws RemoteException {
		if (tempMap.get(choice) == null || !(tempMap.get(choice) > -1 && tempMap.get(choice) < Data.VEG_QUANT.size())) {
			return "Invalid choice\n>>> ";
		}

		veg_quant = Data.VEG_QUANT.get(tempMap.get(choice)).get(1);
		val += base + Double.parseDouble(Data.VEG_QUANT.get(tempMap.get(choice)).get(2));

		Integer y = Integer.parseInt(Data.VEG.get(vegIndex).get(2));
		y = y - (veg_quant.charAt(0) - '0');

		try{
			// create the java mysql update preparedstatement
			System.out.println("To update the quantity of veg in the database");
			String query = "update VEG set veg_quantity_avl=? where veg_index=?";
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1,y);
			preparedStmt.setInt(2,vegIndex);
			// execute the java preparedstatement
			System.out.println("The vegetables quantity changed: "+y+"\nThe index is: "+choice+"\nThe veg index is: "+vegIndex);
			preparedStmt.executeUpdate();
	
			// create the java mysql update preparedstatement
			System.out.println("To update the quantity of veg in the database");
			String query1 = "update VEG1 set veg_quantity_avl=? where veg_index=?";
			PreparedStatement preparedStmt1 = connection1.prepareStatement(query1);
			preparedStmt1.setInt(1,y);
			preparedStmt1.setInt(2,vegIndex);
			// execute the java preparedstatement
			System.out.println("The vegetables quantity changed: "+y+"\nThe index is: "+choice+"\nThe veg index is: "+vegIndex);
			preparedStmt1.executeUpdate();
	
			// create the java mysql update preparedstatement
			System.out.println("To update the quantity of veg in the database");
			String query2 = "update VEG2 set veg_quantity_avl=? where veg_index=?";
			PreparedStatement preparedStmt2 = connection2.prepareStatement(query2);
			preparedStmt2.setInt(1,y);
			preparedStmt2.setInt(2,vegIndex);
			// execute the java preparedstatement
			System.out.println("The vegetables quantity changed: "+y+"\nThe index is: "+choice+"\nThe veg index is: "+vegIndex);
			preparedStmt2.executeUpdate();
	
	
			}
			catch(Exception e)
			{
				System.out.println(e);
			}




		System.out.println("veggie quant chosen: "+veg_quant+" y: "+y);
		clientVegQuantMap.put(client_ID, veg_quant);

		if (y < 0) {

			val -= base + Double.parseDouble(Data.VEG_QUANT.get(tempMap.get(choice)).get(2));
			String res ="Insufficient Inventory :(\n";
			res = "\n------------------------------------------------------------------------------------------------\n";
			res += "Current Choice: " + clientVegMap.get(client_ID) + ", " + "0kg" + " | Rs. 0 \n";
			res += "------------------------------------------------------------------------------------------------\n\n";


			return res;

		}
		Data.VEG.get(vegIndex).set(2, y.toString());
		String res;

		res = "\n------------------------------------------------------------------------------------------------\n";
		res += "Current Choice: " + clientVegMap.get(client_ID) + ", " + clientVegQuantMap.get(client_ID)+" | Rs." + String.format("%.2f", val) + "\n";
		res += "------------------------------------------------------------------------------------------------\n\n";

		return res;

	}


	public String evaluate(int choice, int client_ID) throws RemoteException {
		// if (!(choice > -1 && choice < Data.STATE.size())) {
		// return "Invalid choice\n>>> ";
		// }

		// state = Data.STATE.get(choice).get(0);
		// val *= (1 + Double.parseDouble(Data.STATE.get(choice).get(1)) / 100);

		String res;

		res = "\n------------------------------------------------------------------------------------------------\n";
		res += "Current Choice: " + clientFruitMap.get(client_ID) + ", " + clientFruitQuantMap.get(client_ID) + ", "
				+ clientVegMap.get(client_ID) + ", " + clientVegQuantMap.get(client_ID) + ", " + state
				+ " | Rs." + String.format("%.2f", val) + "\n";
		res += "------------------------------------------------------------------------------------------------\n\n";

		res += "---------------------------------------\nFinal summary\n---------------------------------------\n";

		res += "Fruit: " + clientFruitMap.get(client_ID) + "\n";
		res += "quantity: " + clientFruitQuantMap.get(client_ID) + "\n";
		res += "Vegetable: " + veg + "\n";
		res += "quantity: " + veg_quant + "\n";
		// res += "State: " + state + "\n";
		res += "---------------------------------------\n";
		res += "Total price: Rs." + String.format("%.2f", val) + "\n";
		res += "---------------------------------------";

		return res;

	}

	public static void main(String[] args) {
		final String HOST = "rmi://localhost:1001//Server1";
		Server1 Server;

		try {
			System.out.println("[+] Starting Server");
			Server = new Server1();
			System.out.println("[+] Server Instance Created");

			LocateRegistry.createRegistry(1001);

			Naming.bind(HOST, Server);

			System.out.println("[+] Server bound to Registry succesfully");
			System.out.println("[+] Server Ready");

			ServerSocket s = new ServerSocket(5051);
			int server_timeout = 120000;// waits for 120 seconds until the input arrives
			s.setSoTimeout(server_timeout);
			System.out.println("Server is running at 1001");
			System.out.println("|\n|-Server is running...");

			try {
				while (true) {
					Socket s1 = s.accept();
					System.out.println("|\n|-Connection Received : " + s1);
					DataOutputStream dos = new DataOutputStream(s1.getOutputStream());

					Date dt = new Date();
					System.out.println("    |\n    |-Sent to client :  " + dt);
					dos.writeLong(dt.getTime());

					System.out.println("    |\n    |-Connection Terminated : " + s1);
					s1.close();
					dos.close();

				}
			} catch (SocketTimeoutException e) {
				System.out.println("|\nTerminating server due to timeout...");
				System.out.println("Server is terminated!");
				s.close();
			}

		} catch (Exception ex) {
			System.out.println("[-] Exception: " + ex.getMessage());

		}
	}
}
