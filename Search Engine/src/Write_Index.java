

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;

public class Write_Index 
{
	  static void write_database_tofile()  throws Exception
	    {
                int val=file1.count/5000;
	    	File file = new File("src/com/data/DATABASE"+(val));
	    	// System.out.print("Here");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			// TODO Auto-generated method stub
	    	for (Entry<String, HashMap<String, mylist>> entry : MyHandler.database.entrySet()) 
	    	{
	    		String word_name=entry.getKey();
	    		HashMap<String,mylist> loc_map = new HashMap<String,mylist>(entry.getValue());
	    		bw.write(word_name+"-");
	    		for(Entry<String,mylist> loc_entry:loc_map.entrySet())
	    		{
	    			String doc_name = loc_entry.getKey();
	    			mylist obj = loc_entry.getValue();
	    			String cat="",info="",body="",title="",ref="";
	    			if(obj.title!=0)
	    				title=""+obj.title;
	    			if(obj.body!=0)
	    				body=""+obj.body;
	    			if(obj.cat!=0)
	    				cat=""+obj.cat;
	    			if(obj.infobox!=0)
	    				info=""+obj.infobox;
	    			if(obj.ref!=0)
	    				ref=""+obj.ref;
	    			String append = doc_name+":"+title+","+cat+","+info+","+body+","+ref+";";
	    			bw.write(append);
	    		}
	    		bw.write("\n");
	    	}

			bw.close();
			MyHandler.database.clear();
		
		}


}
