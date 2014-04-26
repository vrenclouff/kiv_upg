package upg_sem2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class RegionComponent {
	
	Point2D.Double points[];
	Path2D.Double regionPath;
	Color color;
	String regionName;
	

	public RegionComponent(String regionName, Color regionColor, Point2D.Double points[]){
		this.points = Arrays.copyOf(points, points.length);
		this.color = regionColor;
		this.regionName = regionName;
	}
	
	public void draw(Graphics2D g2, int windowWidth, int windowHeight, int padding, Point2D.Double max, Point2D.Double min) {
//		int regionWidth = windowWidth - 2*padding;
//		int regionHeight = windowHeight - 2*padding;
//		
//		System.out.println(regionName);
//	//	System.out.println("width: "+regionWidth+"     height: "+regionHeight);
//		
//	//	regionWidth = 10;
		int regionHeight = 60;
		int regionWidth = 60;
 		
		this.createTransformedRegionPath(regionWidth, regionHeight, max, min);
		
		AffineTransform origTransform = g2.getTransform();
		g2.translate(padding, padding);
		g2.rotate(180*Math.PI/180);
		
		this.drawRegion(g2, regionWidth, regionHeight);
		
		g2.setTransform(origTransform);
	}	
	
	private void createTransformedRegionPath(int width, int height, Point2D.Double max, Point2D.Double min) {
		Point2D.Double pointsTransformed[] = new Point2D.Double[points.length];
				
		
		double convertX = width / (max.x - min.x);
		double convertY = height / (max.y - min.y);
		double convert = convertX > convertY ? convertY : convertX; 

		for (int j = 0; j < points.length; j++) {
			double transX = ((points[j].x - min.x) * convert)-300;
			double transY = height - (points[j].y - min.y)  * convert * 1.5; 
			pointsTransformed[j] = new Point2D.Double(transX, transY);
		}
		
		this.regionPath = new Path2D.Double();
		this.regionPath.moveTo(pointsTransformed[0].x, pointsTransformed[0].y);
		for (int i = 1; i < points.length; i++) 
			this.regionPath.lineTo(pointsTransformed[i].x, pointsTransformed[i].y);
		this.regionPath.closePath();
	}
	
	private void drawRegion(Graphics2D g2, int width, int height) {		
		
//		Font font = new Font("Arial", Font.BOLD, 12);
//		g2.setFont(font);
		g2.setStroke(new BasicStroke(1));

		g2.setColor(this.color);
		g2.fill(regionPath);

		g2.setColor(Color.black);
		g2.draw(regionPath);
	}
	
	
	public Point2D.Double[] getPoints() {
		return points;
	}

	public void setPoints(Point2D.Double[] points) {
		this.points = points;
	}
}
