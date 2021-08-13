import java.util.ArrayList;

public class Board {

    private final int size;          // number of non empty tiles, e.g. 8, 15 ...
    private final int width;         // width of the board
    private final int[][] board;     // board of the puzzle
    private int zero_row;
    private int zero_col;
    private final int zero_goal_pos;       // the goal position of the zero
    private final int manhattan_dist;      // sum of the manhattan distances of every tile
    private boolean solvable;        // is the puzzle solvable
    private int g;                   // the cost to get to the node
    private String prevMove;         // the move that led to this board, used to avoid moves like up->down
    private Board prevBoard;

    // this constructor doesn't check if the input is valid
    public Board(int[][] input_board, int board_size, int zero_row, int zero_col, int zero_goal_position)
    {
        // initializing the members
        size = board_size;
        width = (int) Math.sqrt(board_size+1);
        board = input_board;
        this.zero_row = zero_row;
        this.zero_col = zero_col;
        zero_goal_pos = zero_goal_position;
        g = 0;
        prevMove = "";
        solvable = true;
        // calculating the sum of the manhattan distances
        manhattan_dist = manhattan_dist_sum();
    }

    // this constructor checks if the input is valid
    public Board(int[][] input_board, int board_size, int zero_goal_position, boolean input_check)
    {
        if(input_check)
        {
            // check if the input is valid
            valid_input(input_board, board_size, zero_goal_position);
        }

        // initializing the members
        size = board_size;
        width = (int) Math.sqrt(board_size+1);
        board = input_board;

        findZero();

        if(zero_goal_position == -1)
            zero_goal_pos = board_size;
        else zero_goal_pos = zero_goal_position;
        g = 0;
        prevMove = "";
        solvable = true;
        if(input_check)
        {
            solvable = solvable();
            if(!solvable)
            {
                System.out.println("This puzzle can't be solved!");
                System.exit(-1);
            }
        }

        // calculating the sum of the manhattan distances
        manhattan_dist = manhattan_dist_sum();
    }

