package stringMatcher;

public class FullFunctionMatching {
	
	
	public FullFunctionMatching(){
		
		
		
	}
	
	
	
	//Functionality supported:
	//
	public void match(String input,String pattern, String function){
		KMPMatcher(input,pattern);
	}
	
	
	
	
	/* createString
	 * Builds a string of specified length, and with a default character placed in each position
	 * 
	 * @param length - length of the string
	 * @param character
	 * @return string of length filled with character
	 */
	public String createString(int length, char character){
		if(length < 0){	//control code if length is negative
			System.out.println("createStringError::String length is set to less than 0. Creating string with length 0");
			return "";
		}
		String temp = "";	//define base string
		for(int ii = 0; ii < length; ii++)	//build string with characters. If length = 0, this loop will never go through
			temp += character;
		return temp; //return created string
	}	//endmethod createString - general
	//co-methods
	public String createString(int length){	//overloaded method for use with only spaces
		if(length < 0){	//control code if length is negative
			System.out.println("createStringError::String length is set to less than 0. Creating string with length 0");
			return "";
		}
		String temp = "";	//define base string
		for(int ii = 0; ii < length; ii++)	//build string with spaces. If length = 0, this loop will never go through
			temp += " ";	
		return temp; //return created string
	}	//endmethod createString - make a space string
	
	
	
	
	/* change-Character
	 * returns a string with a character changed at a given index
	 * 
	 * @param input - string to be changed
	 * @param index - index of character to be changed
	 * @param character - new value of character at a given index
	 * @return new string with changed character implemented
	 */
	public String changeCharacter(String input, int index, char character){
		if(index < 0 || index >= input.length()){	//error code if index is out of bounds
			System.out.println("changeCharacterError:: Index to change character is out of bounds. Returning string unchanged.");
			return input;
		}
		String lowerHalf = input.substring(0,index);	//grab the characters at lesser indices
		String upperHalf = input.substring(index + 1);	//grab all characters after the index
		lowerHalf += character;	//add in new character
		lowerHalf += upperHalf;	//add in rest of characters
		return lowerHalf;	//return changed string
	}	//endmethod changeCharacter
	
	/* change-Characters
	 * returns a string with a sequence of characters changed to different characters
	 * 
	 * @param input - string to be changed
	 * @param startIndex - start of sequence to be changed - inclusive
	 * @param endIndex - end of sequence to be changed - inclusive
	 * @param newSequence - new string sequence to replace old sequence
	 * @return new string with changed character values
	 */
	public String changeCharacters(String input,int startIndex, int endIndex, String newSequence){
		if(startIndex < 0 || startIndex >= input.length() || endIndex < 0 || endIndex >= input.length() || startIndex > endIndex){	//error code if index is out of bounds
			System.out.println("changeCharactersError:: Indices are out of bounds. Returning string unchanged.");
			return input;
		}
		String lowerHalf = input.substring(0,startIndex);	//grab the characters at lesser indices
		String upperHalf = input.substring(endIndex + 1);	//grab all characters after the index
		lowerHalf += newSequence;	//add in new character
		lowerHalf += upperHalf;	//add in rest of characters
		return lowerHalf;	//return changed string		
	}
	
	/* KMP Matcher
	* Performs string matching algorithm as defined in CLRS p.1005
	* 
	* @param input - original string
	* @param pattern - pattern to be found in original string
	* 
	*/
	private int KMPMatcher(String input, String pattern){
		int n = input.length();
		int m = pattern.length();
		int[] PI;
		PI = ComputePrefixFunction(pattern);
		int q = -1;
		for(int ii = 0; ii< n; ii++){
			while(q > -1 && pattern.charAt(q+1) != input.charAt(ii))
				q = PI[q];
			if(pattern.charAt(q+1) == input.charAt(ii))
				q++;
			if(q == (m-1)){
				System.out.println("Pattern occurs with shift " + (ii-(m-1)));
				q = PI[q];
			}
		}
		
		return -1;
	}
	
	/* Compute-prefix-function
	 * Helper function to the KMP Matcher that computes the prefix of the pattern.
	 * 
	 * @param pattern - the pattern that the input should be matched too
	 */
	private int[] ComputePrefixFunction(String pattern){
		int m = pattern.length();
		int[] PI;
		PI = new int[m];
		PI[0] = -1;
		int k = -1;
		for(int q = 1; q < m; q++){
			while(k>-1 && pattern.charAt(k+1) != pattern.charAt(q))
				k = PI[k];
			if(pattern.charAt(k+1) == pattern.charAt(q))
				k++;
			PI[q] = k;
		}
		return PI;	
	}
	
	
	
	
	
	
}
