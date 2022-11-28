import java.rmi.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Client {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		final String HOST = "rmi://localhost:1234//Server";
		String result;
		Interface Server;

		try {
			System.out.println("[+] Client started");

			Server = (Interface) Naming.lookup(HOST);
			System.out.println("[+] Look up successful");

			int choice;

			result = Server.init();

			////
			Socket s = new Socket("localhost", 50555);

			DataInputStream dis = new DataInputStream(s.getInputStream());

			SimpleDateFormat time_pattern = new SimpleDateFormat("HH:mm:ss.SSSSSS");

			Date dt = new Date();
			long req_time = dt.getTime();

			long s_time = dis.readLong();
			Date server_time = new Date();
			server_time.setTime(s_time);
			Date actual_time = new Date();
			long res_time = actual_time.getTime();

			System.out.println("|\n|-Time returned by server : " + time_pattern.format(server_time));
			long process_delay_latency = res_time - req_time;
			System.out.println("|\n|-Process Delay Latency : " + (double) process_delay_latency / 1000.0 + " seconds");
			System.out.println("|\n|-Actual clock time at client side :" + time_pattern.format(actual_time));

			long client_time = s_time + process_delay_latency / 2;
			dt.setTime(client_time);
			System.out.println("|\n|-Synchronized process client time : " + time_pattern.format(dt));

			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			String sys_time = "" + df.format(dt);

			long error = res_time - client_time;
			System.out.println("|\n|-Synchronization error : " + (double) error / 1000.0 + " seconds");

			System.out.println("Changing system time...");
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			changeSystemTime(sys_time);

			dis.close();
			s.close();
			int client_ID=1;
			
			client_ID=Server.fetchClientIDTime(client_ID,time_pattern.format(dt));
			while (true) {
				System.out.print(result);
				choice = sc.nextInt();
				
				result = Server.fruitChoice(choice, client_ID);
				System.out.println("result: "+ result);

				if(result.equals("locked")){
					System.out.println("Currently the item is unavailable!\nPlease try again later");
					result=Server.init();
					

					continue;
				}
				if (!result.equals("Invalid choice\n>>> ")) {
					break;

				}
			}

			while (true) {
				System.out.print(result);
				choice = sc.nextInt();
				result = Server.fruitquantChoice(choice, client_ID);

				if(result.equals("Insufficient Inventory :(")){
					break;
				}
				if (!result.equals("Invalid choice\n>>> ")) {
					break;

				}

				
			}

			

			while (true) {
				System.out.print(result);
				choice = sc.nextInt();
				result = Server.vegChoice(choice,client_ID);

				

				if (!result.equals("Invalid choice\n>>> ")) {
					break;

				}
			}

			while (true) {
				System.out.print(result);
				choice = sc.nextInt();
				result = Server.vegquantChoice(choice, client_ID);

				if (!result.equals("Invalid choice\n>>> ")) {
					break;

				}
			}

			while (true) {
				System.out.print(result);
				// choice=sc.nextInt();
				result = Server.evaluate(choice, client_ID);

				if (!result.equals("Invalid choice\n>>> ")) {
					break;

				}
			}

			System.out.println(result);

		}

		catch (Exception ex) {
			System.out.println("[-] Exception: " + ex.getMessage());
			ex.printStackTrace();

		}

		sc.close();

	}

	public static String detectOS() throws IOException {
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.indexOf("win") >= 0)
			return "win";
		else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0)
			return "linux";
		else
			return "notsupported";
	}

	public static void changeSystemTime(String time) throws IOException {
		String os = detectOS();
		if (os.equals("linux")) {
			Runtime.getRuntime().exec("date -s " + time);
			System.out.println("System time changed!");
		} else if (os.equals("win")) {
			String cmd = "cmd /c time " + time;
			Runtime.getRuntime().exec(cmd);
		} else {
			System.out.println("Operating System Not Supported!");
		}
	}
}