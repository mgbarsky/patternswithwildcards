package simplegui;
import java.io.*;

import java.util.*;

import util.*;
import algorithm.*;

public class State 
{
	public File [] inputFiles;
	public String strPattern;
	public File outputFile;
	PositionsTree positionsTree;
	KeywordTree keywordTree;
	int numExactPatterns;
	public int maxPatternLength;
	List <SolutionRecord> solutions=new ArrayList <SolutionRecord> ();
	List <SolutionRecord> solutionsWithduplicates=new ArrayList <SolutionRecord> ();
	public int beforeMargin;
	public int afterMargin;
	public int currentOffset;
	HashMap <Integer,Object> filesWithSolutions;
	int currSolutionsPointer;
	public void setPattern(String pattern)
	{
		strPattern=pattern;
		positionsTree=new PositionsTree(pattern);
		positionsTree.buildTree();
		maxPatternLength=positionsTree.maxPatternLength;
		keywordTree=new KeywordTree(positionsTree.exactPatterns);
		keywordTree.buildTree();
		numExactPatterns=positionsTree.exactPatterns.length;
	}
	
	private boolean validString(String str)
	{
		char [] chars=str.toCharArray();
		for(int i=0;i<chars.length;i++)
			if(chars[i]=='(' || chars[i]==')' || chars[i]=='-')
				return false;
		return true;
	}
	
