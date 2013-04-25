package dataStruct;

import java.io.Serializable;

public class IndexStruct implements Serializable{
	public int frequency;
	public int nodeIndex;
	public IndexStruct(int frequency, int nodeIndex) {
		this.frequency = frequency;
		this.nodeIndex = nodeIndex;
	}
}
