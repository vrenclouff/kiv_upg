package upg_sem2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class Canvas extends JFrame {
	
	private static final long serialVersionUID = 1L;
	final int width;
	final int height;
	JPanel drawingArea;
	final RegionComponent [] regioComponent;
	private Point2D.Double max;
	private Point2D.Double min;
	
	

	public Canvas(){
		this.regioComponent = this.readRegions();
		this.calculateLimits();
		this.width = 600;
		this.height = 600;
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)drawingArea.getGraphics();
		for(int i = 0; i < regioComponent.length; i++){
		regioComponent[i].draw(g2, drawingArea.getWidth(), drawingArea.getHeight(), 400, max, min);
		}
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

	
	public RegionComponent[] readRegions(){
		ArrayList<RegionComponent> region = new ArrayList<>();
		
		String file = "bin/upg_sem2/data/kraje_cr.txt";
		String lineWithPoints;
		String pointsString[];
		Point2D.Double points[];
		String singlePoint[];
		String nameRegion;
		Scanner sc = null;
		Color color;
		int dev = 1;
		
		try {sc = new Scanner(new File(file));}
		catch (Exception e) {
			Component parentComponent = null;
			JOptionPane.showConfirmDialog(parentComponent, "File with regions not found \n Program will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
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

	public void run(){
		this.drawingArea = new JPanel();
		this.drawingArea.setPreferredSize(new Dimension(width, height));
		this.add(drawingArea);
		this.pack();
		
		this.setTitle("Mapa CR");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public Point2D.Double getMax() {
		return max;
	}

	public Point2D.Double getMin() {
		return min;
	}
}
