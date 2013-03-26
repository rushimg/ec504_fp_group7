package dataStruct;

import java.util.*;
import java.io.*;

//import DS.StroreAndSearch.Data;
public class StoreAndSearch {
	/* define data type*/
	//private String WordStore,WordSearch;
	private char C1,C2,C3,C4,C5,C6; 
	private char C=' ';
	
    private static HashMap<String, Data> MapShort = new HashMap<String,Data>();//hashmap for string whose length is less or equal to 3
    private static HashMap<String, Data> Map = new HashMap<String,Data>(); //hashmap for string whose length is greater than 3
	//private ArrayList<ArrayList<ArrayList<ArrayList<HashMap<String,Data>>>>> Dic1=new ArrayList<ArrayList<ArrayList<ArrayList<HashMap<String,Data>>>>>();
	private static ArrayList<ArrayList<ArrayList<HashMap<String,Data>>>> Dic2=new ArrayList<ArrayList<ArrayList<HashMap<String,Data>>>>();
	private static ArrayList<ArrayList<HashMap<String,Data>>> Dic3=new ArrayList<ArrayList<HashMap<String,Data>>>();
	private static ArrayList<HashMap<String,Data>> Dic4=new ArrayList<HashMap<String,Data>>();
	private class Data{
		private int Freq;
		private int Index;
	}
	
	/*define method for store */
	
   public void Store(String WordStore,int Freq,int Index) throws IOException {
	   
	  String SubStr;
	  int sz=WordStore.length();
	  Data Info=new Data ();
	  Data InfoShort=new Data ();
	  HashMap<String, Data> mapTest = new HashMap<String, Data>(); //
	  if (sz>3) {
		  C1=WordStore.charAt(0);
		  C2=WordStore.charAt(1);
		  C3=WordStore.charAt(2);
		  SubStr=WordStore.substring(3,sz);
		  Info.Freq=Freq;
		  Info.Index=Index;
		  Map.put(SubStr, Info);
		  //System.out.println(Map.size());
		  for(int i = 0; i< 1000; i++){
			  Dic4.add(mapTest);
		  }
		  Dic4.set(C3-C,Map);
		  Dic3.set(C2-C,Dic4);
		  Dic2.set(C1-C,Dic3);
	  }
	 else {
		  InfoShort.Freq=Freq;
	       InfoShort.Index=Index;
		 MapShort.put(WordStore,InfoShort);
	 }
   }
	
	/*define method for search*/
	
	public int Search(String WordSearch){
		  String SubStr;
		  int sz=WordSearch.length();
		  int IndexReturn=0;
		  if(sz>3){
			  SubStr=WordSearch.substring(3,sz);
			  C4=WordSearch.charAt(0);
			  C5=WordSearch.charAt(1);
			  C6=WordSearch.charAt(2);
			  if(Dic2.get(C4-C).get(C5-C).get(C6-C).get(SubStr)!=null)   
				  IndexReturn=Dic2.get(C4-C).get(C5-C).get(C6-C).get(SubStr).Index;
			  else System.out.println("Search failed, please try another word");
		  }
		  else {
			  if(MapShort.containsKey(WordSearch))
				  IndexReturn=MapShort.get(WordSearch).Index;
		  }
			  
		
		return IndexReturn;
	}
	
	
	
}

