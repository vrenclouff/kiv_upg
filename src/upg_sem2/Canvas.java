package upg_sem2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Canvas extends JFrame {
	
	private static final long serialVersionUID = 1L;
	final int width = 700;
	final int height = 600;
	static Wrap data;
	final String[] arg;
	static JFrame frame;
	static int index;
	
	

	public Canvas(Wrap data, String[] arg){
		this.data = data;
		this.arg = arg;
		this.indexYear(arg[2]);
		
		this.frame = new JFrame();		
		this.frame.setSize(width, height+55);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		this.frame.setLayout(new BorderLayout());
		this.initButtons();
		this.run(new Map(data, getIndex()));
	}
	
	
	private void indexYear(String year){
		for(int i = 0; i < data.getYears().length; i++){
			if(data.getYears()[i].getYear() == Integer.parseInt(year)){
				index = i;
				break;
			}
		}
	}
	
	public static int getIndex() {
		return index;
	}
	

	public static void setIndex(int tmp){
		index = tmp;
	}
	public static void setUpIndex() {
		int tmp = getIndex()+1;
		if(tmp >= data.getYears().length){ tmp = data.getYears().length-1;}
		index = tmp;
	}
	public static void setDownIndex(){
		int tmp = getIndex()-1;
		if(tmp < 0){ tmp = 0;}
		index = tmp;
	}



	private static void initButtons() {
		
		JPanel buttonPanel = new JPanel();
		
		JButton leftEnd = new JButton();
		leftEnd.setText("|<");
		leftEnd.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				setIndex(0);
				System.out.println(data.getYears()[getIndex()].getYear());
			}
		});
		buttonPanel.add(leftEnd);
		

		JButton left = new JButton();
		left.setText("<");
		left.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDownIndex();
				System.out.println(data.getYears()[getIndex()].getYear());
			}
		});				
		buttonPanel.add(left);
		
		JButton runPause = new JButton();
		runPause.setText("Run/Pause");
		runPause.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				
			}
		});				
		buttonPanel.add(runPause);
		
		JButton right = new JButton();
		right.setText(">");
		right.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setUpIndex();
			//	run(new Map(data, getIndex()));
				System.out.println(data.getYears()[getIndex()].getYear());
			}
		});				
		buttonPanel.add(right);
		
		JButton rightEnd = new JButton();
		rightEnd.setText(">|");
		rightEnd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setIndex(data.getYears().length-1);
				System.out.println(data.getYears()[getIndex()].getYear());
				
			}
		});				
		buttonPanel.add(rightEnd);
		
		JButton export = new JButton();
		export.setText("Export");
		export.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				
			}
		});				
		buttonPanel.add(export);
		
//		JButton closeButton = new JButton("Close");
//		closeButton.addActionListener(new ActionListener() {
//			
//			public void actionPerformed(ActionEvent e) {
//				frame.dispose();
//			}
//		});
//		buttonPanel.add(closeButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);	
	}
	

	public static void run(Map drawiMap){	
		frame.add(drawiMap, BorderLayout.CENTER);
						
		frame.setLocationRelativeTo(null);	
		frame.setVisible(true);
		
	}
}
