import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualizer implements Solver.SolverCallback {
    private JFrame frame;
    private JButton solveButton;
    private JButton clearButton;
    private JButton exitButton;
    private JTextField[] fields;
    private Solver solver;
    private SudokuClass sudoku;
    private JComboBox<String> difficultyDropdown;

    public Visualizer() {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(800, 700));

        JPanel sudokuPanel = new JPanel();
        sudokuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sudokuPanel.setLayout(new GridLayout(9, 9));

        fields = getClearedFields();

        for (int x = 0; x < 81; x++) {
            sudokuPanel.add(fields[x]);
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        difficultyDropdown = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficultyDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSudokuGame(difficultyDropdown.getSelectedItem().toString());
            }
        });

        solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> GUIToSudoku(fields));

        clearButton = new JButton("Clear Grid");
        clearButton.addActionListener(e -> clearGrid());

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> frame.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        controlPanel.add(difficultyDropdown);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlPanel.add(createNumberPad());
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlPanel.add(buttonPanel);

        frame.add(sudokuPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.EAST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sudoku Solver");
        frame.pack();
        frame.setVisible(true);
    }

    private void loadSudokuGame(String difficulty) {
        Random random = new Random();
        int[][][] easyGames = {
            {
                {0, 0, 0, 0, 6, 4, 0, 0, 9},
                {6, 0, 0, 0, 7, 0, 0, 8, 4},
                {0, 0, 0, 1, 7, 9, 0, 3, 0},
                {9, 0, 1, 0, 7, 0, 0, 3, 0},
                {0, 0, 2, 0, 6, 0, 0, 9, 0},
                {0, 0, 0, 0, 0, 4, 0, 1, 7},
                {3, 0, 0, 0, 0, 2, 0, 0, 6},
                {0, 0, 0, 0, 0, 7, 3, 0, 1},
                {0, 4, 0, 0, 3, 1, 0, 0, 0}
            },
            {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
            },
            {
                {0, 0, 0, 6, 0, 0, 4, 0, 0},
                {7, 0, 0, 0, 0, 3, 6, 0, 0},
                {0, 0, 0, 0, 9, 1, 0, 8, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 5, 0, 1, 8, 0, 0, 0, 3},
                {0, 0, 0, 3, 0, 6, 0, 4, 5},
                {0, 4, 0, 2, 0, 0, 0, 6, 0},
                {9, 0, 3, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 1, 0, 0}
            }
        };

        int[][][] mediumGames = {
            {
                {0, 0, 0, 2, 6, 0, 7, 0, 1},
                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                {7, 0, 3, 0, 1, 8, 0, 0, 0}
            },
            {
                {0, 0, 0, 0, 6, 0, 0, 0, 0},
                {7, 0, 0, 0, 0, 3, 0, 0, 0},
                {0, 0, 0, 0, 9, 0, 2, 0, 0},
                {5, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 0, 6, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 4, 0},
                {8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 3, 0, 0, 0, 0, 0, 0}
            },
            {
                {0, 0, 0, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 8, 0, 0, 4, 3},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 7, 0, 0},
                {3, 0, 2, 0, 0, 0, 8, 0, 0},
                {0, 0, 0, 3, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 5, 9, 0, 0, 0},
                {0, 6, 0, 0, 4, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
            }
        };

        int[][][] hardGames = {
            {
                {0, 0, 3, 0, 2, 0, 6, 0, 0},
                {9, 0, 0, 3, 0, 5, 0, 0, 1},
                {0, 0, 1, 8, 0, 6, 4, 0, 0},
                {0, 0, 8, 1, 0, 2, 9, 0, 0},
                {7, 0, 0, 0, 0, 0, 0, 0, 8},
                {0, 0, 6, 7, 0, 8, 2, 0, 0},
                {0, 0, 2, 6, 0, 9, 5, 0, 0},
                {8, 0, 0, 2, 0, 3, 0, 0, 9},
                {0, 0, 5, 0, 1, 0, 3, 0, 0}
            },
            {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
            },
            {
                {0, 0, 0, 0, 0, 7, 0, 8, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 8, 0, 0, 5, 0, 0, 0},
                {4, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 3, 0, 0, 9, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 6, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 4, 0, 0, 0, 0, 0}
            }
        };

        int[][] selectedGame = new int[9][9];
        switch (difficulty) {
            case "Easy":
                selectedGame = easyGames[random.nextInt(easyGames.length)];
                break;
            case "Medium":
                selectedGame = mediumGames[random.nextInt(mediumGames.length)];
                break;
            case "Hard":
                selectedGame = hardGames[random.nextInt(hardGames.length)];
                break;
        }

        for (int i = 0; i < 81; i++) {
            int row = i / 9;
            int col = i % 9;
            if (selectedGame[row][col] != 0) {
                fields[i].setText(String.valueOf(selectedGame[row][col]));
                fields[i].setEditable(false);
                fields[i].setBackground(Color.LIGHT_GRAY);
            } else {
                fields[i].setText("");
                fields[i].setEditable(true);
                fields[i].setBackground(Color.WHITE);
            }
        }
    }

    private JPanel createNumberPad() {
        JPanel numberPad = new JPanel();
        numberPad.setLayout(new GridLayout(3, 3, 5, 5));
        numberPad.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(new NumberPadListener());
            numberPad.add(button);
        }

        return numberPad;
    }

    private class NumberPadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String number = source.getText();
            for (JTextField field : fields) {
                if (field.isEditable() && field.hasFocus()) {
                    field.setText(number);
                    break;
                }
            }
        }
    }

    JTextField[] getClearedFields() {
        JTextField[] newFields = new JTextField[81];
        for (int x = 0; x < 81; x++) {
            newFields[x] = new JTextField();
            JTextField f = newFields[x];
            f.setHorizontalAlignment(JTextField.CENTER);
            f.setFont(new Font("Arial", Font.BOLD, 20));
            f.setBackground(Color.WHITE);
            f.setEditable(true);
            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            f.setBorder(border);

            f.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    char pressedKey = ke.getKeyChar();
                    if (pressedKey == 8) {
                        f.setText("");
                        f.setBackground(Color.WHITE);
                    } else if (pressedKey >= '1' && pressedKey <= '9') {
                        f.setText("" + pressedKey);
                        f.setEditable(false);
                        f.setBackground(Color.GRAY);
                    } else {
                        f.setEditable(false);
                    }
                }
            });
            f.setEditable(true);
        }
        applyThickerBorders(newFields);
        return newFields;
    }

    void applyThickerBorders(JTextField[] fields) {
        for (int i = 0; i < 81; i++) {
            int row = i / 9;
            int col = i % 9;

            Border border = fields[i].getBorder();
            Border thickBorder = BorderFactory.createMatteBorder(
                row % 3 == 0 ? 3 : 1,
                col % 3 == 0 ? 3 : 1,
                (row + 1) % 3 == 0 ? 3 : 1,
                (col + 1) % 3 == 0 ? 3 : 1,
                Color.BLACK
            );
            fields[i].setBorder(BorderFactory.createCompoundBorder(thickBorder, border));
        }
    }

    void populateFieldInteractive(JTextField f, int numberToUpdate, int initial) {
        f.setEditable(true);
        if (initial == 0) {
            f.setForeground(Color.BLUE);
        } else {
            f.setForeground(Color.BLACK);
        }
        f.setText("" + numberToUpdate);
        f.setEditable(false);
    }

    void SudokuToGUI(int[][] solution, int[][] initialSudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                populateFieldInteractive(fields[(i * 9) + j], solution[i][j], initialSudoku[i][j]);
            }
        }
    }

    void copySudoku(int[][] originalSudoku, int[][] copiedSudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copiedSudoku[i][j] = originalSudoku[i][j];
            }
        }
    }

    void GUIToSudoku(JTextField[] fields) {
        int[][] inputPuzzle = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String val = fields[9 * i + j].getText();
                if (val.equals("")) {
                    inputPuzzle[i][j] = 0;
                } else {
                    inputPuzzle[i][j] = Integer.parseInt(val);
                }
            }
        }

        sudoku = new SudokuClass(inputPuzzle);
        solver = new Solver(inputPuzzle, this);

        new Thread(() -> {
            if (solver.solve()) {
                int[][] solution = solver.getBoard();
                SwingUtilities.invokeLater(() -> {
                    SudokuToGUI(solution, inputPuzzle);
                    JOptionPane.showMessageDialog(frame, "Sudoku Solved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "No solution exists for the given puzzle", "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void clearGrid() {
        for (int i = 0; i < 81; i++) {
            fields[i].setText("");
            fields[i].setBackground(Color.WHITE);
            fields[i].setForeground(Color.BLACK);
            fields[i].setEditable(true);
        }
    }

    @Override
    public void updateCell(int row, int col, int value) {
        SwingUtilities.invokeLater(() -> {
            JTextField field = fields[row * 9 + col];
            field.setText(value == 0 ? "" : String.valueOf(value));
            field.setForeground(value == 0 ? Color.BLACK : Color.BLUE);
        });
    }

    @Override
    public void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Visualizer::new);
    }
}