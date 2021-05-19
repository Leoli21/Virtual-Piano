import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ButtonPressTest extends JPanel implements ActionListener, Synthesizer
{
	private JButton button = new JButton("Button");
	private JTextArea textArea = new JTextArea(15, 15);
	private ButtonModel model;
	private int iterator = 0;
	private int seconds = 0;
	   
	final MidiChannel[] mc;
	
	public ButtonPressTest() throws MidiUnavailableException
	{
		Timer t1 = new Timer(25, this);
		t1.start();
	
	    button.getModel().addChangeListener(new BtnModelListener());
	    textArea.setFocusable(false);
	    add(button);
	    add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	                                      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	       
	       
	       
	    Synthesizer synth = MidiSystem.getSynthesizer();
	    synth.open();
	       
	    mc = synth.getChannels();
	       
	       
	    Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
	    synth.loadInstrument(instr[0]);
	       
	    mc[5].programChange(instr[233].getPatch().getProgram());
	       
	    int iter = 0;
        for(Instrument i : instr)
        {
	       
        	System.out.println(iter + ". " + i.getName());
        	iter++;
        }
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
	            textArea.append(text);
	            pressed = model.isPressed();
	        }
	    }
	}
	
	private static void createAndShowGui() throws MidiUnavailableException
	{
	    JFrame frame = new JFrame("ButtonPressTest");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(new ButtonPressTest());
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	       
	
	
	}
	
	public static void main(String[] args) throws MidiUnavailableException
	{
	    createAndShowGui();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(model != null)
		{
		
			if(model.isPressed())
			{
				mc[5].noteOn(50,1000);
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
				mc[5].noteOn(50, 0);
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