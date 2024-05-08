package chatting.application;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


import javax.swing.JFrame;
import javax.swing.JPanel;

public class Server extends JFrame {
	
	Server() {
		
		setLayout(null);
		
		JPanel p1 = new JPanel();
		p1.setBackground(new Color(7, 94, 84));
		p1.setBounds(0, 0, 450, 70);
		add(p1);
		
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
		
		setSize(450, 700);
		setLocation(200, 50);
		getContentPane().setBackground(Color.WHITE);
		
		
		
		setVisible(true);
	}

	public static void main(String[] args) {
		new Server();
	}

}
