package simplegui;

public class SolutionRecord implements Comparable<SolutionRecord>
{
	public long startPos;
	public int fileID;
	public long start;
	public int length;
	public String substring="";
	public String toString()
	{
		return "file="+fileID+" start="+startPos+" <from "+start+" len= "+length+">";
	}
	
	public int compareTo(SolutionRecord another)
	{
		if(this.fileID<another.fileID)
			return -1;
		if(this.fileID>another.fileID)
			return 1;
		
		if(this.startPos<another.startPos)
			return -1;
		if(this.startPos>another.startPos)
			return 1;
		return 0;
	}
}
