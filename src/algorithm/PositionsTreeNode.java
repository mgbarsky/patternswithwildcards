package algorithm;
import java.util.*;

public class PositionsTreeNode 
{
	public PositionsTreeNode nextSibling=null;
	public List <PositionsTreeNode> children=new ArrayList <PositionsTreeNode>();
	public Map <Integer,Integer> countByPosition=null;
	public int startPos=0;
	
	public String toString()
	{
		return "start pos="+startPos+" num children="+children.size()+" counts by pos="+countByPosition;
	}
}
