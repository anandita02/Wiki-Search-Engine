import java.util.Comparator;
public class Comp implements Comparator<term> 
{ 
    public int compare(term x, term y) 
    { 
        return x.word.compareTo(y.word);
    } 
}
