import java.io.File;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controler {
	
	public final int NOTE_ON = 0x90;
    public final int NOTE_OFF = 0x80;
    
    
	public Controler() {}

	/**
	 * Make a copy of the given midi file in the same folder by shifting the key tone by a given value
	 * @param path			path of the file to modify
	 * @param decalage		shift value
	 * @throws Exception
	 */
	public void toneShift(String path, int shift) throws Exception{
		
		  File original = new File(path);//Load file
		
	      Sequence sequence = MidiSystem.getSequence(original);//Load midi sequences
	      
	      Sequence seq =new Sequence(Sequence.PPQ, sequence.getResolution());//Copy of midi sequences
	
	      for (Track track :  sequence.getTracks()) {
	    	  
	    	  Track track2 = seq.createTrack ();//Create a new track for each original track
	    	          
	          for (int i=0; i < track.size(); i++) { 
	              MidiEvent event = track.get(i);
	              MidiMessage message = event.getMessage();
	              
	              track2.add(track.get(i));
	   
	              if (message instanceof ShortMessage) {
	                  ShortMessage sm = (ShortMessage) message;
	                  ShortMessage copy = new ShortMessage();
	                             
	                  if (sm.getCommand() == NOTE_ON) {
	                      int key = sm.getData1();
	                      int velocity = sm.getData2();
	         
	                      copy.setMessage(ShortMessage.NOTE_ON, 1, key + shift, velocity);
	                      MidiEvent me = new MidiEvent(copy, event.getTick());
	                      track2.add(me);
	                      
	                  } else if (sm.getCommand() == NOTE_OFF) {
	                      int key = sm.getData1();
	                      int velocity = sm.getData2();
	                      
	                      copy.setMessage(ShortMessage.NOTE_OFF, 1, key + shift, velocity);
	                      MidiEvent me = new MidiEvent(copy, event.getTick());
	                      track2.add(me);
		
	                  } 
	              }      
	          }           
	      }
	
	     
	  
	      System.out.println(renameFile(original.getPath(), shift));
	      
	      File f = new File(renameFile(original.getPath(), shift));
		  MidiSystem.write(seq,1,f);
		
		  //Play the modified midi file
		  Sequencer sequencer = MidiSystem.getSequencer();
	      sequencer.setSequence(seq); // load it into sequencer
	      sequencer.open(); // Open device
	      sequencer.start();  // start the playback
		  sequencer.close();
		}
	
	/**
	 * Rename the file with shift value
	 * @param path		path of the original file
	 * @param decalage 	shift value
	 * @return	new file name
	 */
	private String renameFile(String path, int shift) {
		
        StringBuffer newString = new StringBuffer(path); 
  
        int index = path.length()-4;
        
        String text = shift+"";
        
        if(shift>0)
        	text = "+"+shift;
        		
        newString.insert(index, text ); 
  
        // return the modified String 
        return newString.toString(); 
        
	}
	
	/**
	 * Open file explorer with midi file extension
	 * @param stage
	 * @return	path of the chosen file
	 */
	public String fileExplorer(Stage stage) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select midi file");
		
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Midi files", "*.mid", "*.midi")
            );	
		File f = fileChooser.showOpenDialog(stage);
		if (f!=null)
			return f.getPath();
		else
			return "";
	}

}
