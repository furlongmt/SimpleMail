//Matthew Furlong and Robert Larsen
package login;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

import javax.swing.*;

public class LoginScreen {
	
    private static JFrame frame = new JFrame("Email Login");
    private static boolean hitonce = false;
    
    private LoginScreen(){}

    public static void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
        pane.setLayout(null);
		JLabel l_prompt = new JLabel("Please Log In:");
		JLabel l_advanced = new JLabel("Advanced Settings:");
		JLabel l_user = new JLabel("Username:");
		JLabel l_pass = new JLabel("Password:");;
		JLabel l_host = new JLabel("Host:");
		JLabel l_port = new JLabel("Port:");
		final JLabel l_load = new JLabel("Invalid Credentials");
		final JCheckBox c_remember = new JCheckBox("Remember Me");
		final JTextField t_username = new JTextField(20);
		final JTextField t_password = new JPasswordField(20);
		final JTextField t_host = new JTextField(20);
		final JTextField t_port = new JTextField(20);
        JButton b_cancel = new JButton("Cancel");
        JButton b_login = new JButton("Login");
        final JButton b_advanced = new JButton(">>");
        pane.add(l_prompt);
        pane.add(l_advanced);
        pane.add(l_user);
        pane.add(l_pass);
        pane.add(l_host);
        pane.add(l_port);
        pane.add(t_username);
        pane.add(t_password);
        pane.add(t_host);
        pane.add(t_port);
        pane.add(b_cancel);
        pane.add(b_login);
        pane.add(b_advanced);
        pane.add(l_load);
        pane.add(c_remember);
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
        l_advanced.setFont(font);
        l_user.setFont(font);
        l_pass.setFont(font);
        l_host.setFont(font);
        l_port.setFont(font);
        l_load.setFont(font);
        b_login.setFont(font);
        b_advanced.setFont(font);
        b_cancel.setFont(font);
        font = new Font(font.getFontName(),Font.PLAIN,13);
        t_username.setFont(font);
        t_password.setFont(font);
        t_host.setFont(font);
        t_port.setFont(font);
        pane.setBackground(new Color(240,255,255));
        b_login.setBackground(Color.WHITE);
        b_cancel.setBackground(Color.WHITE);
        b_advanced.setBackground(Color.WHITE);
        l_load.setVisible(false);
        l_load.setForeground(Color.RED);
        Insets insets = pane.getInsets();
        Dimension size = l_prompt.getPreferredSize();
        l_prompt.setBounds(insets.left + 200 - (size.width / 2), 20 + insets.top, size.width, size.height);
        size = l_advanced.getPreferredSize();
        l_advanced.setBounds(insets.left + 625 - (size.width / 2), 20 + insets.top, size.width, size.height);
        size = l_user.getPreferredSize();
        l_user.setBounds(25 + insets.left, 50 + insets.top, size.width, size.height);
        size = l_pass.getPreferredSize();
        l_pass.setBounds(25 + insets.left, 75 + insets.top, size.width, size.height);
        size = l_host.getPreferredSize();
        l_host.setBounds(425 + insets.left, 50 + insets.top, size.width, size.height);
        size = l_port.getPreferredSize();
        l_port.setBounds(425 + insets.left, 75 + insets.top, size.width, size.height);
        size = t_username.getPreferredSize();
        t_username.setBounds(125 + insets.left, 50 + insets.top, size.width, size.height);
        size = t_password.getPreferredSize();
        t_password.setBounds(125 + insets.left, 75 + insets.top, size.width, size.height);
        size = t_host.getPreferredSize();
        t_host.setBounds(525 + insets.left, 50 + insets.top, size.width, size.height);
        size = t_port.getPreferredSize();
        t_port.setBounds(525 + insets.left, 75 + insets.top, size.width, size.height);
        size = b_cancel.getPreferredSize();
        b_cancel.setBounds(50 + insets.left, 135 + insets.top, size.width, size.height);
        size = b_login.getPreferredSize();
        b_login.setBounds(insets.left + (50 + size.width) + 150, 135 + insets.top, size.width, size.height);
        size = b_advanced.getPreferredSize();
        b_advanced.setBounds(insets.left + (145 + size.width) + 150, 10 + insets.top, 50, size.height-5);
        size = l_load.getPreferredSize();
        l_load.setBounds(insets.left + 135, 140 + insets.top, size.width, size.height);
        size = c_remember.getPreferredSize();
        c_remember.setBounds(125 + insets.left, 105 + insets.top, size.width, size.height);
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
				if(t_host.getText().length() > 0 && t_port.getText().length() > 0){
					EmailClient app = EmailClient.getInstance(t_host.getText(),t_port.getText());
					if(t_username.getText().length() > 0 && t_password.getText().length() > 0 && EmailClient.validCreds(t_username.getText(),t_password.getText())){
						InboxTable table = new InboxTable(app.readEmails("All Mail"));
						try {
							EmailMainScreen.setComponentsPane(table);
							FileOutputStream out = new FileOutputStream("login.ser");
							ObjectOutputStream data = new ObjectOutputStream(out);
							if(c_remember.isSelected()){
								String temp[] = {t_username.getText(),t_password.getText(),t_host.getText(),t_port.getText()};
								data.writeObject(temp);
							}
							else{
								String temp[] = {"","","",""};
								data.writeObject(temp);
							}
							data.close();
							out.close();
						} catch (Exception e1) {}
						EmailMainScreen.show(t_username.getText(),t_password.getText());
					}
					else{
						t_password.setBackground(Color.PINK);
						t_password.requestFocus();
					}
				}
			}
        });
        b_cancel.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(-1);
			}
        });
        b_advanced.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
		        if(hitonce){
		        	b_advanced.setText(">>");
		        	Insets insets2 = frame.getInsets();
			        frame.setSize(400 + insets2.left + insets2.right, 200 + insets2.top + insets2.bottom);
			        hitonce = false;
		        }
		        else{
		        	b_advanced.setText("<<");
		        	Insets insets2 = frame.getInsets();
			        frame.setSize(800 + insets2.left + insets2.right, 200 + insets2.top + insets2.bottom);
			        hitonce = true;
		        }
			}
        });
        frame.addWindowListener(new WindowListener () {
			@Override
			public void windowClosing(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					FileInputStream in = new FileInputStream("login.ser");
					ObjectInputStream data = new ObjectInputStream(in);
					String p[] = (String[]) data.readObject();
					t_username.setText(p[0]);
					t_password.setText(p[1]);
					t_host.setText(p[2]);
					t_port.setText(p[3]);
					if(!p[0].equals("") && !p[1].equals("") && !p[2].equals("") && !p[3].equals("")){
						c_remember.setSelected(true);
						t_host.setText("smtp.gmail.com");
						t_port.setText("587");
					}
					data.close();
					in.close();
				} catch(Exception el) {
					t_host.setText("smtp.gmail.com");
					t_port.setText("587");
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
		});
        Insets insets2 = frame.getInsets();
        frame.setSize(400 + insets2.left + insets2.right, 200 + insets2.top + insets2.bottom);
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
        t_username.requestFocus();
    }
    
    public static void hide(){
    	frame.setVisible(false);
    }
    
}
