/* [ChatClient.java]
 * Simple chat client
 * @author Dora Su & Chris Xie
 * @ version 1.0a
 */

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient {
    //Server Variables
    private Socket mySocket; //socket for connection
    private BufferedReader input; //reader for network stream
    private PrintWriter output;  //printwriter for network output
    private boolean running = true; //thread status via boolean
    private boolean loggedIn = false;

    private String name = ""; //username

    //chat window components
    //JButtons
    private JButton sendButton;
    //JTextFields
    private JTextField typeField;
    //JScrollPanes
    private JScrollPane scrollPane;//for scrolling through messages
    //JPanels
    private JPanel panel; //main panel
    private JPanel msgArea;//panel messages are displayed on
    //JLists and ArrayLists
    private JList online;
    private DefaultListModel<String> onlineList;
    //JFrames
    private JFrame window; //frame for displaying everything

    //logon option pane
    int option; //clicked result from the JOptionPane
    //JLabels
    private JLabel welcome;
    private JLabel username;
    private JLabel ip;
    private JLabel port;
    private JLabel errorMessage; //dynamic to display errors upon clicking the login button
    //JTextFields
    private JTextField usernameT;
    private JTextField ipT;
    private JTextField portT;

    //Icons
    private ImageIcon icon;
    private ImageIcon sendIcon;

    /**
     * main method to start and create the client
     *
     * @param args
     */
    public static void main(String[] args) {
        new ChatClient().go();
    }

    /**
     * Runs when client starts, creates all the windows
     */
    public void go() {
        //initialize and customize the frames

        //Chat Window Frame
        window = new JFrame("Mystic Messenger");
        window.setMinimumSize(new Dimension(625, 400));
        window.setBackground(new Color(70, 70, 70));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); //centers the frame on the screen
        window.getRootPane().setDefaultButton(sendButton); //allows send to be automatically clicked when enter is pressed
        //set icon image
        icon = new ImageIcon("dependencies/Icon.png");
        window.setIconImage(icon.getImage());

        //Main Chat Window Panel
        panel = new JPanel();
        panel.setBackground(new Color(70, 70, 70));
        //Uses GridBagLayout with GridBagConstraints
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Message Display Area
        //create a new JPanel to display messages
        msgArea = new JPanel();
        msgArea.setLayout(new BoxLayout(msgArea, BoxLayout.PAGE_AXIS)); //uses BoxLayout that displays every component on a new row
        //add the Message Area Panel to a Scroll Pane
        msgArea.setVisible(true);
        scrollPane = new JScrollPane(msgArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(new Dimension(425, 300)); //makes sure this does not collapse when there are no messages
        //GridBagConstraints
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 10, 2, 0);
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        //add it to the main panel
        panel.add(scrollPane, c);

        //Type Field for Sending Messages
        typeField = new JTextField();
        typeField.setForeground(new Color(250, 250, 250)); //set text to white for visibility
        typeField.setOpaque(false); //transparent
        typeField.setMinimumSize(new Dimension(350, 20)); //prevents typeField from becoming a slit
        typeField.addKeyListener(new TextFieldListener());
        //GridBagConstraints
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 5, 0);
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(typeField, c); //add to main panel


        //Send Message Button
        //Set Icon Image
        sendIcon = new ImageIcon("dependencies/send_icon.png");
        sendButton = new JButton(sendIcon);
        sendButton.addActionListener(new SendButtonListener()); //add button listener for sending messages
        sendButton.setBorder(null);
        sendButton.setContentAreaFilled(false);
        sendButton.setMaximumSize(new Dimension(80, 80)); //prevents button space from becoming too big when the window is expanded
        //GridBagConstraints
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 3;
        c.gridy = 4;
        panel.add(sendButton, c); //add to main panel

        //List of Active/Online Users
        onlineList = new DefaultListModel<>(); //create a default list model: array of usernames of those online
        online = new JList(onlineList); //create a JList that displays the onlineList
//        online.setFont(new Font(null,welcome));
        online.setMinimumSize(new Dimension(200, 400)); //Maintains size even if there is no one online, prevents collapse
        //GridBagConstraints
        c.insets = new Insets(0, 10, 0, 0);
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 4;
        panel.add(online, c); //add to main panel

        //Welcome JLabel
        welcome = new JLabel("Welcome!");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setBounds(30, 20, 330, 60);

        //Username JLabel
        username = new JLabel("Username: ");
        username.setBorder(null);
        username.setBounds(30, 90, (int) username.getPreferredSize().getWidth(), 30);

        //Username JTextField
        usernameT = new JTextField();
        usernameT.setBounds(110, 90, 260, 30);

        //IP Address JLabel
        ip = new JLabel("IP Address: ");
        ip.setBorder(null);
        ip.setBounds(30, 130, (int) ip.getPreferredSize().getWidth(), 30);

        //IP Address JTextField
        ipT = new JTextField();
        ipT.setBounds(110, 130, 260, 30);

        //Port JLabel
        port = new JLabel("Port: ");
        port.setBorder(null);
        port.setBounds(30, 170, (int) ip.getPreferredSize().getWidth(), 30);

        //Port JTextField
        portT = new JTextField();
        portT.setBounds(110, 170, 260, 30);

        //Error Message JLabel
        errorMessage = new JLabel(""); //initially set to no text since there is no error yet
        errorMessage.setBounds(30, 260, 340, 40);
        errorMessage.setForeground(Color.red);

