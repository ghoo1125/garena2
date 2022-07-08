import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Game extends JFrame{

   private Boolean turn;            //��ܪ��a
   private int you;                 //�p��ۤv���a�p��
   private int enemy;               //�p��ĤH���a�p��
   private int[][] map;             //�a�� �a�p:-1 �S��:0 �Ʀr
   private int bomb;                //�a�p��
   private int[][] outmap;          //��ܥΦa��  0����� 1��ܼƦr 2��ܪ��a�@�a�p 3��ܪ��a�G�a�p 
   public Socket s;
   public OutMain om;
   private boolean go;
   public String eid="";
   public Game(Socket sk)  {
     super("��a�p");
	 this.s=sk;
     setSize(1000,800);  
     setVisible(true);
	 go=false;
     outmap = new int[20][];
     map = new int [20][];
	 bomb=41;
	 turn=false;
     for(int i=0;i<20;i++)  {
        map[i] = new int[20];
        outmap[i] = new int[20]; 
   	 }
     for(int i=0;i<20;i++)
       for(int j=0;j<20;j++)  {
         map[i][j]=0;
         outmap[i][j]=0;
       }
	 addMouseListener(
       new MouseAdapter() {
         public void mouseClicked(MouseEvent event) {
		  if(turn==true)  {
           int x=0;
           int y=0;
           y=(event.getY()-100)/30;
		   x=(event.getX()-150)/30;
		   if(x>=0&&x<20&&y>=0&&y<20&&event.getX()>150&&event.getY()>100)  {
             try {
		       PrintWriter out = new PrintWriter(s.getOutputStream(), true);
               out.println(String.format("/coor:%02d,%02d",x,y));
		     }
		     catch (Exception ex )  {
			   System.out.println("Sending Error\n");
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
               out.println("/close");
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
   
   public void set_cor(int x,int y)  {
     if(outmap[y][x]==0)  {
       if(map[y][x]==0)  {     //���p���Ū�
         set(x,y);             //set()�B�z����ܭ���
         if(turn == true)      //�S���a�p���H
           turn = false;
         else 
           turn = true;       
       } 
       else if(map[y][x]>0)  {  //���p���Ʀr
       outmap[y][x]=1;       //������ܼƦr
         if(turn == true)      //�S���a�p���H
           turn = false;
         else 
           turn = true;           
       }
       else  {                 //���a�p
         if(turn == true)   {  //���a�@
           you++;
           outmap[y][x]=2;
		   bomb--;
         }
         else  {               //���a�G
           enemy++;
           outmap[y][x]=3;
		   bomb--;
         }
       }
	   repaint();
       if(you>=21)  {
         JOptionPane.showMessageDialog(null,"You Win!!");
		 JOptionPane.showMessageDialog(null,"Thank you for playing :)");
		 dispose();
	   }
       else if(enemy>=21)  {
         JOptionPane.showMessageDialog(null,"You lose!!");
		 JOptionPane.showMessageDialog(null,"Thank you for playing :)");
		 dispose();
	   }
     }
   }
   public void set_outmap(){
     int count;                    //�p��@��P�򪺦a�p��
     for(int i=0;i<20;i++)  {
       for(int j=0;j<20;j++)  {

          if(map[i][j]==-1)
            continue;

          count=0;
          if((i-1)>=0 && (j-1)>=0)  
            if(map[i-1][j-1]==-1)
              count++;         
          if((j-1)>=0 )
            if(map[i][j-1]==-1)
              count++;  
          if((i+1)<=19 && (j-1)>=0)
            if(map[i+1][j-1]==-1)
              count++;  
          if((i-1)>=0)
            if(map[i-1][j]==-1)
              count++;
          if((i+1)<=19)
            if(map[i+1][j]==-1)
              count++;  
          if((i-1)>=0 && (j+1)<=19)
            if(map[i-1][j+1]==-1)
              count++;  
          if((j+1)<=19)
            if(map[i][j+1]==-1)
              count++;  
          if((i+1)<=19 && (j+1)<=19)
            if(map[i+1][j+1]==-1)
              count++;      

          map[i][j]=count;
      }
     }
   }
   
   public void set_go(boolean go,String eid) {
     this.go=go;
	 this.eid=eid;
	 repaint();
   }

   public void set_map(int x,int y){
     map[x][y]=-1;
   }
   
   public void paint(Graphics g)  {
     if(go){
	   super.paint(g);
	   for(int i=0;i<21;i++)                               //�e�خ�
          g.drawLine(150+i*30,100,150+i*30,700);    
       for(int i=0;i<21;i++)
          g.drawLine(150,100+i*30,750,100+i*30); 
       g.setFont(new Font("Times",Font.ITALIC,16));		  
       g.drawString("You are playing with " + eid +".",600,60); 
       for(int i=0;i<20;i++)
         for(int j=0;j<20;j++)
          if(outmap[i][j]==1)  {       //���
            if(map[i][j]!=0)  {
			 if(map[i][j]==1)  {
			     g.setColor(Color.blue);
			     g.drawString(Integer.toString(map[i][j]),165+j*30,115+i*30);
				 g.setColor(Color.black);
			  }
			  else if(map[i][j]==2)  {
			     g.setColor(Color.GREEN);
			     g.drawString(Integer.toString(map[i][j]),165+j*30,115+i*30);
				 g.setColor(Color.black);
			  }
			  else if(map[i][j]==3)  {
			     g.setColor(Color.red);
			     g.drawString(Integer.toString(map[i][j]),165+j*30,115+i*30);
				 g.setColor(Color.black);
			  }
			  else if(map[i][j]==4)  {
			     g.setColor(Color.magenta);
			     g.drawString(Integer.toString(map[i][j]),165+j*30,115+i*30);
				 g.setColor(Color.black);
			  }
			  else if(map[i][j]==5)  {
			     g.setColor(Color.darkGray);
			     g.drawString(Integer.toString(map[i][j]),165+j*30,115+i*30);
				 g.setColor(Color.black);
			  }
			  
			}
            else
              drawblock(g,i,j);   
          } 
          else if(outmap[i][j]==2)                      //���a�@�a�p ��
             drawblue(g,i,j); 
          else if(outmap[i][j]==3)                      //���a�G�a�p ��
             drawred(g,i,j); 

    
    if(turn == true)  {        	//��ܽ����
	  g.setColor(Color.blue);
 	  Polygon pg=new Polygon();	
 	  pg.addPoint(500,55); 
	  pg.addPoint(520,50);  
	  pg.addPoint(500,45);  
      g.drawLine(500,55,500,65); 
	  g.fillPolygon(pg);
	  g.setColor(Color.black);
	  g.setFont(new Font("Times",Font.ITALIC,18));
	  g.drawString("     Your              turn. ",400,60);
	}
    else  {
	  g.setColor(Color.red);
	  Polygon pg=new Polygon();	
 	  pg.addPoint(500,55); 
	  pg.addPoint(520,50);  
	  pg.addPoint(500,45);  
      g.drawLine(500,55,500,65); 
	  g.fillPolygon(pg);
      g.setColor(Color.black);
	  g.setFont(new Font("Times",Font.ITALIC,18));
	  g.drawString("Not Your               turn. ",400,60);
	  
	}
	
	g.setColor(Color.blue);  //��ܭp��
 	Polygon pg=new Polygon();	
 	pg.addPoint(900,150); 
	pg.addPoint(920,145);  
	pg.addPoint(900,140);  
    g.drawLine(900,150,900,160); 
	g.fillPolygon(pg);
	g.setFont(new Font("Times",Font.ITALIC,18));
	g.drawString(Integer.toString(you),900,180);
    g.setColor(Color.red);  
 	pg=new Polygon();	
 	pg.addPoint(900,250); 
	pg.addPoint(920,245);  
	pg.addPoint(900,240);  
    g.drawLine(900,250,900,260); 
	g.fillPolygon(pg);
    g.drawString(Integer.toString(enemy),900,280);
	g.setColor(Color.black);
	g.drawString("Total Bomb!",870,400);
	g.drawString(Integer.toString(bomb),900,450);
	
	 }
	 else  {
	   g.setFont(new Font("Times",Font.ITALIC,32));
	   g.drawString("���ݹ�⤤...",300,300);
	 }
   } 
   
   public void set_priority(String priority){
     if(priority.equals("/first"))
	   turn=true;
	 else
	   turn=false;
   }
   
   public void drawblue(Graphics g,int i,int j)  {  //�e�źX
     g.setColor(Color.blue);
 	 Polygon pg=new Polygon();	
 	 pg.addPoint(158+j*30,113+i*30); 
	 pg.addPoint(178+j*30,108+i*30);  
	 pg.addPoint(158+j*30,103+i*30);  
     g.drawLine(158+j*30,113+i*30,158+j*30,123+i*30); 
	 g.fillPolygon(pg);
	 g.setColor(Color.black);
   }
   public void drawred(Graphics g,int i,int j)  {  //�e���X
	 g.setColor(Color.red);
 	 Polygon pg=new Polygon();	
 	 pg.addPoint(158+j*30,113+i*30); 
	 pg.addPoint(178+j*30,108+i*30);  
	 pg.addPoint(158+j*30,103+i*30);  
     g.drawLine(158+j*30,113+i*30,158+j*30,123+i*30); 
	 g.fillPolygon(pg);
	 g.setColor(Color.black);
   }
   
   public void drawblock(Graphics g,int i,int j)  {  //�e��l
	 g.setColor(Color.gray);
     g.fillRect(151+j*30,101+i*30,29,29);	
	 g.setColor(Color.black);
   }
   
    public int set(int x,int y)  {  //��ܨ��

     outmap[y][x]=1;
     
     if((x+1)<=19)  {
       if(map[y][x+1]>0 )   //�k��O�Ʀr
         outmap[y][x+1]=1;
       else if(outmap[y][x+1]==1)  //�k��w�Q�]�w
         outmap[y][x+1]=1;
       else
         set(x+1,y);
     }

     if((y+1)<=19)  {
       if(map[y+1][x]>0 )   //�U���O�Ʀr 
         outmap[y+1][x]=1;
       else if(outmap[y+1][x]==1)  {//�U���w�Q�]�w
         outmap[y+1][x]=1;
       }
       else
         set(x,y+1);
     }   
       
     if((x-1)>=0)  {
       if(map[y][x-1]>0 )   //����O�Ʀr
         outmap[y][x-1]=1;
       else if(outmap[y][x-1]==1)  //����w�Q�]�w
         outmap[y][x-1]=1;
       else
         set(x-1,y);
     }

     if((y-1)>=0)  {
       if(map[y-1][x]>0 )    //�W���O�Ʀr
         outmap[y-1][x]=1;
       else if(outmap[y-1][x]==1)  //�W���w�Q�]�w
         outmap[y-1][x]=1;
       else
         set(x,y-1);
     }
      
     if((y-1)>=0 && (x+1)<=19)  {
       if(map[y-1][x+1]>0 )    //�k�W�O�Ʀr
         outmap[y-1][x+1]=1;
       else if(outmap[y-1][x+1]==1)  //�W���w�Q�]�w
         outmap[y-1][x+1]=1;
       else
         set(x+1,y-1);
     }

     if((y+1)<=19 && (x+1)<=19)  {
       if(map[y+1][x+1]>0 )    //�k�U�O�Ʀr
         outmap[y+1][x+1]=1;
       else if(outmap[y+1][x+1]==1)  //�W���w�Q�]�w
         outmap[y+1][x+1]=1;
       else
         set(x+1,y+1);
     }

     if((y+1)<=19 && (x-1)>=0)  {
       if(map[y+1][x-1]>0 )    //���U�O�Ʀr
         outmap[y+1][x-1]=1;
       else if(outmap[y+1][x-1]==1)  //�W���w�Q�]�w
         outmap[y+1][x-1]=1;
       else
         set(x-1,y+1);
     }

     if((y-1)>=0 && (x-1)>=0)  {
       if(map[y-1][x-1]>0 )    //���W�O�Ʀr
         outmap[y-1][x-1]=1;
       else if(outmap[y-1][x-1]==1)  //�W���w�Q�]�w
         outmap[y-1][x-1]=1;
       else
         set(x-1,y-1);
     }

     return 1;
  } 
}