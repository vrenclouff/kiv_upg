package upg_sem2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GetFromTxt {
	
	private String[] file(){
		BufferedReader b = null;
		
		try {b = new BufferedReader(new FileReader("src_txt/years.txt"));}
		catch (FileNotFoundException e) {e.printStackTrace();}
		
		ArrayList<String> list = new ArrayList<>();
		
		String line, path;
		try {
			while((line = b.readLine()) != null){
				path = "src_txt/"+line+".txt";
				list.add(path);
			}
		}
		catch (IOException e) {e.printStackTrace();}
		try {b.close();}
		catch (IOException e) {e.printStackTrace();}
		
		return list.toArray(new String[0]);
	}
	
	public Roky[] export() throws IOException{
		ArrayList<Roky> roky = new ArrayList<>();
				
		
	String [] filename = file();

	for(int z = 0; z < filename.length; z++){
		
		String line;
		int radka = 1;
		
		FileReader red = new FileReader(filename[z]);
		BufferedReader br = new BufferedReader(red);
		
		ArrayList<Data> kraje = new ArrayList<>();
		ArrayList<String> info = new ArrayList<>();

		while((line = br.readLine()) != null){
			String [] parse = line.split(" ");
						
			if(radka == 1){
				for(int j = 0; j < 18; j++){
					kraje.add(new Data(parse[j]));
				}
			}else{
					
				if((radka%2) == 0){
					info.add(parse[0]);
					for(int k = 0; k < 18; k++){
						kraje.get(k).getA().add(parse[k+2]);
					}
				}else{
					for(int k = 0; k < 18; k++){
						kraje.get(k).getB().add(parse[k+2]);
					}
				}
			}
			radka++;
		}
		br.close();
		String rok = filename[z].substring(filename[z].length()-8, filename[z].length()-4);
		roky.add(new Roky(rok, kraje.toArray(new Data[0]), info.toArray(new String[0])));
	}
	return roky.toArray(new Roky[0]);
	
}
}

class Roky{
	String rok;
	Data [] data;
	String [] info;
	public Roky(String rok, Data [] data, String [] info){
		this.rok = rok;
		this.data = data;
		this.info = info;
	}
}