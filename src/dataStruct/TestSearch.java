package dataStruct;
import java.io.IOException;
import java.util.ArrayList;

public class TestSearch {
	//StoreAndSearch testData = new StoreAndSearch();
	static String data1 = new String("thisistestOne");
	static String data2 = new String("ThisisTestTwo");	
	static String data3 = new String("sh");

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		StoreAndSearch testData = new StoreAndSearch();
		testData.Store(data1, 2, 90);
		testData.Store(data1, 1,80);
		testData.Store(data1, 3, 70);
		testData.Store(data2, 4, 60);
		testData.Store(data2, 5, 50);
		testData.Store(data2, 6, 40);
		testData.Store(data3, 7, 30);
		testData.Store(data3, 8, 20);
		testData.Store(data3, 9, 10);
		ArrayList<Integer> retData1=new ArrayList<Integer>();
		ArrayList<Integer> retData2=new ArrayList<Integer>();
		ArrayList<Integer> retData3=new ArrayList<Integer>();

		 retData1 = testData.Search(data1);
		 retData2 = testData.Search(data2);
		 retData3 = testData.Search(data3);

		System.out.println(retData1);
	   System.out.println(retData2);
		System.out.println(retData3);


		 
	}

}
