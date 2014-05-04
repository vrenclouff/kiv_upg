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

/**
 * JFrame pro mapu
 * @author Lukas Cerny
 *
 */

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
	
	
	
	/**
	 * Konstruktor - nastavuje atributy, ktere jsou potreba pro beh programu
	 * @param data - vstupni JSON soubor
	 * @param arg - vstupni parametry
	 */
	public Canvas(Wrap data, String[] arg){
		this.arg = arg;
		Canvas.setData(data);
		this.indexYear(arg[2]);
		Canvas.setDrawiMap(new Map(getData(), arg));
	}
	
	/**
	 * Overi, zda bereme v uvahu hodnoty absolutni pocty pripadu hospitalizaci nebo pocty pripadu na 100 000 obyvatel
	 * @return true/false
	 */
	private boolean isA(){
		char c = arg[1].charAt(0);
		boolean value = false;
		if(c == 'A'){
			value = true;
		}else if(c == 'R'){
			value = false;
		}
		return value;
	}
	
	/**
	 * Nastavi vstupni index, ktery je pak zobrazen pri spusteni
	 * @param year
	 */
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
	/**
	 * Vrati nactena data
	 * @return
	 */
	public static Wrap getData() {
		return data;
	}
	/**
	 * Nastavuje data
	 * @param data
	 */
	public static void setData(Wrap data) {
		Canvas.data = data;
	}
	/**
	 * Vrati index roku
	 * @return
	 */
	public static int getIndex() {
		return index;
	}
	/**
	 * Nastavi index roku
	 * @param tmp
	 */
	public static void setIndex(int tmp){
		index = tmp;
	}
	/**
	 * Zvysi rok o 1
	 */
	public static void setUpIndex() {
		int tmp = getIndex()+1;
		if(tmp >= drawiMap.getCounter()){ tmp = drawiMap.getCounter();}
		index = tmp;
	}
	/**
	 * Snizi rok o 1
	 */
	public static void setDownIndex(){
		int tmp = getIndex()-1;
		if(tmp < 0){ tmp = 0;}
		index = tmp;
	}
	/**
	 * Overi, zda je index na maximalni hodnote
	 * @return
	 */
	public static boolean upIndex(){
		boolean tmp = false;
		if(getIndex() >= drawiMap.getCounter()){			
			tmp = true;
		}
		return tmp;
	}
	
	/**
	 * Vykresli tlacitka pro ovladani programu
	 */
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
	
	/**
	 * Spousti program
	 */
	public void run(){	
		getDrawiMap().setIndex(getIndex());
		frame = new JFrame();	
		
		if(!isA()){
			frame.setTitle("Počet případů hospitalizace na 100 000 obyvatel kraje.");
		}else{
			frame.setTitle("Aboslutní počet případů hospitalizace.");
		}
		frame.setSize(width, height+55);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setLayout(new BorderLayout());
		
		initButtons();
	
		frame.add(getDrawiMap(), BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);	
		frame.setVisible(true);
	}
	/**
	 * Set pro instanci Map
	 * @return
	 */
	public static Map getDrawiMap() {
		return drawiMap;
	}
	/**
	 * Get pro instanci Map
	 * @param drawiMap
	 */
	public static void setDrawiMap(Map drawiMap) {
		Canvas.drawiMap = drawiMap;
	}
}

/**
 * Trida pro casovou smycku mezi roky (1250ms)
 * @author Lukas Cerny
 *
 */

	class MyTask extends TimerTask {
		public MyTask() {}
		/**
		 * Po 1250ms prehodi na dalsi snimek
		 */
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

/**
 * Trida pro casovou smycku mezi roky (5000ms)
 * @author Lukas Cerny
 *
 */
	class MyTaskWait extends TimerTask {
		int i = 0;
		public MyTaskWait() {}
		/**
		 * Spusti se, pokud snimky dojedou na konec
		 * Po 5000ms se opet zadnou snimky stridat po 1250ms
		 */
		public void run() {
			
			Canvas.setIndex(0);
			
			if(i++ == 1){
				if(Canvas.time.isRunningWait()){ Canvas.time.stopWait(); }
				
				if(!Canvas.time.isRunning()){ Canvas.time.start(); }
			}
		}
	}

/**
 * Trida, ktera ridi prubeh casu
 * @author Lukas Cerny
 *
 */
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
		  
		  /**
		   * Zsjistuje, zda bezi smicka s 1250ms
		   * @return
		   */
		  public boolean isRunning(){
			  return running;
		  }
		  /**
		   * Zsjistuje, zda bezi smicka s 5000ms
		   * @return
		   */
		  public boolean isRunningWait(){
				  return runningWait;
		  }
		  /**
		   * Spousti smycku s 1250ms
		   */
		  public void start() {
		    running = true;
		    task = new MyTask();
		    timer.scheduleAtFixedRate(task, 0, 1250);
		  }
		  /**
		   * Spousti smycku s 5000ms
		   */
		  public void startWait() {
			runningWait = true;
			taskWait = new MyTaskWait();
			timerWait.scheduleAtFixedRate(taskWait, 0, 5000);
		  }
		  /**
		   * Zastavi smycku s 1250ms
		   */
		  public void stop() {
		    running = false;
		    task.cancel();
		  }
		  /**
		   * Zastavi smycku s 5000ms
		   */
		  public void stopWait() {
			runningWait = false;
			taskWait.cancel();
		  }
	}


