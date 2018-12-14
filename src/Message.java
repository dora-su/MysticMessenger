import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message extends JPanel {
  private  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
  private LocalDateTime now ;
  private JLabel msg;
  private JLabel nameDate;
  private String message, name;

       public Message (String name, String message){
           this.name = name;
           this.message = message;
           this.setLayout(new BorderLayout());
           now = LocalDateTime.now();

           msg = new JLabel("<html>"+message+"<br><br></html>");
           msg.setFont(new Font("Aileron", Font.PLAIN, 16));
           msg.setBorder(null);
           msg.setBackground(null);
           msg.setOpaque(false);
           this.add(msg, BorderLayout.NORTH);

           nameDate = new JLabel("<html><b>"+name + "</b>" + "@" +dtf.format(now)+"</html>");
           nameDate.setForeground(new Color(70,70,70));
           nameDate.setBackground(null);
           nameDate.setOpaque(false);
           nameDate.setBorder(null);
                   nameDate.setHorizontalAlignment(SwingConstants.RIGHT);
                   this.add(nameDate,BorderLayout.SOUTH);
                   this.setBackground(new Color(190, 232, 232));
this.setVisible(true);

       }

    public Message (String message){
        this.setLayout(new BorderLayout());
        now = LocalDateTime.now();

        msg = new JLabel("<html>"+message+"<br><br></html>");
        msg.setFont(new Font("Aileron", Font.PLAIN, 16));
        msg.setBorder(null);
        msg.setBackground(null);
        msg.setOpaque(false);
        this.add(msg, BorderLayout.NORTH);

        nameDate = new JLabel("<html>"  +dtf.format(now)+"</html>");
        nameDate.setForeground(new Color(70,70,70));
        nameDate.setBackground(null);
        nameDate.setOpaque(false);
        nameDate.setBorder(null);
        nameDate.setHorizontalAlignment(SwingConstants.RIGHT);
        this.add(nameDate,BorderLayout.SOUTH);
        this.setBackground(new Color(176, 224, 230));
        this.setVisible(true);

    }


    public String toString(){
           return message + "\n" + name +" @ " + dtf.format(now);
       }


}
