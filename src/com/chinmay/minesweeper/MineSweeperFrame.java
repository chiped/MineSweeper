package com.chinmay.minesweeper;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MineSweeperFrame extends JFrame {
	
	private GamePanel gamePanel;
	private int gameWidth;
	private int gameHeight;
	private int mineCount;
	private boolean eightConnect;
	public MineSweeperFrame() {
		super("Mine Sweeper");
		initialize();
	}
	
	public void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width/2, screenSize.height/2);
		this.setLocation(screenSize.width/4, screenSize.height/4);
		this.setJMenuBar(createMenuBar());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog((JFrame) e.getSource(), "Are you sure you want to exit ?", "Exit Mine Sweeper", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)                     
                        System.exit(0);
	        }
	    });
		gameWidth = gameHeight = mineCount = 10;
		gamePanel = new GamePanel(gameWidth, gameHeight, mineCount, eightConnect);
		this.add(gamePanel);
		this.setVisible(true);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu gameMenu, helpMenu;
		JMenuItem newGame, options, exit;
		JMenuItem about, instructions;
		
		gameMenu = new JMenu("Game");
		gameMenu.setMnemonic(KeyEvent.VK_G);
		menuBar.add(gameMenu);
		
		newGame = new JMenuItem("New game");
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}			
		});
		gameMenu.add(newGame);
		options = new JMenuItem("Options");
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel settingsPanel = new JPanel(new GridLayout(4,2));
				JTextField gameWidthText = new JTextField(String.valueOf(gameWidth));
				JTextField gameHeightText = new JTextField(String.valueOf(gameHeight));
				JTextField mineCountText = new JTextField(String.valueOf(mineCount));
				JCheckBox eightConnectValue = new JCheckBox();
				eightConnectValue.setSelected(eightConnect);
				settingsPanel.add(new JLabel("Width: "));
				settingsPanel.add(gameWidthText);
				settingsPanel.add(new JLabel("Height: "));
				settingsPanel.add(gameHeightText);
				settingsPanel.add(new JLabel("Mines: "));
				settingsPanel.add(mineCountText);
				settingsPanel.add(new JLabel("Eight Connected: "));
				settingsPanel.add(eightConnectValue);
				boolean validInput = false;
				while(!validInput) {
				int result = JOptionPane.showConfirmDialog((JMenuItem) e.getSource(), settingsPanel, "Settings", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION)  {
                	int width = Integer.parseInt(gameWidthText.getText());
                	int height = Integer.parseInt(gameHeightText.getText());
                	int count = Integer.parseInt(mineCountText.getText());
                	boolean flag = eightConnectValue.isSelected();
                	if(width > 0 && width <=30 && height > 0 && height <= 30 && count < width*height-1) {
                		gameWidth = width;
                		gameHeight = height;
                		mineCount = count;
                		eightConnect = flag;
                		validInput = true;
                		newGame();
                	}
                	else
                		JOptionPane.showConfirmDialog(null, "Enter dimensions between 0 and 30.\nMake sure mine count is less than tiles", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                }
				}
			}			
		});
		gameMenu.add(options);
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog((JMenuItem) e.getSource(), "Are you sure you want to exit ?", "Exit Mine Sweeper", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)                     
                        System.exit(0);
			}			
		});
		gameMenu.add(exit);
		
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);
		
		instructions = new JMenuItem("Instructions");
		instructions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String about = "<HTML>The rules in Minesweeper are simple:" +
						"<ul><li>Uncover a mine, and the game ends.</li>" +
						"<li>Uncover an empty square, and you keep playing.</li>" +
						"<li>Uncover a number, and it tells you how many mines lay<br>" +
						"hidden in the eight surrounding squares—information you<br>" +
						"use to deduce which nearby squares are safe to click.</li></ul><HTML>";
				JOptionPane.showMessageDialog((JMenuItem)arg0.getSource(), about, "About Mine Sweeper", JOptionPane.OK_OPTION);
			}
		});
		helpMenu.add(instructions);
		about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String about = "Mine Sweeper in Java\nCreated by Chinmay Pednekar\nJune 16 2014";
				JOptionPane.showMessageDialog((JMenuItem)arg0.getSource(), about, "About Mine Sweeper", JOptionPane.OK_OPTION);
			}			
		});
		helpMenu.add(about);
		
		return menuBar;
	}

	private void newGame() {
		if(gamePanel!=null)
			gamePanel.newGame(gameWidth, gameHeight, mineCount, eightConnect);
//		invalidate();
//		revalidate();
//		repaint();
	}

}
