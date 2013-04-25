package dataStruct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreIntoFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HashMap<String, ArrayList<IndexStruct> > myHashMap;
	public StoreIntoFile() {
		myHashMap = new HashMap<String, ArrayList<IndexStruct> >();
	}
}
