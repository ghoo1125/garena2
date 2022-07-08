import java.io.*;
import java.util.*;

public class WriteAccount
{
	private ObjectOutputStream output;
	private ObjectInputStream input;
	public Account account1[];
	
	//-----------------------------------------------------------------------------------------
	public void copy() //複製
	{
		Account a;
		try{
			output = new ObjectOutputStream( new FileOutputStream("Database.ser"));
			input = new ObjectInputStream( new FileInputStream("tmp.ser"));		
			while(true)
			{
				a= (Account) input.readObject();
				output.writeObject(a);
			}	
		}
		catch( EOFException eofex){
			return ;
		}
		catch( IOException e){
			System.err.println("error copy");
		}
		catch( ClassNotFoundException cnfe){
			System.err.println("error not file");
		}
	}
	//-----------------------------------------------------------------------------------------	
	public void addAccount( String id, String pw)
	{
		//加入註冊的資料
		Account atmp;
		
		account1=new Account[50];
		int count=0;
		
		try{
			output = new ObjectOutputStream( new FileOutputStream("tmp.ser"));
			input = new ObjectInputStream( new FileInputStream("Database.ser"));
			while(true)
			{
				account1[count]= (Account) input.readObject();
				output.writeObject(account1[count]);
				System.out.printf("%d",count);
				count++;
			}
		}
		catch( EOFException eofex){
			
			try{
				atmp = new Account( count+1 , id , pw , false);
				output.writeObject(atmp);
				output.flush();
			}
			catch( IOException e){
				System.err.println("error add");
			}
			return ;
		
		}
		catch( IOException e){
			System.err.println("error write");
		}
		catch( ClassNotFoundException cnfe){
			System.err.println("error not file");
		}
	}
	//-----------------------------------------------------------------------------------------
	public void CloseFile()
	{
		try{
			if( output!= null )
				output.close();
			if( input!= null )
				input.close();	
		}
		catch( IOException e){
			System.err.println("error closeflie!");
			System.exit(1);
		}
	}
	//-----------------------------------------------------------------------------------------
	public void Modify(Account a)
	{
		account1=new Account[50];
		Account ade;
		int count=0;
		
		try{
			output = new ObjectOutputStream( new FileOutputStream("tmp.ser"));
			input = new ObjectInputStream( new FileInputStream("Database.ser"));
			while(true)
			{
				if(count==a.Number-1){
					ade=(Account) input.readObject();
					account1[count]=a;
				}
				else {
					account1[count]= (Account) input.readObject();
				}
				output.writeObject(account1[count]);
				System.out.printf("%d",count);
				count++;
			}
		}
		catch( EOFException eofex){
			return ;
		}
		catch( IOException e){
			System.err.println("error write");
		}
		catch( ClassNotFoundException cnfe){
			System.err.println("error not file");
		}
	
	}
	//------------------------------------------------------------------------------------

	/*
	public static void main( String []args )
	{
		WriteAccount wa = new WriteAccount();
		Account a = new Account();
		//a.Number=4;a.ID="hardywu";a.Password="7096";a.Online=false;
		//wa.addAccount("hardywu","h4561231");
		//wa.CloseFile();
		//wa.copy();
		//wa.CloseFile();	
		wa.Modify(a);
		wa.CloseFile();
		wa.copy();
		wa.CloseFile();
		
		//wa.Modify("aaa","bbb");
		//wa.CloseFile();
		//wa.copy();
		//wa.CloseFile();	
	}*/
}