/*
AUTHOR: RUCHIR SHARMA
*/

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


 
public class file1 
{
	static boolean first_entry=false;
	static  int base;
	static int count=0;
	public static Set<String> stop_words_hash = new HashSet<String>();
    public static void main(String[] args) 
    {

        long start = System.currentTimeMillis();;
        for(int i=0;i<436;i++)
    	{
    		stop_words_hash.add(Parsing_Helper.stop_words[i]);
    		
    	}
	    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	    try 
	    {
	    	
	        SAXParser saxParser = saxParserFactory.newSAXParser();
	        MyHandler handler = new MyHandler();
	       
		    //saxParser.parse(new File("src/com/test.xml"), handler);
		    String filename = ""+args[0]+"";
		    saxParser.parse(new File(filename), handler);
		   // System.out.println("No of files: "+count);
		    long end = System.currentTimeMillis();;
		    System.out.println((end - start) + " ms");
	    }
	    catch (Exception e) 
	    {
	        //e.printStackTrace();
	    }
    
    }
 
}
