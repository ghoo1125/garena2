//���D
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
		labelid = new JLabel("�w�� "+id+" �Ө�C�����x - -");
		textTop = new JLabel("- - G A R E N A - -");
		add(textTop);
		add(labelid);
	}		
}