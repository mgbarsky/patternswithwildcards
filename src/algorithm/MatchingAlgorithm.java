package algorithm;
import java.util.*;


public class MatchingAlgorithm 
{
	PositionsTree posTree;
	KeywordTree  keyTree;
	char [] text;
	ArrayList <Integer> solutions;
	int numPatterns;
	//String [] exactPatterns;
	
	
	public MatchingAlgorithm(PositionsTree pt, KeywordTree kt, String txt,int exactPatternSetSize)
	{
		text=txt.toCharArray();
		posTree=pt;
		keyTree=kt;
		numPatterns=exactPatternSetSize;
		//exactPatterns=tokens;
	}
	
	public List <Integer> getAllOccurrences()
	{
		
		solutions=new ArrayList <Integer>();
		//match to keyTree starting from each position in text
		for(int i=0;i<text.length;i++)
		{
			int currStart=i;
			int pointer=currStart;
			boolean misMatch=false;
			KeywordTreeNode currNode=keyTree.root;
			while(!misMatch && pointer<text.length)
			{
				char currChar=text[pointer++];
				String key=Character.toString(currChar);
				if(currNode.children.containsKey(key))
				{
					currNode=currNode.children.get(key);
				}
				else
					misMatch=true;
				if(!misMatch)
				{
					//if leaf is reached - increment counter in all map cells in positionstree for a corresponding position
					//which depends on level and current branch of position tree
					if(currNode.patternIndexes!=null && currNode.patternIndexes.size()>0)
					{
						for(int j=0;j<currNode.patternIndexes.size();j++)
						{
							int index=currNode.patternIndexes.get(j);
							markPositions(currStart,index);
						}
					}
				}
			}
		}
		
		//collect solutions
		traverseAndCollectSolutionsRecursion(posTree.root);
		
		return solutions;
	}
	
	private void traverseAndCollectSolutionsRecursion(PositionsTreeNode posNode)
	{
		if(posNode.children==null || posNode.children.size()==0) //leaf - collect
		{
			Iterator<Integer> it=posNode.countByPosition.keySet().iterator();
			while(it.hasNext())
			{
				int posInText=it.next();
				int count=posNode.countByPosition.get(posInText);
				if(count>=numPatterns)
				{
					solutions.add(posInText);
				}
			}
		}
		else
		{
			for(int i=0;i<posNode.children.size();i++)
			{
				traverseAndCollectSolutionsRecursion(posNode.children.get(i));
			}
		}
	}
	
	private void traverseAndMarkRecursion (PositionsTreeNode posNode, int startInText, 
			int currLevel, int patternIndex, int startInPattern)
	{
		int newStartInPattern=startInPattern;
		if(currLevel==patternIndex)
			newStartInPattern=posNode.startPos;
		
		if(posNode.children==null || posNode.children.size()==0) //reached leaf
		{
			int starttomark=startInText-newStartInPattern;
			if(starttomark>=0)
			{
				int count=0;
				if(posNode.countByPosition.containsKey(starttomark))
					count=posNode.countByPosition.get(starttomark);
				count++;
				posNode.countByPosition.put(starttomark, count);
			}
		}
		else
		{
			for(int i=0;i<posNode.children.size();i++)
			{
				traverseAndMarkRecursion(posNode.children.get(i),startInText,
						currLevel+1,patternIndex,newStartInPattern);
			}
		}
			
	}
	private void markPositions(int startInText, int patternIndex)
	{
		traverseAndMarkRecursion(posTree.root,startInText, 0, patternIndex,-1);
	}
}
