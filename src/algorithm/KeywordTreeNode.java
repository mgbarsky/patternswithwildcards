package algorithm;
import java.util.*;
public class KeywordTreeNode 
{
	
	public String nodeCharacter;
	public HashMap <String,KeywordTreeNode> children=new HashMap <String,KeywordTreeNode>();
	List <Integer> patternIndexes;
	
	public KeywordTreeNode (String label)
	{
		nodeCharacter=label;
	}
	public String toString()
	{
		
		return "label="+nodeCharacter+" num children="
			+children.size()+" patternIDs="+patternIndexes;
	}
}
