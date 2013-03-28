package dataStruct;

import java.io.IOException;

public class TestSearch {
	//StoreAndSearch testData = new StoreAndSearch();
	static String data1 = new String("ThisisTestOne");
	static String data2 = new String("ThisisTestTwo");	
	static String data3 = new String("sh");

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		StoreAndSearch testData = new StoreAndSearch();
		testData.Store(data1, 12, 2);
		int retData1 = testData.Search(data1);
		System.out.println(retData1);
		

	}

}
