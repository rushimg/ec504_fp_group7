package stringMatcher;
import java.util.ArrayList;


/*
 * Function Overview:
 * 
 * NOTE: This gives the most explicit definition of each function. There are many overloaded methods that allow you to assume 
 * 		 values for the given variables
 * 
 * 	ArrayList<Integer> KMPMatcher(String input, String pattern)
 * 		checks if pattern is present in input and returns an arrayList with matching indices
 * 
 *  String createString(int length, char character)
 *		creates a string of size length filled with 'character'
 *		
 *  String changeCharacter(String input, int index, char character)
 *		changes the character in input at location index to character
 *		
 *  String changeCharacters(String input, int startIndex, int endIndex, String newSequence)
 *  	changes the sequence in input that is between startIndex and endIndex to the newSequence. 
 *  	Sequences need not be of equal length.
 *  
 *  String removePattern(int after, String input, String pattern, int instances)
 *  	Removes pattern from input after seeing 'after' patterns. Keeps removing pattern until it has seen 'instances' patterns.
 */

public class FullFunctionMatching {
	
	
	public FullFunctionMatching(){
		
		
		
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
	
	
	
	/* removePattern (generic)
	 * remove the string pattern from string input
	 * 
	 * @param input - string from which pattern should be removed
	 * @param pattern - string which is to be removed from input
	 * @return input - pattern
	 */
	/* removePattern(input,pattern)
	 * removes all instances of pattern from input
	 */
	public String removePattern(String input, String pattern){
		if(pattern.length() > input.length()){
			System.out.println("removePatter Error::pattern is longer than input! Returning input unchanged.");
			return input;
		}
		ArrayList<Integer> matches = KMPMatcher(input, pattern);
		String newString = "";
		int index = 0;
		int arrayIndex = 0;
		while(index < input.length()){
			if(index != matches.get(arrayIndex))
				newString += input.charAt(index++);
			else{
				index += pattern.length();
				arrayIndex++;
				if(arrayIndex == matches.size()){
					newString += input.substring(index);
					break;
				}
			}
		}
		return newString;
	}
	/* removePattern(input, pattern, instances)
	 * remove the the first instances of pattern from input
	 * 
	 * @param instances - number of patterns to remove
	 * @note - does not return any indication if there are less instances in the input than specified
	 */
	public String removePattern(String input, String pattern, int instances){
		if(pattern.length() > input.length()){
			System.out.println("removePattern Error::pattern is longer than input! Returning input unchanged.");
			return input;
		}
		else if(instances <= 0){
			System.out.println("removePattern Error::number specified for instances is invalid! Returning input unchanged.");
			return input;
		}
		
		ArrayList<Integer> matches = KMPMatcher(input, pattern);
		String newString = "";
		int index = 0;
		int arrayIndex = 0;
		int instancesRemoved = 0;
		while(index < input.length() && instancesRemoved < instances){
			if(index != matches.get(arrayIndex))
				newString += input.charAt(index++);
			else{
				index += pattern.length();
				arrayIndex++;
				instancesRemoved++;
				if(arrayIndex == matches.size()){
					newString += input.substring(index);
					break;
				}
			}
		}
		return newString;	
	}
	/* removePattern(after, input, pattern)
	 * remove pattern following the "after"th instance
	 * 
	 * @param after - begin removing pattern after seeing this many instances 
	 */
	public String removePattern(int after, String input, String pattern){
		if(pattern.length() > input.length()){
			System.out.println("removePattern Error::pattern is longer than input! Returning input unchanged.");
			return input;
		}
		else if(after < 0){
			System.out.println("removePattern Error::number specified for after is invalid! Returning input unchanged.");
			return input;
		}
		
		ArrayList<Integer> matches = KMPMatcher(input, pattern);
		String newString = "";
		int index = 0;
		int arrayIndex = 0;
		int instancesPassed = 0;
		while(index < input.length()){
			if(index != matches.get(arrayIndex))
				newString += input.charAt(index++);
			else if(instancesPassed < after){
				newString += input.charAt(index++);
				instancesPassed++;
			}
			else{
				index += pattern.length();
				arrayIndex++;
				if(arrayIndex == matches.size()){
					newString += input.substring(index);
					break;
				}
			}
		}
		return newString;	
	}
	/* removePattern(after, input, pattern, instances
	 * remove the next x instances of patter after a set amount of instances
	 * 
	 * @param after - number of instances to ignore
	 * @param instances - number of instances to delete
	 */
	public String removePattern(int after, String input, String pattern, int instances){
		if(pattern.length() > input.length()){
			System.out.println("removePattern Error::pattern is longer than input! Returning input unchanged.");
			return input;
		}
		else if(after < 0 || instances <= 0){
			System.out.println("removePattern Error::number specified for after or instances is invalid! Returning input unchanged.");
			return input;
		}
		
		ArrayList<Integer> matches = KMPMatcher(input, pattern);
		String newString = "";
		int index = 0;
		int arrayIndex = 0;
		int instancesPassed = 0;
		int instancesRemoved = 0;
		while(index < input.length() && instancesRemoved < instances){
			if(index != matches.get(arrayIndex))
				newString += input.charAt(index++);
			else if(instancesPassed < after){
				newString += input.charAt(index++);
				instancesPassed++;
			}
			else{
				index += pattern.length();
				arrayIndex++;
				instancesRemoved++;
				if(arrayIndex == matches.size()){
					newString += input.substring(index);
					break;
				}
			}
		}
		return newString;			
	}
	
	
	/* KMP Matcher
	* Performs string matching algorithm as defined in CLRS p.1005
	* 
	* @param input - original string
	* @param pattern - pattern to be found in original string
	* @return arrayList of all matches with given offsets
	*/
	public ArrayList<Integer> KMPMatcher(String input, String pattern){
		int n = input.length();
		int m = pattern.length();
		ArrayList<Integer> matches = new ArrayList<Integer>();
		int[] PI;
		PI = ComputePrefixFunction(pattern);
		int q = -1;
		for(int ii = 0; ii< n; ii++){
			while(q > -1 && pattern.charAt(q+1) != input.charAt(ii))
				q = PI[q];
			if(pattern.charAt(q+1) == input.charAt(ii))
				q++;
			if(q == (m-1)){
				matches.add(ii - (m-1));
				q = PI[q];
			}
		}
		
		return matches;
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
