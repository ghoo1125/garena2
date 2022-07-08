import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;

public class Server extends JFrame  {

  public Socket []sk;  //全部玩家
  public Socket tmpsk;
  public int max=4;  //登入滿人數
  public Box box = Box.createHorizontalBox();
  public JTextArea SysArea = new JTextArea(12,25);
  public String []name; //全部玩家名
  public int num;//紀錄玩家總個數
  public int[] enemy=new int[10];
  public boolean fullflag;
  public int[] game_wait=new int[10];
  public DataAccount dataaccount1;
  
  public Server()  {    //設定起始值
    super("Server");
	dataaccount1=new DataAccount();
	tmpsk=new Socket();
    sk=new Socket[10];
    name=new String[10];
    SysArea.append("-----------------------Server System-------------------------\n");
    SysArea.setEditable(false);
    box.add(new JScrollPane(SysArea));
    add(box,BorderLayout.CENTER);
    fullflag=true;
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	addWindowListener(
		new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int tmpcount=0;
				SysArea.append("window\n");
				dataaccount1.read.readAccount();
				dataaccount1.account=dataaccount1.read.account;
				tmpcount=dataaccount1.read.i;
				
				while(tmpcount!=0)
				{
					dataaccount1.DisConnectId(dataaccount1.account[tmpcount-1].ID);
					tmpcount--;
				}
				try {
				for(int i=0;i<max;i++)  {
                  if(sk[i]!=null)  {
                    PrintWriter out = new PrintWriter(sk[i].getOutputStream(), true);
                    out.println("/sysclose");
                  }
                }
				}
				catch(Exception ec)  {
				  System.out.println("severclose");
				}
				System.exit(1);
			}
		}
	);
	
	setSize(300,300);   
    setVisible(true);
  }

  public void runServer() {  //開始運作server 接收客戶,設定帳號,產生遊戲thread,產生聊天thread

    try {
      ServerSocket ss =new ServerSocket(8187);
      while(true)  {
	    tmpsk=new Socket();
		tmpsk=ss.accept();
		Server_Thread st = new Server_Thread(sk,SysArea,num,name,tmpsk,game_wait,enemy,max);          
        Thread t =new Thread(st);
        t.start();
	  }
    }
    catch(Exception e)  {
      SysArea.append("Connecting Error!\n");
    }
  }

  public static void main(String[] args)  {
    
     Server p= new Server();
     //JFrame j= new JFrame();
     //j.add(p);
     //j.setSize(300,300);   
     //j.setVisible(true);
     //j.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
     p.runServer();
  }

}

class Server_Thread implements Runnable  {
   public Socket sk[]=new Socket[10];
   public JTextArea SysArea;
   public int num;  //socket號碼
   public boolean errflag=false; //"x"關閉
   public boolean loginflag=false; //判斷帳號
   public boolean fullflag=false;  //判斷滿人
   public String name[]=new String[10]; //存名字
   public Socket tmpsk;
   public int max;  //登入滿人數
   int[] enemy;
   public BufferedReader in;
   public int[] game_wait;
   public DataAccount dataaccount;
	
   public Server_Thread(Socket []s,JTextArea SysArea,int num,String []name,Socket tmpsk,int[] game_wait,int[] enemy,int max) 
   {dataaccount=new DataAccount();sk=s;this.SysArea=SysArea;this.num=num;this.name=name;this.tmpsk=tmpsk;this.max=max;this.game_wait=game_wait;this.enemy=enemy;}

