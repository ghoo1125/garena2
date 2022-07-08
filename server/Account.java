import java.io.*;

public class Account implements Serializable
{
	public int Number;
	public String ID;
	public String Password;
	public boolean Online;
	
	public Account(){this(0,"","",false);}
	public Account( int n, String id, String pw, boolean ol)
	{
		Number = n;	ID = id;	Password = pw;	Online = ol;
	}
	
	public void setAccount( int n, String id, String pw, boolean ol)
	{
		Number = n;	ID = id;	Password = pw;	Online = ol;
	}
	
	public int getAccountNumber(){	return Number; }
	public String getAccountID(){	return ID; }
	public String getAccountPassword(){	return Password; }
	public boolean getAccountOnline(){	return Online; }
	
	@Override
	public String toString()
	{
		return Number+"-"+ID+"-"+Password+"-"+Online ;
	}
}