//        UIManager.put("OptionPane.minimumSize", new Dimension(300, 250));

        UIManager.put("OptionPane.minimumSize", new Dimension(530, 250));
        Object[] fields = {
                welcome,
                username, usernameT,
                ip, ipT,
                port, portT,
                errorMessage
        };
        Object[] buttons = {"Quit", "Let's Go"};

        while (!loggedIn) { //as long as they have not logged in yet, keep creating the option pane
            option = JOptionPane.showOptionDialog(window, fields, "Mystic Messenger", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, icon, buttons, null);
            if ((option == JOptionPane.YES_OPTION) || (option == JOptionPane.CLOSED_OPTION)) {
                System.exit(0);
            } else {
                if ((usernameT.getText().length() == 0) || (ipT.getText().length() == 0) || (portT.getText().length() == 0)) {
                    errorMessage.setText("Fields cannot be blank. Please fill in the missing fields.");
                }

                //Other possible error is that the username has been already taken
                else if (!connect(usernameT.getText(), ipT.getText(), Integer.parseInt(portT.getText()))) {
                    errorMessage.setText("Username is already taken. Please enter a new username.");
                    usernameT.setText("");
                } else {
                    //No Error:
                    //Add the main chat window panel to the frame
                    window.add(panel);
                    window.setVisible(true); //makes main window visible
                    loggedIn = true;
                }
            }
        }
        readMessagesFromServer(); //Starts to wait for messsages from the server
    }

    /**
     * Attempts to connect to the server and creates the socket and streams
     *
     * @param username Username of the client
     * @param ip       IP Address used to connect to the socket
     * @param port     Port used to connect to the socket
     * @return true if successfully connected, false if username is not valid
     */
    private boolean connect(String username, String ip, int port) {
        System.out.println("Attempting to make a connection..");

        try {
            //If username is blank, return false
            if (username.length() == 0) {
                return false;
            }

            mySocket = new Socket(ip, port); //attempt socket connection (local address). This will wait until a connection is made

            InputStreamReader stream1 = new InputStreamReader(mySocket.getInputStream()); //Stream for network input
            input = new BufferedReader(stream1);
            output = new PrintWriter(mySocket.getOutputStream()); //assign printwriter to network stream

            //Send username
            System.out.println("username + " + username);
            output.println("username " + username);
            output.flush();
            try {
                //Check if the username is valid and not in use
                boolean nameValid = false;
                while (!nameValid) {
                    if (input.ready()) {
                        String msg = input.readLine();
                        //If the message is not valid, return false
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
            errorMessage.setText("Connection failed.");
            e.printStackTrace();
        }

        System.out.println("Connection made.");
        return true;
    }

    //Starts a loop waiting for server input and then displays it on the textArea
    public void readMessagesFromServer() {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.out.println("left");
                output.println("client leave");
                output.flush();
                running = false;
            }
        });
        while (running) {  // loop unit a message is received
            try {
                if (input.ready()) { //check for an incoming messge
                    String msg = input.readLine();

                    if (msg.startsWith("client ")) {
                        System.out.println(msg);
                        if (msg.substring(7).startsWith("add ")) {
                            String name = msg.substring(11); // name of client
                            onlineList.addElement(name);
                            System.out.println(name + " add");
                            // add to side panel
                        } else {
                            String name = msg.substring(14); // name of client that left
                            System.out.println(name);
                            onlineList.removeElement(name);
                            //??
                            // remove this client from side panel
                            //
                            //
                        }
                    } else if (msg.startsWith("error ")) {
                        msgArea.add(new Message(msg.substring(6)));
                    } else {
                        //Actual message
                        System.out.println(msg);
                        String[] tokens = msg.split(": ");
                        String msgName = tokens[0];
                        String message = tokens[1];
                        //Add message to message area
                        msgArea.add(new Message(msgName, message, msgName.equals(this.name)));
                    }
                    msgArea.revalidate(); //updates the Message area to show new messages
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
        } catch (Exception e1) {
            System.out.println("Failed to close socket");
        }
        window.dispose(); //close window

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
    // textfield - when enter is clicked in the textfield, it will do the same thing as clicking the send button, makes sending messages easier
    class TextFieldListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER){
                //Send a message to the client
                output.println("msg " + typeField.getText());
                output.flush();
                typeField.setText("");
            }
        }
    }
}