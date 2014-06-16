package com.chinmay.minesweeper;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GamePanel extends JPanel {
	private int gameWidth;
	private int gameHeight;
	private int mineCount;
	private boolean eightConnect;
	private Set<Point> mineLocations;
	private JToggleButton[][] mineField;
	private boolean[][] alreadyPressed;
	private int tilesLeft;
	
	public GamePanel(int gameWidth, int gameHeight, int mineCount, boolean eightConnect) {
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		this.mineCount = mineCount;
		this.eightConnect = eightConnect;
		initialize();
	}
	
	public void initialize() {
		createRandomMines();
		this.setLayout(new GridLayout(gameHeight, gameWidth));
		mineField = new JToggleButton[gameHeight][gameWidth];
		alreadyPressed = new boolean[gameHeight][gameWidth];
		tilesLeft = gameWidth * gameHeight - mineCount;
		for(int y=0;y<gameHeight;++y)
			for(int x=0; x<gameWidth; ++x) {
				final JToggleButton button = new JToggleButton(" ");
				button.setSize(this.getWidth()/gameWidth, this.getHeight()/gameHeight);
				mineField[y][x] = button;
				add(button);
				if(mineLocations.contains(new Point(x, y))) {
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							button.setText("*");
							gameOver(false);						
						}						
					});
				}
				else {
					final int j=y, i=x;
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(!alreadyPressed[j][i]) {
								if(floodFill(i, j, true) == -1)
									gameOver(true);
							}
							else
								button.setSelected(true);
						}						
					});
				}
			}
	}
	
	private void gameOver(boolean won) {
		Object[] options = { "New Game", "No Thanks" };
		String message = won?"You won. Congratulations":"Sorry. You lose";
		int res = JOptionPane.showOptionDialog(null, message, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		if(res == 0)
			newGame();
		else {
			for(int y=0;y<gameHeight;++y)
				for(int x=0; x<gameWidth; ++x) 
					mineField[y][x].setEnabled(false);
		}
	}

	private int floodFill(int x, int y, boolean manual) {
		if(x<0 || x>=gameWidth || y<0 || y>=gameHeight )
			return 0;
		if(mineLocations.contains(new Point(x, y)))
			return 1;
		if(!manual && mineField[y][x].isSelected())
			return 0;
		
		mineField[y][x].setSelected(true);
		alreadyPressed[y][x] = true;
		tilesLeft--;
		
		int surrMines=0;
		surrMines += mineLocations.contains(new Point(x,y-1))?1:0;
		surrMines += mineLocations.contains(new Point(x-1,y))?1:0;
		surrMines += mineLocations.contains(new Point(x+1,y))?1:0;
		surrMines += mineLocations.contains(new Point(x,y+1))?1:0;

		if(eightConnect) {
			surrMines += mineLocations.contains(new Point(x-1,y-1))?1:0;
			surrMines += mineLocations.contains(new Point(x+1,y-1))?1:0;
			surrMines += mineLocations.contains(new Point(x-1,y+1))?1:0;
			surrMines += mineLocations.contains(new Point(x+1,y+1))?1:0;
		}
		
		if(surrMines > 0 )
			mineField[y][x].setText(String.valueOf(surrMines));
		else {
			floodFill(x-0, y-1, false);
			floodFill(x-1, y-0, false);
			floodFill(x+1, y+0, false);
			floodFill(x+0, y+1, false);
			if(eightConnect) {
				floodFill(x-1, y-1, false);
				floodFill(x+1, y-1, false);
				floodFill(x-1, y+1, false);
				floodFill(x+1, y+1, false);
			}
		}
		
		if(tilesLeft == 0)
			return -1;
		return 0;
	}

	private void createRandomMines() {
		mineLocations = new HashSet<Point>();
		Random random = new Random();
		while(mineLocations.size() != mineCount) {
			Point point = new Point(random.nextInt(gameWidth), random.nextInt(gameHeight));
			mineLocations.add(point);
		}
	}

	public void newGame(int gameWidth, int gameHeight, int mineCount, boolean eightConnect) {
		this.removeAll();
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		this.mineCount = mineCount;
		this.eightConnect = eightConnect;
		initialize();
		invalidate();
		revalidate();
		repaint();
	}
	
	private void newGame() {
		removeAll();
		initialize();
		invalidate();
		revalidate();
		repaint();
	}
}
