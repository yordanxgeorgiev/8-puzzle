import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        // getting the user input
        Scanner scanner = new Scanner(System.in);
        int boardSize = scanner.nextInt();
        int zero_pos = scanner.nextInt();
        int width = (int) Math.sqrt(boardSize+1);
        int[][] board = new int[width][width];
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < width; j++)
            {
                board[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Calculating...");

        // starting timer and solving the puzzle
        long startTime = System.nanoTime();
        Board b = new Board(board, boardSize,zero_pos,true);
        Solver s = new Solver(b);
        s.solve();
        long stopTime = System.nanoTime();

        System.out.println("Time: " + (stopTime - startTime)/1_000_000_000 + " seconds");
    }
}
