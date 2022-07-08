//整合
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class OutFrame extends JFrame implements Runnable
{
	OutTop ot;
	OutMain om;
	ChatRoom chatroom;
	Client c;
    BufferedReader in;
    public String eid;
	
	public OutFrame( Client ct ,String id)
	{
		super("Garena");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10,10));
		c = ct;
		ot = new OutTop(id);
		om = new OutMain(c);
		chatroom = new ChatRoom(c);
		add(ot,BorderLayout.NORTH);
		add(om,BorderLayout.CENTER);
		add(chatroom,BorderLayout.SOUTH);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		setSize(800,700);
		setVisible(true);
	}
	
	public void readthread(OutFrame of)  {
	
     Thread t=new Thread(of);
	 t.start();
    }
	
	public void run()  {
      try {
		BufferedReader in = new BufferedReader (new InputStreamReader(c.clientSocket.getInputStream()));
        while(true) {
		  String instr = in.readLine();
          if(instr.charAt(0)!='/')  {
		    chatroom.ChatArea.append(instr+"\n");
			chatroom.sbar=chatroom.spContainer.getVerticalScrollBar();
			chatroom.sbar.setValue(chatroom.sbar.getMaximum());
		  }
		  else if(instr.equals("/h")){
		    chatroom.ChatArea.append("/people 顯示在線人數 \n"+"/to + 帳號:想說的話 密語功能 \n" );
			chatroom.sbar=chatroom.spContainer.getVerticalScrollBar();
			chatroom.sbar.setValue(chatroom.sbar.getMaximum());
		  }
		  else if(instr.equals("/start")){
		    eid=in.readLine();
			om.g.set_go(true,eid);
		  }
		  else if(instr.equals("/start2")){
		    eid=in.readLine();
		    om.g2.set_go(true,eid);
		  }
		  else if(instr.equals("/first2")||instr.equals("/second2")){
		    om.g2.set_priority(instr);
		  }
		  else if(instr.equals("/setok")){
		    om.g2.set_ok();
		  }
		  else if(instr.equals("/first")||instr.equals("/second")){
		    om.g.set_priority(instr);
		  }
		  else if(instr.equals("/outmap")){
		    om.g.set_outmap();
		  }
		   else if(instr.equals("/close")){
		    om.g.enemy_left();
		  }
		  else if(instr.equals("/close2")){
		    om.g2.enemy_left();
		  }
		  else if(instr.equals("/sysclose")){
		    JOptionPane.showMessageDialog( null,"伺服器斷線");
			System.exit(1);
		  }
		  else if(instr.equals("/g2lose")){
		    om.g2.lose();
		  }
		  else if(instr.substring(0, 4).equals("/map")){
		    int x,y;
			x=Integer.parseInt(instr.substring(instr.indexOf(":")+1,instr.indexOf(":")+3));
			y=Integer.parseInt(instr.substring(instr.indexOf(":")+4,instr.indexOf(":")+6));
			om.g.set_map(x,y);
		  }
		  else if(instr.substring(0, 5).equals("/coor")){
		    int x,y;
			x=Integer.parseInt(instr.substring(instr.indexOf(":")+1,instr.indexOf(":")+3));
			y=Integer.parseInt(instr.substring(instr.indexOf(":")+4,instr.indexOf(":")+6));
			om.g.set_cor(x,y);
		  }
		  else if(instr.substring(0, 7).equals("/number"))  {
		    int number;
			number=Integer.parseInt(instr.substring(instr.indexOf(":")+1,instr.indexOf(":")+3));
			om.g2.getnum(number);
		  }
        }
      }   
      catch (IOException e) {
        chatroom.ChatArea.append("Error . Your message aren't sent.\n");
      }
    }
	
}