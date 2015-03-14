
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
	public static Set<String> stop_words_hash = new HashSet<>();
    public static void main(String[] args) 
    {

        long start = System.currentTimeMillis();
        for(int i=0;i<435;i++)
    	{
    		stop_words_hash.add(Parsing_Helper.stop_words[i]);
    		
    	}
	    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	    try 
	    {
	    	System.out.println("STARTED");
	        SAXParser saxParser = saxParserFactory.newSAXParser();
	        MyHandler handler = new MyHandler();
	       
	       saxParser.parse(new File("src/com/wiki_small3gb.xml"), handler);
		// saxParser.parse(new File("/home/ruchir/Large.xml"), handler);
		    System.out.println("No of files: "+count);
		    long end = System.currentTimeMillis();
		    System.out.println("Time taken in parsing: "+(end - start) + " ms");
                    
                  
                    start = System.currentTimeMillis();
                    //Files are parsed, now merge them
                   // merge_function.merging();
                     end = System.currentTimeMillis();
                      System.out.println("Time taken in Merging: "+(end - start) + " ms");
	    }
	    catch (Exception e) 
	    {
                System.out.println("Exception"+e.getLocalizedMessage());
	    }
    
    }
 
}
