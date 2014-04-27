package upg_sem2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Arrays;

public class RegionComponent {
	
	private Point2D.Double points[];
	private Path2D.Double regionPath;
	private Color color;
	private String value;
	private int centerX;
	private int centerY;
	
	public RegionComponent(String regionName, Color regionColor, Point2D.Double points[]){
		this.points = Arrays.copyOf(points, points.length);
		this.color = regionColor;
	}
	
	public void draw(Graphics2D g2, Point2D.Double max, Point2D.Double min) {
		int padding = 300;
		int regionHeight = 60;
		int regionWidth = 60;
 		
	//	g2.drawString(getValue(), centerX, centerY);
		
		this.createTransformedRegionPath(regionWidth, regionHeight, max, min);
		
		AffineTransform origTransform = g2.getTransform();
		g2.translate(padding, padding);
		g2.rotate(180*Math.PI/180);
		
		this.drawRegion(g2);
		
		g2.setTransform(origTransform);
	}	
	
	private void createTransformedRegionPath(int width, int height, Point2D.Double max, Point2D.Double min) {
		Point2D.Double pointsTransformed[] = new Point2D.Double[points.length];
				
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		
		double convertX = width / (max.x - min.x);
		double convertY = height / (max.y - min.y);
		double convert = convertX > convertY ? convertY : convertX; 

		for (int j = 0; j < points.length; j++) {
			double transX = ((points[j].x - min.x) * convert)-300;
			double transY = height - (points[j].y - min.y)  * convert * 1.5; 
			
			if((int)transX > maxX){
				maxX = (int)transX;
			}else if((int)transX < minX){
				minX = (int)transX;
			}
			if((int)transY > maxY){
				maxY = (int)transY;
			}else if((int)transX < minY){
				minY = (int)transY;
			}
			
			
			pointsTransformed[j] = new Point2D.Double(transX, transY);
		}
		
		centerX = maxX - minX;
		centerY = maxY - minY;
		
		this.regionPath = new Path2D.Double();
		this.regionPath.moveTo(pointsTransformed[0].x, pointsTransformed[0].y);
		for (int i = 1; i < points.length; i++) 
			this.regionPath.lineTo(pointsTransformed[i].x, pointsTransformed[i].y);
		this.regionPath.closePath();
	}
	
	private void drawRegion(Graphics2D g2) {		
		
		g2.setStroke(new BasicStroke(1));

		g2.setColor(this.color);
		g2.fill(regionPath);

		g2.setColor(Color.black);
		g2.draw(regionPath);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Point2D.Double[] getPoints() {
		return points;
	}

	public void setPoints(Point2D.Double[] points) {
		this.points = points;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String number) {		
		this.value = number;
	}
}
