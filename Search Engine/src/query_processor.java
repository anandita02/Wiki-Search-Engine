
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author ruchir
 */
class title_mapping
{
    long doc_id;
    long offset;

    title_mapping(long doc_id, long i) {
    this.doc_id=doc_id;
    this.offset=i;
    }

   
}
public class query_processornew
{
     public static void main(String[] args) throws Exception
    {
    	String path_index = ""+args[0]+"";
    	//System.out.println("PAths is:"+path_index);
        //Create stop words hashset
         Set<String> stop_words_hash = new HashSet<>();
          for(int i=0;i<435;i++)
    	{
    		stop_words_hash.add(Parsing_Helper.stop_words[i]);
    		
    	}
         //Read Query from user now
       BufferedReader brinp = new BufferedReader(new InputStreamReader(System.in));
        //Files are parsed, now merge them
                  
       // System.out.println("Reading Secondary index");
        //Read secondary index into main memory
        BufferedReader br = new BufferedReader(new FileReader("/home/ruchir/wikiSupportFiles/Tertiary_Index"));
        
        //DS to hold secondary index
        List<word> ter_index = new ArrayList<word>();
        Comparator<word> c = new Comparator<word>() {
      public int compare(word u1, word u2) 
      {
        return u1.term.compareTo(u2.term);
      }
    };
        String next = br.readLine();
        while(next!=null)
        {   
           // System.out.println("next is:"+next);
            String term = next.substring(0,next.indexOf(":"));
             long   from_index = Long.parseLong(next.substring(next.indexOf(":")+1));
             word w = new word(term,from_index);
             
             ter_index.add(w);
              next = br.readLine();
        }
        br.close();
        
        //System.out.println("Tertiary index read, size is: "+ter_index.size());
        
        //Read Title Mapping index into a map
         BufferedReader br1 = new BufferedReader(new FileReader("/home/ruchir/wikiSupportFiles/Index_Ter"));
          List<title_mapping> title_index = new ArrayList<title_mapping>();
         Comparator<title_mapping> c1;
         c1 = new Comparator<title_mapping>() 
         {
             public int compare(title_mapping u1, title_mapping u2)
             {
                 return (int)(u1.doc_id-u2.doc_id);
             }
         };
          
          String read_title = br1.readLine();
        while(read_title!=null)
        {
            String doc_id = read_title.substring(0,read_title.indexOf("="));
            long offset = Long.parseLong(read_title.substring(read_title.indexOf("=")+1));
            title_mapping e = new title_mapping(Long.parseLong(doc_id),offset);
            title_index.add(e);
            read_title = br1.readLine();
        }
        br1.close();
        //System.out.println("title mapping read, size is: "+title_index.size()+" First is: "+title_index.get(0).doc_id+":-"+title_index.get(0).offset);
        
        
        Map<String,Integer> master_data = new HashMap<>();
       
  

      	int no_of_queries  = Integer.parseInt(brinp.readLine());
        for(int looo=0;looo<no_of_queries;looo++)
        {
        System.out.println("");
         String read = brinp.readLine();
      
      
        read=read.replaceAll("!@#$%+^&;*'.><", "");
        // System.out.println("query: "+read);
         long start = System.currentTimeMillis();
        StringTokenizer st = new StringTokenizer(read," ");
        
        
          
        //Perform optimizations
        while(st.hasMoreElements())
        {
            String str=st.nextToken().toLowerCase();
           int lbcount=2,ltcount=10000,lecount=1,lccount=30,licount=25;
         //   int bcount=5,tcount=1000,ecount=10,ccount=100,icount=50,count_lines=0;
           //Check For Field Queries
           if(str.contains("t:"))
           {
               ltcount*=1000;
              
               str=str.substring(str.indexOf(":")+1);
           }
           if(str.contains("b:")||str.contains("r:"))
           {
              
               lbcount*=1000;
               
               str=str.substring(str.indexOf(":")+1);
           }
           if(str.contains("e:"))
           {
               
               lecount*=1000;
            
               str=str.substring(str.indexOf(":")+1);
           }
           if(str.contains("c:"))
           {
          
               lccount*=1000;
               
               str=str.substring(str.indexOf(":")+1);
           }
           if(str.contains("i:"))
           {
               licount*=1000;
               str=str.substring(str.indexOf(":")+1);
           }
       
           // System.out.println("Searching for"+str);
            str=str.trim();
             if(stop_words_hash.contains(str)) 
            {
                //System.out.println("STOP WORD"+str);
                continue;
            }
            stemmer s = new stemmer();
            s.add(str.toCharArray(),str.length());
            str=s.stem();
           
            //Search position in secondary index
            int index_of_word = Collections.binarySearch(ter_index, new word(str, 0), c);
             long start_index;
            if(index_of_word<0)
            {
                index_of_word*=-1;
                //System.out.println("H95ere"+str);
                if(index_of_word>2)
                    start_index = ter_index.get(index_of_word-2).offset;
                else
                    start_index=0;
            }
            else if(index_of_word>3)
                start_index= ter_index.get(index_of_word-3).offset;
            else
                start_index=0;
           
            //Retrieve next 200 results from primary index and search for the term
            long database_index=-1;
            RandomAccessFile file = new RandomAccessFile("/home/ruchir/wikiSupportFiles/Secondary_Index", "r");
            file.seek(start_index);
            for(int i=0;i<125;i++)
            {
                //0-2614?24915776:3,,,,;24146273:3,,,,;
                String next_record = file.readLine();
                String term_next = next_record.substring(0,next_record.indexOf(":"));
                if(str.equals(term_next))
                {
                   // System.out.println("Matched"+term_next+" at "+i);
                    database_index=Long.parseLong(next_record.substring(next_record.indexOf(":")+1));
                }
            }
            if(database_index==-1)
            {
                //System.out.println("Not Found..!!");
                        
            }
            else
            {
                RandomAccessFile file1 = new RandomAccessFile(path_index, "r");
                file1.seek(database_index);
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<35000;i++)
                {
                    char ch = (char) file1.readByte();
                    if(ch=='\n')
                        break;
                    sb.append(ch);
                }
                
                String next_record = sb.toString();
                
                
            
             //  System.out.println("Read: "+next_record);
                String word_name = next_record.substring(0,next_record.indexOf("-"));
              //  System.out.println("Word is: "+word_name);
                next_record = next_record.substring(next_record.indexOf("-")+1);
                int no_of_rec = Integer.parseInt(next_record.substring(0,next_record.indexOf("?")));
                next_record = next_record.substring(next_record.indexOf("?")+1);
                
                    //Word Found 24162659
                
                 //  System.out.println("Found: !! "+no_of_rec);
                    int local_counter=0;
                    StringTokenizer st1 = new StringTokenizer(next_record,";");
                  //  System.out.println("count is: "+st1.countTokens());
                    while(st1.hasMoreElements())
                    {
                        
                        local_counter++;
                        if(local_counter>50000)
                            break;
                        String next_doc = st1.nextToken();
                        if(!st1.hasMoreElements()&&!next_record.endsWith(";"))
                        {
                            //IF this is the last token, then dont perform the operations
                            break;
                        }
                        String doc_id  = next_doc.substring(0, next_doc.indexOf(":"));
                       
                        next_doc = next_doc.substring(next_doc.indexOf(":")+1);
                        
                      // System.out.println("Doc id is: "+doc_id);
                        
                        String one = next_doc.substring(0,next_doc.indexOf(","));
                        next_doc = next_doc.substring(next_doc.indexOf(",")+1);
                        
                        String two = next_doc.substring(0,next_doc.indexOf(","));
                        next_doc = next_doc.substring(next_doc.indexOf(",")+1);
                        
                        String three = next_doc.substring(0,next_doc.indexOf(","));
                        next_doc = next_doc.substring(next_doc.indexOf(",")+1);
                        
                        String four = next_doc.substring(0,next_doc.indexOf(","));
                        next_doc = next_doc.substring(next_doc.indexOf(",")+1);
                        
                        String five = next_doc;
                       // System.out.println(one+":"+two+":"+three+":"+four+":"+five);
                        int body=0,title=0,info=0,cat=0,ext=0;
                        //title+","+cat+","+info+","+body+","+ref+";";
                        if(!one.isEmpty())
                        {
                            title=Integer.parseInt(one);
                        }
                        if(!two.isEmpty())
                        {
                            cat=Integer.parseInt(two);
                        }
                        if(!three.isEmpty())
                        {
                            info=Integer.parseInt(three);
                        }
                        if(!four.isEmpty())
                        {
                            body=Integer.parseInt(four);
                        }
                        if(!five.isEmpty())
                        {
                            ext=Integer.parseInt(five);
                        }
                        int total = (body*lbcount) +(title*ltcount)+(ext*lecount)+(cat*lccount)+(info*licount); 
                        if(master_data.containsKey(doc_id))
                        {
                            int cur = master_data.get(doc_id);
                            master_data.remove(doc_id);
                            total+=cur*15;
                           
                            
                        }
                        double log_data = (double)15000000/no_of_rec;
                        int log_val = (int)Math.log(log_data);
                        total = (int)total *log_val; 
                        master_data.put(doc_id,total);
                        
                    }
                    
             
                    
                
               
                    
            }
        }
       // System.out.println("Relevant documents: ");
       
        Set<Entry<String, Integer>> set = master_data.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        });
      //  System.out.println("Size of list: "+list.size());
        int count=0;
        for(Map.Entry<String, Integer> entry:list)
        {
            long doc_id =Long.parseLong(entry.getKey());
            String out_title="";
            int index_of_word = Collections.binarySearch(title_index, new title_mapping(doc_id, 0), c1);
            RandomAccessFile file2 = new RandomAccessFile("/home/ruchir/wikiSupportFiles/Index_Title_Mapping", "r");
           
            long start_index = 0;
            if(index_of_word<0)
            {
                index_of_word*=-1;
                //System.out.println("H95ere"+str);
                if(index_of_word>2)
                    start_index = title_index.get(index_of_word-2).offset;
                else
                    start_index=0;
            }
            else if(index_of_word>1)
                start_index=title_index.get(index_of_word-2).offset;
               
           // System.out.println("Locating"+doc_id+" after "+title_index.get(index_of_word-2).doc_id);
            //System.out.println("Locate:"+title_index.get(index_of_word-2).doc_id);
           
            file2.seek(start_index);
           String reading="";
            long lout_title=0,last_title=0;
           for(int i=0;i<102;i++)
           {
                 reading = file2.readLine();
                // System.out.println("353Read this:  "+reading);
                 long l_docid = Long.parseLong(reading.substring(0,reading.indexOf("=")));
                  lout_title=Long.parseLong(reading.substring(reading.indexOf("=")+1));
                 if(l_docid==doc_id)
                 {
                    // System.out.print("\tEqual");
                    
                     break;
                 }
                 last_title=lout_title;
           }
            
            
            //Now read title from main title mapping file
              RandomAccessFile file3 = new RandomAccessFile("/home/ruchir/wikiSupportFiles/titlemap", "r");
            file3.seek(last_title);
            reading=file3.readLine();
              //System.out.println("Read this:  "+reading);
           out_title=reading.substring(reading.indexOf("=")+1);
            
            
           master_data.clear(); 
            System.out.println(out_title);
            count++;
            if(count==10)
                break;
        }
	        long  end = System.currentTimeMillis();
       System.out.println("Time taken in Result Generation: "+(end - start) + " ms");
        }
 
        //System.out.println(g_docid.toString());

    }
}
