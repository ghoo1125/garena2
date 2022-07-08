import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;

public class Client {

	public Socket clientSocket;
	
	public Client()
	{					
		try {
			clientSocket = new Socket("140.113.122.217",8187);
		}
		catch (IOException e) {
		   JOptionPane.showMessageDialog(null,"³s½u¥¢±Ñ!");
		   System.exit(0);
		}
	}
}