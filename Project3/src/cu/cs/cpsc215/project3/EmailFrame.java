package cu.cs.cpsc215.project3;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;

public class EmailFrame extends JFrame {

	private static final int width = 800;
	private static final int height = 600;
	
	public EmailFrame() {
		super("SimpleMail");
		//this.setLocationRelativeTo(null);
		//this.pack();
		centerApp();
		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void centerApp() {
		Dimension windowSize = getSize();
		GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = graphics.getCenterPoint();
		
		int x = center.x - windowSize.width / 2 - width / 2;
		int y = center.y - windowSize.height / 2 - height / 2;
		
		this.setLocation(x,y);
	}
}
