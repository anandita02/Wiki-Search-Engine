

import java.io.BufferedReader;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;
/**
 *
 * @author ruchir
 */
import java.util.Comparator;
 class term
 {
     String word;
     String list;
     int doc_id;
 }
class Comp implements Comparator<term> 
{ 
    public int compare(term x, term y) 
    { 
        return x.word.compareTo(y.word);
    } 
}
public class merge_function 
{
    
    public static void main(String args[]) throws Exception
    {
        try
        {
            System.out.println("In merge funciton");
          int isnull=0;
          long charcount=0;
        //no of files that are divided depends on count value
      // int val=file1.count/5000;
        int val=2808;
        Comparator<term> com = new Comp();
        PriorityQueue<term> my_data =  new PriorityQueue<term>(val+2,com);
         BufferedReader br2[] = new BufferedReader[val+2];
        for(int j=1;j<=val;j++)
            br2[j] = new BufferedReader(new FileReader("/home/ruchir/data/DATABASE"+j));
       // File file = new File("processing"+count);
        
        //Initialize variables, boolean array arr to mark completeness of a text file
        boolean[] arr = new boolean[val+2];
         int rec_read=0,rec_write=0;
        File out_file = new File("src/com/Index/43Final_Database");
        if (!out_file.exists()) 
        {
           out_file.createNewFile();
        }
        
        FileWriter fw = new FileWriter(out_file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
            
        //Secondary Index file
        File out_file1 = new File("src/com/Index/43Second_Index");
        if (!out_file1.exists()) 
        {
           out_file1.createNewFile();
        }
        
        FileWriter fw1 = new FileWriter(out_file1.getAbsoluteFile());
        BufferedWriter bw1 = new BufferedWriter(fw1);
        
        
        
        for(int k=1;k<=val+1;k++)
            arr[k]=true;
        int last;
        String read;
        System.out.println("Starting to read");
        for(int j=1;j<=val;j++)
        {
         
            if(arr[j])
            {                  
               // System.out.println(rec_read);
               read = br2[j].readLine();
               if(read==null)
               {
                    isnull++;
                    System.out.println("Completed File"+j+" at: "+rec_read);
                    arr[j]=false;    
                    br2[j].close();

               }
               else
               {
                    String word = read.substring(0,read.indexOf('-'));
                    read=read.substring(read.indexOf('-')+1);
                    
                    term t = new term();
                    t.list=read;
                    t.word=word;
                    t.doc_id=j;
                    my_data.add(t);
                    rec_read++;
               }
         
            }
      
        }
        
        
        //Seek and Write top element
        term top = my_data.poll();
       
        last=top.doc_id;
         String out_list = top.list,out_word = top.word;
           String lout_list=null,lout_word=null;
        while(isnull<val)
        {
          
           
            if(!arr[last])
            {
                term ltop = my_data.poll();
                
                last=ltop.doc_id;
                lout_list=ltop.list;
                lout_word=ltop.word;
               
                
            }
            else
            {
                
               // System.out.println("in else"+last);
                read = br2[last].readLine();
                if(read==null)
                {
                    if(arr[last])
                            isnull++;
                    System.out.println("158Completed File"+last+"at "+rec_read);
                    arr[last]=false;    
                    br2[last].close();
                   
                }
                else
                {
                    String word = read.substring(0,read.indexOf('-'));
                    read=read.substring(read.indexOf('-')+1);
                    
                  //  System.out.print("\tRead word: "+word);
                    term lt = new term();
                    lt.list=read;
                    lt.word=word;
                    lt.doc_id=last;
                    my_data.add(lt);
                    rec_read++;
                    
                   //System.out.println("Finised entering");
                    term ltop1 = my_data.poll();
                    
                    
                    lout_list=ltop1.list;
                    lout_word=ltop1.word;
                    last=ltop1.doc_id;
                }
              }
             if(lout_word.equals(out_word)&&out_word!=null)
             {
                // System.out.print("\tEqual case");
                 out_list=out_list+lout_list;
             }
             else
             {
                 //System.out.println("next is: "+lout_word+"last was"+out_word);
                  
                      if(rec_write%100==0)
                     {
                         //Write into secondary index
                          
                         bw1.write(out_word);
                         bw1.write(":"+charcount);
                         bw1.write("\n");
                     } 
                     
                          
                        bw.write(out_word+"-");
                        bw.write(out_list);
                        bw.write("\n");
                        charcount+=out_word.length()+out_list.length()+2;
                        rec_write++;
                  
                     out_word=lout_word;
                     out_list=lout_list;
                     lout_word="";
                     lout_list="";
                     

             }
                
        }
        System.out.println("Size of all_data="+my_data.size());
       
         bw.close();
         bw1.close();
        System.out.println("No of records merged: "+rec_read+" Records Written into merged file: "+rec_write);

    
    }catch(Exception e)
    {
        System.out.println("Exception in merge_funciotn"+e.getLocalizedMessage()+":"+e.getMessage());
    }
    }
    
    
    
    
    
    
    /*
    public static void sort_merge() throws Exception
    {
        try{ 
           
            int isnull=0;
        //no of files that are divided depends on count value
        int val=file1.count/1000;
        
        BufferedReader br2[] = new BufferedReader[val+2];
        for(int j=1;j<=val;j++)
            br2[j] = new BufferedReader(new FileReader("src/com/data/DATABASE"+j));
       // File file = new File("processing"+count);
        
        //Initialize variables, boolean array arr to mark completeness of a text file
        boolean[] arr = new boolean[val+2];
         
        File out_file = new File("src/com/data/Final_Database");
        if (!out_file.exists()) 
        {
           out_file.createNewFile();
        }
        int rec_read=0,rec_write=0;
        FileWriter fw = new FileWriter(out_file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
            
        for(int k=1;k<=val+1;k++)
            arr[k]=true;
    
          //Read all blocks of data into my_data
      
        int last=1;
        //System.out.println(rec_read++);
        //System.out.println("Written first entry");
     //   
     TreeMap<String, String> my_data = new TreeMap<String,String>();
     
     String read="";
      for(int j=1;j<=val;j++)
      {
         
         if(arr[j])
         {                  
            // System.out.println(rec_read);
            read = br2[j].readLine();
            if(read==null)
            {
                 isnull++;
                 System.out.println("Completed File"+j+" at: "+rec_read);
                 arr[j]=false;    
                 br2[j].close();
                                      
            }
            else
            {
                 String term = read.substring(0,read.indexOf('-'));
                 read=read.substring(read.indexOf('-')+1);

                 if(my_data.containsKey(term))
                 {
                     String old_val = my_data.get(term);
                     read=old_val+read;
                     my_data.remove(term);
                 }
                 read=read+"?"+j;
                 my_data.put(term,read);
                 rec_read++;
            }
            
         }
      }
      System.out.println("117");
      String key = my_data.firstEntry().getKey();
      String keyval = my_data.get(key);
      last = Integer.parseInt(keyval.substring(keyval.indexOf("?")+1));
      keyval=keyval.substring(0, keyval.indexOf("?"));
 
      my_data.remove(key);
                
    //Write this keyval pair into the final output file..!!
      System.out.println("124"+key+":"+last);
      bw.write(key+"-");
      bw.write(keyval);
      bw.write("\n");
      rec_write++;
      while(isnull<val)
        {
            if(!arr[last])
            {
                 key = my_data.firstEntry().getKey();
                 keyval = my_data.get(key);
                 last = Integer.parseInt(keyval.substring(keyval.indexOf("?")+1));
                 keyval=keyval.substring(0, keyval.indexOf("?"));
                 my_data.remove(key);

                //Write this keyval pair into the final output file..!!
                 System.out.println("140"+key+":"+last);
                 bw.write(key+"-");
                 bw.write(keyval);
                 bw.write("\n");
                 rec_write++;
            }
             else
            {
               // System.out.println("In else");
                read = br2[last].readLine();
                if(read==null)
                {
                        if(arr[last])
                            isnull++;
                       System.out.println("Completed File"+last+"at "+rec_read);
                        arr[last]=false;    
                        br2[last].close();
                        key = my_data.firstEntry().getKey();
                        
                        keyval = my_data.get(key);
                        last = Integer.parseInt(keyval.substring(keyval.indexOf("?") + 1));
                        keyval = keyval.substring(0, keyval.indexOf("?"));

                        my_data.remove(key);

                        //Write this keyval pair into the final output file..!!
                        System.out.println("161" + key + ":" + last);
                        bw.write(key + "-");
                        bw.write(keyval);
                        bw.write("\n");
                        rec_write++;

                }
                else
                {
                    String term = read.substring(0,read.indexOf('-'));
                    read=read.substring(read.indexOf('-')+1);
                    rec_read++;  
                    if(my_data.containsKey(term))
                    {   String old_val = my_data.get(term);
                         old_val=old_val.substring(0,old_val.indexOf("?"));
                         read=old_val+read;
                         my_data.remove(term);
                     }
                    
                    read=read+"?"+last;
                    my_data.put(term,read);
                    
                 
                    key = my_data.firstEntry().getKey();
                    keyval = my_data.get(key);
                      
                    last = Integer.parseInt(keyval.substring(keyval.indexOf("?")+1));
                    keyval=keyval.substring(0, keyval.indexOf("?"));
                    my_data.remove(key);
                    bw.write(key+"-");
                    bw.write(keyval);
                    bw.write("\n");
                    rec_write++;
                       
                   
                    
                   System.out.println("188"+key+": "+last);
                    //System.out.println(key);
                  //Write this keyval pair into the final output file..!!
                  

                 }
            }
    
        }
      
      
        System.out.println("Finally"+my_data.size());
        if(!my_data.isEmpty())
        {
             Iterator entries = my_data.entrySet().iterator();
             while(entries.hasNext())
             {
                 Entry fentry = (Entry)entries.next();
                 key = (String) fentry.getKey();
                  keyval = (String) fentry.getValue();
                 
                 keyval=keyval.substring(0, keyval.indexOf("?"));
                //System.out.println("179Writing into"+key+": "+keyval);
                 bw.write(key+"-");
                 bw.write(keyval);
                 bw.write("\n");
                 rec_write++;
             }
        }
        bw.close();
        System.out.println("No of records merged: "+rec_read+" Records Written into merged file: "+rec_write);
    }catch(Exception e)
    {
        System.out.println("Exception in merge_function: "+e.getLocalizedMessage()+": "+e.toString());
    }
    
    }*/
}
