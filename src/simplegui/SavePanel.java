package simplegui;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.Border;

public class SavePanel extends JPanel
{
	MainWindow guiManager;
	JTextField txtBefore;
	JTextField txtAfter;
	JTextField txtFile;
	final static String saveExplanationString="<html>Select output file name to save solutions as tab-delimited records. <br> Preferable output file formats are electronic tables (e.g. .xls) or text.</html> ";
	final static String marginsExplanationString="<html>Define (positive) number of characters to be added to the output record before and after pattern's start position." +
			"<br> For example, if pattern found at position 10, and before and after margins are set to 3, <br> then the substring starting from position 10-3=7 will be added to the output record." +
			" <br> The output substrings will end at position 10+3=13 or 10+pattern length, whichever is larger.</html>";
	public static final long serialVersionUID=1234567890;
	public SavePanel(MainWindow manager)
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
		
		JLabel label1=new JLabel ("Save to file:");
		JLabel label2=new JLabel ("Add margins:");
		
		columnLabels.add(label1);
		columnLabels.add(Box.createRigidArea(new Dimension(0, 15)));
		columnLabels.add(label2);
		
		JPanel columnHelp=new JPanel();
		BoxLayout verticalHelpLayout=new BoxLayout(columnHelp, BoxLayout.Y_AXIS);
		columnHelp.setLayout(verticalHelpLayout);		
		
		JButton helpSaveFile=new JButton ("<html><b>?</b></html>"); //(new ImageIcon("help-2.jpg"));
		helpSaveFile.addActionListener(new SaveInfoHandler());
		JButton helpMargins=new JButton ("<html><b>?</b></html>");// (new ImageIcon("help-2.jpg"));
		helpMargins.addActionListener(new MarginsInfoHandler());
		
		columnHelp.add(helpSaveFile);
		columnHelp.add(Box.createRigidArea(new Dimension(0, 15)));
		columnHelp.add(helpMargins);
		
		
		
		
			
		JPanel outputFileCell=new JPanel();
		BoxLayout outputFileHorizontalLayout=new BoxLayout(outputFileCell, BoxLayout.X_AXIS);
		outputFileCell.setLayout(outputFileHorizontalLayout);
		
		txtFile=new JTextField(30);	
		if(guiManager.state.outputFile!=null)
			txtFile.setText(guiManager.state.outputFile.getName());
		JButton btnBrowse=new JButton("Browse...");
		btnBrowse.addActionListener(new SaveFileHandler());
		
		outputFileCell.add(txtFile);
		outputFileCell.add(btnBrowse);
		
		JPanel marginsCell=new JPanel();
		BoxLayout marginsHorizontalLayout=new BoxLayout(marginsCell, BoxLayout.X_AXIS);
		marginsCell.setLayout(marginsHorizontalLayout);
		
		txtBefore=new JTextField(5);		
		if(guiManager.state.beforeMargin>0)
			txtBefore.setText(String.valueOf(guiManager.state.beforeMargin));		
		JLabel label4=new JLabel (" chars before ");		
		
		txtAfter=new JTextField(5);
		if(guiManager.state.afterMargin>0)
			txtAfter.setText(String.valueOf(guiManager.state.afterMargin));
		JLabel label5=new JLabel (" chars after");		
		
		marginsCell.add(txtBefore);
		marginsCell.add(label4);
		marginsCell.add(Box.createRigidArea(new Dimension(10, 0)));
		marginsCell.add(txtAfter);
		marginsCell.add(label5);
		
		JPanel inputVerticalColumn=new JPanel();
		BoxLayout verticalInputLayout=new BoxLayout(inputVerticalColumn, BoxLayout.Y_AXIS);
		inputVerticalColumn.setLayout(verticalInputLayout);	
		
		inputVerticalColumn.add(outputFileCell);
		inputVerticalColumn.add(Box.createRigidArea(new Dimension(0, 15)));
		inputVerticalColumn.add(marginsCell);
		
