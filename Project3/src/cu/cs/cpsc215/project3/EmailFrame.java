package cu.cs.cpsc215.project3;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JTable;

public class EmailFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504192480955713515L;
	private static final int width = 800;
	private static final int height = 600;
	
	public EmailFrame(InboxTable model) {
		super("SimpleMail");
		//this.setLocationRelativeTo(null);
		//this.pack();
		JTable table = new JTable(model);
		setup();
		this.getContentPane().add(table);
	}
	
	private void setup() {
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
