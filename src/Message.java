import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message extends JTextPane{
  private  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
  private LocalDateTime now ;

       public Message (String name, String message, boolean isSelf) {
           this.setContentType("text/html");

           now = LocalDateTime.now();
           this.setBorder(new EmptyBorder(10,5,10,5));
           this.setText("<html><font size=+1>" + message + "</font><br><p><b><font size=-1>" + name + "</b> @ " + "<i>" + dtf.format(now) + "</i><br></p></html>");

           this.setForeground(new Color(70, 70, 70));



           if (isSelf) {
               this.setBackground(new Color(190, 232, 232));
           } else {
               this.setBackground(new Color(176, 224, 230));
           }
           this.setOpaque(true);
           this.setEditable(false);
       }


    public Message (String error) {
        this.setContentType("text/html");

        error = error.substring(6);
        now = LocalDateTime.now();
        this.setBorder(new EmptyBorder(10,5,10,5));
        this.setText("<html><font size=-1>" + error + "</font><br><p><font size=-1>" +  "<i>" + dtf.format(now) + "</i><br></p></html>");
        this.setBackground(new Color(249, 200, 199));
        this.setForeground(new Color(70, 70, 70));

        this.setOpaque(true);
        this.setEditable(false);
    }

}
