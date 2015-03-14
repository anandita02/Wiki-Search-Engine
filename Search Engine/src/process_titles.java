/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author ruchir
 */
// This class reads a ID-title document and produces corresponding secondary and tertiary index on that title file
public class process_titles 
{
    public static void main(String args[]) throws Exception
    {
        //
        BufferedReader br = new BufferedReader(new FileReader("/home/ruchir/wikiSupportFiles/out"));
        
        File file = new File("src/com/Index/Index_Title_Mapping");
	if (!file.exists()) {
		file.createNewFile();
	}

	
	FileWriter fw = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bw = new BufferedWriter(fw);
        
        File file1 = new File("src/com/Index/Index_Ter");
	if (!file1.exists()) {
		file1.createNewFile();
	}

	FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
	BufferedWriter bw1 = new BufferedWriter(fw1);
        
        
        long char_count=0,i=0,char_sec=0;
        String next = br.readLine();
        while(next!=null)
        {
            //System.out.println("next: "+next);
            
            String doc_id = next.substring(0,next.indexOf("="));
            char_count+=next.getBytes().length+1;
           char_sec+=doc_id.getBytes().length+2+(""+char_count).getBytes().length;
            bw.write(doc_id+"="+char_count+"\n");
            
            
            if(i%100==0)
                bw1.write(doc_id+"="+char_sec+"\n");
             
            
            i++;
            next = br.readLine();
        }
        bw.close();
        bw1.close();
        br.close();
    }
    
}
