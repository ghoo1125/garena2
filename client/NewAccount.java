import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class NewAccount extends JFrame
{
	JLabel labelt;
	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel labelmes;
	JTextField text1;
	JPasswordField password1;
	JPasswordField password2;
	JButton button1;
	JButton button2;
	JPanel p1;JPanel p2;JPanel p3;JPanel p4;JPanel p5;JPanel p6;
	NewAccountWindow nw;
	Client c;
	
	public NewAccount()
	{
		super("Garena�C�����x--���U");
		GridLayout layout = new GridLayout(6,1,5,5);
		//layout.setAlignment(FlowLayout.CENTER);
		setLocationRelativeTo(null);
		setLayout( layout );
		p1=new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelt = new JLabel("�b�����U");
		label1 = new JLabel("�п�J�b��:");
		text1 = new JTextField(20);
		p1.add(labelt);
		p2=new JPanel(new FlowLayout(FlowLayout.CENTER));
		p2.add(label1);
		p2.add(text1);
		p3=new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2 = new JLabel("�п�J�K�X:");
		password1 = new JPasswordField(20);
		p3.add(label2);
		p3.add(password1);
		p4=new JPanel(new FlowLayout(FlowLayout.CENTER));
		label3 = new JLabel("�ЦA����J�K�X:");
		password2 = new JPasswordField(18);
		p4.add(label3);
		p4.add(password2);
		p5=new JPanel(new FlowLayout(FlowLayout.CENTER));
		button1 = new JButton("���s��g");
		p5.add(button1);
		button2 = new JButton("�T�w");
		p5.add(button2);
		p6=new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelmes = new JLabel("");
		p6.add(labelmes);
		
		nw = new NewAccountWindow();
		button1.addActionListener(nw);	
		button2.addActionListener(nw);
		add(p1);add(p2);add(p3);add(p4);add(p5);add(p6);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setVisible(true);
	}
	
	public class NewAccountWindow implements ActionListener	//�n�J����
	{   
             
		public void actionPerformed( ActionEvent e )
		{			
			if(e.getSource()==button1)
			{
			    text1.setText("");
				password1.setText("");
				password2.setText("");
				labelmes.setText("");
			}
			else if(e.getSource()==button2) //���U�b��
			{
				if( (password1.getText()).equals(password2.getText()) ){
					 c = new Client();
					 ChecktoServer(text1.getText(),password1.getText());
				}
				else{
				  labelmes.setText("�K�X���P �Э��s��J!");
				  password1.setText("");
				  password2.setText("");
				}
			}
			else
			{  labelmes.setText("error!"); }
		}
	}
	
	public void ChecktoServer(String id,String pw)
	{
		try  {
			PrintWriter out = new PrintWriter(c.clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader (new InputStreamReader(c.clientSocket.getInputStream()));
			out.println("/new");
			out.println(id);
            out.println(pw);
			
			String check=in.readLine();
			
			if(check.equals("/ok")) {
				button1.removeActionListener(nw);
				button2.removeActionListener(nw);
				JOptionPane.showMessageDialog( null,"���U���\");
				this.dispose();			    
			}
			else if(check.equals("/had"))
			{
				text1.setText("");
				password1.setText("");
				password2.setText("");
				labelmes.setText("���b���w���H�ϥ� �Э��s��J");
			}
			else if(check.equals("/full"))
			{
				text1.setText("");
				password1.setText("");
				password2.setText("");
				labelmes.setText("���A�����L�� �еy�Ԧb����!");
			}
		}
		catch(Exception e)  {
          System.out.println("New Error");
        }	
	
	}	
	
	public static void main(String []args)
	{
		NewAccount na = new NewAccount();
			
	}
}