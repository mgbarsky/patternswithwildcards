package util;
import java.io.*;
import java.util.*;
public class FastaToTextFiles 
{
	private static String removeIllegalCharacters(String rawString, HashMap<Character,Object> invalidChars)
	{
		char [] chars=rawString.toCharArray();
		StringBuffer newSB=new StringBuffer();
		for(int i=0;i<chars.length;i++)
		{
			if(!invalidChars.containsKey(chars[i]))
				newSB.append(chars[i]);
		}
		return newSB.toString();
	}
	
	public static void main(String [] args)
	{
		if(args.length<1)
		{
			System.out.println("To run: java -Xms512m -Xmx1024m util.FastaToTextFiles <fastafilename>");
			return;
		}
		HashMap<Character,Object> invalidChars=new HashMap<Character,Object>();
		//\ / : * ? " < > | , .
		invalidChars.put('\\', null);
		invalidChars.put('/', null);
		invalidChars.put(':', null);
		invalidChars.put('*', null);
		invalidChars.put('?', null);
		invalidChars.put('"', null);
		invalidChars.put('<', null);
		invalidChars.put('>', null);
		invalidChars.put('|', null);
		invalidChars.put(',', null);
		invalidChars.put('.', null);
		try
		{			
			FileInputStream fis = new FileInputStream(args[0]);
			DataInputStream   dis = new DataInputStream(fis);
			BufferedReader in= new BufferedReader(new InputStreamReader(dis));
			
			//FileWriter fwriter = new FileWriter(processedInputFile);
			BufferedWriter out = null;
			
			String line=null;
			String currFileName=null;			
			
			while ((line = in.readLine()) != null) 
	        {
				if(line.startsWith(">")) //new file named >name
				{
					currFileName=line.substring(1,line.length()).trim();
					currFileName=removeIllegalCharacters(currFileName,invalidChars);
					currFileName+=".txt";
					if(out!=null)
						out.close();
					FileWriter fwriter = new FileWriter(currFileName);
					out=new BufferedWriter(fwriter);
				}
				else
				{
					out.write(line+System.getProperty("line.separator"));
				}
	        }
			in.close();
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();			
		}
	}
}
