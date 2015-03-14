
import java.util.*;



import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
//Class to store the count

 
public class MyHandler extends DefaultHandler 
{
 
	
	
    public static Map<String, HashMap<String, mylist>> database = new TreeMap<>();   
	
    boolean bpage = false;
	
    StringBuilder all_data = new StringBuilder();
    boolean btitle=false,flag_readid=false;
    boolean bid=false;
    boolean brev = false;
    boolean btext=false;
  
    String all_title;
    static String curid;
    StringBuilder loc_body=new StringBuilder();

    StringBuilder loc_cat = new StringBuilder();
    StringBuilder loc_str = new StringBuilder();
    StringBuilder loc_title = new StringBuilder();
    StringBuilder loc_extlink = new StringBuilder();
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
    {
 
        if (qName.equalsIgnoreCase("page")) 
        {
            //create a new Employee and put it in Map
            if(file1.count%5000==0&&file1.count!=0)
            {
                try
        	{
        		Write_Index.write_database_tofile();
        	}catch(Exception e)
        	{
        		System.out.print("Exception in writing to disk");
        	}
            }
            file1.count++;
        	bpage=true;
        } 
        else if(qName.equalsIgnoreCase("title")&&bpage)
        {
        	btitle=true;
        	bpage=false;
        }
        else if(qName.equalsIgnoreCase("id")&&flag_readid)
        {
           
            bid=true;
            flag_readid= false;
        }
        else if(qName.equalsIgnoreCase("revision")) 
        {
            brev=true;
        }
        else if(qName.equalsIgnoreCase("text")&&brev) 
        {
            btext=true;
            all_data.setLength(0);
            brev=false;
        }
        
    }
 
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException 
    {
        try{
            if(qName.equalsIgnoreCase("title")) 
        {
           //Check if count=1000, if yes then write into text file
        	btitle=false;
        	flag_readid=true;
        }
        else if(qName.equalsIgnoreCase("text")) 
        {
          
        	btext=false;
        	
        	handle_text();
        	all_data.setLength(0);
        	//System.out.println("Printing data:"+database);
        	
        }
        else if(qName.equalsIgnoreCase("file"))
        {
        	System.out.print("Size of database"+database.size()+"\n");
        	//System.out.print(database.keySet());
        	try
        	{
        		if(!database.isEmpty())
                            Write_Index.write_database_tofile();
        	}catch(Exception e)
        	{
        		System.out.print("Exception in writing to disk");
        	}
        	loc_title.setLength(0);
        	loc_body.setLength(0);
        	loc_cat.setLength(0);
        	loc_str.setLength(0);
                
                
        
        }
        }catch(Exception e){}
    }
 
 
   

	

