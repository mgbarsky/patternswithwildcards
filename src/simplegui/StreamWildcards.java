package simplegui;
/**
 * This program has GUI, so the usage is self-explanatory
 * 
 * It uses a keyword tree, generated from the input pattern with wildcards
 * Then the input file is streamed through the keyword tree, directly from disk
 * 
 * Thus we can find patterns with wildcards in virtually unlimited inputs
 * It uses readLine(), so it is important that there would be lines in the input file
 * 
 * @author Marina Barsky
 * Copyright 2011 UVic
 * Written by Marina Barsky (mgbarsky@gmail.com)
 * Released under the GPL
 * 
 * To test the program, you can select provided input file: Lalala.txt
 * and find all occurences of the pattern: ther(1-4)e
 * or you can search for Zinc finger transcription factor 'tgt(6)tgt(36-42)cat(6)cat' in the entire human genome
 */
public class StreamWildcards {
	public static void main(String[] args) 
	{
		MainWindow window=new MainWindow();
		
		window.createAndShowInputGUI();
	}
}
