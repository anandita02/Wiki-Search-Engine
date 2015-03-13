------------------------------------------------------------------------------------------------------------------------------------
							README: INDEX CREATION
------------------------------------------------------------------------------------------------------------------------------------
List of FILES:

(i) file1.java (Main File)
(ii) MyHandler.java (to scrap relevant data from XML)
(iii) Parsing_Helper.java (Contains parse functions, enters data into MAP)
(iv) Write_Index.java (To write into index files)
(v) stemmer.java (Porter Stemmer code)
(vi) mylist.java (Helper file, only class def)

------------------------------------------------------------------------------------------------------------------------------------
STRUCTURE of In-Memory Data Structure:
------------------------------------------------------------------------------------------------------------------------------------

<Outer_Map>
KEY: WORD, VALUE: <Inner_Map>
<Inner_Map>
KEY: DocumentID, VALUE: <mylist>
mylist class object has four variables:
(i) body (INTEGER)
(ii) category (INTEGER)
(iii) infobox (INTEGER)
(iv) title (INTEGER)

------------------------------------------------------------------------------------------------------------------------------------
FLOW OF CONTROL: 
------------------------------------------------------------------------------------------------------------------------------------

File1 reads the content of XML File and SAX Parser passes it to MYHandler.java. MyHandler contains the following functions:
(i) startElement: It identifies Open tag in XML file
(ii) character: It reads the data enclosed between opentag and closetag
(iii) endElement: It identifies Close tag in XML file
Using the above three functions and some boolean variables, the data enclosed between <Title> and </title> and <text></text> is identified page wise. 

Considering only the text which is classified as INFOBOX, CATEGORY, TITLE,BODY character by character. The valid words are then passed to helper functions so as to apply the algorithms and insert into the MAP. string is then pased word by word (SINGLE PARSING)

The MyHandler class then calls handle_title, handle_body, handle_infobox, handle_category functions from Parsing_Helper.java. These helper functions in Parsing_Helper class performs fhe following actions in order:
(i) Convert to lower case
(ii) Trim the String
(iii) Check if the string is present in stop_words array
(iv) Apply Stemming algorithm
(v) Remove if string length<2
(vi) Enter the string into MAP

On detection of closing tag of file, the data of MAP data structure is written into the index file.

------------------------------------------------------------------------------------------------------------------------------------
EXTRA FEATURES: 
------------------------------------------------------------------------------------------------------------------------------------

(i) Not storing zeros in index file. 
Explanation: Many words in index were appearing in only one or two of the four possible categories. For such words, "0" count was getting stored again and again. Similar to the concept of sparse matrices, i decided to store only the values which are non-zero. This resulted in a significant reduction of index size.
Result: Reduction of size of index from 35-36 MB to 26-27 MB.

(ii)  Not Storing DOC ID's directly in index file.
Explanation: The document ID's of the documents were in increasing order. Storing document id's of 6 digit's again and again for every occurance of word was making the data redundant. As document id's are unique, i stored the updated doc id's in the index file:
UPDATED_DOC_ID = DOC_ID_CURRENT_DOCUMENT - DOC_ID_OF_FIRST_DOCUMENT (BASE).
Result: Reduction of size of index from 27-28 MB to 23-24 MB.

(iii) SINGLE One-Time PARSING of DATA
Explanation: The data is parsed only once, hence reducing the overall memory and time complexity.