    private void findZero()
    {
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(board[i][j] == 0)
                {
                    zero_row = i;
                    zero_col = j;
                    return;
                }
            }
        }
    }

    private void valid_input(int[][] input_board, int board_size, int zero_position)
    {
        // check for valid arguments
        if(input_board == null || board_size <= 0 || zero_position < -1 || zero_position > board_size)
        {
            System.out.println("Error, wrong input!");
            System.exit(-1);
        }

        // the width of the board
        double board_width = Math.sqrt(board_size + 1);

        // check if the board is square
        if(board_width != Math.floor(board_width))
        {
            System.out.println("Error, the board is not of the right size!");
            System.exit(-1);
        }

        // check if the input_board contains all the numbers from 0 to board_size
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < board_width; i++)
        {
            for(int j = 0; j < board_width; j++)
            {
                numbers.add(input_board[i][j]);
            }
        }
        for(int i = 0; i < board_size+1; i++)
        {
            if(!numbers.contains(i))
            {
                System.out.println("Error, wrong input in the matrix!");
                System.exit(-1);
            }
        }
    }

    private boolean solvable()
    {
        // the board in row-major order
        int[] board2 = new int[size+1];
        int iter = 0;
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < width; j++)
            {
                board2[iter] = board[i][j];
                iter++;
            }
        }

        if(width % 2 == 0)
        {
            // if the width is even the puzzle is solvable if:
            // the number of inversions plus the row of the blank square is odd
            return (count_inversions(board2) + zero_row) % 2 != 0;
        }
        else
        {
            // if the width is odd the puzzle is solvable if:
            // the number of inversions is even.
            return count_inversions(board2) % 2 == 0;
        }
    }

    private int count_inversions(int[] arr)
    {
        int inv_count = 0;
        for (int i = 0; i < size; i++)
            for (int j = i + 1; j < size+1; j++)
                if (arr[i] != 0 && arr[j] != 0 && arr[i] > arr[j])
                    inv_count++;

        return inv_count;
    }

    private int manhattan_dist_of_tile(int row, int col)
    {
        int tile = board[row][col];
        if(tile - 1 >= zero_goal_pos)
            tile++;
        int goal_row = (tile-1) / width;
        int goal_col = (tile-1) % width;

        return Math.abs(goal_row-row) + Math.abs(goal_col-col);
    }

    private int manhattan_dist_sum()
    {
        if(solvable)
        {
            int sum = 0;
            for(int i = 0; i < width; i++)
            {
                for(int j = 0; j < width; j++)
                {
                    if(board[i][j] != 0)
                    {
                        sum += manhattan_dist_of_tile(i,j);
                    }
                }
            }
            return sum;
        }
        return -1;
    }

    public int getManhattan_dist() {
        return manhattan_dist;
    }

    public boolean isSolved()
    {
        return  manhattan_dist == 0;
    }

    public void print()
    {
        System.out.println();
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < width; j++)
            {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    public ArrayList<Board> getSuccessors()
    {
        ArrayList<Board> successors = new ArrayList<>();

        if(zero_col != width-1 && !prevMove.equals("right"))
        {
            int[][] board2 = new int[width][width];
            for(int i = 0; i < width; i++)
            {
                System.arraycopy(board[i], 0, board2[i], 0, width);
            }
            board2[zero_row][zero_col] = board2[zero_row][zero_col+1];
            board2[zero_row][zero_col+1] = 0;
            Board left = new Board(board2, size, zero_row, zero_col+1,zero_goal_pos);
            left.prevMove = "left";
            left.prevBoard = this;
            left.g = this.g+1;
            successors.add(left);
        }
        if(zero_col != 0 && !prevMove.equals("left"))
        {
            int[][] board2 = new int[width][width];
            for(int i = 0; i < width; i++)
            {
                System.arraycopy(board[i], 0, board2[i], 0, width);
            }
            board2[zero_row][zero_col] = board2[zero_row][zero_col-1];
            board2[zero_row][zero_col-1] = 0;
            Board right = new Board(board2, size, zero_row, zero_col-1, zero_goal_pos);
            right.prevMove = "right";
            right.prevBoard = this;
            right.g = this.g+1;
            successors.add(right);
        }
        if(zero_row != width-1 && !prevMove.equals("down"))
        {
            int[][] board2 = new int[width][width];
            for(int i = 0; i < width; i++)
            {
                System.arraycopy(board[i], 0, board2[i], 0, width);
            }
            board2[zero_row][zero_col] = board2[zero_row+1][zero_col];
            board2[zero_row+1][zero_col] = 0;
            Board up = new Board(board2, size, zero_row+1, zero_col, zero_goal_pos);
            up.prevMove = "up";
            up.prevBoard = this;
            up.g = this.g+1;
            successors.add(up);
        }
        if(zero_row != 0 && !prevMove.equals("up"))
        {
            int[][] board2 = new int[width][width];
            for(int i = 0; i < width; i++)
            {
                System.arraycopy(board[i], 0, board2[i], 0, width);
            }
            board2[zero_row][zero_col] = board2[zero_row-1][zero_col];
            board2[zero_row-1][zero_col] = 0;
            Board down = new Board(board2, size, zero_row-1, zero_col, zero_goal_pos);
            down.prevMove = "down";
            down.prevBoard = this;
            down.g = this.g+1;
            successors.add(down);
        }

        successors.sort(new BoardComparator());
        return successors;
    }

    public boolean equals(Object obj)
    {
        if (obj != null && getClass() == obj.getClass()) {
            Board b = (Board)obj;
            for(int i = 0; i < width; i++)
            {
                for(int j = 0; j < width; j++)
                {
                    if(this.board[i][j] != b.board[i][j])
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public int getG() {
        return g;
    }

    public String getPrevMove() {
        return prevMove;
    }

    public Board getPrevBoard() {
        return prevBoard;
    }

}
