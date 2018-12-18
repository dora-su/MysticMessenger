/* [Message.java]
 * Formats messages as a JTextPane, allows easy display of messages
 * @author Dora Su
 * 14 December 2018
 */

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message extends JTextPane {

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); //DateTimeFormatter to Display Time/Date in a certain format
    private LocalDateTime now; //Variable for receiving the time

    /**
     * Constructor for a regular message
     *
     * @param name    username of the person who sent the message
     * @param message message text
     * @param isSelf  boolean to check if the message was sent by the client him/herself, used for display differences
     */
    public Message(String name, String message, boolean isSelf) {
        this.setBorder(new EmptyBorder(7, 5, 7, 5)); //Padding for the message
        this.setForeground(new Color(70, 70, 70)); //Set text colour
        this.setOpaque(true);
        this.setEditable(false); //prevents editing of text
        this.setContentType("text/html"); //Uses HTML formatting

        //Gets the time the message is created
        now = LocalDateTime.now();

        //Set the text to the message with a line break that shows the sender & date/time on the next line
        this.setText("<html><font size=+1>" + message + "</font><br><b><font size=-1>" + name + "</b> @ " + "<i>" + dtf.format(now) + "</i></html>");

        //Checks if message was sent by the client that message is displayed
        if (isSelf) {
            //Set colour to light blue if it was a message sent by themself
            this.setBackground(new Color(190, 232, 232));
        } else {
            //Set colour to darker blue if it was a message sent by another user
            this.setBackground(new Color(176, 224, 230));
        }

        this.setSize(425,10);
//        this.setPreferredSize(new Dimension(425,this.getPreferredSize().height));
        this.setVisible(true);
    }

    /**
     * Constructor for an error message from server
     *
     * @param error error message
     */
    public Message(String error) {
        this.setBorder(new EmptyBorder(7, 5, 7, 5)); //Padding for the message
        this.setForeground(new Color(70, 70, 70)); //Set text colour
        this.setBackground(new Color(249, 200, 199)); //Set the background to a soft red colour
        this.setOpaque(true);
        this.setEditable(false); //prevents editing of text
        this.setContentType("text/html"); //Uses HTML formatting

        //Gets the time the message is created
        now = LocalDateTime.now();

        //Sets text to the error message followed by the time of the error on the next line
        this.setText("<html><font size=-1>" + error + "</font><br><font size=-1>" + "<i>" + dtf.format(now) + "</i></html>");
        this.setVisible(true);
    }

}
