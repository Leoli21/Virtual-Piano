import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;

public class PianoKeys2 extends JButton
{
	private String keyType;
	private String note;
	
	private int xPos;
	final int yPos = 0;
	
	private Dimension blackKeySize = new Dimension(45,300);
	private Dimension whiteKeySize = new Dimension(75,500);
	
	public PianoKeys2(String kT, String n, int x)
	{
		this.keyType = kT;
		this.note = n;
		this.xPos = x;
	
		if(keyType.equals("White"))
		{
			this.setBounds(x,0, whiteKeySize.width,whiteKeySize.height);
		}
		else
		{
			this.setBounds(x,0,blackKeySize.width,blackKeySize.height);
		}
	}
//	public void paintComponent(Graphics g)
//	{
//		super.paintComponent(g);
//		if(keyType.equals("White"))
//			g.setColor(Color.WHITE);
//		else
//			g.setColor(Color.BLACK);	
//		
//	}
	public String getKeyType()
	{
		return keyType;
	}
	
	public String getNote()
	{
		return note;
	}
	
	public int getWKeyWidth()
	{
		return whiteKeySize.width;
	}
	public int getBKeyWidth()
	{
		return blackKeySize.width;
	}
}

