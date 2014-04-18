package upg_sem2;

public class Main {
	
	public static void main(String [] arg){
		GetJsonFile json = new GetJsonFile();
		Wrap wraper = json.importJson();

		System.out.println(wraper.getYears()[13].getYear());
	}
}
	
	
