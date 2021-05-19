import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PianoMain extends JFrame implements ActionListener
{
	private int instrument;
	private int volume;
	private int keyWidth = 30;
	private ButtonModel model;
	private int iterator = 0;
	private int seconds = 0;
	   
	private boolean pressed = false;
	
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	
	public PianoMain()
	{
		
		setBounds(100,100,keyWidth*52 + 16,500);
		setTitle("Piano Keys");
		
		
		Timer t1 = new Timer(25, this);
		t1.start();
	
		for(int i = 0; i < 52; i++)
		{
		
			JButton button = new JButton();
			button.setBackground(Color.WHITE);
			button.setForeground(Color.GRAY);
			button.setFont(new Font("MS Mincho", Font.PLAIN, 18));
			button.setBounds(keyWidth*i,150,keyWidth,100);
			button.getModel().addChangeListener(new BtnModelListener());
			add(button);
			buttons.add(button);
		}
		
		
		
		this.setResizable(false);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	public static void main(String[] args) throws MidiUnavailableException
	{
		new PianoMain();
	}
	
	private class BtnModelListener implements ChangeListener
	{
	    private boolean pressed = false;  // holds the last pressed state of the button
	
	    @Override
	    public void stateChanged(ChangeEvent e)
	    {
	        model = (ButtonModel) e.getSource();
	
	        // if the current state differs from the previous state
	        if (model.isPressed() != pressed)
	        {
	            String text = "Button pressed: " + model.isPressed() + "\n";
	            pressed = model.isPressed();
	             
	        }
	    }
	}
	
	   
	
	public void actionPerformed(ActionEvent e)
	{
		for(int i = 0; i < buttons.size(); i++)
		{
			ButtonModel model = buttons.get(i).getModel();
			model.addChangeListener(new BtnModelListener());
			
			if(model.isPressed())
			{
				System.out.println(i + " Was pressed!");
			}
		
		}
	
	}
}
