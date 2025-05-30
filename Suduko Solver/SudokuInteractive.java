import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.MatteBorder;


public class SudokuInteractive extends JFrame {
    private final int SIZE = 9;
    private JButton[][] boardButtons = new JButton[SIZE][SIZE];
    private JPanel numberPanel;
    //private JPanel levelPanel;
    JLabel lifeline=new JLabel("");
    JLabel showlevel=new JLabel("");
    JPanel statusPanel = new JPanel(new GridLayout(1, 1));
    private int selectedRow = -1, selectedCol = -1;
    private int[][] solution=new int[9][9];  // assume a correct solution for validation
    private int[][] puzzle=new int[9][9];    // initial puzzle state
    int row[][]=new int[9][9];
    int col[][]=new int[9][9];
    int box[][]=new int[9][9];
    int chances=2,streak=0,k=0;
    int filled = 0,count=40;
   // String level="";
    public SudokuInteractive(int streak,int count) {
       
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font font = new Font("Arial", Font.BOLD, 18);
        //Level Getting from user;
       // levelPanel=new JPanel(new GridLayout(1, 3));
        
        //  gridPanel.setVisible(false);
        //  statusPanel.setVisible(false);
       //JButton levels[]=new JButton[3];
      // String levels[]={"Easy","Medium","Hard"};
    //   levelPanel.setVisible(true);
    //     for(k=0;k<3;k++)
    //     {
    //         JButton btn1 = new JButton(levels[k]);
    //         btn1.setFont(font);
    //         btn1.addActionListener(e -> {
    //             level=levels[k];
    //             levelPanel.setVisible(false);
    //              gridPanel.setVisible(true);
    //             statusPanel.setVisible(true);
    //             solve(solution,row,col,box,0,0);
    //             if(level.equals("Easy")) generatePuzzle(40);
    //             else if(level.equals("Hard")) generatePuzzle(55);
    //             else  generatePuzzle(45);
    //              // fills 'puzzle' and 'solution'
        

    //         });
    //         levelPanel.add(btn1);
    //     }
        
        
        // Top Grid Panel
        this.streak=streak;
        this.count=count;
        String temp="Streak : "+String.valueOf(streak);
        showlevel.setText(temp);
        statusPanel.add(showlevel);
        temp="Life: ";
        for(int i=0;i<=chances;i++)
        {
            temp+="x ";
        }
        lifeline.setText(temp);
        statusPanel.add(lifeline);
        JButton restart=new JButton("Restart");
        restart.addActionListener(l ->{
            SwingUtilities.invokeLater(() -> new SudokuInteractive(0,40).setVisible(true));
        });
        statusPanel.add(restart);
       
        solve(solution,row,col,box,0,0);
        // if(level.equals("Easy")) generatePuzzle(40);
        // else if(level.equals("Hard")) generatePuzzle(55);
        // else  
        if(count>=64) generatePuzzle(63);
       else generatePuzzle(count);
        //  // fills 'puzzle' and 'solution'

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setFont(font);
                btn.setFocusable(false);

                int r = row, c = col;
                if (puzzle[row][col] != 0) {
                    btn.setText(String.valueOf(puzzle[row][col]));
                    btn.setEnabled(false);
                    filled++;
                } else {
                    btn.addActionListener(e -> {
                        selectedRow = r;
                        selectedCol = c;
                       // btn.setBorder(new MatteBorder(1, 1, 1, 1, Color.red));
                        showNumberPanel(btn);
                        // Thread thread=new Thread();
                        // thread.sleep(5000);
                       // btn.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
                    });
                }
                btn.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
                if(row==2 || row==5)
                {
                    if(col==2 || col==5)
                    {
                        btn.setBorder(new MatteBorder(1, 1, 3, 3, Color.black));
                    }
                    // Border defaultBorder=UIManager.getBorder("");
                    // Border colourBorder=new MatteBorder(0, 0, 2, 0, Color.black);
                    // btn.setBorder(new CompoundBorder(colourBorder,defaultBorder));
                    else btn.setBorder(new MatteBorder(1, 1, 3, 1, Color.black));
                }
                else if(col==2 || col==5)
                {
                    // Border defaultBorder=UIManager.getBorder(c);
                    // Border colourBorder=new MatteBorder(0, 0, 0, 2, Color.black);
                    // btn.setBorder(new CompoundBorder(colourBorder,defaultBorder));
                    btn.setBorder(new MatteBorder(1, 1, 1, 3, Color.black));
                }
                boardButtons[row][col] = btn;
                gridPanel.add(btn);
            }
        }

        // Bottom Number Selection Panel (hidden by default)
        numberPanel = new JPanel(new GridLayout(1, 9));
        numberPanel.setVisible(false);
        for (int i = 1; i <= 9; i++) {
            int number = i;
            JButton numBtn = new JButton(String.valueOf(i));
            numBtn.setFont(font);
            numBtn.addActionListener(e -> handleNumberSelection(number));
            numberPanel.add(numBtn);
        }
     //  add(levelPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(numberPanel, BorderLayout.SOUTH);
    }

    private void showNumberPanel(JButton btn) {
        numberPanel.setVisible(true);
        revalidate();
        repaint();
       // btn.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
    }

    private void handleNumberSelection(int number) {
        if (selectedRow != -1 && selectedCol != -1) {
            boardButtons[selectedRow][selectedCol].setText(String.valueOf(number));

            // Validate input against solution
            if (solution[selectedRow][selectedCol] == number) {
                boardButtons[selectedRow][selectedCol].setForeground(Color.GREEN);
                boardButtons[selectedRow][selectedCol].setEnabled(false);
                filled++;
                if(filled==81)
                {
                    streak++;
                    JOptionPane.showMessageDialog(this,"You Won");
                    JOptionPane.showMessageDialog(this,"Play Again");

                   SwingUtilities.invokeLater(() -> new SudokuInteractive(streak,count+3).setVisible(true));
                 
                }
            } else {
                if(chances==0)
                {
                    streak=1;
                    JOptionPane.showMessageDialog(this,"You Lose");
                    JOptionPane.showMessageDialog(this,"Play Again");
                    SwingUtilities.invokeLater(() -> new SudokuInteractive(streak,count).setVisible(true));
                  
                }
                chances--;
                boardButtons[selectedRow][selectedCol].setForeground(Color.RED);
                String  temp="Life : ";
                for(int i=0;i<=chances;i++)
                {
                    temp+="x ";
                }
                lifeline.setText(temp);
            }

            numberPanel.setVisible(false);
        }
    }

    private void generatePuzzle(int count) {
        // Hardcoded sample puzzle and its solution for simplicity
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++)
            {
                puzzle[i][j]=solution[i][j];
            }
        }
        Random rand=new Random();
        int attempt=0;
        while(count>0 && attempt<1000)
        {
            int ro=rand.nextInt(9);
            int co=rand.nextInt(9);
            if(puzzle[ro][co]!=0)
            {
                puzzle[ro][co]=0;
                count--;
            }
            attempt++;
        }
        }

       
    
    public static boolean solve(int[][] solution,int[][] row,int[][] col,int[][] box,int i,int j)
    {
        if(i==9)return true;
        if(j==9)return solve(solution, row, col, box, i+1, 0);
        if(solution[i][j]!=0)return solve(solution, row, col, box, i,j+1);
        ArrayList<Integer> nums = new ArrayList<>();
        for (int k = 0; k < 9; k++) nums.add(k);
        Collections.shuffle(nums);  // random order each time
        
        for (int k : nums) {
            if (row[i][k] == 0 && col[j][k] == 0) {
                int box_no = (i / 3) * 3 + (j / 3);
                if (box[box_no][k] == 0) {
                    row[i][k] = 1;
                    col[j][k] = 1;
                    box[box_no][k] = 1;
                    solution[i][j] = k + 1;
                    if (solve(solution, row, col, box, i, j + 1)) return true;
                    row[i][k] = 0;
                    col[j][k] = 0;
                    box[box_no][k] = 0;
                    solution[i][j] = 0;
                }
            }
        }
        return false;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuInteractive(0,40).setVisible(true));
    
    }
}