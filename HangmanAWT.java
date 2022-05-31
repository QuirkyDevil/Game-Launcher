package Microproject;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.*;
import java.net.*;
import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

class Hangman_GUI extends JFrame {
    Hangman_GUI() {
        this.setTitle("HANGMAN");
        this.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1300, 750);
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
        this.add(new Hangman_Panel());
    }
}

class Hangman_Panel extends JPanel {
    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int HEART_X = 1240;
    public int tries_number = 6;
    String guessed_correct_temp = "";
    
    JTextField textField;
    JLabel label, guessed_correct, tries;

    public Vector<String> words = new Vector<String>();

    void get_words() {
        try {
            Scanner scanner = new Scanner(new File("assets/words.txt"));
            while (scanner.hasNext()) {
                words.add(scanner.next());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        mystry_word  = words.get(random.nextInt(words.size())).toLowerCase();
        System.out.println(mystry_word);
    }

    
    public Random random = new Random();
    
    public Vector<Character> player_guess = new Vector<Character>();
    public String mystry_word = "test";
    public Vector<Character> mystry_word_array = new Vector<Character>();
    public Vector<Character> guessed_array = new Vector<Character>();

    File hangman_images[] = {
            new File("assets/Hangman/hangman0.jpeg"),
            new File("assets/Hangman/hangman1.jpeg"),
            new File("assets/Hangman/hangman2.jpeg"),
            new File("assets/Hangman/hangman3.jpeg"),
            new File("assets/Hangman/hangman4.jpeg"),
            new File("assets/Hangman/hangman5.jpeg"),
            new File("assets/Hangman/hangman6.jpeg"),
        };

    void conver_Vector(){
        for (int i = 0; i < mystry_word.length(); i++) {
            mystry_word_array.add(mystry_word.charAt(i));
        }
    }

    Hangman_Panel() {    
    get_words();
    conver_Vector();
    this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    this.setVisible(true);
    this.setBackground(new Color(176, 229, 124));
    this.setLayout(null);

    JLabel label = new JLabel("HANGMAN");
    label.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
    FontMetrics metrics = getFontMetrics(label.getFont());
    label.setBounds(550, metrics.getHeight(), 300,  100);

    JLabel tries = new JLabel("Tries Left: " + tries_number);
    tries.setFont(new Font("Luckiest Guy", Font.BOLD, 20));
    tries.setBounds(0, 0, 300,  50);
    
    for (int i = 0; i < mystry_word_array.size(); i++) {
        guessed_correct_temp += "_ ";
    }

    JLabel guessed_correct = new JLabel(guessed_correct_temp);
    guessed_correct.setFont(new Font("Luckiest Guy", Font.BOLD, 30));
    FontMetrics metrics2 = getFontMetrics(guessed_correct.getFont());
    guessed_correct.setBounds(((SCREEN_WIDTH/2) - metrics2.stringWidth(mystry_word)/2), 600, 700,  100);

    textField = new JTextField(6);
    textField.setBorder(new BevelBorder(BevelBorder.LOWERED));
    textField.setBackground(new Color(176, 229, 124));
    textField.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
    textField.setBounds(300, SCREEN_HEIGHT - 300, 700, 100);    
    textField.setHorizontalAlignment(JTextField.CENTER);

    
    textField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String guess = textField.getText().toLowerCase();
            textField.setText("");
            player_guess.add(((String) guess).charAt(0));

            if(tries_number == 1) {
                tries_number--;
                tries.setText("Tries left: " + tries_number);
                repaint();
                
                JOptionPane.showMessageDialog(null, "You lost! The word was: " + mystry_word);
                new MainMenu();
                Window window = SwingUtilities.getWindowAncestor(Hangman_Panel.this);
                window.dispose();
            }
            if(guess.length() == 1) {
                if(mystry_word.contains(guess)) { // t
                    for (int i = 0; i < mystry_word_array.size(); i++) { // t e s t   guess: t

                        if (mystry_word_array.get(i) == guess.charAt(0)) {
                            if(guessed_array.size() == 0){
                                guessed_array.add(guess.charAt(0)); // guessed_array: [t _ _ t]
                            }
                            else{
                                if(guessed_array.size() != 0) {
                                    if((i + 1) > guessed_array.size()){
                                        guessed_array.add(guess.charAt(0));
                                    }
                                    else{
                                        guessed_array.set(i, guess.charAt(0));
                                    }
                                }
                            }
                        } else {
                            if(guessed_array.size() != mystry_word_array.size()) { 
                                guessed_array.add('_');
                        }
                    }
                }
                    for(int j = 0; j < mystry_word.length(); j++) {
                        String temp = ""; // ""
                        for(int k = 0; k < guessed_array.size(); k++) {
                            temp += guessed_array.get(k);  
                        }
                        guessed_correct.setText(temp);
                    }
                } 
                else {
                    tries_number--;
                    tries.setText("Tries left: " + tries_number);
                    repaint();
                }
            }
            if (guess.length() > 1) {
                if(guess.equals(mystry_word)) {
                    JOptionPane.showMessageDialog(null, "You won!");
                    new MainMenu();
                    Window window = SwingUtilities.getWindowAncestor(Hangman_Panel.this);
                    window.dispose();
                } else {
                    tries_number--;
                    tries.setText("Tries left: " + tries_number);
                    repaint();
                }
            }
            if(mystry_word_array.equals(guessed_array)) { 
                JOptionPane.showMessageDialog(null, "You won!");
                new MainMenu();
                Window window = SwingUtilities.getWindowAncestor(Hangman_Panel.this);
                window.dispose();
            }
        }
    });


    this.add(textField);
    this.add(guessed_correct);
    this.add(label);
    this.add(tries);  
    }

    @Override
    protected void paintComponent(Graphics g) {
        Image image = null;
        try {
            image = ImageIO.read(
                    new URL("https://cdn.kapwing.com/video_image-Bz5ouo4Jn.jpg"));  
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.paintComponent(g);
        for(int i = tries_number; i > 0; i--) {
            g.drawImage(image, HEART_X - (20*i), 10, 20, 20, null);
        }

        try{
            if (tries_number != 6) {  
                System.out.println(tries_number);
                Image img = ImageIO.read(new File("assets/Hangman/hangman" + (tries_number) + ".jpeg"));
                g.drawImage(img, SCREEN_WIDTH / 2 - 150, 120, 350, 300, null);
            }
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

}

public class HangmanAWT {
    public static void main(String[] args) {
        new Hangman_GUI();
    }
}
