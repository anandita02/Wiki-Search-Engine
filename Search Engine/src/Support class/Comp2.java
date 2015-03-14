import java.util.Comparator;
public class Comp2 implements Comparator<store> 
{ 
    @Override
    public int compare(store x, store y) 
    { 
        return (y.rank-x.rank);
    } 
}