		JPanel contentRow=new JPanel();
		BoxLayout horizontalContentLayout=new BoxLayout(contentRow, BoxLayout.X_AXIS);
		contentRow.setLayout(horizontalContentLayout);	
		
		
		contentRow.add(columnLabels);		
		contentRow.add(columnHelp);
		contentRow.add(Box.createRigidArea(new Dimension(15, 0)));
		contentRow.add(inputVerticalColumn);		
		
		contentRow.setBorder(emptyBorder);
		
		JPanel contentPane=new JPanel();
		contentPane.setBorder(myBorder);
		contentPane.add(contentRow);
		
		JPanel buttonsRow=new JPanel();
		BoxLayout horizontalButtonLayout=new BoxLayout(buttonsRow, BoxLayout.X_AXIS);
		buttonsRow.setLayout(horizontalButtonLayout);
		
		JButton btnBack=new JButton ("Back");
		btnBack.addActionListener(new BackHandler());	
		JButton btnSave=new JButton ("Save");		
		btnSave.addActionListener(new RunHandler());
		JButton btnCancel=new JButton ("Cancel");
		btnCancel.addActionListener(new CancelHandler());
		
		buttonsRow.add(btnBack);
		buttonsRow.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonsRow.add(btnSave);
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
	
	class SaveFileHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
	    {
			if(guiManager.state.solutions!=null && guiManager.state.solutions.size()>0)
			{
				String defaultDir=System.getProperty("user.dir") + File.separator;
				JFileChooser chooser=new JFileChooser(defaultDir);
				if(guiManager.state.outputFile!=null)
					chooser.setCurrentDirectory(guiManager.state.outputFile);
				int option=chooser.showSaveDialog(SavePanel.this);
				if (option == JFileChooser.APPROVE_OPTION)
				{
					guiManager.state.outputFile = chooser.getSelectedFile();
					txtFile.setText(guiManager.state.outputFile.getName());
				}				
			}
			else
			{
				JOptionPane.showMessageDialog(SavePanel.this,"There are no occurrences to save");
				return;
			}			
	    }
	}
	
	class RunHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			if(guiManager.state.outputFile==null)
			{
				JOptionPane.showMessageDialog(SavePanel.this,"Output file is not selected.");
				return;
			}
			if(guiManager.state.solutions==null|| guiManager.state.solutions.size()==0)
			{
				JOptionPane.showMessageDialog(SavePanel.this,"There are no occurrences to save.");
				return;
			}
			try
			{
				guiManager.state.beforeMargin=Integer.parseInt(txtBefore.getText());
				guiManager.state.afterMargin=Integer.parseInt(txtAfter.getText());
			}
			catch(NumberFormatException e)
			{
				guiManager.state.beforeMargin=0;
				guiManager.state.afterMargin=0;
			}
			if(guiManager.state.beforeMargin<0)
				guiManager.state.beforeMargin=0;
			if(guiManager.state.afterMargin<0)
				guiManager.state.afterMargin=0;
			SavePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			String errMsg=guiManager.state.writeSolutionsToFile();
			if(errMsg==null)
			{
				JOptionPane.showMessageDialog(SavePanel.this,"Save to file completed.");
			}
			else
			{
				JOptionPane.showMessageDialog(SavePanel.this,"Error writing to file: "+errMsg);
			}
			SavePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
	    }
	}
	 
	class CancelHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			System.exit(0);
	    }
	}
	
	class BackHandler implements ActionListener
	{
	    public void actionPerformed(ActionEvent ae)
	    {
	    	//disable buttons and texts, show message about running
			//when done - show save as form
			guiManager.outputFrame.setVisible(false);
			guiManager.outputFrame.dispose();
			
			guiManager.createAndShowInputGUI();
	    }
	}
	
	class SaveInfoHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			JOptionPane.showMessageDialog(SavePanel.this,saveExplanationString);
	    }
	}
	
	class MarginsInfoHandler implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
	    {
			JOptionPane.showMessageDialog(SavePanel.this,marginsExplanationString);
	    }
	}
}