   public void run()  {     
     try {
        
       if(checkid()==true) //檢查帳密 (包括判斷滿人)  
         loginflag=true;
		
       if(errflag)  //x強制關閉
         loginflag=false;
         
       while(loginflag)  {
	      
         String instr=in.readLine();
         if(instr.length()>0){
   		  if(instr.charAt(0)!='/')  {  //傳送大廳聊天訊息
            for(int i=0;i<max;i++)  {
              if(sk[i]!=null)  {
                 PrintWriter out = new PrintWriter(sk[i].getOutputStream(), true);
                 out.println(name[num-1]+ ">> "+ instr);
              }
            }
            SysArea.append("Person " + num + " sending Success!\n");
          }
		  else if(instr.equals("/h"))  { //顯示可能指令
		    PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
            out.println("/h");
		  }
		  else if(instr.equals("/people"))  {  //顯示人數
		    int count=0;
			PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);		
		    for(int i=0;i<sk.length;i++)
			  if(sk[i]!=null)
			    count++;
			out.println("線上人數 : "+count+"人");	
		  }
		  else if(instr.equals("/g1win")) {
		    game_wait[num-1]=0;
			game_wait[enemy[num-1]-1]=0;
			enemy[num-1]=0;
		  }
		  else if(instr.equals("/close"))
		  {
		    if(game_wait[num-1]==2){
		      PrintWriter out = new PrintWriter(sk[enemy[num-1]-1].getOutputStream(), true);
			  game_wait[enemy[num-1]-1]=0;
			  enemy[enemy[num-1]-1]=0;
			  enemy[num-1]=0;
			  out.println("/close");
			}
			game_wait[num-1]=0;
		  }
		  else if(instr.equals("/close2"))
		  {
		    if(game_wait[num-1]==4||game_wait[num-1]==5){
		      PrintWriter out = new PrintWriter(sk[enemy[num-1]-1].getOutputStream(), true);
			  game_wait[enemy[num-1]-1]=0;
			  enemy[enemy[num-1]-1]=0;
			  enemy[num-1]=0;
			  out.println("/close2");
			}
			game_wait[num-1]=0;
		  }
		  else if(instr.equals("/wait2"))  {
		    game_wait[num-1]=3;
			for(int i=1;i<=10;i++){
			  if(game_wait[i-1]==3 && i!=num){
			    PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
			    PrintWriter out2 = new PrintWriter(sk[i-1].getOutputStream(), true);
				enemy[num-1]=i;
				enemy[i-1]=num;
			    out.println("/start2");
				out.println(name[enemy[num-1]-1]);
			    out2.println("/start2");
				out2.println(name[num-1]);
				out.println("/first2");
				out2.println("/second2");
				game_wait[i-1]=4;
				game_wait[num-1]=4;					//成功找到配對
				break;
			  }
			}
		  }
		  else if(instr.equals("/wait"))  {
		    game_wait[num-1]=1;
		    for(int i=1;i<=10;i++){
			  if(game_wait[i-1]==1 && i!=num){
			    PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
			    PrintWriter out2 = new PrintWriter(sk[i-1].getOutputStream(), true);
				enemy[num-1]=i;
				enemy[i-1]=num;
			    out.println("/start");
				out.println(name[enemy[num-1]-1]);
			    out2.println("/start");
				out2.println(name[num-1]);
				game_wait[i-1]=2;
				game_wait[num-1]=2;					//成功找到配對
				out.println("/first");
				out2.println("/second");
				int[][] map;
				int bomb=41;
				map = new int [20][];
				for(int j=0;j<20;j++)  {
				  map[j] = new int[20];
				}
				for(int j=0;j<20;j++)
				  for(int k=0;k<20;k++)  {
					map[j][k]=0;
				  }
				Random rd =new Random();
				while(bomb!=0)  {                  //設好地雷位置
					int x=rd.nextInt(20);
					int y=rd.nextInt(20);
				if(map[x][y]==-1)
					continue;
				else  {
					map[x][y]=-1;
					out.println("/map:"+String.format("%02d,%02d",x,y));
					out2.println("/map:"+String.format("%02d,%02d",x,y));
				}
				bomb--;
				}
				out.println("/outmap");
				out2.println("/outmap");
				break;
			  }
			}
		  }
		  else if(instr.equals("/setok")){
		    game_wait[num-1]=5;
			if(game_wait[enemy[num-1]-1]==5){
			  PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
			  PrintWriter out2 = new PrintWriter(sk[enemy[num-1]-1].getOutputStream(), true);
			  out.println("/setok");
			  out2.println("/setok");
			}
		  }
		  else if(instr.equals("/g2win")) {
		    game_wait[num-1]=0;
			game_wait[enemy[num-1]-1]=0;
			PrintWriter out2 = new PrintWriter(sk[enemy[num-1]-1].getOutputStream(), true);
			enemy[enemy[num-1]-1]=0;
			enemy[num-1]=0;
			out2.println("/g2lose");
		  }
		  else if(instr.substring(0,instr.length()>=8 ? 8 : instr.length()).equals("/number:"))  {
		    PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
			PrintWriter out2 = new PrintWriter(sk[enemy[num-1]-1].getOutputStream(), true);
			out.println(instr);
			out2.println(instr);
		  }
		  else if(instr.substring(0,instr.length()>=4 ? 4 : instr.length()).equals("/to ") && instr.indexOf(":")>=0)  {  //密語 
		    String checkid=instr.substring(4,instr.indexOf(":"));
		    Boolean find=false;
			for(int i=0;i<name.length;i++)  {
			  if(checkid.equals(name[i]) && i!=(num-1)) {
				PrintWriter out = new PrintWriter(sk[i].getOutputStream(), true);	
				out.println(name[num-1]+"偷偷跟你說>>"+instr.substring(instr.indexOf(":")+1));
				PrintWriter out2 =new PrintWriter(sk[num-1].getOutputStream(), true);	
				out2.println("你偷偷跟"+name[i]+"說>>"+instr.substring(instr.indexOf(":")+1));
				find=true;
				break;
			  }
			  else
			    find=false;
			}
			if(find==false)  {
			  PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);	
			  out.println("無此id");
			}
		  }
		  else  if(instr.substring(0,instr.length()>=6 ? 6 : instr.length()).equals("/coor:")) {  //傳送遊戲座標
			PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
			PrintWriter out2 = new PrintWriter(sk[enemy[num-1]-1].getOutputStream(), true);
			out.println(instr);
			out2.println(instr);
          }
		  else if(instr.charAt(0)=='/')  {      //聊天訊息錯誤
            PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
            out.println(" 無效的指令");
          }
		 }
       }
     }
     catch(Exception e)  {
       SysArea.append("Sending Error!\n");
     }
	 
     /*連結結束*/
	 if(fullflag==false)  {
       dataaccount.DisConnectId(name[num-1]);
	   SysArea.append("Person " + num + " Connection Ended!\n"); 
       sk[num-1]=null;
     }
        
  }

  public Boolean checkid()  {
      
      try  {
        
		for(int i=0;i<max;i++)
           if(sk[i]==null)  {
             num=i+1;
			 sk[i]=tmpsk;
             fullflag=false;
             break;
           }
           else   
             fullflag=true;
	    	  
	     if(fullflag)  {  //滿人
	       PrintWriter out = new PrintWriter(tmpsk.getOutputStream(), true);
		   out.println("/full");
		   return false;
	     }
		 else  {
		   in= new BufferedReader(new InputStreamReader(sk[num-1].getInputStream()));
           PrintWriter out = new PrintWriter(sk[num-1].getOutputStream(), true);
           
		   String firsttmp = in.readLine();   //接收第一個值判斷是要登入還是要註冊
		   if(firsttmp.equals("/login"))      //登入
		   {
			String idtmp = in.readLine();
			String pwtmp = in.readLine();
			String tmp = dataaccount.CheckID(idtmp,pwtmp);
			SysArea.append(tmp+"!\n");
			if(tmp.equals("/ok")){
				out.println(tmp);
				name[num-1]=dataaccount.account[dataaccount.current].ID;
				SysArea.append("Login Success!\n"); 
				return true;
			}
			else {
				SysArea.append("Login failed!\n"); 
				out.println(tmp);
				return false;
			}
		   }
		   else if(firsttmp.equals("/new")){  //註冊
			  String idtmp = in.readLine();
			  String pwtmp = in.readLine();
			  if(dataaccount.ShearchAndCreate(idtmp,pwtmp)){
				out.println("/ok");
				SysArea.append("New Success!\n"); 
			  }else{
			    out.println("/had");
				SysArea.append("New failed!\n");
			  }
			  return false;
		   }
		   else{return false;}
         }
		 
	  }
      catch(IOException e)  {         //如果強制按"x"關閉 
        SysArea.append("Login error\n");
        sk[num-1]=null;
        errflag=true;    
        return false;
      }
  }

}


  