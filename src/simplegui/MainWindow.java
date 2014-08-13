package simplegui;
import java.awt.*;
import javax.swing.*;

public class MainWindow 
{ 
	JFrame inputFrame;
	JFrame outputFrame;
	public State state=new State();
	
	public void createAndShowInputGUI( ) 
	{
		try {
		    // Set cross-platform Java L&F (also called "Metal")
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }

	        //Create and set up the window.
		inputFrame = new JFrame("Search for patterns with wildcards");
		inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        //Add content to the window.
		InputPanel inputPane=new InputPanel(this);
		inputFrame.add(inputPane, BorderLayout.CENTER);
	    inputFrame.setResizable(false) ;   
	        //Display the window.
		inputFrame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    // Determine the new location of the window
	    int w = inputFrame.getSize().width;
	    int h = inputFrame.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    // Move the window
	    inputFrame.setLocation(x, y);
		inputFrame.setVisible(true);
	}	
	
	public void createAndShowSaveGUI( ) 
	{
	        //Create and set up the window.
		String frameCaption="Search results";
		if(state.solutions!=null)
		{
			frameCaption="Found "+state.solutions.size()+" occurrence(s) of pattern "+state.strPattern;
		}
		outputFrame = new JFrame(frameCaption);
		outputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        //Add content to the window.
		SavePanel savePane=new SavePanel(this);
		outputFrame.add(savePane, BorderLayout.CENTER);
		outputFrame.setResizable(false) ;      
	        //Display the window.
		outputFrame.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    // Determine the new location of the window
	    int w = outputFrame.getSize().width;
	    int h = outputFrame.getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    // Move the window
	    outputFrame.setLocation(x, y);
		outputFrame.setVisible(true);
	}
}
