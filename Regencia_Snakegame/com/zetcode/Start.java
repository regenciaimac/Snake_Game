package com.zetcode;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;
public class Start {
    JFrame frame;
    JProgressBar progressBar;
    int progress;
    Start() {
        frame = new JFrame("Start");

        JButton startButton = new JButton("START");
        startButton.setBounds(170, 300, 150,40);
        Font small = new Font("Serif Bold", Font.BOLD, 25);

        JLabel backgroundLabel = new JLabel(new ImageIcon("com/img/bg1.png"));
        backgroundLabel.setBounds(0, 0, 300, 300);
        
        backgroundLabel.setLayout(null);
        backgroundLabel.add(startButton);
        
        frame.setContentPane(backgroundLabel);
        frame.add(startButton);
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        startButton.setFont(small);
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startProgress();
            }
        });
    }

    public void startProgress() {
        frame.getContentPane().removeAll();

        progressBar = new JProgressBar(0, 70);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(150, 300, 200, 30);

        frame.add(progressBar);
        frame.revalidate();
        frame.repaint();

        progress = 0;

        Timer timer = new Timer(70, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (progress >= 70) {
                    ((Timer) e.getSource()).stop();
                    goToNextClass();
                } else {
                    progress++;
                    progressBar.setValue(progress);
                }
            }
        });

        timer.start();
    }

    public void goToNextClass() {
        frame.dispose(); 
    
        Snake s = new Snake();
        s.show();
    }

    public static void main(String args[]) {
        new Start();
    }
}
