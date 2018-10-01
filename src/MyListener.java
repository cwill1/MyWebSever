import java.io.*;
import java.net.*;


public class MyListener {

    public static void main(String a []) throws IOException{
        int q_len = 6;
        // define a destination for port connection
        int portNumber = 2000;
        Socket serverSocket;
        //create a ServerSocket to listen for incoming requests

        ServerSocket serverSocketListener = new ServerSocket(portNumber, q_len);

        //create a while loop to continously check for servsock incoming requests /connections and accept them
        //once created assign a new Assistant thread to deal with them
        //it essentially just listens for connections and then spawns a Assistant thread to go and deal with it

        System.out.println
                ("Clark Elliot's Inet server 1.8 starting up, listening at port 2000.\n");
        while(true){
            serverSocket = serverSocketListener.accept();
            new Assistant(serverSocket).start();
        }
    }
}


class Assistant extends Thread {
    //create a socket to open a communication stream between Client and Server
    Socket connectionSocket;
    //create a Assistant socket apart from the main thread to delegate work to be done
    //use a constructor and assign a socket to
    Assistant (Socket s) {connectionSocket = s;}

    public void run(){
        //declare a PrintStream for output support
        PrintStream output = null;
        //declare a BufferedReader to get characters typed in
        BufferedReader in = null;
        try{
            //print output from output and read in input from in
            in = new BufferedReader


                    (new InputStreamReader(connectionSocket.getInputStream()));

            //create a print stream to print out information that is sent from the client

            output = new PrintStream(connectionSocket.getOutputStream());
            //set up a reponse mechanism for when the client reaches out to this server.
            //read in the input from the client machine and print out "Looking up" with the information from the client machine
            //if there is an error in the read or write process catch it and handle it

            try{
                String hostname;
                hostname = in.readLine();
                System.out.println("Looking up: " + hostname);
                //send name and output stream to scanHostAddress
                scanHostAddress(hostname, output);

            }catch(IOException x){
                System.out.println("Server read error");
                x.printStackTrace();
            }

            //close the communication between the server and the client
            connectionSocket.close();

            //close the Input/Output operation if there is a failure


        }catch (IOException ioe){System.out.println(ioe);}

        //catch and print the input/output exception if there is one
    }

    //accept a hostname or an IP address and a PrintStream as a parameter
    //print out the hostname or IPaddress once it recieves the communication from the client
    //if the results from the remote address is found.
    //if the name is not found throw an exception

    static void scanHostAddress(String name, PrintStream out){
        try{
            out.println("Looking up hostname or IP address: " + name + "...");
            InetAddress machine = InetAddress.getByName(name);
            out.println("Host name: " + machine.getHostName());
            out.println("Host IP: " + convertByteTextToString (machine.getAddress()));

        }catch(UnknownHostException ex){
            out.println("Failed in atempt to look up: " + name);
        }
    }

    //read in an array of bytes. Iterate through the bytes using a 4 loop.
    //logical & the result to get the appropriate text we need.
    //convert the final result to string

    static String convertByteTextToString(byte ip[]){
        StringBuffer result = new StringBuffer ();
        for (int i = 0; i < ip.length; ++ i){
            if (i > 0) result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }
}
