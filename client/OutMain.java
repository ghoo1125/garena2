//�D�n�j�U
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class OutMain extends JPanel
{
    public Game g;
	public Game2 g2;
	public int game_flag;
	Socket s;
	JButton game1;
	JButton game2;
	
	PrintWriter out;
	private Image bgImage;
	public OutMain(Client c)
	{
		setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
		this.s=c.clientSocket;
		
		Icon picture1 = new ImageIcon(getClass().getResource("Bingo.png"));
		game1 = new JButton("���G",picture1);
		game1.setHorizontalTextPosition(SwingConstants.CENTER);
		game1.setVerticalTextPosition(SwingConstants.BOTTOM);
		add(game1);
		
		Icon picture2 = new ImageIcon(getClass().getResource("p1.png"));
		game2 = new JButton("��a�p",picture2);
		game2.setHorizontalTextPosition(SwingConstants.CENTER);
		game2.setVerticalTextPosition(SwingConstants.BOTTOM);
		add(game2);
		
		Choosegame cg = new Choosegame();
		game1.addActionListener(cg);
		game2.addActionListener(cg);
		
	}
	
	public void paintComponent(Graphics g) {
		bgImage = loadImage("back.jpg");
		g.drawImage(bgImage, 0, 0, null);
	}
	private Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }
	
	public void set_om(){
	  g.set_om(this);
	}
	public void set_om2(){
	  g2.set_om(this);
	}
	
	private class Choosegame implements ActionListener  //���U�C�����s��}�l�i��C��
	{
		public void actionPerformed(ActionEvent e)
		{
			
			
			if(e.getSource()==game1)
			{
			  if(game_flag==0)  {
			    game_flag=1;
			    g2=new Game2(s);
				set_om2();
				JOptionPane.showMessageDialog(null,String.format("�i�J %s Good Luck:)", e.getActionCommand() ) );
			    try{
		          PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		          out.println("/wait2");
				}
		        catch(IOException ex){
				  System.out.println("Input error");
				}
			  }
			  else
			    JOptionPane.showMessageDialog(null,"�C���i�椤�A�νХ�������L�C��");
			}
			else if(e.getSource()==game2)  {
			  if(game_flag==0)
			  {
			    game_flag=1;
			    g = new Game(s);
				set_om();
				JOptionPane.showMessageDialog(null,String.format("�i�J %s Good Luck:)", e.getActionCommand() ) );
			    try{
		          PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		          out.println("/wait");
				}
		        catch(IOException ex){
				  System.out.println("Input error");
				}
              }
			   else
			    JOptionPane.showMessageDialog(null,"�C���i�椤�A�νХ�������L�C��");
			}
			else
			{}	
		}
	}
		
}