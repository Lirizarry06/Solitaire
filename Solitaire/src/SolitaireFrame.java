import javax.swing.*;
import java.awt.*;
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
		
		//get a screen size and put a frame on the center of screen
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setLocation(new Point((int)(screenSize.width/2.0 - FRAME_WIDTH/2.0), 
						(int)(screenSize.height/2.0 - FRAME_HEIGHT/2.0 )));	
		
		//set icon and title bar color
		frame.setIconImage(new ImageIcon("card-images/icon.png").getImage());
		
		//set up game panel
		this.sPanel = new SolitairePanel();
		sPanel.setBackground(Color.GREEN);
		
		//new game button
		JButton newGameButton = new JButton("New Game");
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sPanel.newGame();
			}
		});
		
		//undo button
		JButton undoButton = new JButton("Undo");
		
		//redo button
		JButton redoButton = new JButton("Redo");
		
		//set up top bar
		JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topBar.setBackground(Color.GREEN);
		topBar.add(newGameButton);
		topBar.add(undoButton);
		topBar.add(redoButton);
		
		//set up timer bar
		JPanel timerBar = new JPanel(new BorderLayout());
		timerBar.setBackground(Color.GREEN);
		JLabel timer = new JLabel("Timer: 0 ");
		timerBar.add(timer, BorderLayout.EAST);
		
		//add all components to frame and make visible
		frame.add(sPanel, BorderLayout.CENTER);
		frame.add(topBar, BorderLayout.NORTH);
		frame.add(timerBar, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
}