	private boolean validWildCard(String str)
	{
		String [] strPositions=str.split("[-]");
		if(strPositions.length!=1 && strPositions.length!=2)
			return false;
		int from=-1;
		int to=-1;
		try
		{
			from=Integer.parseInt(strPositions[0]);
			if(strPositions.length==2)
			{
				to=Integer.parseInt(strPositions[1]);
				if(from>=to)
					return false;
			}
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
	public String validate(String pattern)
	{
		String msg=null;
		if(pattern==null || pattern.equals(""))
			return "No pattern entered.";
		
		String [] tokens=pattern.split("[()]");
		boolean isCharString=true;
		for(int i=0;i<tokens.length;i++)
		{
			if(isCharString)
			{
				if(!validString(tokens[i]))
					return "Invalid pattern format in string: check token "+(i+1)+".";
				isCharString=false;
			}
			else
			{
				if(!validWildCard(tokens[i]))
					return "Invalid pattern format in wild card: check token "+(i+1)+".";
				isCharString=true;
				if(i==tokens.length-1)
					return "Invalid pattern format: wild card at the end of the pattern.";
			}			
		}
		return msg;
	}
	
	public boolean searchFiles()
	{
		filesWithSolutions=new HashMap<Integer,Object>();
		solutions=new ArrayList <SolutionRecord> ();
		solutionsWithduplicates=new ArrayList <SolutionRecord> ();
		for(int i=0;i<inputFiles.length;i++)
		{			
			if(!searchFile(i))
				return false;
		}
		Collections.sort(solutionsWithduplicates);
		//remove possible duplicates at the end of line
		long prevStart=-1;
		for(int i=0;i<solutionsWithduplicates.size();i++)
		{
			SolutionRecord rec=solutionsWithduplicates.get(i);
			if(rec.startPos!=prevStart)
			{
				solutions.add(rec);
				prevStart=rec.startPos;
			}
		}
		solutionsWithduplicates=null;
		return true;
	}
	
	public boolean searchFile(int fileIndex)
	{
		try
		{			
			FileInputStream fis = new FileInputStream(inputFiles[fileIndex]);
				
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			String line=null;
			int offset=0;
			String text=null;
			int lineNumber=0;
			while ((line = in.readLine()) != null)
            {
                if(lineNumber==0)
                {
                	text=line.trim().toLowerCase();
                }
                else
                {
                	offset+=Math.max(0, text.length()-maxPatternLength);
                	text=text.substring(Math.max(0, text.length()-maxPatternLength))+line.trim().toLowerCase();                	
                } 
                MatchingAlgorithm algo=new MatchingAlgorithm(positionsTree, 
        				keywordTree, text,numExactPatterns);
        		List <Integer> partialSolutions=algo.getAllOccurrences();
        		
        		for(int i=0;i<partialSolutions.size();i++)
        		{
        			int start=partialSolutions.get(i);
        			SolutionRecord rec=new SolutionRecord();
        			rec.fileID=fileIndex;
        			rec.startPos=start+offset;
        			
        			solutionsWithduplicates.add(rec);
        			filesWithSolutions.put(fileIndex, null);
        		}
        		lineNumber++;
        		positionsTree.resetMarkedHashMaps();
            }

			in.close();
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		if(Debug.DEBUG_MODE)
		{
			System.out.println(solutionsWithduplicates);
		}
		
		
		return true;
	}
	
	private boolean writeSolutionsForFile(int fileID, BufferedWriter out)
	{
		int currFileID=fileID;
		int i=currSolutionsPointer;
		
		try
		{
			File processedInputFile=new File("tmp_"+fileID);
			FileInputStream fis = new FileInputStream(processedInputFile);
			DataInputStream   dis = new DataInputStream(fis);
			BufferedReader in= new BufferedReader(new InputStreamReader(dis));
			
			String line=null;
			String prevLine=null;
			long lineStart=0;
			long lineEnd=0;
			
			
			while ((line = in.readLine()) != null) 
	        {
				lineEnd+=line.length();
				
				while(i<solutions.size() 
						&& solutions.get(i).fileID==currFileID 
						&& solutions.get(i).start+solutions.get(i).length<lineEnd)
				{
					//add substring, remove possible rare duplicates
					
					int startInLine=(int)(solutions.get(i).start-lineStart);
					if(startInLine>=0)
					{
						solutions.get(i).substring=line.substring(startInLine, startInLine+solutions.get(i).length).toLowerCase();
					}
					else
					{
						int startInPrevLine=prevLine.length()+startInLine;
						solutions.get(i).substring=prevLine.substring(startInPrevLine).concat(
								line.substring(0, solutions.get(i).length+startInLine)).toLowerCase();
					}
										
					i++;
				}
	
				lineStart=lineEnd;
				prevLine=line;
	        }
			
			int newSolutionPointer=i;
			//write solutions to file
			StringBuffer sb=null;
			for(int j=currSolutionsPointer;j<newSolutionPointer;j++)
			{
				SolutionRecord rec=solutions.get(j);
				
				sb=new StringBuffer();
				sb.append(inputFiles[fileID].getName());
				sb.append(General.OUTPUT_DELIMITER).append(rec.startPos);
				sb.append(General.OUTPUT_DELIMITER).append(rec.start);
				sb.append(General.OUTPUT_DELIMITER).append(rec.substring);
				sb.append(System.getProperty("line.separator"));
				out.write(sb.toString());					
			}
			in.close();
			
			//set pointer in solutions to a new position
			currSolutionsPointer=newSolutionPointer;
			processedInputFile.delete();
			return true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}		
	}
	
	private boolean rewriteInputFile(int fileID,int minLinelength)
	{
		try
		{
			File processedInputFile=new File("tmp_"+fileID);
			FileInputStream fis = new FileInputStream(inputFiles[fileID]);
			DataInputStream   dis = new DataInputStream(fis);
			BufferedReader in= new BufferedReader(new InputStreamReader(dis));
			
			FileWriter fwriter = new FileWriter(processedInputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String line=null;
			StringBuffer newLine=new StringBuffer();
			
			int currLineLength=0;
			while ((line = in.readLine()) != null) 
	        {
				newLine.append(line);
				currLineLength+=line.length();
				if(currLineLength>minLinelength+minLinelength)
				{
					//write new larger line and restart buffer and line length
					newLine.append(System.getProperty("line.separator"));
					out.write(newLine.toString());
					newLine=new StringBuffer();
					currLineLength=0;
				}
	        }
			if(currLineLength>0)
			{
				//write remaining line
				out.write(newLine.toString());
			}
			in.close();
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public String writeSolutionsToFile()
	{
		
		for(int i=0;i<solutions.size();i++)
		{
			SolutionRecord rec=solutions.get(i);
			rec.start=rec.startPos-beforeMargin;
			
			long delta=0;
			if(rec.start<0)
			{
				delta=-rec.start;
				rec.start=0;
			}
			if(afterMargin==0)
				rec.length=beforeMargin-(int)delta+maxPatternLength;
			else
				rec.length=beforeMargin-(int)delta+Math.max(maxPatternLength,afterMargin);			
		}
		String errMsg=null;
		try
		{
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			//write titles of columns
			StringBuffer sb=new StringBuffer();
			sb.append("File name");
			sb.append(General.OUTPUT_DELIMITER).append("Pattern start pos");
			sb.append(General.OUTPUT_DELIMITER).append("Substring start pos");
			sb.append(General.OUTPUT_DELIMITER).append("Substring");
			sb.append(System.getProperty("line.separator"));
			out.write(sb.toString());
			int prevFileID=-1;
			currSolutionsPointer=0;
			int substringLength=this.beforeMargin+Math.max(this.afterMargin,this.maxPatternLength);
			for(int i=0;i<inputFiles.length;i++)
			{
				if(i!=prevFileID)
				{
					if(filesWithSolutions.containsKey(i))
					{
						rewriteInputFile(i,substringLength);
						if(!writeSolutionsForFile(i,out))
						{
							out.close();
							return "Error saving solutions for input file "+inputFiles[i].getName();
						}
					}
					prevFileID=i;
				}
			}
		
			out.close();
		}		
		catch(Exception e)
		{
			return e.getMessage();
		}
		return errMsg;
		
	}
	
	
}
