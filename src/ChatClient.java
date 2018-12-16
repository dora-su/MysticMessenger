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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient {

    private JButton sendButton, clearButton;
    private JTextField typeField;
    // private JTextArea msgArea;
    private JScrollPane scrollPane;
    private JPanel msgArea;
    private JList online;
    private ArrayList<String> onlineList;
    private JPanel southPanel;
    private Socket mySocket; //socket for connection
    private BufferedReader input; //reader for network stream
    private PrintWriter output;  //printwriter for network output
    private boolean running = true; //thread status via boolean
    private JFrame window;
    private String name = "woah";

    private JDialog logon;
    private JLabel welcome;
    private JButton login;
    private JLabel username;
    private JLabel ip;
    private JLabel port;
    private JTextField usernameT;
    private JTextField ipT;
    private JTextField portT;
    private JLabel errorMessage;
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

        GridBagConstraints c = new GridBagConstraints();
        window = new JFrame("Mystic Messenger");
        window.setBackground(new Color(70, 70, 70));
        //set icon image

        window.setIconImage(icon.getImage());
        window.setMinimumSize(new Dimension(625, 400));

        southPanel = new JPanel();
        southPanel.setBackground(new Color(70, 70, 70));
        southPanel.setLayout(new GridBagLayout());

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // create buttons
        ImageIcon sendIcon = new ImageIcon("dependencies/send_icon.png");
        msgArea = new JPanel();
        msgArea.setLayout(new BoxLayout(msgArea, BoxLayout.PAGE_AXIS));

        scrollPane = new JScrollPane(msgArea);
//        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // scrollPane.setPreferredSize(new Dimension(370,500));
        scrollPane.setMinimumSize(new Dimension(425, 300));
//        c.ipady = 500;
//        c.ipadx = 370;
        c.fill = GridBagConstraints.BOTH;
        //c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 10, 2, 0);
        c.gridx = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.gridy = 3;
        southPanel.add(scrollPane, c);

        // make message areas
        typeField = new JTextField(90);
        typeField.setForeground(new Color(250,250,250));
        typeField.setOpaque(false);
        typeField.setMinimumSize(new Dimension(350, 20));
        c.insets = new Insets(5, 10, 5, 0);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.0;
        c.anchor = (GridBagConstraints.CENTER);
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        southPanel.add(typeField, c);

        sendButton = new JButton(sendIcon);
        sendButton.addActionListener(new SendButtonListener());
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setMaximumSize(new Dimension(60, 60));
        c.anchor = (GridBagConstraints.LAST_LINE_END);
        c.weightx = 0.0;
        c.gridx = 3;
        c.gridy = 4;
        southPanel.add(sendButton, c);

        //implement online list
        onlineList = new ArrayList<>();
        online = new JList(onlineList.toArray());
        //online.setSelectionMode();
        online.setMinimumSize(new Dimension(200, 400));
        c.insets = new Insets(0, 10, 0, 0);
        c.gridx = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridheight = 4;
        c.fill = GridBagConstraints.BOTH;
        southPanel.add(online, c);
//         make client window
        logon = new JDialog(window,"Mystic Messenger");
        logon.setResizable(false);
//        logon.setBackground(new Color(70,70,70));
        logon.setIconImage(icon.getImage());
        logon.setSize(400, 380);

        logon.setLayout(null);

        welcome = new JLabel("Welcome!");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setBounds(30, 20, 330, 60);
        welcome.setFont(new Font("Aileron", 0, 30));
        logon.add(welcome);

        username = new JLabel("Nickname: ");
        tempWidth = (int) username.getPreferredSize().getWidth();
        username.setBorder(null);
        username.setBounds(30, 90, tempWidth, 30);
        logon.add(username);


        usernameT = new JTextField();
        usernameT.setBounds(110, 90, 260, 30);
        logon.add(usernameT);

        ip = new JLabel("IP Address: ");
        tempWidth = (int) ip.getPreferredSize().getWidth();
        ip.setBorder(null);
        ip.setBounds(30, 130, tempWidth, 30);
        logon.add(ip);


        ipT = new JTextField();
        ipT.setBounds(110, 130, 260, 30);
        logon.add(ipT);

        port = new JLabel("Port: ");
        tempWidth = (int) port.getPreferredSize().getWidth();
        port.setBorder(null);
        port.setBounds(30, 170, tempWidth, 30);
        logon.add(port);

        portT = new JTextField();
        portT.setBounds(110, 170, 260, 30);
        logon.add(portT);

        errorMessage = new JLabel("");
        errorMessage.setBounds(30, 260, 340, 40);
        errorMessage.setForeground(Color.red);

        login = new JButton("Let's Go!");
        tempWidth = (int) login.getPreferredSize().getWidth();
        login.setBounds(30, 220, 340, 40);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //somehow check name
                if ((usernameT.getText().length() == 0) || (ipT.getText().length() == 0) || (portT.getText().length() == 0)) {
                    errorMessage.setText("Fields cannot be blank. Please fill in the missing fields.");
                }
                // boolean ugly = connect(usernameT.getText(), ipT.getText(), Integer.parseInt(portT.getText()));
                //System.out.println(ugly);
                if (!connect(usernameT.getText(), ipT.getText(), Integer.parseInt(portT.getText()))) {
                    errorMessage.setText("Username is already taken. Please enter a new username.");
                    usernameT.setText("");
                }
                name = usernameT.getText();
                System.out.println(name);
                logon.dispose();
                readMessagesFromServer();
                window.setEnabled(true);
            }
        });
        errorMessage.setVisible(true);
        logon.add(errorMessage);
        logon.add(login);

        //logon.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        logon.addWindowListener(new LoginCloseListener());
        logon.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        logon.setFocusable(true);
        logon.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);

        window.setLocationRelativeTo(null);
        logon.setLocation(window.getX()+(window.getWidth()-logon.getWidth())/2,window.getY()+(window.getHeight()-logon.getHeight())/2);
       logon.setVisible(true);

        window.add(southPanel);

        window.setVisible(true);
        window.setEnabled(false);
    }

    //Attempts to connect to the server and creates the socket and streams
    private boolean connect(String username, String ip, int port) {
        System.out.println("Attempting to make a connection..");

        try {
            if (username.length() == 0) {
                return false;
            }
            mySocket = new Socket(ip, port); //attempt socket connection (local address). This will wait until a connection is made

            InputStreamReader stream1 = new InputStreamReader(mySocket.getInputStream()); //Stream for network input
            input = new BufferedReader(stream1);
            output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream

            // send username
            System.out.println("username + " + username);
            output.println("username " + username);
            output.flush();
            try {
                boolean nameValid = false;
                while (!nameValid) {
                    if (input.ready()) {
                        String msg = input.readLine();
                        // System.out.println("ready");
                        if (!msg.equals("valid")) {
                            return false;
                        } else {
                            nameValid = true;
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {  //connection error occured
            System.out.println("Connection to Server Failed");
            e.printStackTrace();
        }

        System.out.println("Connection made.");
        return true;
    }

    //Starts a loop waiting for server input and then displays it on the textArea
    public void readMessagesFromServer() {

        while (running) {  // loop unit a message is received
            try {
                if (input.ready()) { //check for an incoming messge
                    String msg = input.readLine();

                    if (msg.startsWith("client ")) {
                        if (msg.substring(7).startsWith("add ")) {
                            String name = msg.substring(11);
                            // add to side panel
                        } else {
                            String name = msg.substring(17);
                            // remove this client from side panel
                            //
                        }
                    } else if (msg.startsWith("error ")) {
                        msgArea.add(new Message(msg.substring(6)));
                    } else {
                        msg = msg.substring(4);
                        String[] tokens = msg.split(": ");
                        String msgName = tokens[0];
                        String message = tokens[1];
                        msgArea.add(new Message(msgName, message, msgName.equals(this.name)));
                    }

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
            output.println("msg " + typeField.getText());
            output.flush();
            typeField.setText("");
        }
    }

    // QuitButtonListener - Quit the program
    class LoginCloseListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {

        }

        @Override
        public void windowClosed(WindowEvent e) {
            window.dispose();
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }


}