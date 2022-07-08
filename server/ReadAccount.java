import java.io.*;
import java.util.*;

public class ReadAccount
{
	public Account account[];
	private ObjectInputStream input;
	public int i;
	public void OpenFile()
	{
		try{
			input = new ObjectInputStream( new FileInputStream("Database.ser"));
		}
		catch( IOException e){
			System.err.println("error openflie!");
		}
	}

	public void readAccount()
	{
		OpenFile();
		
		account = new Account[50];
		i=0;
		try{
			
			while( true)
			{
				account[i] = (Account) input.readObject();			
				System.out.println(account[i].toString());
				i++;
			}
		}
		catch( EOFException eofex){
			System.err.println("eof");
			return ;
		}
		catch( IOException e){
			System.err.println("error read");
		}
		catch( ClassNotFoundException cnfe){
			System.err.println("error not file");
		}
		
		CloseFile();
	}
	
	public void CloseFile()
	{
		try{
			if( input!= null )
				input.close();
		}
		catch( IOException e){
			System.err.println("error closeflie!");
			System.exit(1);
		}
	}
	
	public static void main( String []args )
	{
		ReadAccount ra = new ReadAccount();
		//ra.OpenFile();
		ra.readAccount();
		//ra.CloseFile();	
	}
}