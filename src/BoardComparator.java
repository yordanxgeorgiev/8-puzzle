import java.util.Comparator;

public class BoardComparator implements Comparator<Board> {
    @Override
    public int compare(Board b1, Board b2) {
        int a = b1.getG() + b1.getManhattan_dist();
        int b = b2.getG() + b2.getManhattan_dist();
        if(a < b)
            return 1;
        else if(a > b)
            return -1;
        return 0;
    }
}
