package dataStruct;


import java.util.*;
import java.io.*;

public class StoreAndSearch implements Serializable {
	/* define data type */
	private char C1, C2, C3, C4, C5, C6;
	private char C = ' ';

	private static class Data {
		private ArrayList<Integer> Freq = new ArrayList<Integer>();
		private ArrayList<Integer> Index = new ArrayList<Integer>();
	}
	
	// hashmap for  string whose length is  less or equal to 3
	private static HashMap<String, Data> MapShort = new HashMap<String, Data>();
	
	// //hashmap for string whose length is greater than 3
//	private static HashMap<String, Data> Map = new HashMap<String, Data>(); 
	// private ArrayList<ArrayList<ArrayList<ArrayList<HashMap<String,Data>>>>>
	// Dic1=new
	// ArrayList<ArrayList<ArrayList<ArrayList<HashMap<String,Data>>>>>();
	private static ArrayList<ArrayList<ArrayList<HashMap<String, Data>>>> Dic2 = new ArrayList<ArrayList<ArrayList<HashMap<String, Data>>>>();
	//private static ArrayList<ArrayList<HashMap<String, Data>>> Dic3 = new ArrayList<ArrayList<HashMap<String, Data>>>();
	//private static ArrayList<HashMap<String, Data>> Dic4 = new ArrayList<HashMap<String, Data>>();
	private static ArrayList<Integer> ReturnIndex = new ArrayList<Integer>();
	private static ArrayList<Integer> ReturnFreq = new ArrayList<Integer>();


	StoreAndSearch() {
		HashMap<String, Data> mapTest = new HashMap<String, Data>(); //
		Data dataTest = new Data();
		String strTest = " ";
		mapTest.put(strTest, dataTest);
		ArrayList<ArrayList<HashMap<String, Data>>> Dic33 = new ArrayList<ArrayList<HashMap<String, Data>>>();
		ArrayList<HashMap<String, Data>> Dic44 = new ArrayList<HashMap<String, Data>>();
		for (int i = 0; i < 96; i++) {
			Dic44.add(mapTest);
		}
		for (int i = 0; i < 96; i++) {
			Dic33.add(Dic44);
		}
		for (int i = 0; i < 96; i++) {
			Dic2.add(Dic33);
		}
	}

	/* define method for store */
     
	public void Store(String WordStore, int Freq, int Index)  throws IOException {
        
		String SubStr;
		int sz = WordStore.length();
		Data Info = new Data();
		Data InfoShort = new Data();
		
		if (Check(WordStore) == 0) {
			ArrayList<ArrayList<HashMap<String, Data>>> Dic3 = new ArrayList<ArrayList<HashMap<String, Data>>>();
			ArrayList<HashMap<String, Data>> Dic4 = new ArrayList<HashMap<String, Data>>();
			HashMap<String, Data> mapTest = new HashMap<String, Data>(); //
			Data dataTest = new Data();
			String strTest = " ";
			mapTest.put(strTest, dataTest);
			for (int i = 0; i < 96; i++) {
				Dic4.add(mapTest);
			}
			for (int i = 0; i < 96; i++) {
				Dic3.add(Dic4);
			}
			HashMap<String, Data> Map = new HashMap<String, Data>();
			
			if (sz > 3) {
				C1 = WordStore.charAt(0);
				C2 = WordStore.charAt(1);
				C3 = WordStore.charAt(2);
				SubStr = WordStore.substring(3, sz);
				Info.Freq.add(Freq);
				Info.Index.add(Index);
				Map.put(SubStr, Info);
				Dic4.set(C3 - C, Map);
				Dic3.set(C2 - C, Dic4);
				Dic2.set(C1 - C, Dic3);
			System.out.println(Map.size());
			} else {
				InfoShort.Freq.add(Freq);
				InfoShort.Index.add(Index);
				MapShort.put(WordStore, InfoShort);

			}
		} else if (Check(WordStore) != 0) {
			if (sz > 3) {
				char C7 = WordStore.charAt(0);
				char C8 = WordStore.charAt(1);
				char C9 = WordStore.charAt(2);

				String SubStr2 = WordStore.substring(3, sz);
				Dic2.get(C7 - C).get(C8 - C).get(C9 - C).get(SubStr2).Index
						.add(Index);
				Dic2.get(C7 - C).get(C8 - C).get(C9 - C).get(SubStr2).Freq
						.add(Freq);

			} else {
				
				MapShort.get(WordStore).Index.add(Index);
				MapShort.get(WordStore).Freq.add(Freq);
			}
		}
		
		
	}


