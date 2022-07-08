//標題
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class OutTop extends JPanel
{
	JLabel textTop;
	JLabel labelid;
	public OutTop(String id)
	{
		setSize(5,5);
		labelid = new JLabel("歡迎 "+id+" 來到遊戲平台 - -");
		textTop = new JLabel("- - G A R E N A - -");
		add(textTop);
		add(labelid);
	}		
}