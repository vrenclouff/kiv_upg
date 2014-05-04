package upg_sem2;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Spousti program a overuje vstupni parametry
 * @author Lukas Cerny
 *
 */

public class Main {
	
	/**
	 * Overi vstupni parametry
	 * @param arg
	 * @param wraper
	 */
	private void inputCheck(String [] arg, Wrap wraper){
		String [] disease  = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV",
				"XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "CELKEM"};
		String [] hospitalizations = {"A", "A+", "R", "R+"};
		
		Component parentComponent = null;
		
		if(arg.length == 0 || arg.length > 4 || arg.length < 4 ){
			JOptionPane.showConfirmDialog(parentComponent, "Parameters not load \nProgram will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		boolean diseaseBl = false;
		for(int i = 0; i < disease.length; i++){
			if(arg[0].equals(disease[i])){ diseaseBl = true;}
		}
		if(!diseaseBl){
			JOptionPane.showConfirmDialog(parentComponent, "Disease not valid (I - XXI) \nProgram will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
				System.exit(1);
		}
		boolean hospitalizationsBl = false;
		for(int j = 0; j < hospitalizations.length; j++){
			if(arg[1].equals(hospitalizations[j])){hospitalizationsBl = true;}
		}
		if(!hospitalizationsBl){
			JOptionPane.showConfirmDialog(parentComponent, "Symbol of hospitalizations not valid \n A, A+, R, R+ \nProgram will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		boolean yearBl = false;
		int year = Integer.parseInt(arg[2]);
		for(int i = 0; i < wraper.getYears().length; i++){
			if(year != wraper.getYears()[i].getYear() || year != -1){yearBl = true;}
		}
		if(!yearBl){
			JOptionPane.showConfirmDialog(parentComponent, "Years not valid \nProgram will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
				System.exit(1);
		}
	}	
	
	/**
	 * Spousti program
	 * @param arg
	 */
	public static void main(String [] arg){
		GetJsonFile json = new GetJsonFile();
		Main mn = new Main();

		Wrap wraper = json.importJson();
		mn.inputCheck(arg, wraper);		
				
		Canvas canvas = new Canvas(wraper, arg);
		canvas.run();
	}
}
	
	
