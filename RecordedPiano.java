import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class RecordedPiano extends JFrame implements ActionListener, Synthesizer
{
	final MidiChannel[] mc;
	final int wKeyWidth = 75;
	final int bKeyWidth = 45;
	
	private ArrayList<PianoKeys2> piano;
	private static ArrayList<String> names = new ArrayList<String>();
	private ArrayList<Note> listOfNotes = new ArrayList<Note>();
	private String[] keyNotations = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", 
	"B"};
	private Instrument[] instr;
	
	private int xPosition = 0;
	private int octaves = 4;
	private int iterator = 0;	
	private int currentNote = 20;
	private int numNotes = 0;

	private boolean currentlyPressed = false;
	
	private ButtonModel model;
	private FileWriter writer = null;
	private PianoKeys2 pressedNote = null;
	
	private int instrumentNum;
	private int volume;
	private String titleName = "";
	
	private long startTimeNote = System.currentTimeMillis();
	private long endTimeNote = System.currentTimeMillis();
	private long timeElapsedNote = 0;
	private long startTimeRest = System.currentTimeMillis();
	private long endTimeRest = System.currentTimeMillis();
	private long timeElapsedRest = 0;
		
	public RecordedPiano(int inst, int loudness, String titleName) throws MidiUnavailableException
	{
		this.setBounds(100,100,wKeyWidth*7*octaves,500);
		this.setLayout(null);
		this.setTitle("Piano");
		this.setResizable(true);

		try
		{
			writer = new FileWriter(titleName + ".txt");
		}
	    catch (IOException e) 
	    {
	        System.out.println("Looks like an error has been detected.");
	        e.printStackTrace();
	    }	
		
		instrumentNum = inst;
		volume = loudness;
		this.titleName = titleName;
		      
	    Synthesizer synth = MidiSystem.getSynthesizer();
	    synth.open();       
	    mc = synth.getChannels();	    
	    instr = synth.getDefaultSoundbank().getInstruments();
	    synth.loadInstrument(instr[0]);	       
	    mc[5].programChange(instr[instrumentNum].getPatch().getProgram());
	    int iter = 0;
        for(Instrument i : instr)
        {       
        	System.out.println(iter + ". " + i.getName());
        	names.add(i.getName());
        	iter++;
        }
		
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
		
		//this code runs when you exit the program
		Runtime.getRuntime().addShutdownHook(new Thread() 
		{

		    @Override
		    public void run() 
		    {
		    	System.out.println("Saving song...");
				try
		      	{
					writer.append(titleName + " " + instrumentNum + " " + volume + " " + numNotes + " false");
					writer.append("\n");
					for(Note n : listOfNotes)
					{
			          	writer.append(n.getKeyNum()+" ");
			          	writer.append(n.getTimeHeld() +" ");
			          	writer.append(n.checkIfRest()+" ");
			          	writer.append("\n");

			          	writer.flush();
					}
		      	}
		      	catch(IOException e)
		      	{
		      	
		      	}

		  		try 
		  		{
		  			writer.close();
		  		}
		  		catch (IOException e) 
		  		{
		  		
		  			e.printStackTrace();
		  		}
		    }

		});
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
		boolean changeNote = false;
		for(int note = 0; note < piano.size(); note++)
		{
			ButtonModel model = piano.get(note).getModel();
			model.addChangeListener(new BtnModelListener());
			
			if(pressedNote != null)
			{
				model = pressedNote.getModel();
				model.addChangeListener(new BtnModelListener());
				changeNote = true;
			}
			
			if(model.isPressed())
			{
				
				if(!changeNote)
					pressedNote = piano.get(note);
				
				if(!currentlyPressed)
				{
					startTimeNote = System.currentTimeMillis();
					endTimeRest = System.currentTimeMillis();
					timeElapsedRest = endTimeRest - startTimeRest;
					listOfNotes.add(new Note(0, timeElapsedRest, true));
					currentNote = note;
					currentlyPressed = true;
					iterator = 0;
					mc[5].noteOn(note + 36,volume);
					numNotes++;
					
				}
				iterator++;
				

			}
			else
			{
				
				if(currentlyPressed)
				{
					startTimeRest = System.currentTimeMillis();
					endTimeNote = System.currentTimeMillis();
					timeElapsedNote = endTimeNote - startTimeNote;
					listOfNotes.add(new Note(currentNote, timeElapsedNote, false));
					currentlyPressed = false;
					iterator = 0;
					pressedNote = null;
					numNotes++;
				
				}			
				mc[5].noteOff(note + 36, 0);
				iterator++;

			}
		
		}
		
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