import java.util.Stack;

public class Solver {

    private int bound;
    Board board;

    public Solver(Board board)
    {
        this.board = board;
        bound = board.getManhattan_dist();
    }

    public void solve()
    {
        while(!search(board, bound)){}
    }

    private boolean search(Board node, int bound)
    {
        // Stack for keeping the nodes we got to
        Stack<Board> stack = new Stack<>();
        stack.push(node);

        // the lowest bound that exceeds the limit, -1 indicates it's not yet assigned
        int minBound = -1;

        // f = g + h -> the cost of a node
        // g -> the cost to get to a node
        // h -> the heuristic cost of a node
        int f;

        while(!stack.isEmpty())
        {
            Board cur = stack.pop(); // getting the top node of the stack

            if(cur.isSolved())     // if we found solution
            {
                System.out.println("Solved!");
                printMoves(cur);
                return true;
            }

            f = cur.getG() + cur.getManhattan_dist(); // calculating f

            if(f <= bound) // not expanding the nodes with too high cost
            {
                for(Board b : cur.getSuccessors())  // for every child of the node
                {
                    if(!stack.contains(b)) // not inserting elements that are already in the stack, they won't be optimal
                    {
                       stack.push(b); // adding the successor to the queue
                    }
                }
            }
            else
            {
                // when f exceeds the bound update the minBound
                if(minBound == -1)
                {
                    minBound = f;
                }
                else
                {
                    if(minBound > f)
                    {
                        minBound = f;
                    }
                }
            }
        }
        // setting the new bound to the minimal bound that exceeded the limit
        this.bound = minBound;

        return false;
    }

    private void printMoves(Board cur)
    {
        Board current = cur;
        try
        {
            Board prev = current.getPrevBoard();
            Stack<String> reverse_moves = new Stack<>();
            String prevMove = current.getPrevMove();
            
            while(!prevMove.equals(""))
            {
                reverse_moves.push(prevMove);
                current = prev;
                prev = prev.getPrevBoard();
                prevMove = current.getPrevMove();
            }
            System.out.println(reverse_moves.size());
            while(!reverse_moves.isEmpty())
            {
                System.out.println(reverse_moves.pop());
            }
        }
        catch (Exception ignored){}
    }
}
