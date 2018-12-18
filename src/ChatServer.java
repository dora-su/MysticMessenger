/*
    ChatServer.java
    Version: 2.0
    Author: Chris Xie
    Date: December 14, 2018
    Runs a server that clients can connect to
 */

//imports for network communication

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    ServerSocket serverSock;// server socket for connection
    static Boolean running = true;  // controls if the server is accepting clients
    private ArrayList<ConnectionHandler> clients = new ArrayList<ConnectionHandler>(); // list of all the clients

    /**
     * Main
     *
     * @param args parameters from command line
     */
    public static void main(String[] args) {
        new ChatServer().go(); //start the server
    }

    /*
     * Go
     * Starts the server
     */
    private void go() {
        System.out.println("Waiting for a client connection..");

        Socket client = null;//hold the client connection

        try {
            serverSock = new ServerSocket(5000);  //assigns an port to the server
            //serverSock.setSoTimeout(15000);  //15 second timeout
            while (running) {  //this loops to accept multiple clients
                client = serverSock.accept();  //wait for connection
                ConnectionHandler c = new ConnectionHandler(client, this);
                clients.add(c);
                System.out.println("Client connected");
                //Note: you might want to keep references to all clients if you plan to broadcast messages
                //Also: Queues are good tools to buffer incoming/outgoing messages
                Thread t = new Thread(c); //create a thread for the new client and pass in the socket
                t.start(); //start the new thread
            }
        } catch (Exception e) {
            // System.out.println("Error accepting connection");
            //close all and quit
            try {
                client.close();
            } catch (Exception e1) {
                System.out.println("Failed to close socket");
            }
            System.exit(-1);
        }
    }

    /*
     * Processes a message to determine if it is a command or a normal message, and sends it to clients accordingly
     *
     * @param msg  the message
     * @param name the name of the client who sent the message
     */
    private void handle(String msg, String name) {

        // private messages in form "/pm name message"
        if (msg.startsWith("msg ")) { // user sent text
            if (msg.substring(4).startsWith("/pm ")) {
                msg = msg.substring(4);
                String[] words = msg.split(" ");
                // receiver of private message
                String receiver = words[1];
                boolean sent = false;
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).getName().equals(receiver)) {
                        clients.get(i).send(name + " (private)", msg.substring(4 + receiver.length() + 1));
                        sent = true;
                    } else if (clients.get(i).getName().equals(name)) {
                        clients.get(i).sentPM(receiver, msg.substring(4 + receiver.length() + 1));
                    }
                }
                if (!sent) {
                    for (int i = 0; i < clients.size(); i++) {
                        if (clients.get(i).getName().equals(name)) {
                            clients.get(i).sendError("Username not found.");
                        }
                    }
                }
            } else {
                // standard messages
                for (int i = 0; i < clients.size(); i++) {
                    //make sure they have username
                    if (clients.get(i).getName() != null) {
                        clients.get(i).send(name, msg.substring(4));
                    }
                }
            }
        } else if (msg.startsWith("client ")) { // list of online clients
            System.out.println(msg);
            if (msg.charAt(7) == 'r') {
                String removeName = msg.substring(14);
                for (ConnectionHandler i : clients) {
                    if (i.getName().equals(removeName)) {
                        clients.remove(i);
                    }
                }
            }
            clientList();
        }
    }

    private void clientList() {
        String[] names = new String[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            names[i] = clients.get(i).getName();
        }
        for (ConnectionHandler i : clients) {
            i.sendClient(names);
        }
    }

    /*
     * Determines whether a username has been used already or not
     * @param name the name
     * @return true, if the name has not been taken or false if it has
     */
    private boolean usableName(String name) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getName() != null) {
                if (clients.get(i).getName().equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    //***** Inner class - thread for client connection
    class ConnectionHandler implements Runnable {
        private PrintWriter output; //assign printwriter to network stream
        private BufferedReader input; //Stream for network input
        private Socket client;  //keeps track of the client socket
        private boolean running;
        private ChatServer server;
        private String name;

        /* ConnectionHandler
         * Constructor
         * @param the socket belonging to this client connection
         */
        ConnectionHandler(Socket s, ChatServer server) {
            this.client = s;  //constructor assigns client to this
            this.server = server;
            try {  //assign all connections to client
                this.output = new PrintWriter(client.getOutputStream());
                InputStreamReader stream = new InputStreamReader(client.getInputStream());
                this.input = new BufferedReader(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            running = true;
        } //end of constructor

        /*
         * Gets the name of the client
         * @return the name of the client
         */
        private String getName() {
            return name;
        }

        /**
         * run
         * executed on start of thread
         */
        public void run() {

            //Get a message from the client
            String msg = "";

            // get the name of the client
            boolean nameSet = false;
            while (!nameSet) {
                try {
                    if (input.ready()) {
                        String name = input.readLine().substring(9);
                        if (usableName(name)) {
                            this.name = name;
                            nameSet = true;
                            output.println("valid");
                            output.flush();
                        } else {
                            output.println("Username has been taken.");
                            output.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name);
            server.handle("client add " + name, name);
            //Get a message from the client
            while (running) {  // loop unit a message is received

                try {
                    if (input.ready()) { //check for an incoming messge
                        msg = input.readLine();  //get a message from the client
                        if (msg.equals("client leave")) {
                            running = false;
                        } else {
                            server.handle(msg, this.name);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Failed to receive msg from the client");
                    e.printStackTrace();
                }
            }

            //close the socket
            try {
                input.close();
                output.close();
                client.close();
                server.handle("client remove " + name, name);
            } catch (Exception e) {
                System.out.println("Failed to close socket");
            }
        } // end of run()

        /*
         * Sends a message back to the client
         * @param name the name of the sender
         * @param msg the message sent
         */
        private void send(String name, String msg) {
            // other people sent the message
            output.println(name + ": " + msg);
//            output.println(name);
//            output.println(msg);
            output.flush();
        }

        /*
         * Sends an error to the client
         * @param error the error
         */
        private void sendError(String error) {
            output.println("error " + error);
            output.flush();
        }

        /*
         * Sends a private message to the client
         * @param receiver the receiver of the message
         * @param msg the message sent
         */
        private void sentPM(String receiver, String msg) {
            output.println(getName() + " to " + receiver + " (private) : " + msg);
            output.flush();
        }

        /*
         * Sends the client a client add or client remove
         * @param msg the message
         */
        private void sendClient(String[] names) {
            String msg = "client ";
            for (int i = 0; i < names.length; i++) {
                msg += names[i] + " ";
            }
            output.println(msg);
            output.flush();
        }
    } //end of inner class
} //end of Class