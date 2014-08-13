package simplegui;
import java.io.*;

//sample. input file Lalala.txt
//pattern the(1-4)e
public class ConsoleApplication 
{
	public static void main(String [] args)
	{
		if(args.length<4)
		{
			System.out.println("Usage: java -Xms512m -Xmx1024m simplegui.ConsoleApplication " +
					"<input folder> <input file> <pattern> <output file> [<from>] [<to>]");
			return;
		}
		String inputfolder=args[0];
		String inputFileName=inputfolder+System.getProperty("file.separator")+args[1];
		String pattern=args[2];
		String outputFileName=inputfolder+System.getProperty("file.separator")+args[3];
		
		File [] inputFiles=new File[1];
		
		inputFiles[0]=new File(inputFileName);			
		
		
		State state=new State();
		if(args.length>5)
		{
			state.beforeMargin=Integer.parseInt(args[4]);
			state.afterMargin=Integer.parseInt(args[5]);
		}
		
		state.inputFiles=inputFiles;
		state.outputFile=new File(outputFileName);
		String errMsg=state.validate(pattern);
		if(errMsg!=null)
		{
			System.out.println(errMsg);
			return;
		}
		
		state.setPattern(pattern);
		state.searchFiles();
		
		System.out.println(state.solutions.size()+" solutions matched your search");
		
		if(state.solutions.size()>0)
		{
			errMsg=state.writeSolutionsToFile();
			if(errMsg!=null)
			{
				System.out.println(errMsg);
				return;
			}
		}
			
	}
}
