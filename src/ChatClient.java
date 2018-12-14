/* [ChatClient.java]
 * A not-so-pretty implementation of a basic chat client
 * @author Mangat
 * @ version 1.0a
 */

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    private JButton sendButton, clearButton;
    private JTextField typeField;
   // private JTextArea msgArea;
    private JScrollPane scrollPane;
    private JList<JLabel> msgArea = new JList<>();
    private JPanel southPanel;
    private Socket mySocket; //socket for connection
    private BufferedReader input; //reader for network stream
    private PrintWriter output;  //printwriter for network output
    private boolean running = true; //thread status via boolean
    private JFrame window;


    private JFrame logon;
    private JPanel logonScreen;
    private JLabel welcome;
    private JButton login;
    private JLabel username;
    private JLabel ip;
    private JLabel port;
    private JTextField usernameT;
    private JTextField ipT;
    private JTextField portT;
    ImageIcon icon = new ImageIcon("dependencies/Icon.png");
    private int tempWidth;

private DefaultListModel msgs = new DefaultListModel();
    public static void main(String[] args) {

        new ChatClient().go();
    }

    /**
     * Runs when client starts
     */
    public void go() {
        // make client window
        GridBagConstraints c = new GridBagConstraints();
        logon = new JFrame("Mystic Messenger");
        logon.setResizable(false);
//        logon.setBackground(new Color(70,70,70));
        logon.setIconImage(icon.getImage());
        logon.setSize(400,350);
        logonScreen = new JPanel();


        logonScreen.setLayout(null);

        welcome = new JLabel("Welcome!");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setBounds(30,20, 330, 60);
        welcome.setFont(new Font("Aileron", 0, 30));
        logonScreen.add(welcome);

        username = new JLabel("Nickname: ");
        tempWidth = (int) username.getPreferredSize().getWidth();
        username.setBorder(null);
        username.setBounds(30, 90, tempWidth,30);
        logonScreen.add(username);


        usernameT = new JTextField();
        usernameT.setBounds(110, 90, 260, 30);
        logonScreen.add(usernameT);

        ip = new JLabel("IP Address: ");
        tempWidth = (int) ip.getPreferredSize().getWidth();
        ip.setBorder(null);
        ip.setBounds(30, 130, tempWidth,30);
        logonScreen.add(ip);


        ipT = new JTextField();
        ipT.setBounds(110, 130, 260,30);
        logonScreen.add(ipT);

        port = new JLabel("Port: ");
        tempWidth = (int) port.getPreferredSize().getWidth();
        port.setBorder(null);
        port.setBounds(30, 170, tempWidth,30);
        logonScreen.add(port);

        portT = new JTextField();
        portT.setBounds(110, 170, 260,30);
        logonScreen.add(portT);

        login = new JButton("Let's Go!");
        tempWidth = (int) login.getPreferredSize().getWidth();
        login.setBounds(30, 220, 340, 40);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        logonScreen.add(login);

        logon.add(logonScreen);

        logon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logon.setFocusable(true);
        logon.setVisible(true);

//
//
//        window = new JFrame("Mystic Messenger");
//        window.setBackground(new Color(70,70,70));
//        //set icon image
//
//        window.setIconImage(icon.getImage());
//        window.setMinimumSize(new Dimension(425,700));
//
//        southPanel = new JPanel();
//        southPanel.setBackground(new Color(70,70,70));
//        southPanel.setLayout(new GridBagLayout());
//
//
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        // create buttons
//        ImageIcon sendIcon = new ImageIcon("dependencies/send_icon.png");
//
//
//       // msgArea = new JList<>();
//        msgArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        msgArea.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//        msgArea.setVisibleRowCount(-1);
//        msgArea = new JList<>(msgs);
//        scrollPane = new JScrollPane(msgArea);
////        scrollPane.setOpaque(false);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//       // scrollPane.setPreferredSize(new Dimension(370,500));
//        scrollPane.setMinimumSize(new Dimension(425,600));
////        c.ipady = 500;
////        c.ipadx = 370;
//        c.fill = GridBagConstraints.BOTH;
//        //c.anchor = GridBagConstraints.LINE_START;
//        c.insets = new Insets(0,10,2,0);
//        c.gridx = 1;
//        c.weightx = 1.0;
//        c.gridwidth=2;
//        c.gridy=3;
//        southPanel.add(scrollPane,c);
//
//        // make message areas
//        typeField = new JTextField(90);
//        typeField.setOpaque(false);
//        typeField.setMinimumSize(new Dimension(350,30));
//        c.insets = new Insets(5,10,5,0);
//        c.weightx = 1.0;
//        c.weighty = 1.0;
//        c.anchor = (GridBagConstraints.WEST);
//        c.gridx = 1;
//        c.gridy = 4;
//        c.gridwidth  = 1;
//        southPanel.add(typeField,c);
//
//        sendButton = new JButton(sendIcon);
//        sendButton.addActionListener(new SendButtonListener());
//        sendButton.setBorderPainted(false);
//        sendButton.setContentAreaFilled(false);
//        sendButton.setMaximumSize(new Dimension(60,60));
//      c.anchor = (GridBagConstraints.LAST_LINE_END);
//        c.gridx = 2;
//        c.gridy=4;
//        southPanel.add(sendButton,c);
//
////
////        clearButton = new JButton("QUIT");
////        clearButton.addActionListener(new QuitButtonListener());
//
//        JLabel errorLabel = new JLabel("");
//
//
//
//
//        // add to panel
//
//       // southPanel.add(errorLabel);
//        //southPanel.add(clearButton);
//
////        Message message = new Message("ugly","gul");
////        southPanel.add(message);
//        // set window layouts
//        window.add(southPanel);
//
//        // set window size
//        window.setSize(350, 700);
//        window.setVisible(true);
//
//        // call a method that connects to the server
//        conecnt("127.0.0.1", 5000);
//        // after connecting loop and keep appending[.append()] to the JTextArea
//
//        readMessagesFromServer();
    }

    //Attempts to connect to the server and creates the socket and streams
    public Socket connect(String ip, int port) {
        System.out.println("Attempting to make a connection..");

        try {
            mySocket = new Socket(ip, port); //attempt socket connection (local address). This will wait until a connection is made

            InputStreamReader stream1 = new InputStreamReader(mySocket.getInputStream()); //Stream for network input
            input = new BufferedReader(stream1);
            output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream

        } catch (IOException e) {  //connection error occured
            System.out.println("Connection to Server Failed");
            e.printStackTrace();
        }

        System.out.println("Connection made.");
        return mySocket;
    }

    //Starts a loop waiting for server input and then displays it on the textArea
    public void readMessagesFromServer() {

        while (running) {  // loop unit a message is received
            try {

                if (input.ready()) { //check for an incoming messge

                    String msg, name;
                    name = input.readLine();
                    msg = input.readLine();
                    msgs.addElement(new JLabel(msg));
                    msgs.addElement(new JLabel(name));
                     //read the message
                   // msgArea.append(msg + "\n");
                }

            } catch (IOException e) {
                System.out.println("Failed to receive msg from the server");
                e.printStackTrace();
            }
        }
        try {  //after leaving the main loop we need to close all the sockets
            input.close();
            output.close();
            mySocket.close();
        } catch (Exception e) {
            System.out.println("Failed to close socket");
        }
        window.dispose();
    }
    //****** Inner Classes for Action Listeners ****

    // send - send msg to server (also flush), then clear the JTextField
    class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            //Send a message to the client
            output.println(typeField.getText());
            output.flush();
            typeField.setText("");
        }
    }

    // QuitButtonListener - Quit the program
    class QuitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            running = false;
        }
    }


}