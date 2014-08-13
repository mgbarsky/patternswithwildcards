package algorithm;
import java.util.*;
import util.*;

public class PositionsTree 
{
	PositionsTreeNode root=new PositionsTreeNode();
	String [] tokens=null;
	int [] leftStartPositions;
	Interval [] wcIntervals;
	public String [] exactPatterns;
	public int maxPatternLength=0;
	public PositionsTree(String pattern)
	{
		tokens=pattern.split("[()]");
	}
	
	
	public String buildTree()
	{
		String msg=null;
		int numTokens=tokens.length;
		int numWildCards=(numTokens-1)/2;
		wcIntervals=new Interval[numWildCards];
		int numExactPatterns=numTokens-numWildCards;
		exactPatterns=new String [numExactPatterns];
		
		for(int i=0,j=0;i<numTokens;i=i+2)
		{
			exactPatterns[j++]=tokens[i];
		}
		
		for(int i=1,j=0;i<numTokens;i=i+2,j++)
		{
			String [] interval=tokens[i].split("[-]");
			wcIntervals[j]=new Interval();
			wcIntervals[j].from=Integer.parseInt(interval[0]);
			wcIntervals[j].width=1;
			if(interval.length==2)
			{
				wcIntervals[j].to=Integer.parseInt(interval[1]);	
				wcIntervals[j].width=wcIntervals[j].to-wcIntervals[j].from+1;
			}
		}
		leftStartPositions=new int[numExactPatterns];
		
		leftStartPositions[0]=0;
		for(int i=1;i<numExactPatterns;i++)
		{
			int currLeftStartPos=leftStartPositions[i-1]+exactPatterns[i-1].length()
						+wcIntervals[i-1].from;
			leftStartPositions[i]=currLeftStartPos;	
		}
		
		//recursively add children to position tree
		addChildPositions(root, 0);
		
		if(Debug.DEBUG_MODE)
			Debug.printPositionTree(root);
		
		for(int i=0;i<numExactPatterns;i++)
			maxPatternLength+=exactPatterns[i].length();
		for(int i=0;i<numWildCards;i++)
		{
			if(wcIntervals[i].width==1)
				maxPatternLength+=wcIntervals[i].from;
			else
				maxPatternLength+=wcIntervals[i].to;
		}
			
		return msg;
	}
	
	private void addChildPositions(PositionsTreeNode parent, int parentLevel)
	{
		if(parentLevel==exactPatterns.length-1) //last level
		{
			parent.countByPosition=new HashMap<Integer,Integer>();
			return;
		}
		for(int i=0;i<wcIntervals[parentLevel].width;i++)
		{
			PositionsTreeNode child=new PositionsTreeNode();
			child.startPos=parent.startPos+exactPatterns[parentLevel].length()
				+wcIntervals[parentLevel].from+i;
			parent.children.add(child);
			addChildPositions(child, parentLevel+1);
		}
	}
	
	public void resetMarkedHashMaps()
	{
		resetPositionsTreeNodeRecursion(this.root);
	}
	
	private void resetPositionsTreeNodeRecursion(PositionsTreeNode node)
	{
		if(node.children==null|| node.children.size()==0)
		{
			node.countByPosition=null;
			node.countByPosition=new HashMap <Integer,Integer>();
			return;
		}
		for(int i=0;i<node.children.size();i++)
			resetPositionsTreeNodeRecursion(node.children.get(i));
	}
	
}
