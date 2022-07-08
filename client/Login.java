//�n��
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Login extends JFrame
{
	boolean flagtest;
	Client c;
	JTextField text1;
	JPasswordField password1;
	JLabel labelt;
	JLabel label1;
	JLabel label2;
	JButton button1;
	JButton button2;
	JPanel pt;
	JPanel p1;
	JPanel p2;
	JPanel p3;
	JPanel ptotal;
	LoginWindow lw;
	String currentID;
	String currentPassword;
	boolean flag;  //�P�_�O�_�n�J���\
	OutFrame of;
	private Image bgImage;
	public Login( boolean f)
	{
		super("Garena�C�����x--Login!!");
		flag=f;
		flagtest=false;
		setLocationRelativeTo(null);
		//GridLayout layout = new GridLayout(5,1,5,5);
		//layout.setAlignment(FlowLayout.CENTER);
		//setLayout( layout );
		pt = new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelt = new JLabel("- - G A R E N A - -");
		pt.add(labelt);
		label1 = new JLabel("�b��:");
		text1 = new JTextField(20);
		p1 = new JPanel(new FlowLayout());
		p1.add(label1);
		p1.add(text1);
		label2 = new JLabel("�K�X:");
		password1 = new JPasswordField(20);
		p2 = new JPanel(new FlowLayout());
		p2.add(label2);
		p2.add(password1);
		p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button1 = new JButton("�n�J");
		p3.add(button1);
		button2 = new JButton("���U");
		p3.add(button2);
		ptotal = new JPanel(new GridLayout(5,1,5,5));
		ptotal.add(pt);ptotal.add(p1);ptotal.add(p2);ptotal.add(p3);		
		add(ptotal);
		lw = new LoginWindow();
		button1.addActionListener(lw);
		button2.addActionListener(lw);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		setSize(300,250);
		setVisible(true);
	}
	
	
	public class LoginWindow implements ActionListener	//�n�J����
	{   
             
		public void actionPerformed( ActionEvent e )
		{			
			if(e.getSource()==button1)
			{
			    c = new Client();  //�s�u
       			loginAccount();
				
                if(flag)  {
				  of.readthread(of);
                }
			}
			else if(e.getSource()==button2) //���U�b��
			{
				NewAccount na = new NewAccount();
			}
		}
	}
	
	public void loginAccount()   //�P�_�n�J�b�� ���T�^��OutFrame�_�hnull
	{		
		currentID = new String(text1.getText());
		currentPassword = new String(password1.getPassword());
		
		try  {
			PrintWriter out = new PrintWriter(c.clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader (new InputStreamReader(c.clientSocket.getInputStream()));
			out.println("/login");
			out.println(currentID);
            out.println(currentPassword);
			String check=in.readLine();
			if(check.equals("/ok")) {

				text1.setText("");
				password1.setText("");
				button1.removeActionListener(lw);
				button2.removeActionListener(lw);
				JOptionPane.showMessageDialog( null,"�n�J���\");
				super.dispose();
				of = new OutFrame(c,currentID);  //�n�J���\ �ò��ͤj�U
				of.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				flag=true;
			}
			else if(check.equals("/have"))
			{
				flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"�n�J���� ���b���w�n�J���x!");
			}
			else if(check.equals("/no id"))
			{
				flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"�n�J���� �L���b��!�Э��s��J");
			}
			else if(check.equals("/nok"))
			{
				flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"�n�J���� �K�X���~!�Э��s��J");
			}
			else if(check.equals("/full")) {
			    flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"�n�J���� �u�W�H�Ƥw���A�еy�ԦA��~");
			}
		}
		catch(Exception e)  {
          System.out.println("Login Error");
        }	
	}
}