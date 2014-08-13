package util;
import java.util.*;
import algorithm.*;

public class Debug 
{
	public static boolean DEBUG_MODE=false;
	private static void printPositionsTreeNodeRecursion(PositionsTreeNode node, int indent)
	{
		System.out.println(General.getIndent(indent)+node);
		for(int i=0;i<node.children.size();i++)
			printPositionsTreeNodeRecursion(node.children.get(i),indent+1);
	}
	
	public static void printPositionTree(PositionsTreeNode root)
	{
		System.out.println("ROOT: "+root);
		for(int i=0;i<root.children.size();i++)
			printPositionsTreeNodeRecursion(root.children.get(i),1);
	}
	
	private static void printKeywordTreeNodeRecursion(KeywordTreeNode node, int indent)
	{
		System.out.println(General.getIndent(indent)+node);
		Iterator <String> it=node.children.keySet().iterator();
		while(it.hasNext())
		{
			String c=it.next();
			KeywordTreeNode child=node.children.get(c);
			printKeywordTreeNodeRecursion(child,indent+1);
		}			
	}
	public static void printKeywordTree(KeywordTreeNode root)
	{
		System.out.println("ROOT: "+root);
		Iterator <String> it=root.children.keySet().iterator();
		while(it.hasNext())
		{
			String c=it.next();
			KeywordTreeNode child=root.children.get(c);
			printKeywordTreeNodeRecursion(child,1);
		}			
	}
}
