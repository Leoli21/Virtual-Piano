import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.VoiceStatus;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Piano extends JFrame implements ActionListener, Synthesizer
{
//	private PianoKeys2[][] piano; 
	private ArrayList<PianoKeys2> piano;
	
	final MidiChannel[] mc;
	
	
	
	//Option I
	private String[] keyNotations = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", 
	"B"};
	private int xPosition = 0;
	
	private double timeHeld; //Timer???
	
	final int octaves = 4;
	
	private int iterator = 0;
	private int seconds = 0;
	
	
//	//Option II
//	private int whiteXPos = 0;
//	private int blackXPos = 45;
	final int wKeyHeight = 500;
	final int wKeyWidth = 75;
	final int bKeyWidth = 45;
	
	private ButtonModel model;
	
//	private String[] whiteKeyNotations = {"C", "D", "E", "F", "G", "A", "B"};
//	private String[] blackKeyNotations = {"C#/Db", "D#/Eb", "F#/Gb", "G#/Ab", "A#/Bb"};
	
	public Piano() throws MidiUnavailableException
	{
		this.setBounds(100,100,wKeyWidth*7*octaves+20,wKeyHeight+45);
		this.setLayout(null);
		this.setTitle("Piano");
		this.setResizable(true);
		
	       
	    Synthesizer synth = MidiSystem.getSynthesizer();
	    synth.open();
	       
	    mc = synth.getChannels();
	    
	    Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
	    synth.loadInstrument(instr[0]);
	       
	    mc[5].programChange(instr[0].getPatch().getProgram());
	       
		
		Timer t1 = new Timer(25,this);
		t1.start();
		JLayeredPane panel = new JLayeredPane();
		panel.setLayout(null);
		panel.setBounds(0,0,wKeyWidth*7*octaves,500);
		
	    int iter = 0;
        for(Instrument i : instr)
        {       
        	System.out.println(iter + ". " + i.getName());
        	iter++;
        }
			
		piano = new ArrayList<PianoKeys2>();
		
//		//Option I: Constructing by White then Black Keys
//		//Constructing White Keys
//		for(int wKey = 0; wKey < octaves * 7; wKey++)
//		{
//			
//			piano.add(new PianoKeys2("White", whiteKeyNotations[wKey%7], whiteXPos));
//			whiteXPos+=wKeyWidth;
//		}
//		//Constructing Black Keys
//		for(int bKey = 0; bKey < octaves * 5; bKey++)
//		{
//			if(bKey % 5 < 2)
//			{
//				piano.add(new PianoKeys2("Black", blackKeyNotations[bKey%5], blackXPos));
//				blackXPos += (2*bKeyWidth); 
//			}
//			else
//			{
//				blackXPos+=bKeyWidth; 	
//				piano.add(new PianoKeys2("Black", blackKeyNotations[bKey%5], blackXPos));
//				if(bKey % 5 == 4)
//				{
//					blackXPos += (3*bKeyWidth);
//				}
//				else
//				{
//					blackXPos += (wKeyWidth/2);
//				}
//			}
//		}	
		
		//Option II: Constructing all Keys in Order (Still WIP- the spacing of black keys is slightly off)
		for(int key = 0; key < octaves * 12; key++)
		{
			if(key % 12 < 5) //First Half of Octave (5 Keys: 3 white + 2 black)
			{
				if((key % 12) % 2 == 0) //Add white key to piano
				{
					piano.add(new PianoKeys2("White", keyNotations[key%12] , xPosition));
					if(key % 12 == 4)
					{
						xPosition+=wKeyWidth;
					}
					else if(key% 12 == 2)
					{
						xPosition += (wKeyWidth-15);
					}
					else
					{
						xPosition+=bKeyWidth;
					}
				}
				else //Add black key to piano
				{
					piano.add(new PianoKeys2("Black", keyNotations[key%12], xPosition));
					if(key % 12 == 3)
					{
						xPosition += 15;
					}
					else 
					{
						xPosition+=(wKeyWidth-bKeyWidth);
					}
					
				}
			}
			else //Second Half of Octave (7 Keys: 4 white + 3 black)
			{
				if((key % 12) % 2 == 1) //Add White key
				{
					piano.add(new PianoKeys2("White", keyNotations[key%12] , xPosition));
					if(key % 12 == 11)
					{
						xPosition +=wKeyWidth;
					}
					else if(key % 12 == 7)
					{
						xPosition += (wKeyWidth - (bKeyWidth/2));
					}
					else if(key % 12 == 9)
					{
						xPosition += 60;
					}
					else
					{
						xPosition+=bKeyWidth;
					}
				}
				else //Add Black Key
				{
					piano.add(new PianoKeys2("Black", keyNotations[key%12], xPosition));
					if(key%12 == 8)
					{
						xPosition += (bKeyWidth/2);
					}
					else if(key%12 == 10)
					{
						xPosition += 15;
					}
					else
					{
						xPosition+=(wKeyWidth-bKeyWidth);
					}
				}
			}
		}
		
		//Adding Keys to Panel
		for(int i = 0; i < piano.size(); i++)
		{
			PianoKeys2 key = piano.get(i);
			key.getModel().addChangeListener(new BtnModelListener());
			
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
	public static void main(String[] args) throws MidiUnavailableException 
	{
		new Piano();

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
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		for(int note = 0; note < piano.size(); note++)
		{
			ButtonModel model = piano.get(note).getModel();
			model.addChangeListener(new BtnModelListener());
			
			if(model != null && model.isPressed())
			{
				System.out.println(note + " Was pressed!");
				
		mc[5].noteOn(note + 36,1000);
				iterator++;
				if(iterator > 40)
				{	   
					iterator = 0;
		
					seconds++;
					System.out.println("Seconds: " + seconds);
				}
			}
			else
			{
				mc[5].noteOn(0, 0);
			}
		
		}
		if(model != null)
		{
		
			if(model.isPressed())
			{

			}

		}
		
	}
	@Override
	public Info getDeviceInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void open() throws MidiUnavailableException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public long getMicrosecondPosition() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxReceivers() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxTransmitters() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Receiver getReceiver() throws MidiUnavailableException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Receiver> getReceivers() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Transmitter getTransmitter() throws MidiUnavailableException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Transmitter> getTransmitters() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getMaxPolyphony() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long getLatency() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public MidiChannel[] getChannels() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public VoiceStatus[] getVoiceStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isSoundbankSupported(Soundbank soundbank) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean loadInstrument(Instrument instrument) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void unloadInstrument(Instrument instrument) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean remapInstrument(Instrument from, Instrument to) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Soundbank getDefaultSoundbank() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Instrument[] getAvailableInstruments() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Instrument[] getLoadedInstruments() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean loadAllInstruments(Soundbank soundbank) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void unloadAllInstruments(Soundbank soundbank) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean loadInstruments(Soundbank soundbank, Patch[] patchList) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void unloadInstruments(Soundbank soundbank, Patch[] patchList) {
		// TODO Auto-generated method stub
		
	}
}