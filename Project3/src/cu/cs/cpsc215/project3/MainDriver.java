package cu.cs.cpsc215.project3;

import javax.swing.JFrame;

public class MainDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EmailClient app = new EmailClient();
		//app.sendMessage("Example", "This should be the body of the message \n and this should be a new line", "mfurlon@g.clemson.edu");
		
		InboxTable table = new InboxTable(app.readEmails("inbox"));
		
		//app.getMessageBody(0);
		
		JFrame frame = new EmailFrame(table);
	}

}
