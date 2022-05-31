package Microproject;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class TicTacToe extends JFrame {
    public TicTacToe() {
        this.setTitle("TicTacToe");
        this.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        this.getContentPane().setBackground(new Color(176, 229, 124));
        this.setResizable(true);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                int result = javax.swing.JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to exit to MAIN MENU?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    new MainMenu();
                    dispose();
                } else {
                    System.exit(0);
                }
            }
        });
        this.setLayout(new BorderLayout());
        this.add(new TicTacToe_Label(), BorderLayout.NORTH);
        this.add(new TicTacToe_Panel(), BorderLayout.CENTER);
        this.add(new TicTacToe_Options(), BorderLayout.SOUTH);
        this.add(new Empty_Panel(), BorderLayout.EAST);
        this.add(new Empty_Panel(), BorderLayout.WEST);

    }
}

class TicTacToe_Label extends JLabel {    
    TicTacToe_Label() {
        this.setText("Tic  Tac  Toe");
        FontMetrics metrics = this.getFontMetrics(this.getFont());
        this.setPreferredSize(new Dimension(1300, metrics.getHeight() + 100));
        this.setBounds(0, metrics.getHeight(), 1300, 300);
        this.setFont(new Font("Luckiest Guy", Font.BOLD, 50));
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }
}

class Empty_Panel extends JPanel {
    Empty_Panel() {
        this.setBackground(new Color(176, 229, 124));
        this.setPreferredSize(new Dimension(50, 50));
    }
}

class TicTacToe_Options extends JPanel {
    TicTacToe_Options() {
        this.setBackground(new Color(176, 229, 124));
        this.setPreferredSize(new Dimension(100, 100));
        this.setLayout(new GridLayout(1, 6, 40, 30));

        JButton reset = new JButton("Reset");
        reset.setBackground(new Color(176, 229, 124));
        reset.setPreferredSize(new Dimension(100, 100));
        reset.setFont(new Font("Luckiest Guy", Font.BOLD, 20));

        JButton mainMenu = new JButton("Main Menu");
        mainMenu.setBackground(new Color(176, 229, 124));
        mainMenu.setPreferredSize(new Dimension(100, 100));
        mainMenu.setFont(new Font("Luckiest Guy", Font.BOLD, 20));
        JButton exit = new JButton("Exit");

        exit.setBackground(new Color(176, 229, 124));
        exit.setPreferredSize(new Dimension(100, 100));
        exit.setFont(new Font("Luckiest Guy", Font.BOLD, 20));

        this.add(reset);
        this.add(new Empty_Panel());
        
        this.add(mainMenu);
        this.add(new Empty_Panel());
        this.add(exit);

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    TicTacToe_Panel.reset();
            }
        });
        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    new MainMenu();
                    Window window = SwingUtilities.getWindowAncestor(TicTacToe_Options.this);
                    window.dispose();
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}

class TicTacToe_Panel extends JPanel {
    private static JButton[][] buttons = new JButton[3][3];
    private static boolean player1Turn = true;
    private static int turnCount = 0;

    TicTacToe_Panel() {
        setLayout(new GridLayout(3, 3, 40, 30));
        setBackground(new Color(176, 229, 124));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.red, Color.blue));
                buttons[i][j].setBackground(new Color(176, 229, 124));
                buttons[i][j].setFont(new Font("Luckiest Guy", Font.BOLD, 60));
                buttons[i][j].setEnabled(true);
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        if (button.getText().equals("")) {
                            if (player1Turn) {
                                button.setText("X");
                                button.setForeground(new Color(194, 94, 6));
                            } else {
                                button.setText("O");
                                button.setForeground(new Color(13, 135, 217));
                            }
                            turnCount++;
                            if (checkForWinner()) {
                                JOptionPane.showMessageDialog(TicTacToe_Panel.this, "Player " + (player1Turn ? "1" : "2") + " wins!");
                                reset();
                            } else if (turnCount == 9) {
                                JOptionPane.showMessageDialog(TicTacToe_Panel.this, "Draw!");
                                reset();
                            } else {
                                player1Turn = !player1Turn;
                            }
                        }
                    }
                });
                add(buttons[i][j]);
            }
            
        }
    }

    private boolean checkForWinner() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (!field[i][0].equals("") && field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (!field[0][i].equals("") && field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])) {
                return true;
            }
        }
        if (!field[0][0].equals("") && field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])) {
            return true;
        }
        if (!field[0][2].equals("") && field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])) {
            return true;
        }
        return false;
    }
    
    public static void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        turnCount = 0;
        player1Turn = true;
    }
}