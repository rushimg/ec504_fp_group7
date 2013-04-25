package dataStruct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreIntoFile implements Serializable {
	/**
	 * 
	 */
	public HashMap<String, ArrayList<IndexStruct> > myHashMap;
	public StoreIntoFile() {
		myHashMap = new HashMap<String, ArrayList<IndexStruct> >();
	}
}
