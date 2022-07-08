//登錄
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
	boolean flag;  //判斷是否登入成功
	OutFrame of;
	private Image bgImage;
	public Login( boolean f)
	{
		super("Garena遊戲平台--Login!!");
		flag=f;
		flagtest=false;
		setLocationRelativeTo(null);
		//GridLayout layout = new GridLayout(5,1,5,5);
		//layout.setAlignment(FlowLayout.CENTER);
		//setLayout( layout );
		pt = new JPanel(new FlowLayout(FlowLayout.CENTER));
		labelt = new JLabel("- - G A R E N A - -");
		pt.add(labelt);
		label1 = new JLabel("帳號:");
		text1 = new JTextField(20);
		p1 = new JPanel(new FlowLayout());
		p1.add(label1);
		p1.add(text1);
		label2 = new JLabel("密碼:");
		password1 = new JPasswordField(20);
		p2 = new JPanel(new FlowLayout());
		p2.add(label2);
		p2.add(password1);
		p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		button1 = new JButton("登入");
		p3.add(button1);
		button2 = new JButton("註冊");
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
	
	
	public class LoginWindow implements ActionListener	//登入視窗
	{   
             
		public void actionPerformed( ActionEvent e )
		{			
			if(e.getSource()==button1)
			{
			    c = new Client();  //連線
       			loginAccount();
				
                if(flag)  {
				  of.readthread(of);
                }
			}
			else if(e.getSource()==button2) //註冊帳號
			{
				NewAccount na = new NewAccount();
			}
		}
	}
	
	public void loginAccount()   //判斷登入帳號 正確回傳OutFrame否則null
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
				JOptionPane.showMessageDialog( null,"登入成功");
				super.dispose();
				of = new OutFrame(c,currentID);  //登入成功 並產生大廳
				of.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				flag=true;
			}
			else if(check.equals("/have"))
			{
				flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"登入失敗 此帳號已登入平台!");
			}
			else if(check.equals("/no id"))
			{
				flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"登入失敗 無此帳號!請重新輸入");
			}
			else if(check.equals("/nok"))
			{
				flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"登入失敗 密碼錯誤!請重新輸入");
			}
			else if(check.equals("/full")) {
			    flag=false;   
				text1.setText("");
				password1.setText("");
				JOptionPane.showMessageDialog( null,"登入失敗 線上人數已滿，請稍候再試~");
			}
		}
		catch(Exception e)  {
          System.out.println("Login Error");
        }	
	}
}