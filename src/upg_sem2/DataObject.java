package upg_sem2;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Obal
 * Obsahuje pole roku
 * @author Lukas Cerny
 *
 */
class Wrap{
	  private Years [] years;
	  
	  public Wrap(Years [] years){
		  this.years = years;
	  }
	  
	  public void setYears(Years [] years){
		  this.years = years;
	  }
	  public Years[] getYears(){
		  return this.years;
	  }
}

/**
 * Obsahuje pole regionu
 * @author Lukas Cerny
 *
 */
class Years{
  private int year;
  private Regions [] regions;
  
  public Years(Regions [] regions, int year){
	  this.regions = regions;
	  this.year = year;
  }
  
  public void setYear(int year){
	  this.year = year;
  }
  public void setRegions(Regions [] regions){
	  this.regions = regions;
  }
  public int getYear(){
	  return this.year;
  }
  public Regions[] getRegions(){
	  return this.regions;
  }
}

/**
 * Obsahuje hodnoty onemocneni
 * @author Lukas Cerny
 *
 */
class Regions{
  private String name;
  private String [] a;
  private String [] b;
  
  public Regions(String [] a, String [] b, String name){
	  this.name = name;
	  this.a = a;
	  this.b = b;
  }
  
  public void setName(String name){
	  this.name = name;
  }
  public void setA(String [] a){
	  this.a = a;
  }
  public void setB(String [] b){
	  this.b = b;
  }
  public String[] getA(){
	  return this.a;
  }
  public String[] getB(){
	  return this.b;
  }
  public String getName(){
	  return this.name;
  }
}

/**
 * Sestavi data podle zadaneho formatu
 * @author Lukas Cerny
 *
 */
public class DataObject {

	
	private Wrap wrap = new Wrap(setData());
	
	private Years[] setData(){
		
		GetFromTxt mn = new GetFromTxt();
		Roky[] roky = null;
		
		try {roky = mn.export();} 
		catch (IOException e) {e.printStackTrace();}
		
		int length1 = roky.length;
		int length2 = roky[0].data.length;
		
		ArrayList<Years> ye = new ArrayList<>();
		for(int j = 0; j < length1; j++){
			ArrayList<Regions> rg = new ArrayList<>();
						
			for(int i = 0; i < length2; i++){
				
				ArrayList<String> a = roky[j].data[i].getA();
				ArrayList<String> b = roky[j].data[i].getB();
				String name = roky[j].data[i].getName();
				rg.add(new Regions(a.toArray(new String[0]), b.toArray(new String[0]), name));
			}
			int rok = Integer.parseInt(roky[j].rok);
			ye.add(new Years(rg.toArray(new Regions[0]), rok));

		}
		return ye.toArray(new Years[0]);
	}
	
	public Wrap getWrap(){
		return this.wrap;
	}
	
	public String toString() {
		return "DataObject [Wrap="+wrap+"]";
	}
}
		
	
/*

wrap {
  2000 {
    PHA {
	  a [
        I,
		II,
		III,
		IV,
		V
      ]
	},
	STC {
	  a [
        I,
		II,
		III,
		IV,
		V
      ]
	}
  },
  2001 {
    PHA {
	  a [
        I,
		II,
		III,
		IV,
		V
      ]
	},
	STC {
	  a [
        I,
		II,
		III,
		IV,
		V
      ]
	}
  }
}
*/

