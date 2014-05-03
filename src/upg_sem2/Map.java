package upg_sem2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Map extends JPanel {
	
	
	private static final long serialVersionUID = 1L;
	private RegionComponent [] regioComponent;
	private ArrayList<BufferedImage> imageYears;
	private Point2D.Double max;
	private Point2D.Double min;
	private Wrap data;
	private String[] arg;
	private int index;
	private final int diseaseIndex;
	private final int width = 680;
	private final int height = 500;
	private double maxValue;
	private double minValue;
	private double maxValuePlus;
	private double minValuePlus;
	private final boolean isA;
	private final boolean isPlus;
	private int counter;
	double [] regress;
	
	public Map(Wrap data, String [] arg){
		this.regioComponent = this.readRegions();
		this.calculateLimits();
		this.data = data;
		this.imageYears = new ArrayList<>();
		this.arg = arg;
		this.diseaseIndex = getDiseaseIndex(arg[0]);
		this.isA = isA();
		this.isPlus = isPlus();
		this.counter = -1;
		this.regress = new double[data.getYears()[0].getRegions().length];
		
		
		this.loadImages();
		this.setSize(width, height);
		this.setPreferredSize(this.getSize());
	}
	
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
	private boolean isPlus(){
		boolean value = false;
		
		if(arg[1].length() == 2){
			value = true;
		}
		
		return value;
	}
	
	private int getDiseaseIndex(String tmp){
		String [] disease  = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV",
				"XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "CELKEM"};
		int value = 0;
		for(int i = 0; i < disease.length; i++){
			if(tmp.equals(disease[i])){
				value = i;
			}
		}
		return value;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean getIsPlus(){
		return isPlus;
	}
	public int getCounter(){
		return counter;
	}
	
	private void loadImages(){
		int tmp = data.getYears().length;
		if(!isPlus){
			for(int i = 0; i < tmp; i++){
				setIndex(i);
				imageYears.add(i, drawYearScreen());
				counter++;
			}
			imageYears.add(tmp, drawVisual());
			counter++;
		}else{
			for(int i = 0; i < tmp; i++){
				setIndex(i);
				imageYears.add(i, drawYearScreenPlus());
				counter++;
			}
		}
		
	}
	
	private void drawGraf(Graphics2D g2, int x, int y, int i){
		
		
		int lengthYear = data.getYears().length-1;
		int len = (int)(36.0/(double)lengthYear);
		int x1 = x;
		int x2 = x+len;
		int tmpY;
		int valueBack = 0;
		
		g2.drawLine(x-1, y, x-1, y-30);
		g2.drawLine(x-1, y, x+40, y);
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(2));
		
		
		String number;
		double tmp;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		for(int j = 0; j <= lengthYear; j++){
			if(isA){
				number = data.getYears()[j].getRegions()[i].getA()[diseaseIndex];
			}else{
				number = data.getYears()[j].getRegions()[i].getB()[diseaseIndex];
			}
			tmp = Double.parseDouble( number.replace(",",".") );
			
			if(tmp > max){
				max = tmp;
			}else if(tmp < min){
				min = max;
			}
		}
		
		
		
		for(int k = 0; k <= lengthYear; k++){
			if(isA){
				number = data.getYears()[k].getRegions()[i].getA()[diseaseIndex];
			}else{
				number = data.getYears()[k].getRegions()[i].getB()[diseaseIndex];
			}
			tmp = Double.parseDouble( number.replace(",",".") );
			
			tmpY = (int)((2.0/3.0 * 30.0 * tmp)/max);
			
			if(k != 0){
				g2.drawLine(x1, y-valueBack, x2, y-tmpY);
				x1 += len;
				x2 += len;
			}
			
			valueBack = tmpY;
		
			
		}
	}
	
	private BufferedImage drawVisual(){
		int [] regCtr = {465, 280, 440, 350, 530, 350, 340, 310, 360, 180, 390, 250, 275, 140, 220, 350, 70, 210, 110, 290, 230, 270, 170, 170, 460, 100, 550, 260};
		int [] regions = {11, 10, 12, 9, 7, 8, 6, 2, 4, 3, 1, 5, 0, 13};
		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
		Graphics2D g = (Graphics2D) img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		int tmp = 0;
		
		for(int i = 0; i < regioComponent.length; i++){
			regioComponent[i].setColor(Color.white);
			regioComponent[i].draw(g2, max, min);
			
			drawGraf(g2, regCtr[tmp++], regCtr[tmp++], regions[i]);
		}
		g2.setColor(Color.black);
		g2.drawString("Praha", 460, 115);
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		g2.drawString("Vizualizace", 30, 60);
		
		return img;
	}
	
	private BufferedImage drawYearScreen() {
		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
		int [] regions = {11, 10, 12, 9, 7, 8, 6, 2, 4, 3, 1, 5, 0, 13};
		int [] regCtr = {490, 290, 440, 350, 530, 350, 340, 310, 360, 180, 390, 250, 275, 130, 220, 350, 70, 210, 110, 270, 230, 265, 170, 160, 230, 225, 550, 260};
		String t = "";
		int tmp = 0;
		Graphics2D g = (Graphics2D) img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		ratio();
		drawLegend(g2);
		
		for(int i = 0; i < regioComponent.length; i++){
			regioComponent[i].setColor(setColor(regions[i]));
			if(isA){
				t = String.valueOf(Math.round(Double.parseDouble( data.getYears()[getIndex()].getRegions()[regions[i]].getA()[diseaseIndex].replace(",",".") )));
			}else{
				t = String.valueOf(Math.round(Double.parseDouble( data.getYears()[getIndex()].getRegions()[regions[i]].getB()[diseaseIndex].replace(",",".") )));
			}
			regioComponent[i].draw(g2, max, min);
			g2.drawString(t, regCtr[tmp++], regCtr[tmp++]);
		}
				
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		
		g2.drawString(String.valueOf(data.getYears()[getIndex()].getYear()), 30, 60);
		
		
		return img;	
	}
	
	private BufferedImage drawYearScreenPlus() {
		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
		int [] regions = {11, 10, 12, 9, 7, 8, 6, 2, 4, 3, 1, 5, 0, 13};
		int [] regCtr = {490, 290, 440, 350, 530, 350, 340, 310, 360, 180, 390, 250, 275, 130, 220, 350, 70, 210, 110, 270, 230, 265, 170, 160, 230, 225, 550, 260};
		String t = "";
		int tmp = 0;
		Graphics2D g = (Graphics2D) img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		regressionLine();
		
		for(int i = 0; i < regioComponent.length; i++){
			
			regioComponent[i].setColor(setColorPlus(regions[i]));
			if(isA){
				t = String.valueOf(Math.round(Double.parseDouble( data.getYears()[getIndex()].getRegions()[regions[i]].getA()[diseaseIndex].replace(",",".") )));
			}else{
				t = String.valueOf(Math.round(Double.parseDouble( data.getYears()[getIndex()].getRegions()[regions[i]].getB()[diseaseIndex].replace(",",".") )));
			}
			regioComponent[i].draw(g2, max, min);
//			g2.drawString(t, regCtr[tmp++], regCtr[tmp++]);
		}
				
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		
		g2.drawString(String.valueOf(data.getYears()[getIndex()].getYear()), 30, 60);
		
		
		return img;	
	}
	
	
	
	private double regressionLine(){
		
		double [] list = new double[3];
		double value = 0;
		String number = "";
		int ind = 0;
		
		int forMax = getIndex();
		int forMin = getIndex()-2;
	
		
		if(forMin < 0){ forMin = 0; }
				
		for(int i = 0; i < data.getYears()[0].getRegions().length-4; i++){
			ind = 0;
			for(int j = forMin; j <= forMax; j++){
				if(isA){
					number = data.getYears()[j].getRegions()[i].getA()[diseaseIndex];
				}else{
					number = data.getYears()[j].getRegions()[i].getB()[diseaseIndex];
				}
				list[ind++] = Double.parseDouble( number.replace(",",".") );	
			}
			regress[i] = countRegress(list);
		}
		ratioPlus(regress);
		
		return value;
		
	}
	
	private double countRegress(double [] array){
		double value = 0.0;
		double x1 = array[0];
		double x2 = array[1];
		double x3 = array[2];
		double y1 = 1.0;
		double y2 = 2.0;
		double y3 = 3.0;
		double b;
		double a;
		double y;
		
		if(x2 == 0 && x3 == 0){ return 0;}
		
		if(x3 == 0){
			b =((2*(y1*x1+y2*x2))-((y1+y2)*(x1+x2))/(((y1+y2)*(y1*y1+y2*y2))-((y1+y2)*(y1+y2))));
			a = ((y1+y2)/2)-(b*(x1+x2)/2);
			y = b*2-a;
			value = x2-y;
			
		}else{
			b =((3*(y1*x1+y2*x2+y3*x3))-((y1+y2+y3)*(x1+x2+x3))/(((y1+y2+y3)*(y1*y1+y2*y2+y3*y3))-((y1+y2+y3)*(y1+y2+y3))));
			a = ((y1+y2+y3)/3)-(b*(x1+x2+x3)/3);
			y = b*3-a;
			value = x3-y;
		}
		
		return value;
	}
	
	private void drawLegend(Graphics2D g2){
		String str;
				
		str = String.valueOf(Math.round(maxValue)) +" - "+String.valueOf(Math.round(minValue+(3.0/4.0*(maxValue-minValue))));
		g2.setColor(Color.black);
		g2.drawString(str, width-130, 60);
		
		str = String.valueOf(String.valueOf(Math.round(minValue+(3.0/4.0*(maxValue-minValue))-1) +" - "+String.valueOf(Math.round(minValue+(2.0/4.0*(maxValue-minValue))))));
		g2.setColor(Color.black);
		g2.drawString(str, width-130, 76);
		
		str = String.valueOf(String.valueOf(Math.round(minValue+(2.0/4.0*(maxValue-minValue))-1) +" - "+String.valueOf(Math.round(minValue+(1.0/4.0*(maxValue-minValue))))));
		g2.setColor(Color.black);
		g2.drawString(str, width-130, 90);
		
		str = String.valueOf(String.valueOf(Math.round(minValue+(1.0/4.0*(maxValue-minValue))-1) +" - "+String.valueOf(Math.round(minValue))));
		g2.setColor(Color.black);
		g2.drawString(str, width-130, 105);
		
		g2.setColor(getColor(0));
		g2.fillRect(width-150, 50, 10, 10);
		g2.setColor(getColor(1));
		g2.fillRect(width-150, 65, 10, 10);
		g2.setColor(getColor(2));
		g2.fillRect(width-150, 80, 10, 10);
		g2.setColor(getColor(3));
		g2.fillRect(width-150, 95, 10, 10);
		
		g2.setColor(Color.black);
		g2.drawRect(width-150, 50, 10, 10);
		g2.drawRect(width-150, 65, 10, 10);
		g2.drawRect(width-150, 80, 10, 10);
		g2.drawRect(width-150, 95, 10, 10);
		
	}
	

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(imageYears.get(getIndex()), 0, 0, null);
		
		
		if(getIndex() != getCounter() || data.getYears().length-1 == getCounter()){
			minimizeImg(g2);
		}else{
			Font font = new Font("Arial", Font.BOLD, 14);
			g2.setFont(font);
			g2.setStroke(new BasicStroke(2));
			g2.drawString("sem musim napsat nejaky text", 150, 600);
		}

	}	
	
	private void minimizeImg(Graphics2D g2){
		int widthImg = width/6;
		int ratio = width/height;
		int heightImg = widthImg*ratio;
		int x = 0;
		int y = 500;
		int tmp = 0;
		int position = 0;
		
		for(int i = 0; i < data.getYears().length; i++){
			if(getIndex() == tmp){ 
				tmp++;
				continue; }
			if(position == 6){
				y += heightImg;
				x = 0;
			}
			g2.drawString(String.valueOf(data.getYears()[tmp].getYear()), x, y);
			g2.drawImage(imageYears.get(tmp), x, y, widthImg, heightImg, null);
			x = x + widthImg;
			position++;
			tmp++;
			
		}
	}
	
	private Color getColor(int i){
		Color[] color = {new Color(68, 151,101), new Color(74, 201, 124), new Color(79, 250, 148), new Color(175, 246, 203)};
		return color[i];
	}
	
	private Color getColorPlus(int i){
		Color[] color = {new Color(255, 102, 102), new Color(255, 153,153), new Color(128, 255, 0), new Color(204, 255, 153)};
		return color[i];
	}
	
	private Color setColor(int i){
		double tmp = 0;
		String number = "";
		double ratio = maxValue - minValue;
		int ind = 0;
		
		if(isA){
			number = data.getYears()[getIndex()].getRegions()[i].getA()[diseaseIndex];
		}else{
			number = data.getYears()[getIndex()].getRegions()[i].getB()[diseaseIndex];
		}
		tmp = Double.parseDouble( number.replace(",",".") );
		
		if(tmp >= minValue && tmp <= ((1.0/4.0*ratio)+minValue)){
			ind = 3;
		}else if(tmp < ((2.0/4.0*ratio)+minValue) && tmp >= ((1.0/4.0*ratio)+minValue)){
			ind = 2;
		}else if(tmp >= ((2.0/4.0*ratio)+minValue) && tmp < ((3.0/4.0*ratio)+minValue)){
			ind = 1;
		}else if(tmp <= maxValue && tmp >= ((3.0/4.0*ratio)+minValue)){
			ind = 0;
		}

		return getColor(ind);
	}
	
	private Color setColorPlus(int i){
		int ind = 0;
		double tmp = regress[i];
		double ratio = maxValuePlus - minValuePlus;
		
		if(tmp == 0) { return new Color(255,255,255); }
		
		if(tmp >= minValuePlus && tmp <= ((1.0/4.0*ratio)+minValuePlus)){
			ind = 3;
		}else if(tmp < ((2.0/4.0*ratio)+minValuePlus) && tmp >= ((1.0/4.0*ratio)+minValuePlus)){
			ind = 2;
		}else if(tmp >= ((2.0/4.0*ratio)+minValuePlus) && tmp < ((3.0/4.0*ratio)+minValuePlus)){
			ind = 1;
		}else if(tmp <= maxValuePlus && tmp >= ((3.0/4.0*ratio)+minValuePlus)){
			ind = 0;
		}

		
		return getColorPlus(ind);
	}
	
	private void ratio(){
		maxValue = Double.MIN_VALUE;
		minValue = Double.MAX_VALUE;
		double tmp = 0.0;
		String number = "";
				
		
		for(int i = 0; i < data.getYears()[getIndex()].getRegions().length-4; i++){
			if(isA){
				number = data.getYears()[getIndex()].getRegions()[i].getA()[diseaseIndex];
			}else{
				number = data.getYears()[getIndex()].getRegions()[i].getB()[diseaseIndex];
			}
			if(number.equals("x")){ continue; }
			tmp = Double.parseDouble( number.replace(",",".") );
			
			if(tmp > maxValue){
				maxValue = tmp;
			}else if(tmp < minValue){
				minValue = tmp;
			}
		}
	}
	
	private void ratioPlus(double [] array){
		double tmp = 0.0;
		maxValuePlus = Double.MIN_VALUE;
		minValuePlus = Double.MAX_VALUE;
		
		for(int i = 0; i < array.length; i++){
			tmp = array[i];
			
			if(tmp > maxValuePlus){
				maxValuePlus = tmp;
			}else if(tmp < minValuePlus){
				minValuePlus = tmp;
			}
		}
	}

	private void calculateLimits() {
		
		max = (Point2D.Double) regioComponent[0].getPoints()[0].clone();
		min = (Point2D.Double) regioComponent[0].getPoints()[0].clone();
		
		for(int j = 0; j < regioComponent.length; j++){
			for(int i = 0; i < regioComponent[j].getPoints().length; i++){
				if(regioComponent[j].getPoints()[i].x > max.x) min.x = regioComponent[j].getPoints()[i].x;
				if(regioComponent[j].getPoints()[i].y > max.y) min.y = regioComponent[j].getPoints()[i].y;
			}
		}
	}
	
	private RegionComponent[] readRegions(){
		ArrayList<RegionComponent> region = new ArrayList<>();
		Scanner sc = null;
		String file = "bin/upg_sem2/data/kraje_cr.txt";
		
		String lineWithPoints;
		String pointsString[];
		Point2D.Double points[];
		String singlePoint[];
		String nameRegion;
		
		try {
			sc = new Scanner(new File(file));
		}
		catch (Exception e) {
			Component parentComponent = null;
			JOptionPane.showConfirmDialog(parentComponent, "File with regions not found \nProgram will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		try {
			
			while ((nameRegion = sc.nextLine().trim()).length() != 0) {
				
				lineWithPoints = sc.nextLine().trim();
				lineWithPoints = lineWithPoints.substring(1, lineWithPoints.length() - 1);
				pointsString = lineWithPoints.split("\\),\\(");
				points = new Point2D.Double[pointsString.length];
				
				for (int i = 0; i < points.length; i++) {
					singlePoint = pointsString[i].split(",");
					points[i] = new Point2D.Double(Double.parseDouble(singlePoint[1]), Double.parseDouble(singlePoint[0]));
				}
				region.add(new RegionComponent(nameRegion, new Color(255,255,255), points));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Component parentComponent = null;
			JOptionPane.showConfirmDialog(parentComponent, "File not could parsed \n Program will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return region.toArray(new RegionComponent[0]);
	}

}

















