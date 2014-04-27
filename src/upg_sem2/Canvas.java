package upg_sem2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Canvas extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final int width = 700;
	private final int height = 600;
	private static Wrap data;
	private static JFrame frame;
	private static int index;
	private static Map drawiMap;
	
	
	public Canvas(Wrap data, String[] arg){
		Canvas.setData(data);
		this.indexYear(arg[2]);
		Canvas.setDrawiMap(new Map(getData(), arg));
	}
	
	private void indexYear(String year){
		for(int i = 0; i < getData().getYears().length; i++){
			if(getData().getYears()[i].getYear() == Integer.parseInt(year)){
				index = i;
				break;
			}
		}
	}
	public static Wrap getData() {
		return data;
	}
	public static void setData(Wrap data) {
		Canvas.data = data;
	}
	public static int getIndex() {
		return index;
	}
	public static void setIndex(int tmp){
		index = tmp;
	}
	public static void setUpIndex() {
		int tmp = getIndex()+1;
		if(tmp >= getData().getYears().length){ tmp = getData().getYears().length-1;}
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
				getDrawiMap().setIndex(getIndex());
				getDrawiMap().repaint();
		//		System.out.println(data.getYears()[getIndex()].getYear());
			}
		});
		buttonPanel.add(leftEnd);
		

		JButton left = new JButton();
		left.setText("<");
		left.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDownIndex();
				getDrawiMap().setIndex(getIndex());
				getDrawiMap().repaint();
		//		System.out.println(data.getYears()[getIndex()].getYear());
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
				getDrawiMap().setIndex(getIndex());
				getDrawiMap().repaint();
				
		//		System.out.println(data.getYears()[getIndex()].getYear());
			}
		});				
		buttonPanel.add(right);
		
		JButton rightEnd = new JButton();
		rightEnd.setText(">|");
		rightEnd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setIndex(getData().getYears().length-1);
				getDrawiMap().setIndex(getIndex());
				getDrawiMap().repaint();
				
		//		System.out.println(data.getYears()[getIndex()].getYear());	
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
	
	public void run(){	
		getDrawiMap().setIndex(getIndex());
		frame = new JFrame();		
		frame.setSize(width, height+55);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setLayout(new BorderLayout());
		
		initButtons();
	
		frame.add(getDrawiMap(), BorderLayout.CENTER);
		
		frame.setLocationRelativeTo(null);	
		frame.setVisible(true);
	}

	public static Map getDrawiMap() {
		return drawiMap;
	}

	public static void setDrawiMap(Map drawiMap) {
		Canvas.drawiMap = drawiMap;
	}
}
