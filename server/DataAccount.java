import java.io.*;

public class DataAccount
{
	public Account account[];
	public ReadAccount read;
	public WriteAccount write;
	public int lenght;
	int current;
	public DataAccount()
	{
		lenght=0;	
		account = new Account[50];
		read = new ReadAccount();
		write = new WriteAccount();
	}
	//-------------------------------------------------------------------------------
	public String CheckID( String id ,String password)
	{
		boolean find=false;
		current=0;
		read.OpenFile();
		read.readAccount();
		this.account=read.account;
		this.lenght=read.i;
		read.CloseFile();
		
		while(!find&&current<lenght)
		{
			if(account[current].ID.equals(id)){
				find=true;
				if(account[current].Online)
					return "/have";						//���b���w���n�J���A
				if(account[current].Password.equals(password)){
					account[current].Online=true;
					write.Modify(account[current]);
					write.copy();
					write.CloseFile();
					return "/ok";                     //�b���K�X���T
				}
				else	
					return "/nok";						//�K�X���~
			}
			current++;
		}
		if(!find)
			return "/no id";		//�L���b��
		else
			return "/error imposible";
	}
	//-------------------------------------------------------------------------------
	public void DisConnectId(String id)
	{
		boolean find=false;
		current=0;
		read.OpenFile();
		read.readAccount();
		this.account=read.account;
		this.lenght=read.i;
		read.CloseFile();
	
		while(!find&&current<lenght)
		{
			if(account[current].ID.equals(id)){
				find=true;
				account[current].Online=false;
				write.Modify(account[current]);
				write.copy();
				write.CloseFile();
			}
			current++;
		}
	}
	//-------------------------------------------------------------------------------
	public boolean ShearchAndCreate(String id,String pw)
	{
		boolean find=false;
		current=0;
		read.OpenFile();
		read.readAccount();
		this.account=read.account;
		this.lenght=read.i;
		read.CloseFile();
	
		while(!find&&current<lenght)
		{
			if(account[current].ID.equals(id)){
				find=true;
				return false;
			}
			current++;
		}
		
		if(!find){
			write.addAccount(id,pw);
			write.copy();
			write.CloseFile();
			return true;
		}
		else{return false;}
	}	
	//-------------------------------------------------------------------------------
	public static void main( String []args)
	{
		DataAccount data = new DataAccount();
		System.out.println(data.CheckID("verna","aaa1"));
	}
}