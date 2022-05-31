package Microproject;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;


public class MainMenu extends Frame implements ActionListener{
    JFrame frame;
    JPanel panel = new JPanel();
    JPanel label_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton[] buttons = new JButton[5];

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int BUTTON_WIDTH = 200;
    static final int BUTTON_HEIGHT = 50;
    static final int BUTTON_GAP = 10;


    MainMenu() {
        Dictionary<Integer, String> dic = new Hashtable<Integer, String>();
        dic.put(0, "Snake Game");
        dic.put(1, "Hangman");
        dic.put(2, "TicTacToe");
        dic.put(3, "R P S");
        dic.put(4, "Exit");

        String imagearr[] = new String[5];
        imagearr[0] = "assets/numbers/1.png";
        imagearr[1] = "assets/numbers/2.png";
        imagearr[2] = "assets/numbers/3.png";
        imagearr[3] = "assets/numbers/4.png";
        imagearr[4] = "assets/numbers/5.png";

        frame = new JFrame("Main Menu");

        frame.setVisible(true);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image title_bar_image = Toolkit.getDefaultToolkit().getImage("assets/numbers/mainmenu.png");
        frame.setIconImage(title_bar_image);

        ImageIcon icon = new ImageIcon((new ImageIcon("assets/numbers/mainmenu.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        
        textfield.setIcon(icon);
        textfield.setOpaque(true);
        textfield.setText("GAME LAUNCHER");
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setForeground(new Color(194, 94, 6));
        textfield.setBackground(new Color(176, 229, 124));
        textfield.setFont(new Font("Luckiest Guy", Font.BOLD, 80));
        textfield.setBorder(BorderFactory.createEmptyBorder(50, 10, 0, 10));
        

        panel.setBackground(new Color(176, 229, 124));
        panel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT - 100);
        panel.setLayout(new GridLayout(5, 1, 0, 10));    
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
       

        for (int i = 0; i <= 4; i++) {
            buttons[i] = new JButton(dic.get(i));
            buttons[i].addActionListener(this);


            buttons[i].setFocusable(false);
            buttons[i].setForeground(new Color(13, 135, 217));
            buttons[i].setBackground(new Color(176, 229, 124));
            buttons[i].setFont(new Font("Luckiest Guy", Font.BOLD, 40));

            buttons[i].setBounds(250, 250, BUTTON_WIDTH, BUTTON_HEIGHT);            
            buttons[i].setIcon(new ImageIcon((new ImageIcon(imagearr[i])).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
           

            panel.add(buttons[i]);
        }


        label_panel.setLayout(new BorderLayout());
        label_panel.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT - 700);
        label_panel.add(textfield);

        frame.add(label_panel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        
    }   

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons[0]) {
            frame.dispose();
            new GameFrame();
        } else if (e.getSource() == buttons[1]) {
            frame.dispose();
            new Hangman_GUI();
        } else if (e.getSource() == buttons[2]) {
            frame.dispose();
            new TicTacToe_GUI();
        } else if (e.getSource() == buttons[3]) {
            frame.dispose();
            new RPS_GUI();
        } else if (e.getSource() == buttons[4]) {
            frame.dispose();
        } else if (e.getSource() == buttons[4]) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new MainMenu();
    }

}
