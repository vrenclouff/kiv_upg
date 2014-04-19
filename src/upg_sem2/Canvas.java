package upg_sem2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Canvas extends JFrame {
	
	private static final long serialVersionUID = 1L;
	final int width = 600;
	final int height = 600;
	static JFrame frame = new JFrame();
	static Map drawingArea;
	
	

	public Canvas(){
		this.drawingArea = new Map();
	}

	
	private static void initButtons() {
		
		JPanel buttonPanel = new JPanel();
		
		JButton zmensiButton = new JButton();
		zmensiButton.setText("Save");
		zmensiButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		buttonPanel.add(zmensiButton);
		

		JButton updateButton = new JButton();
		updateButton.setText("Print");
		updateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				
			}
		});				
		buttonPanel.add(updateButton);
		
		
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		buttonPanel.add(closeButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);	
	}
	

	public void run(){
		
		frame.setSize(width, height+55);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setLayout(new BorderLayout());		
		frame.add(drawingArea, BorderLayout.CENTER);
				
		initButtons();
				
		frame.setLocationRelativeTo(null);	
		frame.setVisible(true);
		
	}
}
