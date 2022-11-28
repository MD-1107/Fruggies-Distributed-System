import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;


public class LoadBalancing
{
    HashMap <Integer, Boolean> portStatus=new HashMap<>();
    // int request_no;
    //  int port_request_no;

    //  void initialize()
    //  {
    //     portStatus.put(1000, false);
    //     portStatus.put(1001, false);
    //     portStatus.put(1002, false);
    //     portStatus.put(1003, false);

    //  }


    String getHost(int request_no)
    {
        String host=" ";
        System.out.println("The value of request no is: "+request_no);

        int case_no=request_no%4; //number of requests taken as four

        switch(case_no)
        {
            case 0: host="rmi://localhost:1000//Server";
            System.out.println("The client is connected to server");
            // portSet(case_no);
            break;

            case 1: host="rmi://localhost:1001//Server1";
            System.out.println("The client is connected to server1");
            // portSet(case_no);
            break;

            case 2: host="rmi://localhost:1002//Server2";
            System.out.println("The client is connected to server2");
            // portSet(case_no);
            break;

            default: host="rmi://localhost:1000//Server";
            System.out.println("The client is connected to server");
            // portSet(case_no);
            break;
        }
        
        request_no++;

        return host;
    }


    int portSet(int port_request_no)
    {
        int num=0;
        int case_no=port_request_no%3; //number of requests taken as four

        switch(case_no)
        {
            case 0: num=5050;
            System.out.println("The client is connected to server");
            // portSet(case_no);
            break;

            case 1: num=5051;
            System.out.println("The client is connected to server1");
            // portSet(case_no);
            break;

            case 2: num=5052;
            System.out.println("The client is connected to server2");
            // portSet(case_no);
            break;

            default: num=5050;
            System.out.println("The client is connected to server");
            // portSet(case_no);
            break;
        }
        
        port_request_no++;

        return num;
    }
    

    
}
