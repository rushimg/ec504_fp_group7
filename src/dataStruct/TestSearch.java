package dataStruct;

import java.io.*;
import java.util.*;


public class TestSearch {
	//StoreAndSearch testData = new StoreAndSearch();
	static String data1 = new String("time");
	static String data2 = new String("ThisisTestTwo");	
	static String data3 = new String("sh");

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		StoreAndSearch testData = new StoreAndSearch();
		testData.Store("technology", 2, 90);
		testData.Store("teaa", 3, 80);
		testData.Store("technology", 2, 100);
		
		testData.Store("timer", 3, 85);
		testData.Store("tadaad", 1, 70);
		
		testData.Store(data2, 4, 60);
		testData.Store(data2, 4, 70);
		testData.Store(data2, 4, 80);
		testData.Store(data2, 5, 50);
		testData.Store(data3, 6, 40);
		testData.Store(data3, 7, 30);
		testData.Store(data3, 8, 20);
		testData.Store(data3, 9, 10);
		testData.Store(data3, 9, 101);
		ArrayList<Integer> retData1=new ArrayList<Integer>();
		ArrayList<Integer> retData2=new ArrayList<Integer>();
		ArrayList<Integer> retData3=new ArrayList<Integer>();
		
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("DS.txt"));
		outStream.writeObject(testData);
		outStream.close();
		
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("DS.txt"));
		StoreAndSearch decoded = (StoreAndSearch) inStream.readObject();
		
		
		 retData1 = testData.Search("technology");
		 
		 retData2 = decoded.Search(data2);
		 retData3 = decoded.Search(data3);

		System.out.println(retData1);
	    System.out.println(retData2);
		System.out.println(retData3);


		
	}

}
