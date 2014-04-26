package upg_sem2;

import java.awt.Color;
import java.awt.Component;
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
	private Point2D.Double max;
	private Point2D.Double min;
	private Wrap data;
	private int year;
	private int index;
	private final int width = 700;
	private final int height = 600;
	
	public Map(Wrap data){
		this.regioComponent = this.readRegions();
		this.calculateLimits();
		this.data = data;
		
		this.setSize(width, height);
		this.setPreferredSize(this.getSize());
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}


	private BufferedImage drawYearScreen() {
		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );
		Graphics2D g = (Graphics2D) img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		
		for(int i = 0; i < regioComponent.length; i++){
			regioComponent[i].draw(g2, max, min);
		}
		g2.setColor(Color.white);
		g2.drawString(String.valueOf(data.getYears()[getIndex()].getYear()), 10, 10);
		
		return img;	
	}
	

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(drawYearScreen(), 0, 0, null);
	}	

	private void calculateLimits() {
		
		max = (Point2D.Double) regioComponent[0].getPoints()[0].clone();
		min = (Point2D.Double) regioComponent[0].getPoints()[0].clone();
		
		for(int j = 0; j < regioComponent.length; j++){
			for(int i = 0; i < regioComponent[j].getPoints().length; i++){
				if(regioComponent[j].getPoints()[i].x < min.x) min.x = regioComponent[j].getPoints()[i].x;
				if(regioComponent[j].getPoints()[i].y < min.y) min.y = regioComponent[j].getPoints()[i].y;
				if(regioComponent[j].getPoints()[i].x > max.x) min.x = regioComponent[j].getPoints()[i].x;
				if(regioComponent[j].getPoints()[i].y > max.y) min.y = regioComponent[j].getPoints()[i].y;
			}
		}
	}
	
	private RegionComponent[] readRegions(){
		ArrayList<RegionComponent> region = new ArrayList<>();
		
		String file = "bin/upg_sem2/data/kraje_cr.txt";
		String lineWithPoints;
		String pointsString[];
		Point2D.Double points[];
		String singlePoint[];
		String nameRegion;
		Color color;
		int dev = 1;
		
		Scanner sc = null;
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
				if((dev++)%2 == 0) color = new Color(118,236,185);
				else color = new Color(2,185,106);
				
				region.add(new RegionComponent(nameRegion, color, points));
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
