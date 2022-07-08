import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;

public class ChatRoom extends JPanel{

  public static PrintWriter out;
  public JTextField intext;  
  public JTextArea ChatArea;
  String message;
  Client c;
  Socket s;
  JScrollBar   sbar;
  JScrollPane   spContainer;
  
  public ChatRoom( Client ct)  {
    c = ct;
    Box box = Box.createHorizontalBox();
    setLayout(new BorderLayout(10,10));
	ChatArea = new JTextArea(10,5);
    ChatArea.append("-------------------------------聊天室-----------------------------\n");
	ChatArea.append("/h 顯示可用指令 \n");
    ChatArea.setEditable(false);
	spContainer=new JScrollPane(ChatArea);   
    spContainer.getViewport().add(ChatArea); 
    box.add(spContainer);
    add(box,BorderLayout.CENTER);
    intext=new JTextField(25);
    intext.setEditable(true);
    add(intext,BorderLayout.SOUTH);
    intext.addActionListener(
      new ActionListener()  {
        public void actionPerformed(ActionEvent e)  {
           try {
		     out = new PrintWriter(c.clientSocket.getOutputStream(), true);
		     message=e.getActionCommand();
             intext.setText("");
             out.println(message);
			}
			catch (Exception ex )  {
			   ChatArea.append("Sending Error\n");
			}
        }
      }
    );    
    
  } 
}