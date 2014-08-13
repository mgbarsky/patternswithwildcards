package algorithm;
import java.util.*;
import util.*;

public class KeywordTree 
{
	String [] exactPatterns;
	KeywordTreeNode root=new KeywordTreeNode("");
	public KeywordTree(String [] tokens)
	{
		exactPatterns=tokens;
	}
	
	public void buildTree()
	{
		for(int i=0;i<exactPatterns.length;i++)
		{
			addPatternFromRoot(exactPatterns[i],i);
		}
		
		if(Debug.DEBUG_MODE)
			Debug.printKeywordTree(root);
	}
	
	private void addPatternFromRoot(String pattern, int patternIndex)
	{
		char [] characters=pattern.toCharArray();
		KeywordTreeNode curr=root;
		for(int i=0;i<characters.length;i++)
		{
			String currChar=Character.toString(characters[i]);
			if(curr.children.containsKey(currChar))
			{
				curr=curr.children.get(currChar);
			}
			else
			{
				KeywordTreeNode newNode=new KeywordTreeNode(currChar);
				curr.children.put(currChar, newNode);
				curr=curr.children.get(currChar);
			}
			if(i==characters.length-1) //leaf
			{
				if(curr.patternIndexes==null)
					curr.patternIndexes=new ArrayList <Integer>();
				curr.patternIndexes.add(patternIndex);
			}
		}
	}
}
