import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.*;



public class Game2 extends JFrame{
  public OutMain om;
  public Socket s;
  boolean turn=true;  //����
  boolean setmapflag=false;  //�]�w�a�Ϫ��A
  boolean playflag=false;  //�i�J�C�����A
  boolean startflag=false;  //�_�l�a�ϧP�_��
  boolean overflag=false;  //����
  boolean firstflag=true;  //�M�w�֥�
  int map[][];  //�s1~25
  int outmap[][];  //0����� 1���
  int x;  //�y��
  int y;
  int number=1;  
  int line;  //����
  int drawnum=1;  //�O�H�e��
  public String eid="";
  public Game2(Socket sk){
    super("BINGO");
    setSize(1000,800);
    setVisible(true);
	s=sk;
	outmap = new int[5][];
     map = new int [5][];
     for(int i=0;i<5;i++)  {
        map[i] = new int[5];
		outmap[i] = new int[5];
     }
     for(int i=0;i<5;i++)  
	     for(int j=0;j<5;j++)  {  
		   map[i][j]=0;
		   outmap[i][j]=0;
		 }
    addMouseListener(
        new MouseAdapter()  
        {
        	public void mouseClicked(MouseEvent event)  {   //���U�ƹ�
			 if(startflag){
			  x=(event.getX()-300)/80;
        	  y=(event.getY()-200)/80; 
			  if(setmapflag)  {
				if(number>1 && event.isMetaDown())  {
				  for(int i=0;i<5;i++)
				    for(int j=0;j<5;j++)
					  if(map[i][j]==(number-1))  {
					     outmap[i][j]=0;
						 map[i][j]=0;
						 number--;
						 if(number<=0)
						   number=1;
                         repaint();
					  }
				}
                else if(y<=4 && x<=4 && y>=0 && x>=0 && !(event.isMetaDown()))	{			
				  if(outmap[y][x]==0)  {
				    outmap[y][x]=1;
					map[y][x]=number;
					number++;
				    if(number==26)  {  //����
					  setmapflag=false;
					  try{
					  PrintWriter out = new PrintWriter(s.getOutputStream(), true);
					  out.println("/setok");  //�]�w�n�a��
					  }
					  catch(IOException e){}
					}
        	        repaint();  
				  }
				}
              }
              else  if(playflag && y<=4 && x<=4 && y>=0 && x>=0 && !(event.isMetaDown())) {
			    if(turn==true)  {
				  if(map[y][x]!=-1)  {
				    try{
					  PrintWriter out = new PrintWriter(s.getOutputStream(), true);
					  out.println(String.format("/number:%02d",map[y][x]));  //�]�w�n�a��
					}
					catch(IOException e){}
					map[y][x]=-1;
                    repaint();
                  }
				}
               }	
			  }
            }
        }	 
     );
	 setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	  addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
		    try {
		       PrintWriter out = new PrintWriter(s.getOutputStream(), true);
               out.println("/close2");
		     }
		     catch (Exception ex )  {
			   System.out.println("Sending Error\n");
		     }
			om.game_flag=0;
			dispose();
          }
        }
      );
   }
   public void enemy_left(){
     JOptionPane.showMessageDialog(this,"Your enemy has left!");
	 om.game_flag=0;
	 dispose();
   }
  public void set_om(OutMain o) {
   om=o;
  }
  public void paint(Graphics g){
    super.paint(g);
	if(startflag){
	  for(int i=0;i<=5;i++)                               //�e�خ�
       g.drawLine(300+i*80,200,300+i*80,600);    
      for(int i=0;i<=5;i++)
       g.drawLine(300,200+i*80,700,200+i*80); 
	   g.setFont(new Font("Times",Font.ITALIC,16));
	   g.drawString("You are playing with " + eid +".",350,60);
	  if(!playflag)  {  //�_�l�e��
	   if(setmapflag){
	   g.setFont(new Font("Times",Font.ITALIC,32));
	   g.drawString("Please set number      in your map.",250,120);
	   g.setColor(Color.red);
	   g.drawString(Integer.toString(number),530,120);  
	   g.setFont(new Font("Times",Font.ITALIC,20));
	   g.setColor(Color.BLACK);
	   g.drawString("(Left mouse for set. Right mouse for delete)",280,140);
	   }
	   g.setFont(new Font("Serif",Font.ITALIC,36));
	   g.setColor(Color.GREEN);
	   for(int i=0;i<5;i++)  
	     for(int j=0;j<5;j++)  
		   if(outmap[i][j]==1)  
		     g.drawString(Integer.toString(map[i][j]),j*80+330,i*80+250);  
	 }
	 else if(playflag){  //�C���}�l
	   g.setFont(new Font("Serif",Font.ITALIC,36));  //�a��
	   g.setColor(Color.GREEN);
	   for(int i=0;i<5;i++)  
	     for(int j=0;j<5;j++)  
		   if(map[i][j]>0)  
		     g.drawString(Integer.toString(map[i][j]),j*80+330,i*80+250);
           else if(map[i][j]==-1)  {
		     Polygon pg=new Polygon();	
		     g.setColor(Color.gray);
 	         pg.addPoint(j*80+301,i*80+201); 
	         pg.addPoint(j*80+379,i*80+201);  
		     pg.addPoint(j*80+379,i*80+279); 
	         pg.addPoint(j*80+301,i*80+279); 
	         g.fillPolygon(pg);
	         g.setColor(Color.GREEN);
           }		   
			 
       if(turn == true)  {        	//��ܽ����
	     if(firstflag)  {
		   g.setColor(Color.blue);
           g.setFont(new Font("Times",Font.ITALIC,32));
	       g.drawString("You First. ",430,120);
	       g.setColor(Color.black);
		   firstflag=false;
		 }
		 else  {
	       g.setColor(Color.blue);
           g.setFont(new Font("Times",Font.ITALIC,32));
		   g.drawString("Your competitor choose ",300,80);
	       g.drawString("Your turn. ",430,120);
		   g.setColor(Color.red);
	       g.drawString(Integer.toString(drawnum),650,80);
	       g.setColor(Color.black);
		 } 
	   }
       else  {
	     g.setColor(Color.blue);
         g.setFont(new Font("Times",Font.ITALIC,32));
         g.drawString("Please wait for your competitor...",280,90);
	     g.setColor(Color.black);
	   }
	   
	   g.setFont(new Font("Times",Font.ITALIC,32));
	   g.drawString("Point",800,300);
	   if(line>=1)//�p��
	     g.drawLine(820,320,860,320);
	   if(line>=2)  
	     g.drawLine(840,320,840,360);
	   if(line>=3)
	     g.drawLine(840,340,855,340);
	   if(line>=4)
	     g.drawLine(830,340,830,360);
	   
     }
	}
	else{
	  g.setFont(new Font("Times",Font.ITALIC,32));
	  g.drawString("���ݹ�⤤...",300,300);
	}
	if(startflag==true&&setmapflag==false&&playflag==false){
      g.setFont(new Font("Times",Font.ITALIC,32));
	  g.drawString("���ݹ���l�Ʀa��",345,175);
    }
  }
  public void set_priority(String st) {
    if(st.equals("/first2"))
	  turn=true;
	else
	  turn=false;
  }
  public void set_ok(){
    playflag=true;
	repaint();
  }
  public void set_go(boolean startflag,String eid) {
    this.startflag=startflag;
	setmapflag=true;
	this.eid=eid;
	repaint();
  }
   public void lose()  {  //�ѥ~���I�s server�ݶ�/g1win���O�M��~��������N�I�s
      JOptionPane.showMessageDialog(null,"Sorry,you lose!!");
	  JOptionPane.showMessageDialog(null,"Thank you for playing :)");
	  dispose();
   }
   public boolean over()  {
	 boolean getflag=false;
	 int i,j;
     if(x==y)  {    //���W�k�U�׽u
	   for(i=0,j=0;i<5;i++,j++)
	     if(map[i][j]!=-1) {
		   getflag=false;
		   break;
		 }
		 else 
		   getflag=true;
	   if(getflag)  {
         line++;
         getflag=false;		 
       }		  	 
	 }
	 if((x+y)==4)  {    //�k�W���U�׽u
	   for(i=0,j=4;i<5;i++,j--)
	     if(map[i][j]!=-1) {
		   getflag=false;
		   break;
		 }
		 else 
		   getflag=true;
	   if(getflag)  {
         line++;
         getflag=false;		 
       }		  	 
	 }
	 
	 for(i=0;i<5;i++)  //���u
	   if(map[i][x]!=-1) { 
		 getflag=false;
		 break;
	   }
	   else 
		   getflag=true;  
	 if(getflag)  {
       line++;
       getflag=false;		 
     }		
	 
     for(i=0;i<5;i++)  //��u
	   if(map[y][i]!=-1) { 
		 getflag=false;
		 break;
	   }
	   else 
		   getflag=true;  
	 if(getflag)  {
       line++;
       getflag=false;		 
     }		  
	 if(line>=5)
	   return true;
	 else
	   return false;
	   
   }
   public void getnum(int num) {   //�q�~��Ū��ƶi��  �ѥ~���I�s
	 drawnum=num;
     for(int i=0;i<5;i++)
	   for(int j=0;j<5;j++)
	     if(num==map[i][j]){
            map[i][j]=-1;
			y=i;
			x=j;
		 }
     turn= (turn==true? false:true);
	 firstflag=false;
	 repaint();
	 if(over()==true)  {
	   overflag=true;
	   turn=false;
	   try{
	     PrintWriter out = new PrintWriter(s.getOutputStream(), true);
	     out.println("/g2win");
	   }
	   catch(IOException ex)
	   {}
	   JOptionPane.showMessageDialog(null,"Bingo!!");
	   JOptionPane.showMessageDialog(null,"Thank you for playing :)");
	   dispose();
	 }
   }

}
   