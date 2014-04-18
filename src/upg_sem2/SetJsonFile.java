package upg_sem2;

import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
 
public class SetJsonFile {
    public static void main(String[] args) {
 
	DataObject obj = new DataObject();
	Gson gson = new Gson();

	String json = gson.toJson(obj);
 
	try {
		FileWriter writer = new FileWriter("file.json");
		writer.write(json);
		writer.close();
	}
	catch (IOException e) {e.printStackTrace();} 
    }
}
