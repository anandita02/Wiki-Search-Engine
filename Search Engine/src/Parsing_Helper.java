
import java.util.HashMap;




public class Parsing_Helper
{
	//private static final Pattern UNDESIRABLES = Pattern.compile("[][(){},.;!?<>%|\\~@*-+^&%$#_-:;\"[=]0123'456789");

	public static String stop_words[]= {"coord","gr","tr","td","nbsp","http","https","www","a","about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear","clearly","come","could","d","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","i","if","important","in","interest","interested","interesting","interests","into","is","it","its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q","quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","x","y","year","years","yet","you","young","younger","youngest","your","yours","z"};
	
	static void handle_body(String str) 
    {
		/*
		  * BOTH STEMMER AND STOP WORDS USED
		*/
		
    	//System.out.println("BODY is: "+str);
		//str=str.replaceAll("[^A-Za-z]", "");
		
		str=str.toLowerCase();
		str=str.trim();
	    		
    	if(str.length()<=2)
    		return;
    	if(!file1.stop_words_hash.contains(str)) //Check for Stop Words
	{
    		/*
    		 * HANDLE STEMMING HERE
    		 */
    		stemmer s = new stemmer();
    		s.add(str.toCharArray(),str.length());
    		str=s.stem();
    	
    		if(str.length()<=2)
        		return;
    		
    		//System.out.print(str+" ");
	    	if(MyHandler.database.get(str)==null)
			{
	        	HashMap<String,mylist> loc_map=new HashMap<String,mylist>();
	        	MyHandler.database.put(str, loc_map);
	        	//System.out.print("Size: "+database.size());
	        }
	        
	    	//Database already contains string, append data
	        if(MyHandler.database.get(str)==null||!MyHandler.database.get(str).containsKey(MyHandler.curid))
	        {
	        	MyHandler.database.get(str).put(MyHandler.curid, new mylist());
	        }
	         	
	        MyHandler.database.get(str).get(MyHandler.curid).body++;
			
	}
    	
    }


	static void handle_category(String str) 
    {
		/*
		  * BOTH STEMMER AND STOP WORDS USED
		*/
    	
    		
    	str=str.toLowerCase();
            //str=str.replaceAll("[^A-Za-z]", "");
            str = str.trim();
    	if(str.length()<=2)
    		return;
		if(!file1.stop_words_hash.contains(str)) //Check for Stop Words
		{
    		/*
    		 * HANDLE PRUNING HERE
    		 */
    		stemmer s = new stemmer();
    		s.add(str.toCharArray(),str.length());
    		str=s.stem();
    		if(str.length()<=2)
        		return;
    	
	    	if(MyHandler.database.get(str)==null)
			{
	       		HashMap<String,mylist> loc_map=new HashMap<>();
	       		MyHandler.database.put(str, loc_map);
	       		//System.out.print("Size: "+database.size());
	       	}
	        
	    		
	    	//Database already contains string, append data
	       	if(MyHandler.database.get(str)==null||!MyHandler.database.get(str).containsKey(MyHandler.curid))
	       	{
	       		MyHandler.database.get(str).put(MyHandler.curid, new mylist());
	       	}
	         	
	       	MyHandler.database.get(str).get(MyHandler.curid).cat++;
		}
		
    }
	
	 public static void handle_infobox(String str)
	 {
	    	
		 /*
		  * BOTH STEMMER AND STOP WORDS USED
		  */
		//str=str.replaceAll("[^A-Za-z]", "");
		str=str.trim();
		str=str.toLowerCase();
		if(str.length()<=2||str.isEmpty())
			return;
				
		if(!file1.stop_words_hash.contains(str))
		{
			stemmer s = new stemmer();
	    	s.add(str.toCharArray(),str.length());
	    	str=s.stem();
	    	if(str.length()<=2||str.isEmpty())
				return;
   			/*
    			 * Code for entry into map
   			 */
	    			
	        	if(MyHandler.database.get(str)==null)
                        {
                            HashMap<String,mylist> loc_map=new HashMap<String,mylist>();
		         
                            MyHandler.database.put(str, loc_map);
		         
                            //System.out.print("Size: "+database.size());
		         
                        }
		         	//Database already contains string, append data
		         	//System.out.println("curid: "+curid);
		         	
		         	
		         	if(MyHandler.database.get(str)==null||!MyHandler.database.get(str).containsKey(MyHandler.curid))
		         	{
		         		MyHandler.database.get(str).put(MyHandler.curid, new mylist());
		         	}
		         	
		         	MyHandler.database.get(str).get(MyHandler.curid).infobox++;
		         	
				
			}
	    }
	    public static void handle_title(String str)
		{
	    
	         	str=str.toLowerCase();
	         	
	         	str=str.trim();
	         	if(str.length()==0||str.isEmpty())
	         		return;
	         	
	         	/*
	         	 * ONLY STEMMER, NO STOP WORD REMOVAL FOR TITLE
	         	 */
	         	
	         	stemmer s = new stemmer();
    			s.add(str.toCharArray(),str.length());
    			str=s.stem();
	         	
    			/*
    			 * Code for entry into map
    			 */
		        	if(MyHandler.database.get(str)==null)
		         	{
		         		HashMap<String,mylist> loc_map=new HashMap<>();
		         		MyHandler.database.put(str, loc_map);
		         		//System.out.print("Size: "+database.size());
		         	}
		         	//Database already contains string, append data
		         	
		         	
		         	
		         	if(MyHandler.database.get(str)==null||!MyHandler.database.get(str).containsKey(MyHandler.curid))
		         	{
		         		MyHandler.database.get(str).put(MyHandler.curid, new mylist());
		         	}
		         	
		         	MyHandler.database.get(str).get(MyHandler.curid).title++;
				
	        
	        //System.out.println("Printing data:"+database);

		
                }
    static void handle_extlink(String str) 
    {
		/*
		  * BOTH STEMMER AND STOP WORDS USED
		*/
    	//System.out.println("extlink: "+str);
    		
    	str=str.toLowerCase();
    	//str=str.replaceAll("[^A-Za-z]", "");
    	str.trim();
    	if(str.length()<=2)
    		return;
	if(!file1.stop_words_hash.contains(str)) //Check for Stop Words
	{
    		/*
    		 * HANDLE PRUNING HERE
    		 */
    		stemmer s = new stemmer();
    		s.add(str.toCharArray(),str.length());
    		str=s.stem();
    		if(str.length()<=2)
        		return;
    	
	    	if(MyHandler.database.get(str)==null)
		{
	       		HashMap<String,mylist> loc_map=new HashMap<String,mylist>();
	       		MyHandler.database.put(str, loc_map);
	       		//System.out.print("Size: "+database.size());
	       	}
	        
	    		
	    	//Database already contains string, append data
	       	if(MyHandler.database.get(str)==null||!MyHandler.database.get(str).containsKey(MyHandler.curid))
	       	{
	       		MyHandler.database.get(str).put(MyHandler.curid, new mylist());
	       	}
	         	
	       	MyHandler.database.get(str).get(MyHandler.curid).ref++;
        }
		
    }

}
