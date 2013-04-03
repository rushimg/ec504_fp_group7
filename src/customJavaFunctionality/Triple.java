package customJavaFunctionality;
import java.util.ArrayList;

public class Triple {
	public String first;
	public int second;
	public ArrayList<Integer> third;
	
	public Triple(String first, int second){
		third = new ArrayList<Integer>();
		this.first = first;
		this.second = second;
	}
}
