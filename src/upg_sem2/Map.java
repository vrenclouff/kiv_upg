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
	private final boolean isA;
	
	public Map(Wrap data, String [] arg){
		this.regioComponent = this.readRegions();
		this.calculateLimits();
		this.data = data;
		this.imageYears = new ArrayList<>();
		this.arg = arg;
		this.diseaseIndex = getDiseaseIndex(arg[0]);
		this.isA = isA();
		
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
	
	private void loadImages(){
		int tmp = data.getYears().length;
		for(int i = 0; i < tmp; i++){
			setIndex(i);
			imageYears.add(i, drawYearScreen());
		}
		imageYears.add(tmp, drawVisual());
		
	}
	
	private BufferedImage drawVisual(){
		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
		Graphics2D g = (Graphics2D) img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		int tmp = 0;
		int [] regCtr = {490, 290, 440, 350, 530, 350, 340, 310, 360, 180, 390, 250, 275, 130, 220, 350, 70, 210, 110, 270, 230, 265, 170, 160, 500, 100, 550, 260};
		for(int i = 0; i < regioComponent.length; i++){
			regioComponent[i].setColor(Color.white);
			regioComponent[i].draw(g2, max, min);
			
			g2.drawString("graf", regCtr[tmp++], regCtr[tmp++]);
		}
		
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
		
	}
	

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(imageYears.get(getIndex()), 0, 0, null);
		if(getIndex() != data.getYears().length){
			minimizeImg(g2);
			// nejaky text
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
