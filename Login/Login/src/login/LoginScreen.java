//Matthew Furlong and Robert Larsen
package login;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

public class LoginScreen {
	
    private static JFrame frame = new JFrame("Email Login");
    
    private LoginScreen(){}

    public static void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        Container pane = frame.getContentPane();
        
        pane.setLayout(null);
		JLabel l_prompt = new JLabel("Please Log In:");
		JLabel l_user = new JLabel("Username:");
		JLabel l_pass = new JLabel("Password:");
		final JLabel l_load = new JLabel("Invalid Credentials");
		final JTextField t_username = new JTextField(20);
		final JTextField t_password = new JPasswordField(20);
		//TODO remove
		t_username.setText("cu2150tester@gmail.com");
		t_password.setText("wordpass1");
        JButton b_cancel = new JButton("Cancel");
        JButton b_login = new JButton("Login");

        pane.add(l_prompt);
        pane.add(l_user);
        pane.add(l_pass);
        pane.add(t_username);
        pane.add(t_password);
        pane.add(b_cancel);
        pane.add(b_login);
        pane.add(l_load);
        
        URL fontUrl;
        Font font = Font.getFont("FreeSans");
		try {
			fontUrl = new URL("http://ff.static.1001fonts.net/o/p/open-sans.regular.ttf");
	        font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
	        font = font.deriveFont(Font.PLAIN,12);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(font);
		} catch (Exception e1) {
			font = Font.getFont("FreeSans");
		}
        font = new Font(font.getFontName(),Font.BOLD,13);
        l_prompt.setFont(font);
        l_user.setFont(font);
        l_pass.setFont(font);
        l_load.setFont(font);
        b_login.setFont(font);
        b_cancel.setFont(font);
        font = new Font(font.getFontName(),Font.PLAIN,13);
        t_username.setFont(font);
        t_password.setFont(font);
        pane.setBackground(new Color(240,255,255));
        b_login.setBackground(Color.WHITE);
        b_cancel.setBackground(Color.WHITE);
        l_load.setVisible(false);
        l_load.setForeground(Color.RED);
        
        Insets insets = pane.getInsets();
        Dimension size = l_prompt.getPreferredSize();
        l_prompt.setBounds(insets.left + 200 - (size.width / 2), 20 + insets.top,
                size.width, size.height);
        size = l_user.getPreferredSize();
        l_user.setBounds(25 + insets.left, 50 + insets.top,
                size.width, size.height);
        size = l_pass.getPreferredSize();
        l_pass.setBounds(25 + insets.left, 100 + insets.top,
                size.width, size.height);
        size = t_username.getPreferredSize();
        t_username.setBounds(125 + insets.left, 50 + insets.top,
                size.width, size.height);
        size = t_password.getPreferredSize();
        t_password.setBounds(125 + insets.left, 100 + insets.top,
                size.width, size.height);
        size = b_cancel.getPreferredSize();
        b_cancel.setBounds(50 + insets.left, 135 + insets.top,
                     size.width, size.height);
        size = b_login.getPreferredSize();
        b_login.setBounds(insets.left + (50 + size.width) + 150, 135 + insets.top,
                     size.width, size.height);
        size = l_load.getPreferredSize();
        l_load.setBounds(insets.left + 135, 140 + insets.top,
                     size.width, size.height);
        
        t_password.addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e) {
				t_password.setBackground(Color.WHITE);
				l_load.setVisible(false);
			}
        });
        b_login.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				l_load.setVisible(true);
				if(t_username.getText().length() > 0 && t_password.getText().length() > 0 && EmailClient.validCreds(t_username.getText(),t_password.getText())){
					EmailClient app = EmailClient.getInstance();
					InboxTable table = new InboxTable(app.readEmails("All Mail"));
					try {
						EmailMainScreen.setComponentsPane(table);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					EmailMainScreen.show(t_username.getText(),t_password.getText());
				}
				else{
					t_password.setBackground(Color.PINK);
					t_password.requestFocus();
				}
			}
        	
        });
        b_cancel.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(-1);
			}
        });
        
 
        Insets insets2 = frame.getInsets();
        frame.setSize(400 + insets2.left + insets2.right,
                      200 + insets2.top + insets2.bottom);
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }
    
    public static void hide(){
    	frame.setVisible(false);
    }
    
}
