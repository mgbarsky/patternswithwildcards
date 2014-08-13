package simplegui;
import java.awt.*;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;
import util.*;

public class InputPanel extends JPanel
{
	//public static final String formatExplanationString="Specify set of strings separated by wildcards. For example <aab(2-5)abc(4)bc> is a pattern where token <aab> is separated from token <abc> by any 2 to 5 characters. ";
	public static final String inputExplanationString="Select flat input file or multiple files by pressing Browse button. The content is treated as text.";
	public static final String formatExplanationString="<html><p align='left'>Pattern template consists of a set of strings (not case-sensitive) and wildcards (arbitrary characters). <br>Indicate strings in plain text, and use  bracketed numbers  to indicate wildcards." +
			"<br> For example <b><i>aab(2-5)abc(4)bc</i></b> is a pattern where token <b><i>aab</i></b> is separated from token <b><i>abc</i></b> by 2 to 5 wildcards.<p></html>";
	public static final long serialVersionUID=1234567889;
	MainWindow guiManager;
	JTextField txtFile;
	JTextField txtPattern;
	JLabel label0;
	
	public InputPanel(MainWindow manager)
	{
		guiManager=manager;
		
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border emptyBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        Border topEmptyBorder = BorderFactory.createEmptyBorder(10,0,0,0);
        
		Border myBorder = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
	        
		JPanel columnLabels=new JPanel();
		BoxLayout verticalLabelLayout=new BoxLayout(columnLabels, BoxLayout.Y_AXIS);
		columnLabels.setLayout(verticalLabelLayout);		
		
		JLabel label1=new JLabel ("Select file(s):");
		JLabel label2=new JLabel ("Pattern:");
		
		columnLabels.add(label1);
		columnLabels.add(Box.createRigidArea(new Dimension(0, 15)));
		columnLabels.add(label2);
		
		JPanel columnHelp=new JPanel();
		BoxLayout verticalHelpLayout=new BoxLayout(columnHelp, BoxLayout.Y_AXIS);
		columnHelp.setLayout(verticalHelpLayout);		
		
		JButton helpInputFile=new JButton ("<html><b>?</b></html>"); //(new ImageIcon("help-2.jpg"));
		helpInputFile.addActionListener(new InputInfoHandler());
		JButton helpPattern=new JButton ("<html><b>?</b></html>");// (new ImageIcon("help-2.jpg"));
		helpPattern.addActionListener(new PatternInfoHandler());
		
		columnHelp.add(helpInputFile);
		columnHelp.add(Box.createRigidArea(new Dimension(0, 15)));
		columnHelp.add(helpPattern);
		
		JPanel columnTxtFields=new JPanel();
		BoxLayout verticalTxtLayout=new BoxLayout(columnTxtFields, BoxLayout.Y_AXIS);
		columnTxtFields.setLayout(verticalTxtLayout);		
		
		txtFile=new JTextField(30);	
		if(guiManager.state.inputFiles!=null && guiManager.state.inputFiles.length>0 )
			txtFile.setText(General.filesToCaption(guiManager.state.inputFiles));
		txtPattern=new JTextField(30);		
		if(guiManager.state.strPattern!=null)
			txtPattern.setText(guiManager.state.strPattern);
		
		columnTxtFields.add(txtFile);
		columnTxtFields.add(Box.createRigidArea(new Dimension(0, 15)));
		columnTxtFields.add(txtPattern);
		
		
		JPanel columnButtons=new JPanel();
		BoxLayout verticalBtnLayout=new BoxLayout(columnButtons, BoxLayout.Y_AXIS);
		columnButtons.setLayout(verticalBtnLayout);	
		
		JButton btnBrowse=new JButton("Browse...");		
		btnBrowse.addActionListener(new FileSelectionHandler());
		JButton btnValidate=new JButton("Validate");		
		btnValidate.addActionListener(new ValidationHandler());
		
		columnButtons.add(btnBrowse);
		columnButtons.add(Box.createRigidArea(new Dimension(0, 15)));
		columnButtons.add(btnValidate);
		
		JPanel contentRow=new JPanel();
		BoxLayout horizontalContentLayout=new BoxLayout(contentRow, BoxLayout.X_AXIS);
		contentRow.setLayout(horizontalContentLayout);	
		
		
		contentRow.add(columnLabels);		
		contentRow.add(columnHelp);
		contentRow.add(Box.createRigidArea(new Dimension(15, 0)));
		contentRow.add(columnTxtFields);
		contentRow.add(columnButtons);
		
		contentRow.setBorder(emptyBorder);
		
		JPanel contentPane=new JPanel();
		contentPane.setBorder(myBorder);
		contentPane.add(contentRow);
		
		JPanel buttonsRow=new JPanel();
		BoxLayout horizontalButtonLayout=new BoxLayout(buttonsRow, BoxLayout.X_AXIS);
		buttonsRow.setLayout(horizontalButtonLayout);
		
		JButton btnSearch=new JButton ("Search");		
		btnSearch.addActionListener(new RunHandler());
		JButton btnCancel=new JButton ("Cancel");
		btnCancel.addActionListener(new CancelHandler());
		
		
		buttonsRow.add(btnSearch);
		buttonsRow.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonsRow.add(btnCancel);
		buttonsRow.add(Box.createRigidArea(new Dimension(10, 0)));
		
		
		JPanel buttonsPane=new JPanel(new BorderLayout());	
		buttonsPane.add(buttonsRow,BorderLayout.EAST);
		buttonsPane.setBorder(topEmptyBorder);
		
		BoxLayout verticalMainLayout=new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(verticalMainLayout);
		this.setBorder(emptyBorder);
		
		this.add(contentPane);
		this.add(buttonsPane);
		
		
	}
	
