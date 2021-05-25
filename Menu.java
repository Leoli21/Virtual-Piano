import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Menu extends JFrame
{	
	//**************EXISTING SONG CODE****************************
	private ArrayList<Note> listOfNotes = new ArrayList<Note>();
	private ArrayList<Note> listOfNotes2 = new ArrayList<Note>();
	private ArrayList<Note> listOfNotes3 = new ArrayList<Note>();
	
	private String savedTitle;
	private int savedInstrument;
	private int savedVolume;
	private int numLines;
	
	private int savedNote;
	private double savedTimeHeld;
	private boolean savedIsRest;

	private ArrayList<String> existingSongs = new ArrayList<String>();
	private String[] normalArrayOfSongs;
	//************************************************************
	private String[] listOfInstruments = new String[235];
	private Instrument[] instr;
		
	private JLabel lblName = null;
	
	private boolean recorded;
	private boolean playExistingSong = false;
	private boolean multipleNotes = false;
	
	private int iter = 0;	
	private int currentInstrument;
	private int volumeInt;
	private int songIterator = 0;
	private int numNotes = 0;
	
	public Menu() throws MidiUnavailableException, IOException
	{
		setBounds(500,100,800,500);
		setTitle("Virtual Piano");
		setLayout(new GridBagLayout());
 
		normalArrayOfSongs = new String[existingSongs.size()];
		for(int i = 0; i < existingSongs.size(); i++)
		{
			normalArrayOfSongs[i] = existingSongs.get(i);
		}
		
	    Synthesizer synth = MidiSystem.getSynthesizer();
	    synth.open();
		
		instr = synth.getDefaultSoundbank().getInstruments();
		
        for(Instrument i : instr)
        {       
        	System.out.println(iter + ". " + i.getName());
        	listOfInstruments[iter] = "" + iter + ". " +  i.getName();
        	iter++;
        }
			
		
		GridBagConstraints gbc = new GridBagConstraints();
				
		gbc.insets = new Insets(5, 5, 5, 5);	
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		JLabel title = new JLabel("Virtual Piano");
		title.setFont(new Font("MS Mincho", Font.BOLD, 50));	
		title.setForeground(Color.blue);	
		gbc.gridwidth = 3;
		add(title, gbc);
		
		lblName = new JLabel("Loudness: ");
		lblName.setFont(new Font("MS Mincho", Font.PLAIN, 16));	
		lblName.setForeground(Color.blue);	
		gbc.gridwidth = 1;
		gbc.gridy++;
		add(lblName, gbc);
				
		JTextField textBody = new JTextField();
		textBody.setPreferredSize(new Dimension(300, 30));
		textBody.setEnabled(true);
		textBody.setVisible(true);
		gbc.gridwidth = 1;
		gbc.gridx++;
		add(textBody, gbc);
		
		JLabel songTitle = new JLabel("Song title: ");
		songTitle.setFont(new Font("MS Mincho", Font.PLAIN, 16));	
		songTitle.setForeground(Color.blue);
		songTitle.setVisible(false);
		gbc.gridwidth = 1;
		gbc.gridx--;
		gbc.gridy++;
		add(songTitle, gbc);
				
		JTextField titleName = new JTextField();
		titleName.setPreferredSize(new Dimension(300, 30));
		titleName.setEnabled(true);
		titleName.setVisible(false);
		gbc.gridwidth = 1;
		gbc.gridx++;
		add(titleName, gbc);
		
		JLabel existingSong = new JLabel("Existing Song Name: ");
		existingSong.setFont(new Font("MS Mincho", Font.PLAIN, 16));	
		existingSong.setForeground(Color.blue);
		existingSong.setVisible(false);
		gbc.gridwidth = 1;
		gbc.gridx--;
		gbc.gridy++;
		add(existingSong, gbc);
				
		JTextField existingSongName = new JTextField();
		existingSongName.setPreferredSize(new Dimension(300, 30));
		existingSongName.setEnabled(true);
		existingSongName.setVisible(false);
		gbc.gridwidth = 1;
		gbc.gridx++;
		add(existingSongName, gbc);
		
		
		JLabel Label2 = new JLabel("Instrument: ");
		Label2.setFont(new Font("MS Mincho", Font.PLAIN, 16));	
		Label2.setForeground(Color.blue);	
		Label2.setVisible(true);
		gbc.gridwidth = 1;
		gbc.gridx--;
		gbc.gridy++;
		add(Label2, gbc);
	
		JComboBox typeOfFile = new JComboBox(listOfInstruments);
		//getSelectedItem
		typeOfFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String s = (String) typeOfFile.getSelectedItem();	
				currentInstrument = Integer.valueOf(s.substring(0,1));
				try
				{
					currentInstrument = Integer.valueOf(s.substring(0,2));
				}
				catch(Exception exc1)
				{
					
				}
				try
				{
					currentInstrument = Integer.valueOf(s.substring(0,3));
				}
				catch(Exception exc2)
				{
					
				}
			}
		});		
		gbc.gridx++;
		add(typeOfFile, gbc);
		

		
		JCheckBox chkEnable2 = new JCheckBox("Play Existing Song");		
		chkEnable2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (chkEnable2.isSelected())
				{
					recorded = false;
					playExistingSong = true;
					existingSong.setVisible(true);
					existingSongName.setVisible(true);
					songTitle.setVisible(false);
					titleName.setVisible(false);
				}
				else
				{
					playExistingSong = false;
					existingSong.setVisible(false);
					existingSongName.setVisible(false);
				}
			}
		});
		gbc.gridx--;
		gbc.gridy++;
		add(chkEnable2, gbc);
		
		JCheckBox chkEnable = new JCheckBox("Record this song");		
		chkEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (chkEnable.isSelected())
				{
					recorded = true;
					playExistingSong = false;
					songTitle.setVisible(true);
					titleName.setVisible(true);
					existingSong.setVisible(false);
					existingSongName.setVisible(false);
				}
				else
				{
					recorded = false;
					songTitle.setVisible(false);
					titleName.setVisible(false);
				}
			}
		});
		gbc.gridx++;
		gbc.gridwidth = 1;
		add(chkEnable, gbc);		
				
		JButton declaration = new JButton("Terms of Use");
		declaration.setPreferredSize(new Dimension(200, 40));
	  
		declaration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
	
			}
		});		
		gbc.gridx--;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(declaration, gbc);
		
		
		JButton button = new JButton("Create new object");
		button.setPreferredSize(new Dimension(200, 40));
	  
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					volumeInt = Integer.valueOf(textBody.getText());
				}
				catch(Exception f)
				{
					volumeInt = 1000;
				}
				try 
				{
					if(recorded)
					{
						String title = titleName.getText();
						if(title.equals(""))
						{
							title = "Song";
						}
						new RecordedPiano(currentInstrument, volumeInt, title);
					}
					else if(playExistingSong)
					{
						//*******************************************************************************		
						boolean songsExist = false;
						File file = null;
						
						String songName = existingSongName.getText();
						
						try
						{
							file = new File(songName + ".txt");
							
					        if (file.createNewFile()) 
					        {
					            System.out.println("This is not an existing file! Creating " + file.getName());        
					        }
					        else 
					        {
					            System.out.println("Pulling up existing song...");
					            songsExist = true;
					        }
						}
					    catch (IOException exc2) 
					    {
					        System.out.println("Looks like an error has been detected.");
					        exc2.printStackTrace();
					    }
						
						Scanner read = null;
						try 
						{
							read = new Scanner(file);
						}
						catch (FileNotFoundException exc3) 
						{	
							exc3.printStackTrace();
						} 
				
						
						if(songsExist == true)
						{
				
							savedTitle = read.next();
							savedInstrument = read.nextInt();
							savedVolume = read.nextInt();
							numLines = read.nextInt();
							try
							{
								multipleNotes = read.nextBoolean();
							}
							catch(Exception exc)
							{
								System.out.println("There is only 1 note");
							}
							
							read.nextLine();
							
							for(int i = 0; i < numLines; i++)
							{
								savedNote = read.nextInt();
								savedTimeHeld = read.nextDouble();
								savedIsRest = read.nextBoolean();
								
								listOfNotes.add(new Note(savedNote, savedTimeHeld, savedIsRest));
								
								if(multipleNotes)
								{
									savedNote = read.nextInt();
									savedTimeHeld = read.nextDouble();
									savedIsRest = read.nextBoolean();
									listOfNotes2.add(new Note(savedNote, savedTimeHeld, savedIsRest));
									
									savedNote = read.nextInt();
									savedTimeHeld = read.nextDouble();
									savedIsRest = read.nextBoolean();
									listOfNotes3.add(new Note(savedNote, savedTimeHeld, savedIsRest));
								}
								read.nextLine();
								
						
							}
	
							new PianoReader(savedInstrument, savedVolume, savedTitle,
									listOfNotes, listOfNotes2, listOfNotes3);


						}
						else
						{
							JOptionPane.showMessageDialog(null, "Bruh");
						}
				//******************************************************************************* 
					}
					else
					{
						new Piano(currentInstrument, volumeInt);
					}
					} 
				catch (MidiUnavailableException e1) 
				{
					e1.printStackTrace();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		});		
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(button, gbc);
		
		
		this.setResizable(false);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
}