package util;
import java.io.File;




public class General 
{
	public static String OUTPUT_DELIMITER ="\t";
	
	public static String arrayToString(String [] arr)
	{
		if(arr==null || arr.length==0)
			return "";
		StringBuffer ret=new StringBuffer();
		ret.append(arr[0]);
		for(int i=1;i<arr.length;i++)
			ret.append("; ").append(arr[i]);
		return ret.toString();
	}

	public static String bytesToString(byte [] arr)
	{
		StringBuffer ret=new StringBuffer();
		
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]!=10 && arr[i]!=13)
				ret.append((char)arr[i]);
		}
		return ret.toString();
	}

	public static String getIndent(int n)
	{
		String ret="";
		for(int i=0;i<n;i++)
			ret+=" ";
		return ret;
	}
	
	public static String filesToCaption(File [] files)
	{
		StringBuffer ret=new StringBuffer("");
		if(files!=null && files.length>0)
		{
			ret.append(files[0].getName());
			for(int i=1;i<files.length;i++)
			{
				ret.append("; ").append(files[i].getName());
			}
		}
		return ret.toString();
	}
}
