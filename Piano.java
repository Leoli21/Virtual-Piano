import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

public class Piano extends JFrame implements ActionListener, MouseListener
{
//	private PianoKeys2[][] piano; 
	private ArrayList<PianoKeys2> piano;
	
	//Option I
//	private String[] keyNotations = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", 
//	"B"};
//	private int xPosition = 0;
	
	//private double timeHeld; //Timer???
	
	final int octaves = 4;
	
	
	//Option II
	private int whiteXPos = 0;
	private int blackXPos = 45;
	
	final int wKeyWidth = 75;
	final int bKeyWidth = 45;
	
	private String[] whiteKeyNotations = {"C", "D", "E", "F", "G", "A", "B"};
	private String[] blackKeyNotations = {"C#/Db", "D#/Eb", "F#/Gb", "G#/Ab", "A#/Bb"};
	
	public Piano()
	{
		this.setBounds(100,100,2100,500);
		this.setLayout(null);
		this.setTitle("Piano");
		this.setResizable(false);
		
		JLayeredPane panel = new JLayeredPane();
		panel.setLayout(null);
		panel.setBounds(0,0,2100,500);
			
		piano = new ArrayList<PianoKeys2>();
		
		//Option I: Constructing by White then Black Keys
		//Constructing White Keys
		for(int wKey = 0; wKey < octaves * 7; wKey++)
		{
			piano.add(new PianoKeys2("White", whiteKeyNotations[wKey%7], whiteXPos));
			whiteXPos+=wKeyWidth;
		}
		//Constructing Black Keys
		for(int bKey = 0; bKey < octaves * 5; bKey++)
		{
			if(bKey % 5 < 2)
			{
				piano.add(new PianoKeys2("Black", blackKeyNotations[bKey%5], blackXPos));
				blackXPos += (2*bKeyWidth); 
			}
			else
			{
				blackXPos+=bKeyWidth; 	
				piano.add(new PianoKeys2("Black", blackKeyNotations[bKey%5], blackXPos));
				if(bKey % 5 == 4)
				{
					blackXPos += (3*bKeyWidth);
				}
				else
				{
					blackXPos += (wKeyWidth/2);
				}
			}
		}	
		
//		//Option II: Constructing all Keys in Order (Still WIP- the spacing of black keys is slightly off)
//		for(int key = 0; key < octaves * 12; key++)
//		{
//			if(key % 12 < 5)
//			{
//				if((key % 12) % 2 == 0) //Add white key to piano
//				{
//					piano.add(new PianoKeys2("White", keyNotations[key%12] , xPosition));
//					if(key % 12 == 4)
//					{
//						xPosition+=wKeyWidth;
//					}
//					else
//					{
//						xPosition+=bKeyWidth;
//					}
//				}
//				else //Add black key to piano
//				{
//					piano.add(new PianoKeys2("Black", keyNotations[key%12], xPosition));
//					xPosition+=(wKeyWidth-bKeyWidth);
//				}
//			}
//			else
//			{
//				if((key % 12) % 2 == 1)
//				{
//					piano.add(new PianoKeys2("White", keyNotations[key%12] , xPosition));
//					if(key % 12 == 11)
//					{
//						xPosition +=wKeyWidth;
//					}
//					else
//					{
//						xPosition+=bKeyWidth;
//					}
//				}
//				else
//				{
//					piano.add(new PianoKeys2("Black", keyNotations[key%12], xPosition));
//					xPosition+=(wKeyWidth-bKeyWidth);
//				}
//			}
//		}
		
		//Adding Keys to Panel
		for(int i = 0; i < piano.size(); i++)
		{
			PianoKeys2 key = piano.get(i);
			if(key.getKeyType().equals("White"))
			{
				key.setBackground(Color.WHITE);
				key.setOpaque(true);
				panel.add(key,0,-1);
			}
			else
			{
				key.setBackground(Color.BLACK);
				key.setOpaque(true);
				panel.add(key,1,-1);
			}
		}
		this.add(panel);
		
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
		
	}
	public static void main(String[] args) 
	{
		new Piano();

	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
