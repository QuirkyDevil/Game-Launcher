package Microproject;


import java.awt.*;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;


class RPS_GUI extends JFrame {
    RPS_GUI() {
        this.setTitle("TicTacToe");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        this.getContentPane().setBackground(new Color(176, 229, 124));
        this.setResizable(true);
        this.setSize(1000, 1000);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
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
        this.add(new RPS_Panel(), BorderLayout.CENTER);
        this.add(new Empty_Panel(), BorderLayout.EAST);
        this.add(new Empty_Panel(), BorderLayout.WEST);
        this.add(new Empty_Panel(), BorderLayout.SOUTH);
    }

}

class Empty_Panel extends JPanel {
    Empty_Panel() {
        this.setBackground(new Color(176, 229, 124));
        this.setPreferredSize(new Dimension(50, 50));
    }
}

class RPS_Panel extends JPanel{
    public static JPanel left_wing = new JPanel();
    public static JPanel right_wing = new JPanel();
    public static JPanel center_wing = new JPanel();
    public static JPanel north_panel = new JPanel();

    Timer timer;

    public int player_choice = 0;
    public int player_score = 0;
    public int computer_score = 0;
    public int max_tries = 5;

    public static Random random = new Random();
    public static int random_number = random.nextInt(3);

    String[] words = {"Rock", "Paper", "Scissors"};    


    JButton[] buttons = new JButton[6];
    RPS_Panel() {
        this.setBackground(new Color(176, 229, 124));
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 1000));

        left_wing = new JPanel();
        left_wing.setBackground(new Color(176, 229, 124));
        left_wing.setLayout(new GridLayout(3, 1, 10, 10));
        left_wing.setPreferredSize(new Dimension(300, 1000));

        right_wing = new JPanel();
        right_wing.setBackground(new Color(176, 229, 124));
        right_wing.setLayout(new GridLayout(3, 1, 10, 10));
        right_wing.setPreferredSize(new Dimension(300, 1000));

        center_wing = new JPanel();
        center_wing.setLayout(new GridLayout(3, 1, 10, 10));
        center_wing.setBackground(new Color(176, 229, 124));
        center_wing.setLayout(new GridLayout(3, 1));
        center_wing.setPreferredSize(new Dimension(300, 1000));

        URL url = null;
        try {
            url = new URL("https://i.imgur.com/bFYroWk.gif");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
        center_wing.add(new Empty_Panel());
        center_wing.add(label);


        JLabel text = new JLabel("");
        text.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
        text.setForeground(new Color(13, 135, 217));
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setVerticalAlignment(JLabel.CENTER);


        north_panel = new JPanel();
        north_panel.setBackground(new Color(176, 229, 124));
        north_panel.setLayout(null);
        north_panel.setPreferredSize(new Dimension(1000, 100));

        JLabel title = new JLabel("Rock Paper Scissors");
        title.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
        title.setForeground(new Color(0, 0, 0));
        title.setBounds(300, 10, 700, 50);
        title.setForeground(new Color(194, 94, 6));
        north_panel.add(title);

        



        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setBackground(new Color(176, 229, 124));
            buttons[i].setFont(new Font("Luckiest Guy", Font.BOLD, 50));
            buttons[i].setHorizontalAlignment(JButton.CENTER);
            buttons[i].setVerticalAlignment(JButton.CENTER);
            if (i % 2 == 0) {
                left_wing.add(buttons[i]);
                buttons[i].setText(words[i / 2]);
            } else {
                right_wing.add(buttons[i]);
                buttons[i].setText(words[i / 2]);
            }
            buttons[i].setBorder(BorderFactory.createBevelBorder(1, Color.red, Color.blue));
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String button_text = button.getText();

                    if (button.getParent() == right_wing) {
                        return;
                    }
                    String button_label = words[random_number];
                    getButtonWithLabel(button_label);

                    if (button_text.equals(words[0])) {
                        player_choice = 0;
                    } else if (button_text.equals(words[1])) {
                        player_choice = 1;
                    } else if (button_text.equals(words[2])) {
                        player_choice = 2;
                    }
                    button.setBackground(new Color(194, 94, 6));

                    check_Winner();

                }
            });
            
        }
        this.add(left_wing, BorderLayout.WEST);
        this.add(right_wing, BorderLayout.EAST);
        this.add(center_wing, BorderLayout.CENTER);
        this.add(north_panel, BorderLayout.NORTH);


    }
    
    JButton getButtonWithLabel(String label) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getText().equals(label)) {
                if (buttons[i].getParent() == right_wing) {
                    buttons[i].setBackground(new Color(13, 135, 217));
                    return buttons[i];
                }
            }
        }
        return null;
    }

    void check_Winner(){

        if (player_choice == 0) {
            if (random_number == 0) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[0] + " Tie!");
            } else if (random_number == 1) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[1] + " You lose!");
                computer_score++;
            } else if (random_number == 2) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[2] + " You win!");
                player_score++;
            }
        } else if (player_choice == 1) {
            if (random_number == 0) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[0] + " You win!");
                player_score++;
            } else if (random_number == 1) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[1] + " Tie!");
            } else if (random_number == 2) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[2] + " You lose!");
                computer_score++;
            }
        } else if (player_choice == 2) {
            if (random_number == 0) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[0] + " You lose!");
                computer_score++;
            } else if (random_number == 1) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[1] + " You win!");
                player_score++;
            } else if (random_number == 2) {
                JOptionPane.showMessageDialog(null, "Computer: " + words[2] + " Tie!");
            }
        }
        reset();
    }
    void reset(){
        random_number = random.nextInt(3);
        if (max_tries != 0) {
            max_tries--;
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setBackground(new Color(176, 229, 124));
            }
        }
        if (max_tries == 0) {
            JOptionPane.showMessageDialog(null, "Computer: " + words[random_number] + " You lose!");
            computer_score++;
            JOptionPane.showMessageDialog(null, "Computer: " + computer_score + " Player: " + player_score);

            Window window = SwingUtilities.getWindowAncestor(RPS_Panel.this);
            window.dispose();
            new MainMenu();
        }


    }
}

public class RPS {
    public static void main(String[] args) {
        new RPS_GUI();
    }
}
