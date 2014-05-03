package upg_sem2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Canvas extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final int width = 680;
	private final int height = 730;
	private String[] arg;
	private static Wrap data;
	private static JFrame frame;
	private static int index;
	private static Map drawiMap;
	public static Time time;
	
	
	
	
	public Canvas(Wrap data, String[] arg){
		this.arg = arg;
		Canvas.setData(data);
		this.indexYear(arg[2]);
		Canvas.setDrawiMap(new Map(getData(), arg));
	}
	
	private void indexYear(String year){
		int tmp = Integer.parseInt(year);
		
		if(tmp == -1){
			if(arg[1].length() > 1){
				Component parentComponent = null;
				JOptionPane.showConfirmDialog(parentComponent, "For sum is not virtualization.\nProgram will be closed.", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}else{
				index = getData().getYears().length;
				return;
			}
		}
		
		for(int i = 0; i < getData().getYears().length; i++){
			if(getData().getYears()[i].getYear() == tmp){
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
		if(tmp >= drawiMap.getCounter()){ tmp = drawiMap.getCounter();}
		index = tmp;
	}
	public static void setDownIndex(){
		int tmp = getIndex()-1;
		if(tmp < 0){ tmp = 0;}
		index = tmp;
	}
	public static boolean upIndex(){
		boolean tmp = false;
		if(getIndex() >= drawiMap.getCounter()){			
			tmp = true;
		}
		return tmp;
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
			}
		});				
		buttonPanel.add(left);
		
		JButton runPause = new JButton();
		runPause.setText("Run/Pause");
		time = new Time();
		runPause.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {	
				
				if(!time.isRunning()){
					time.start();
				}else{
					time.stop();
				}
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
			}
		});				
		buttonPanel.add(right);
		
		JButton rightEnd = new JButton();
		rightEnd.setText(">|");
		rightEnd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setIndex(drawiMap.getCounter());
				getDrawiMap().setIndex(getIndex());
				getDrawiMap().repaint();
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
			
		frame.add(buttonPanel, BorderLayout.SOUTH);	
	}
	
	public void run(){	
		getDrawiMap().setIndex(getIndex());
		frame = new JFrame();		
		frame.setTitle("Type of disease: "+arg[0]);
		frame.setSize(width, height+55);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setLayout(new BorderLayout());
		
		initButtons();
	
		frame.add(getDrawiMap(), BorderLayout.CENTER);
		frame.setResizable(false);
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



	class MyTask extends TimerTask {
		public MyTask() {}
		public void run() {
			if(Canvas.upIndex()){
				if(Canvas.time.isRunning()){ Canvas.time.stop(); }
				
				if(!Canvas.time.isRunningWait()){ Canvas.time.startWait(); }
			}		
			Canvas.getDrawiMap().setIndex(Canvas.getIndex());
			Canvas.getDrawiMap().repaint();
			Canvas.setUpIndex();
		}
	}
	
	class MyTaskWait extends TimerTask {
		int i = 0;
		public MyTaskWait() {}
		public void run() {
			
			Canvas.setIndex(0);
			
			if(i++ == 1){
				if(Canvas.time.isRunningWait()){ Canvas.time.stopWait(); }
				
				if(!Canvas.time.isRunning()){ Canvas.time.start(); }
			}
		}
	}

	class Time {
		  private boolean running;
		  private boolean runningWait;
		  private MyTask task;
		  private MyTaskWait taskWait;
		  private Timer timer;
		  private Timer timerWait;
		  public Time() {
		    this.timer = new Timer(true);
		    this.timerWait = new Timer(true);
		  }
		  
		  public boolean isRunning(){
			  return running;
		  }
		  public boolean isRunningWait(){
				  return runningWait;
		  }
		  public void start() {
		    running = true;
		    task = new MyTask();
		    timer.scheduleAtFixedRate(task, 0, 1250);
		  }
		  public void startWait() {
			runningWait = true;
			taskWait = new MyTaskWait();
			timerWait.scheduleAtFixedRate(taskWait, 0, 5000);
		  }
		  public void stop() {
		    running = false;
		    task.cancel();
		  }
		  public void stopWait() {
			runningWait = false;
			taskWait.cancel();
		  }
	}


