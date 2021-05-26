import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PianoReader extends JFrame implements ActionListener, Synthesizer
{
	final MidiChannel[] mc;
	final int wKeyWidth = 75;
	final int bKeyWidth = 45;
	
	private ArrayList<PianoKeys2> piano;
	private static ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Note> listOfNotes;
	private ArrayList<Note> listOfNotes2;
	private ArrayList<Note> listOfNotes3;
	private String[] keyNotations = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", 
	"B"};
	private Instrument[] instr;
	
	private int xPosition = 0;
	private int octaves = 4;
	private int iterator = 0;	
	private int currentNote = 20;

	private boolean currentlyPressed = false;
	
	private ButtonModel model;
	private FileWriter writer = null;
	private PianoKeys2 pressedNote = null;
	
	private int instrumentNum;
	private int volume;
	private String titleName = "";
	
	private long startTime = System.currentTimeMillis();
	private long endTime = System.currentTimeMillis();
	
	private final Color PRESSED_COLOR = new Color(184, 207, 229);
		
	public PianoReader(int inst, int loudness, String titleName, ArrayList<Note> notes,
			ArrayList<Note> notes2, ArrayList<Note> notes3) throws MidiUnavailableException, IOException
	{
		this.setBounds(100,100,wKeyWidth*7*octaves,500);
		this.setLayout(null);
		this.setTitle("Piano");
		this.setResizable(true);

		try
		{
			writer = new FileWriter("Recorded_notes.txt");
		}
	    catch (IOException e) 
	    {
	        System.out.println("Looks like an error has been detected.");
	        e.printStackTrace();
	    }
		
		
		instrumentNum = inst;
		volume = loudness;
		this.titleName = titleName;
		listOfNotes = notes;
		listOfNotes2 = notes2;
		listOfNotes3 = notes3;
		      
	    Synthesizer synth = MidiSystem.getSynthesizer();
	    synth.open();       
	    mc = synth.getChannels();	    
	    instr = synth.getDefaultSoundbank().getInstruments();
	    synth.loadInstrument(instr[0]);	       
	    mc[5].programChange(instr[instrumentNum].getPatch().getProgram());
	    int iter = 0;
		
		Timer t1 = new Timer(25	,this);
		t1.start();
		
		JLayeredPane panel = new JLayeredPane();
		panel.setLayout(null);
		panel.setBounds(0,0,wKeyWidth*7*octaves,500);		
		
		piano = new ArrayList<PianoKeys2>();
		
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
	            pressed = model.isPressed();
	             
	        }
	    }
	}
	@Override
	

	public void actionPerformed(ActionEvent arg0) 
	{
		for(int i = 0; i < listOfNotes.size(); i++)
		{				
			int keyNum = listOfNotes.get(i).getKeyNum();
			int pressTime = (int)(listOfNotes.get(i).getTimeHeld());
			int savedKey = 0;
			int keyNum2 = 0;
			int pressTime2 = 0;
			int keyNum3 = 0;
			int pressTime3 = 0;
			
			if(listOfNotes2.size() != 0 || listOfNotes3.size() != 0)
			{
				keyNum2 = listOfNotes2.get(i).getKeyNum();
				pressTime2 = (int)(listOfNotes2.get(i).getTimeHeld());
				
				keyNum3 = listOfNotes3.get(i).getKeyNum();
				pressTime3 = (int)(listOfNotes3.get(i).getTimeHeld());
			}
			
			int j = 0;
			boolean clicked = false;
			//piano.get(keyNum).doClick();
			
//			if(listOfNotes2.size() != 0 && !listOfNotes2.get(i).checkIfRest())
//			{
//				mc[2].noteOn(keyNum2 + 36, volume);
//			}
//			if(listOfNotes3.size() != 0 && !listOfNotes3.get(i).checkIfRest())
//			{
//				mc[3].noteOn(keyNum3 + 36, volume);
//			}
//			
			startTime = System.currentTimeMillis();
			
			while((int)(endTime-startTime) < pressTime)
			{		
				if(!clicked)
				{
					if(!listOfNotes.get(i).checkIfRest())
					{
						mc[5].noteOn(keyNum + 36, volume);
						clicked = true;	
						piano.get(keyNum).doClick();						
					}
					else
					{
						mc[5].noteOn(keyNum + 36, 0);
						clicked = true;			
					}
					if(listOfNotes2.size() != 0 || listOfNotes3.size() != 0)
					{
						if(!listOfNotes2.get(i).checkIfRest())
						{
							mc[5].noteOn(keyNum2 + 36, volume);
							clicked = true;	
							piano.get(keyNum).doClick();						
						}
						else
						{
							mc[5].noteOn(keyNum2 + 36, 0);
							clicked = true;			
						}
						if(!listOfNotes3.get(i).checkIfRest())
						{
							mc[5].noteOn(keyNum3 + 36, volume);
							clicked = true;	
							piano.get(keyNum).doClick();						
						}
						else
						{
							mc[5].noteOn(keyNum3 + 36, 0);
							clicked = true;			
						}
						
					}
				}
				
				endTime = System.currentTimeMillis();
				
			}
			clicked = false;
			mc[5].noteOff(keyNum + 36, 0);
			

			
		}
		System.exit(0);
		
		
		
	}
	public ArrayList<String> getNames()
	{
		return names;
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