	class RunHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			String strPattern=txtPattern.getText().trim().toLowerCase();
			if(guiManager.state.inputFiles==null || guiManager.state.inputFiles.length==0)
			{
				//msgbox
				JOptionPane.showMessageDialog(InputPanel.this,"Input file(s) is not selected.","Invalid input file(s).",
                        JOptionPane.ERROR_MESSAGE);
				return;
			}
			String errMessage=guiManager.state.validate(strPattern);
			if(errMessage==null || errMessage.equals(""))
			{				
				guiManager.state.setPattern(strPattern);
				
				InputPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				if(!guiManager.state.searchFiles())
				{
					JOptionPane.showMessageDialog(InputPanel.this,"Error processing files.","Unexpected error.",
	                        JOptionPane.ERROR_MESSAGE);
					InputPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}				
			}
			else
			{
				JOptionPane.showMessageDialog(InputPanel.this,errMessage,"Invalid pattern format.",
                        JOptionPane.ERROR_MESSAGE);
				InputPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
	   
			if(guiManager.state.solutions.size()==0)
			{
				JOptionPane.showMessageDialog(InputPanel.this,"A specified pattern was not found in input file(s).");
				InputPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			//disable buttons and texts, show message about running
			//when done - show save as form
			guiManager.inputFrame.setVisible(false);
			guiManager.inputFrame.dispose();
			
			guiManager.createAndShowSaveGUI();
			
	    }
	}
	 
	class CancelHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			System.exit(0);
	    }
	}
	 
	class InputInfoHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			JOptionPane.showMessageDialog(InputPanel.this,inputExplanationString);
	    }
	}
	
	class PatternInfoHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			JOptionPane.showMessageDialog(InputPanel.this,formatExplanationString);
	    }
	}
	
	class FileSelectionHandler implements ActionListener
	{
		
	    public void actionPerformed(ActionEvent ae)
	    {
	    	String defaultDir=System.getProperty("user.dir") + File.separator;
	    	JFileChooser chooser=new JFileChooser(defaultDir);
	    	chooser.setMultiSelectionEnabled(true);
	    	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    	
	    	int option=chooser.showOpenDialog(InputPanel.this);
	    	
	    	if (option == JFileChooser.APPROVE_OPTION)
	    	{	    		
	    		try
	    		{	    			
	    			guiManager.state.inputFiles=chooser.getSelectedFiles();	    		
	    			txtFile.setText(General.filesToCaption(guiManager.state.inputFiles));
	    		}
	    		catch (Exception e) 
	    		{
	    			JOptionPane.showMessageDialog(InputPanel.this,e.getMessage(),"Error selecting input files.",
	                        JOptionPane.ERROR_MESSAGE);
	    		}	    		
	    	}	    	
	    }
	}
	
	
	
	class ValidationHandler implements ActionListener
	{
	    public void actionPerformed(ActionEvent ae)
	    {
	    	String strPattern=txtPattern.getText().trim().toLowerCase();
			
			String errMessage=guiManager.state.validate(strPattern);
			if(errMessage!=null && !errMessage.equals(""))
			{				
				JOptionPane.showMessageDialog(InputPanel.this,errMessage,"Pattern format error",
                        JOptionPane.ERROR_MESSAGE);
				return;
						
			}
			else
			{
				JOptionPane.showMessageDialog(InputPanel.this,"The pattern is formatted properly");
				return;
			}		
			
	    }
	}
}
