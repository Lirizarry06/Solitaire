import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/* Program: Solitaire.java
 * Date: 8 April 2022
 * Desc: This file sets up the frame for the Solitaire program.
 */

public class SolitaireFrame {
	public static final int FRAME_WIDTH = 750;
	public static final int FRAME_HEIGHT = 700;
	private SolitairePanel sPanel;
	
	public SolitaireFrame() {
		//boilerplate code to set up JFrame
		JFrame frame = new JFrame("Solitaire");
		frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// get a screen size and put a frame on the center of screen
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setLocation(new Point((int)(screenSize.width/2.0 - FRAME_WIDTH/2.0), 
						(int)(screenSize.height/2.0 - FRAME_HEIGHT/2.0 )));	
		
		this.sPanel = new SolitairePanel();
		frame.add(sPanel, BorderLayout.CENTER);
		JPanel settingsBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		settingsBar.setBackground(Color.GREEN);
		JPanel timerBar = new JPanel(new BorderLayout());
		timerBar.setBackground(Color.GREEN);
		
		JButton newGameButton = new JButton("New Game");
		JButton undoButton = new JButton("Undo");
		JButton redoButton = new JButton("Redo");
		JLabel timer = new JLabel("Timer: 0 ");
		
		settingsBar.add(newGameButton);
		settingsBar.add(undoButton);
		settingsBar.add(redoButton);
		timerBar.add(timer, BorderLayout.EAST);
		
		frame.add(settingsBar, BorderLayout.NORTH);
		frame.add(timerBar, BorderLayout.SOUTH);

		frame.setVisible(true);
	}
	
	public void newGame() {
		this.sPanel = new SolitairePanel();
	}
}
