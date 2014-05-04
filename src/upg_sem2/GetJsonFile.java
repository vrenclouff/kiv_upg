package upg_sem2;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

import com.google.gson.Gson;

/**
 * Nacte hodnoty do objektu ze souboru file.json s pouzitim knihovny
 * @author Lukas Cerny
 *
 */
public class GetJsonFile {

	public Wrap importJson(){	 
		Gson gson = new Gson();
		try {
			BufferedReader br = new BufferedReader(new FileReader("bin/upg_sem2/data/file.json"));
			DataObject obj = gson.fromJson(br, DataObject.class);
			return obj.getWrap();
		}
		catch (IOException e) {
			Component parentComponent = null;
			JOptionPane.showConfirmDialog(parentComponent, "JSON file not found \n Program will be closed", "ERROR", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}
}
