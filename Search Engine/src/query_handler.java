/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.*;

/**
 *
 * @author ruchir
 */
class out_format
{
    String term;
    int rank;
}
class word
{
    String term;
    long offset;

    word(String str,long off)
    {
        this.term = str;
        this.offset=off;
    }
}
class Comp1 implements Comparator<out_format> 
{ 
    @Override
    public int compare(out_format x, out_format y) 
    { 
        return (y.rank-x.rank);
    } 
}
public class query_handler 
{
    //This is the main class that would handle queries and report the output
    public static void main(String[] args) throws Exception
    {
         //Read Query from user now
        Scanner sc = new Scanner(System.in);
       
        
        
        
        //Files are parsed, now merge them
                  
        System.out.println("Reading Secondary index");
        //Read secondary index into main memory
        BufferedReader br = new BufferedReader(new FileReader("src/com/Index/Sorted_Second_Index"));
        
        //DS to hold secondary index
        List<word> sec_index = new ArrayList<word>();
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
            String term = next.substring(0,next.indexOf(":")-1);
             long   from_index = Long.parseLong(next.substring(next.indexOf(":")+1));
             word w = new word(term,from_index);
             
             sec_index.add(w);
              next = br.readLine();
        }
        
        System.out.println("Secondary index read, size is: "+sec_index.size());
        
        Map<String,Integer> master_data = new HashMap<>();
        Set<String> g_docid = new HashSet<>();
        
        int bcount=5,tcount=100,ecount=10,ccount=40,icount=50; 
        //Assuming Simple queries
        //Timer
        
         String read = sc.nextLine();
         long start = System.currentTimeMillis();
        StringTokenizer st = new StringTokenizer(read," ");
        
        
        //Only single word query..!! Best Case
          
        //Perform optimizations
        while(st.hasMoreElements())
        {
            String str=st.nextToken().toLowerCase();
           // System.out.println("Searching for"+str);
            str=str.trim();
            stemmer s = new stemmer();
            s.add(str.toCharArray(),str.length());
            str=s.stem();

            //Search position in secondary index
            int index_of_word = Collections.binarySearch(sec_index, new word(str, 0), c);
             long start_index;
            if(index_of_word<0)
            {
                index_of_word*=-1;
                //System.out.println("H95ere"+str);
                start_index = sec_index.get(index_of_word-2).offset;
            }
            else if(index_of_word>3)
                start_index= sec_index.get(index_of_word-3).offset;
            else
                start_index=0;
            //System.out.println("start_index: "+start_index);
            /*
            LINEAR SEARCH CODE in Secondary Index
            for(int i=0;i<sec_index.size();i++)
            {
                word lw = sec_index.get(i);
                if(str.compareTo(lw.term)<=0)
                {
                    if(i==0)
                        start_index=0;
                    else
                        start_index = sec_index.get(i-1).offset;
                    break;
                }
                if(i==sec_index.size()-1)
                    start_index = lw.offset;
        }*/
           // System.out.println("starting at: "+start_index);
            //Retrieve next 200 results from primary index and search for the term
            RandomAccessFile file = new RandomAccessFile("src/com/Index/Sorted_Database", "r");
            file.seek(start_index);
            for(int i=0;i<125;i++)
            {
                //0-2614?24915776:3,,,,;24146273:3,,,,;
                String next_record = file.readLine();
            //    file.re
             //  System.out.println("Read: "+next_record);
                String word_name = next_record.substring(0,next_record.indexOf("-"));
                next_record = next_record.substring(next_record.indexOf("-")+1);
                int no_of_rec = Integer.parseInt(next_record.substring(0,next_record.indexOf("?")));
                next_record = next_record.substring(next_record.indexOf("?")+1);
                if(word_name.compareTo(str)==0)
                {
                    //Word Found 24162659
                    Set<String> l_docid = new HashSet<>();
                   System.out.println("Found: !! "+no_of_rec);
                    int local_counter=0;
                    StringTokenizer st1 = new StringTokenizer(next_record,";");
                  //  System.out.println("count is: "+st1.countTokens());
                    while(st1.hasMoreElements())
                    {
                        local_counter++;
                       /* if(local_counter>10000)
                            break;*/
                        String next_doc = st1.nextToken();
                        String doc_id  = next_doc.substring(0, next_doc.indexOf(":"));
                        l_docid.add(doc_id);
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
                        int total = (body*bcount) +(title*tcount)+(ext*ecount)+(cat*ccount)+(info*icount); 
                        if(master_data.containsKey(doc_id))
                        {
                            int cur = master_data.get(doc_id);
                            master_data.remove(doc_id);
                            total+=cur;
                            
                        }
                        master_data.put(doc_id,total);
                        
                    }
                    
                    //Perform Intersection of two sets
                    Set<String> intersection = new HashSet<String>(l_docid); // use the copy constructor
                    intersection.retainAll(g_docid);
                    if(!intersection.isEmpty())
                    {
                        g_docid.retainAll(l_docid);

                    }
                    else if(g_docid.isEmpty())
                        g_docid=l_docid;
                    break;
                }
                else if(word_name.compareTo(str)>0)
                {
                    System.out.println("Word not found");
                    break;
                }
                    
            }
        }
       // System.out.println("Relevant documents: ");
       
        Comparator<out_format> com = new Comp1();
        PriorityQueue<out_format> final_out;
        final_out = new PriorityQueue<out_format>(g_docid.size()+2,com);
        
        Iterator iterator = g_docid.iterator(); 
        while (iterator.hasNext())
        {
          
            out_format o = new out_format();
            String l_next = (String) iterator.next();
            o.rank=master_data.get(l_next.toString());
            o.term=l_next.toString();
            final_out.add(o);
        }
        System.out.println("Size of final"+final_out.size());
        //Display top 10 results
        for(int i=0;i<=10;i++)
        {
             if(final_out.isEmpty())
                break;
            System.out.println(final_out.poll().term);
           
        }
        //System.out.println(g_docid.toString());
        long  end = System.currentTimeMillis();
        System.out.println("Time taken in Result Generation: "+(end - start) + " ms");
    }
}