	/*public  void StoreFile() throws IOException{
		
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("DS.txt"));
		outStream.writeObject(Dic2);
		outStream.close();
	}*/
	
	
	/* define method for search */

	public ArrayList<Integer> Search(String WordSearch) {
		String SubStr;
		int sz = WordSearch.length();
		ArrayList<Integer> FreqCopy = new ArrayList<Integer>();
		ArrayList<Integer> IndexCopy = new ArrayList<Integer>();
		 ArrayList<Integer> CheckFreq = new ArrayList<Integer>();
		 ArrayList<Integer> CheckFreq1 = new ArrayList<Integer>();
		if (sz > 3) {
			SubStr = WordSearch.substring(3, sz);
			C4 = WordSearch.charAt(0);
			C5 = WordSearch.charAt(1);
			C6 = WordSearch.charAt(2);

			if (Dic2.get(C4 - C).get(C5 - C).get(C6 - C).get(SubStr) == null) {
				// System.out.println("Search failed, please try another word");
				return null;
			} else if (Dic2.get(C4 - C).get(C5 - C).get(C6 - C).get(SubStr) != null) {
				ReturnIndex = Dic2.get(C4 - C).get(C5 - C).get(C6 - C)
						.get(SubStr).Index;
				ReturnFreq = Dic2.get(C4 - C).get(C5 - C).get(C6 - C)
						.get(SubStr).Freq;


				FreqCopy.addAll(ReturnFreq);
				//System.out.println(FreqCopy);
				Collections.sort(FreqCopy);
				Collections.reverse(FreqCopy);
        
				Iterator<Integer> itr = FreqCopy.iterator();

				while (itr.hasNext()) {
					int a = itr.next();
					
					if(CheckFreq.contains(a)!=true){
					for (int i = 0; i < ReturnFreq.size(); i++)
					{
						int b = ReturnFreq.get(i);
						int c = ReturnIndex.get(i);
						if (b == a) {
							IndexCopy.add(c);
							
						}
						
					}
					CheckFreq.add(a);

				}
				
				}
			}

		} else {
			if (!MapShort.containsKey(WordSearch)) {
				return null;

			} else {

				ReturnIndex = MapShort.get(WordSearch).Index;
				ReturnFreq = MapShort.get(WordSearch).Freq;
     
				FreqCopy.addAll(ReturnFreq);
				Collections.sort(FreqCopy);
				Collections.reverse(FreqCopy);

				Iterator<Integer> itr = FreqCopy.iterator();

				while (itr.hasNext()) {
					int a = itr.next();
					if(CheckFreq1.contains(a)!=true){
					for (int i = 0; i < ReturnFreq.size(); i++)
					{
						int b = ReturnFreq.get(i);
						int c = ReturnIndex.get(i);
						
						if (b == a) {
							IndexCopy.add(c);
						}
					} CheckFreq1.add(a);
					}

				}

			}
		}

		return IndexCopy;
	}

	public int Check(String WordSearch) {
		String SubStr;
		int sz = WordSearch.length();

		if (sz > 3) {
			SubStr = WordSearch.substring(3, sz);
			C4 = WordSearch.charAt(0);
			C5 = WordSearch.charAt(1);
			C6 = WordSearch.charAt(2);

			if (Dic2.get(C4 - C).get(C5 - C).get(C6 - C).get(SubStr) == null) {
				return 0;
			} else
				return 1;

		} else {
			if (!MapShort.containsKey(WordSearch))
				return 0;
			else
				return 1;
		}
	}

}