	@Override
    public void characters(char ch[], int start, int length) throws SAXException 
    {
	if(btitle) 
        {
        	//New page encountered, process the name
            
        	all_title=new String(ch, start, length);
        	
            //System.out.println("Title is: "+title);
        }
       
        else if (bid) 
        {
        	//Getting the ID of document
        	curid = new String(ch,start,length);
        	/*if(!file1.first_entry)
        	{
        		file1.base=Integer.parseInt(curid);
        		file1.first_entry=true;
        	}
        	else
        	{
        		int cal = Integer.parseInt(curid)-file1.base;
        		curid=""+cal;
        	}*/
        		//System.out.println(curid);
        	loc_title.setLength(0); //Clear the local buffer so as to avoid previous entries
                
        	for(int i=0;i<all_title.length();i++)
        	{
        		char current = all_title.charAt(i);
        		if((current>=65&&current<=90)||(current>=97&&current<=122)||(current>=48&&current<=57))
        		{
        			loc_title.append(current);
        		}
        		else
        		{
        			Parsing_Helper.handle_title(loc_title.toString());
        			loc_title.setLength(0);
        		}
        	}
            bid = false;
        }
        else if(btext)
        {
        	//Start Reading..!!
        	 all_data.append(ch, start, length);
        	
        }
    }
    
   
    public void handle_text() throws Exception
    {
    	/*
    	 * MAIN FUNCTION THAT HANDLES EVERYTHING
    	 */
    	try{
    	boolean flag_infobox = false;
        boolean flag_extindex=false;
    	int len = all_data.length();
    	//System.out.print(all_data);
    	
    	//System.out.println(all_data);
    	for(int i=0;i<len;i++)
    	{
    		//check for info-box
    		if(all_data.charAt(i)=='{')
    		{
    			
    			if(i+9<len&&all_data.substring(i+1, i+9).equalsIgnoreCase("{infobox"))
    			{
    				//String loc_str="";
    				
    				
    				flag_infobox=true;
    				boolean flag_addtext=false;
    				i=i+9;
    				int count_par=2;
    	        	
        			while(flag_infobox&&i<len)
        			{
        				char current = all_data.charAt(i);
        				//System.out.print(current);
        				if(current=='{')
        					count_par++;
        				else if(current=='}')
        					count_par--;
        				
        				if(count_par==0||i>=len)
        				{
        					flag_infobox = false;
        					i++;
        					break;
        				}
        				
        				//Add Text Between [ and ] to process
        				if(current=='['&&all_data.charAt(i+1)=='[')
        				{
        					flag_addtext=true;
        				}
        				else if(current==']'&&all_data.charAt(i+1)==']')
        				{
        					Parsing_Helper.handle_infobox(loc_str.toString());
        					flag_addtext=false;
        					loc_str.setLength(0);
        				}
        				
        				if(flag_addtext)
        				{
        					//System.out.print("\tAdding");
        					if((current>=65&&current<=90)||(current>=97&&current<=122))
        					{
        						loc_str.append(current);
            					//infoboxtext.append(current);
            				}
        					else
        					{
        						Parsing_Helper.handle_infobox(loc_str.toString());
        						loc_str.setLength(0);
        					}
        					
        					
        				}
        				i++;
        				
        				
        			}
        			
        			
    			} //Infobox if ends here
                        else if(i+8<len&&all_data.substring(i+1, i+8).equalsIgnoreCase("{geobox"))
                        {
                            //System.out.print("GEOBOX");
                            int loc_par=2;
    				i=i+8;
    				boolean flag_geo=true;
    				while(flag_geo&&i<len)
    				{
    					char current = all_data.charAt(i);
    					//System.out.print(current);
    					if(current=='{')
    						loc_par++;
    					else if(current=='}')
    						loc_par--;
    					
    					if(loc_par==0||i>=len)
    					{
    						flag_geo=false;
    						i++;
    						break;
    						
    					}
    					i++;
    				}
                        }
    			else if(i+6<len&&all_data.substring(i+1, i+6).equalsIgnoreCase("{cite"))
    			{
    				//System.out.print("\nCITE\n");
    				int loc_par=2;
    				i=i+6;
    				boolean flag_cite=true;
    				while(flag_cite&&i<len)
    				{
    					char current = all_data.charAt(i);
    					//System.out.print(current);
    					if(current=='{')
    						loc_par++;
    					else if(current=='}')
    						loc_par--;
    					
    					if(loc_par==0||i>=len)
    					{
    						flag_cite=false;
    						i++;
    						break;
    						
    					}
    					i++;
    				}
    				//System.out.print("\nCITE END\n");
    			}
    			else if(i+4<len&&all_data.substring(i+1, i+4).equalsIgnoreCase("{gr"))
    			{
    				//System.out.print("\nGR\n");
    				int loc_par=2;
    				i=i+4;
    				boolean flag_cite=true;
    				while(flag_cite&&i<len)
    				{
    					char current = all_data.charAt(i);
    					if(current=='{')
    						loc_par++;
    					else if(current=='}')
    						loc_par--;
    					
    					if(loc_par==0||i>=len)
    					{
    						flag_cite=false;
    						i++;
    						break;
    						
    					}
    					i++;
    				}
    				//System.out.print("\nGR END\n");
    			}
    			else if(i+7<len&&all_data.substring(i+1, i+7).equalsIgnoreCase("{coord"))
    			{
    				//System.out.print("\nco-ordinate: ");
    				int loc_par=2;
    				i=i+7;
    				boolean flag_coord=true;
    				while(flag_coord&&i<len)
    				{
    					char current = all_data.charAt(i);
    					//System.out.print(current);
    					if(current=='{')
    						loc_par++;
    					else if(current=='}')
    						loc_par--;
    					
    					if(loc_par==0||i>=len)
    					{
    						flag_coord=false;
    						i++;
    						break;
    						
    					}
    					i++;
    				}
    				//System.out.println("\nCo-ordinate end");
    			}
    			
    		
    		}
    		else if(all_data.charAt(i)=='[') 
			{
    			//System.out.println("\nhere"+all_data.charAt(i));
    			if(i+11<len&&all_data.substring(i+1, i+11).equalsIgnoreCase("[Category:"))
    			{
    				//System.out.print("\nCATEOGRY\n");
    				//String loc_cat="";
    				//Take Category
                                flag_extindex=false;
    				int loc_par=2;
    				i=i+11;
    				boolean is_cat=true;
    				//System.out.println("\nhere");
    				while(is_cat)
    				{
    					char current=all_data.charAt(i);
    					
    					//System.out.print(current);
    					if(current==']')
    						loc_par--;
    					else if(current=='[')
    						loc_par++;
    					else
    					{
    						if((current>=65&&current<=90)||(current>=97&&current<=122))
        					{
        						loc_cat.append(current);
            					//infoboxtext.append(current);
            				}
    						else
    						{
    							Parsing_Helper.handle_category(loc_cat.toString());
    							loc_cat.setLength(0);
    						
    						}
    						
    					}
    					if(loc_par==0||i>=len)
    					{
    						Parsing_Helper.handle_category(loc_cat.toString());
    						is_cat=false;
    						loc_cat.setLength(0);
    					}
    					i++;
    				}
    			}
    			else if(i+8<len&&all_data.substring(i+1, i+8).equalsIgnoreCase("[image:"))
    			{
    				//We have to ignore all the data inside [Image
					//System.out.println("IMAGE: ");
    				int loc_par=2;
    				i=i+8;
    				boolean flag_image=true;
    				while(flag_image&&i<len)
    				{
    					char current = all_data.charAt(i);
    					//System.out.print(current);
    					if(current=='[')
    						loc_par++;
    					else if(current==']')
    						loc_par--;
    					
    					if(loc_par==0||i>=len)
    					{
    						flag_image=false;
    						i++;
    						break;
    						
    					}
    					i++;
    				}
    				//System.out.println("IMAGE END");
    			}
				else if(i+7<len&&all_data.substring(i+1, i+7).equalsIgnoreCase("[file:"))
				{
					//System.out.print("\nFILE\n");
					//We have to ignore all the data inside [file
					int loc_par=2;
    				i=i+7;
    				boolean flag_image=true;
    				while(flag_image&&i<len)
    				{
    					char current = all_data.charAt(i);
    					//System.out.print(current);
    					if(current=='[')
    						loc_par++;
    					else if(current==']')
    						loc_par--;
    					
    					if(loc_par==0||i>=len)
    					{
    						flag_image=false;
    						i++;
    						break;
    						
    					}
    					i++;
    				}
    				//System.out.print("\nFILE END\n");
				}
				
			}
    		else if(all_data.charAt(i)=='<') 
		{
    			//Check for Existence of possible followup's of <
    			 if (i+4<len&&all_data.substring(i+1,i+4).equals("!--")) 
    			 {
    				 //Check and Eliminate comments
    				 i=i+4;
    				 int locate_close = all_data.indexOf("-->",i);
    				 if(locate_close+2<len&&locate_close>0)
    					 i=locate_close+2;
    			 }
    			 
    			 else if(i+8<len&&all_data.substring(i+1,i+8).equalsIgnoreCase("gallery"))
    			 {
    				 //System.out.print("\nGALLERY START at i ="+i+"\n");
    				 //Check and eliminate gallery
    				 i=i+8;
    				 int locate_close = all_data.indexOf("</gallery>" , i+1);
    				 if(locate_close+10<len&&locate_close>0)
    					 i=locate_close+10;
    				 //System.out.print("\nGALLERY END at i ="+i+"\n");
    			 }
                         else if(i+5<len&&all_data.substring(i+1,i+5).equalsIgnoreCase("ref>"))
                         {
                           //System.out.println("Removing ref");
                          	 i=i+5;
    				 int locate_close = all_data.indexOf("</ref>" , i+1);
                                
                                if(locate_close+5<len&&locate_close>0)
    					 i=locate_close+6;
    				 //System.out.print("\nGALLERY END at i ="+i+"\n");
    			    
                             
                                     
                         }
                         else
        		{
    				char current = all_data.charAt(i);
    				while(current!=' '&&i<len)
    				{
                                    current=all_data.charAt(i);
                                    i++;
    				}
        		}
		}
                else if(all_data.charAt(i)=='='&&i+1<all_data.length()&&all_data.charAt(i+1)=='=')
                {
                   // System.out.println("Here");
                    //Check for External Links
                    i=i+2;
                    while(all_data.charAt(i)==' '&&i<len)
                    {
                        i++;
                    }
                    if(i+14<len&&all_data.substring(i, i+14).equalsIgnoreCase("External links"))
                    {
                             i=i+14;
                             flag_extindex=true;
                             //System.out.println("extindex is nw true");
                    }

                }
                else if(all_data.charAt(i)=='*'&&flag_extindex)
                {
                    
                            i++;
                    loc_extlink.setLength(0);
                    if(i+1<all_data.length()&&all_data.charAt(i+1)=='[')
                    {
                       // System.out.print("\tSearcing space");
                        i++;
                        while(all_data.charAt(i)!=' ')
                            i++;
                    }
                    //End of URL. now append the link
                    char current = all_data.charAt(i);

                    while(current!=']'||i>len)
                    {
                       if((current>=65&&current<=90)||(current>=97&&current<=122))
                            loc_extlink.append(current);


                        else
                        {	
                            Parsing_Helper.handle_extlink(loc_extlink.toString());
                            loc_extlink.setLength(0);
                        }
                       i++;
                       current = all_data.charAt(i);
                    
                    }
                }
    		else
    		{
    			//BODY
    			//System.out.print("\ni is:"+i);
    			
    			char current = all_data.charAt(i);
    		//	String check = ""+current;
    			if(current=='#')
    			{
    				while(current!=' '&&i<len)
    				{
    					current=all_data.charAt(i);
    					i++;	
    				}
    			}
    			else
	    		{
    				//System.out.print(current);
	    		
    				//check=check.replaceAll("[^A-Za-z]", "");
	    			//String punct="[][(){},.;!?<>%|\\~@`~*-+^&%$#_-:;/\"=0123'456789";
	    			if((current>=65&&current<=90)||(current>=97&&current<=122))
						loc_body.append(current);
	    			
	    			else
	    			{	
	    				Parsing_Helper.handle_body(loc_body.toString());
					loc_body.setLength(0);
	    			}
	    			
	    		}
    			
    		}
    	}

     
    	//System.out.println(infoboxtext);
    	//System.out.println("Categories: "+category);
    	//System.out.println("Printing data:"+database);
    	all_data.setLength(0);
    }
    catch(Exception e)
    {
        
    }
    
    } //End of Function

}
