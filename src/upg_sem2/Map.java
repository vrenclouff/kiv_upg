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

/**
 * 
 * @author Lukas Cerny
 *
 */

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
	private double [] regress;
	
	/**
	 * Konstruktor - nastavuje atributy, ktere jsou potreba pro beh programu
	 * @param data - vstupni JSON soubor
	 * @param arg - vstupni parametry
	 */
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
	 * Overi, zda se vykresli trend vyvoje
	 * @return true/false
	 */
	private boolean isPlus(){
		boolean value = false;
		
		if(arg[1].length() == 2){
			value = true;
		}
		
		return value;
	}
	
	/**
	 * Nazvy onemocneni pro vypis
	 * @param i - druh onemocneni
	 * @return nazev onemocneni
	 */
	private String diseaseName(int i){
		String [] diseaseText = {
	/*1*/		"Některé infekční a parazitární nemoci",
	/*2*/		"Novotvary",
	/*3*/		"Nemoci krve, krvetvorných orgánů a imunity",
	/*4*/		"Nemoci endokrinní, výživy a přeměny látek",
	/*5*/		"Poruchy duševní a poruchy chování",
	/*6*/		"Nemoci nervové soustavy",
	/*7*/		"Nemoci oka a očních adnex",
	/*8*/		"Nemoci ucha a bradavkového výběžku",
	/*9*/		"Nemoci oběhové soustavy",
	/*10*/		"Nemoci dýchací soustavy",
	/*11*/		"Nemoci trávicí soustavy",
	/*12*/		"Nemoci kůže a podkožního vaziva",
	/*13*/		"Nemoci svalové a kosterní soustavy a pojivové tkáně",
	/*14*/		"Nemoci močové a pohlavní soustavy",
	/*15*/		"Těhotenství, porod a šestinedělí",
	/*16*/		"Některé stavy vzniklé v perinatálním období",
	/*17*/		"Vrozené vady, deformace a chromosomální abnormality",
	/*18*/		"Příznaky, znaky a nálezy nezařazené jinde",
	/*19*/		"Poranění, otravy a následky vnějších příčin",
	/*20*/		"Název neuveden",
	/*21*/		"Faktory ovlivňující zdravotní stav a kontakt se zdravotnickými službami",
	/*CELKEM*/  "Celkový počet hospitalizací"
		};
		return diseaseText[i];
	}
	
	/**
	 * Vrati index onemocneni, se kterym pracuji v celem programu
	 * @param tmp - string onemocneni
	 * @return index onemocneni
	 */
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
	/**
	 * Vraci index roku
	 * @return
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * Nastavi idnex roku na hodnotu
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * Vrati tru/false zda uzivatel v parametru zadal + (trend)
	 * @return
	 */
	public boolean getIsPlus(){
		return isPlus;
	}
	/**
	 * Vrati pocet snimku, ktere se zobrazuji (minRok - maxRok, + vizualizace)
	 * @return
	 */
	public int getCounter(){
		return counter;
	}
	
	/**
	 * Nacte vizualizaci v jednotlivych letech do pole
	 */
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
	
	/**
	 * Vykresli graf pro dany region na XY souradnicich
	 * @param g2 - graficky kontext
	 * @param x - souradnice x
	 * @param y - souradnice y
	 * @param i - index regionu
	 */
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
			if(diseaseIndex != 21){
				if(isA){
					number = data.getYears()[j].getRegions()[i].getA()[diseaseIndex];
				}else{
					number = data.getYears()[j].getRegions()[i].getB()[diseaseIndex];
				}
				tmp = Double.parseDouble( number.replace(",",".") );
			}else{
				tmp = getCounteValue(i, j);
			}
			
			if(tmp > max){
				max = tmp;
			}else if(tmp < min){
				min = max;
			}
		}
				
		for(int k = 0; k <= lengthYear; k++){
			if(diseaseIndex != 21){
				if(isA){
					number = data.getYears()[k].getRegions()[i].getA()[diseaseIndex];
				}else{
					number = data.getYears()[k].getRegions()[i].getB()[diseaseIndex];
				}
				tmp = Double.parseDouble( number.replace(",",".") );
			}else{
				tmp = getCounteValue(i, k);
			}
			
			tmpY = (int)((2.0/3.0 * 30.0 * tmp)/max);
			
			if(k != 0){
				g2.drawLine(x1, y-valueBack, x2, y-tmpY);
				x1 += len;
				x2 += len;
			}
			
			valueBack = tmpY;
		
			
		}
	}
	
	/**
	 * Vytvori mapu celkove vizualizace (parametr -1)
	 * @return
	 */
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
		
		return img;
	}
	
	/**
	 * Vytvori mapu poctu hospitalizaci v danem roce
	 * @return
	 */
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
			if(diseaseIndex != 21){
				if(isA){
					t = String.valueOf(Math.round(Double.parseDouble( data.getYears()[getIndex()].getRegions()[regions[i]].getA()[diseaseIndex].replace(",",".") )));
				}else{
					t = String.valueOf(Math.round(Double.parseDouble( data.getYears()[getIndex()].getRegions()[regions[i]].getB()[diseaseIndex].replace(",",".") )));
				}
			}else{
				t = String.valueOf(Math.round(getCounteValue(i, getIndex())));
			}
			regioComponent[i].draw(g2, max, min);
			g2.drawString(t, regCtr[tmp++], regCtr[tmp++]);
			
		}
				
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		g2.drawString(diseaseName(diseaseIndex), 30, 30);
		g2.drawString(String.valueOf(data.getYears()[getIndex()].getYear()), 30, 60);
		
		
		return img;	
	}
	
	/**
	 * Vytvori mapu poctu hospitalizaci v horizontu 3 let (trend)
	 * @return
	 */
	private BufferedImage drawYearScreenPlus() {
		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_4BYTE_ABGR );
		int [] regions = {11, 10, 12, 9, 7, 8, 6, 2, 4, 3, 1, 5, 0, 13};
		Graphics2D g = (Graphics2D) img.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		regressionLine();
		
		for(int i = 0; i < regioComponent.length; i++){
			regioComponent[i].setColor(setColorPlus(regions[i]));
			regioComponent[i].draw(g2, max, min);
		}
				
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		g2.drawString(diseaseName(diseaseIndex), 30, 30);
		g2.drawString(String.valueOf(data.getYears()[getIndex()].getYear()), 30, 60);
		
		
		return img;	
	}
	
	
	/**
	 * Vypocte pres metodu nejmensich ctvercu hodnotu pro dany region
	 * @return
	 */
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
	/**
	 * Vypocet regresni primky
	 * @param array - pole o 3 hodnotach
	 * @return
	 */
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
	
	/**
	 * Vykresli legendu
	 * @param g2
	 */
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
	
	/**
	 * Vykresluje mapu
	 */
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

			int yearMin = data.getYears()[0].getYear();
			int yearMax = data.getYears()[data.getYears().length-1].getYear();
			String text = "Počet případů hospitalizace v letech "+String.valueOf(yearMin)+" - "+String.valueOf(yearMax)+" v ČR";
			g2.drawString(text, 80, 550);
			g2.drawString(diseaseName(diseaseIndex), 80, 600);
			
		}

	}	
	
	/**
	 * Vykresli male mapy
	 * @param g2 - graficky kontext
	 */
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
	
	/**
	 * Vybere barvu na danem index
	 * @param i - index vyberu barvy
	 * @return
	 */
	private Color getColor(int i){
		Color[] color = {new Color(68, 151,101), new Color(74, 201, 124), new Color(79, 250, 148), new Color(175, 246, 203)};
		return color[i];
	}
	
	/**
	 * Vybere barvu pro trend na danem index
	 * @param i - index vyberu barvy
	 * @return
	 */
	private Color getColorPlus(int i){
		Color[] color = {new Color(255, 102, 102), new Color(255, 153,153), new Color(128, 255, 0), new Color(204, 255, 153)};
		return color[i];
	}
	
	/**
	 * Nastavuje barvu, ktera nejlepe vystihuje danou hodnotu
	 * @param i
	 * @return
	 */
	private Color setColor(int i){
		double tmp = 0;
		String number = "";
		double ratio = maxValue - minValue;
		int ind = 0;
		
		if(diseaseIndex != 21){
			if(isA){
				number = data.getYears()[getIndex()].getRegions()[i].getA()[diseaseIndex];
			}else{
				number = data.getYears()[getIndex()].getRegions()[i].getB()[diseaseIndex];
			}
			tmp = Double.parseDouble( number.replace(",",".") );
		}else{
			tmp = getCounteValue(i, getIndex());
		}
		
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
	
	/**
	 * Nastavuje barvu, ktera nejlepe vystihuje danou hodnotu pro trend
	 * @param i
	 * @return
	 */
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
	
	/**
	 * Spocte celkovy pocet onemocneni pro dany kraj
	 * @param k - index kraje
	 * @param ind - index roku
	 * @return
	 */
	private double getCounteValue(int k, int ind){
		double tmp = 0.0;
		String number = "";
		double count = 0.0;
		
		for(int j = 0; j < data.getYears()[ind].getRegions()[0].getA().length; j++){
			if(isA){
				number = data.getYears()[ind].getRegions()[k].getA()[j];
			}else{
				number = data.getYears()[ind].getRegions()[k].getB()[j];
			}
			if(number.equals("x")){ continue; }
			tmp = Double.parseDouble( number.replace(",",".") );

			count += tmp;
		}
		return count;
	}
	
	/**
	 * Zjisti max a min pro dany rok
	 */
	private void ratio(){
		maxValue = Double.MIN_VALUE;
		minValue = Double.MAX_VALUE;
		double tmp = 0.0;
		String number = "";
		double count = 0.0;
				
		if(diseaseIndex != 21){
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
		}else{
			for(int k = 0; k < data.getYears()[getIndex()].getRegions().length-4; k++){
				
				count = getCounteValue(k, getIndex());
				
				if(count > maxValue){
					maxValue = count;
				}else if(count < minValue){
					minValue = count;
				}
			}
			
		}
	}
	
	/**
	 * Zjisti max a min pro vyvoj trendu
	 * @param array - pole vypoctu trendu v danem roce
	 */
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
	
	/**
	 * Nastavi max a min hodnoty pro vykresleni mapy
	 */
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
	
	/**
	 * Nacte regiony ze souboru a ulozi do pole
	 * @return
	 */
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

















