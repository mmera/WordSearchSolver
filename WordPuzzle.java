package assignment;
import java.io.*;
import java.util.*;


public class WordPuzzle{

	private List<String> potentialWords = new ArrayList<>();
	private HashMap<String, Integer> actualWords = new HashMap<>();
	private List<String[]> puzzleBoard = new ArrayList<String[]>();
	private HashSet<String> dictionary = new HashSet<>();

	private String puzzleFile;
	private String dictFile;
	private String outputFile;

	private int puzzleSize = 0;

	public WordPuzzle(String puzzleFile, String dictFile,String outputFile){
		this.puzzleFile = puzzleFile;
		this.dictFile   = dictFile;
		this.outputFile = outputFile;
		readPuzzle();
		readDict();
	}

	/************************************************************************
	 * READ AND WRITE METHODS												|
	 ************************************************************************/

	/**Method that parses a CSV file into a 2D matrix
	 * @return Returns a 2D matrix in the form of a List of String[]
	 */
	private List<String[]> readPuzzle(){

		BufferedReader br = null;

		try{
			br = new BufferedReader(new FileReader(new File(puzzleFile)));
			String line = "";
			String splitBy = ",";

			while((line = br.readLine()) != null){
				String[] row = line.split(splitBy);
				puzzleSize   = row.length;
				puzzleBoard.add(row);
			}

		}catch(IOException e){
			System.err.println("An error occured with the following file: " + puzzleFile);
			System.exit(1);

		}finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Error when closing Reader!");
				e.printStackTrace();
			}
		}
		return puzzleBoard;
	}
	/**
	 * Method that parses a CSV file into a HashSet 
	 * @return HashSet used to store the words in the dictionary given 
	 */
	private HashSet<String> readDict(){

		BufferedReader br = null;

		try{
			br = new BufferedReader(new FileReader(new File(dictFile)));
			String word = "";

			while((word = br.readLine()) != null){
				dictionary.add(word.toLowerCase());
			}

		}catch(IOException e){
			System.err.println("An error occured with the following file: " + dictFile);
			System.exit(1);

		}finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Error when closing Reader!");
				e.printStackTrace();
			}
		}
		return dictionary;

	}
	/**
	 * A method that takes the entries in a HashMap and writes 
	 * them to a CSV file containing the solution to the puzzle in the form of:
	 * 
	 * word, count
	 * earth,1
	 * ...
	 * scrabble,2
	 */
	private void writeOutputCSV(){
		String comma  = ",";
		String nLine  = "\n";
		String header = "word,count";

		FileWriter fw = null;

		try{
			fw = new FileWriter(outputFile);
			fw.append(header);
			fw.append(nLine);

			for(Map.Entry<String, Integer> entry:actualWords.entrySet()){
				fw.append(entry.getKey());
				fw.append(comma);
				fw.append(entry.getValue().toString());
				fw.append(nLine);
			}

		}catch(Exception e){
			System.err.println("Error writing to file: "+outputFile);
			e.printStackTrace();

		}finally {
			try {
				fw.close();
			} catch (IOException e) {
				System.err.println("Error when closing FileWriter!");
				e.printStackTrace();
			}
		}

	}

	/************************************************************************
	 * SEARCH METHODS												        |
	 ************************************************************************/

	/**
	 * The following methods index into the 2D matrix using for loops in order to build
	 * a potential word. 
	 */

	private void findWordsInRows(){
		StringBuilder b = new StringBuilder();
		String word     = null;
		String reverse  = null;

		for(int row=0; row<puzzleSize; ++row){
			for(int col=0; col<puzzleSize; ++col){
				
				word = b.append(puzzleBoard.get(row)[col]).toString();
				
			}
			parseString(word);
			b.setLength(0);
		}
	}

	private void findWordsInColumns(){
		StringBuilder b = new StringBuilder();
		String word     = null;

		for(int col=0; col<puzzleSize; ++col){
			for(int row=0; row<puzzleSize; ++row){
				
				word = b.append(puzzleBoard.get(row)[col]).toString();
				
			}
			parseString(word);
			b.setLength(0);
		}
	}

	private void findWordsInDiagonals(){
		StringBuilder b = new StringBuilder();
		String word     = null;

		for(int k=puzzleSize-1; k>0; k--){
			for(int col=0, row=k; row<puzzleSize-1; row++, col++){

				word = b.append(puzzleBoard.get(row)[col]).toString();

				if(word.length()==puzzleSize){
					parseString(word);

				}
			}
			
			b.setLength(0);
		}

		for(int k=0; k<puzzleSize-1; k++){
			for(int row=0, col=k; col<=puzzleSize-1; row++, col++){
				
				word = b.append(puzzleBoard.get(row)[col]).toString();
				
				if(word.length()==puzzleSize){
					parseString(word);
		
				}
			}
			b.setLength(0);
		}

		for( int k=0; k<puzzleSize; k++) {
			for( int col=0; col<=k; col++) {
				
				int row = k - col;
				word = b.append(puzzleBoard.get(row)[col]).toString();
				
				if(word.length()==puzzleSize){
					parseString(word);
				}
			}
			b.setLength(0);
		}

		for(int k=puzzleSize-2; k>=0; k--){
			for(int row=0; row<=k; row++){
				
				int col = k - row;
				word = b.append(puzzleBoard.get(puzzleSize-row-1)[puzzleSize-col-1]).toString();
				
				if(word.length()==puzzleSize){
					parseString(word);
				}
			}
			b.setLength(0);
		}

	}

	/**
	 * A method that looks at the list of possible words and see if they contain any words in the dictionary.
	 * If it finds a matching word then it checks the hashMap that contains the actual words. If the hashMap already
	 * has that word accounted for then it simply increases the count for that word. If it's a new word, then it creates a 
	 * new entry in the map with the new word as the key and the respective value set to 1. 
	 *  
	 */

	private void findWordsInList(){
		for(String word:potentialWords){
			if(dictionary.contains(word)){
				if(actualWords.containsKey(word)){
					actualWords.put(word, actualWords.get(word)+1);
				}
				else{
					actualWords.put(word, 1);
				}
			}
		}
	}
	
	

	/************************************************************************
	 * DRIVER METHODS												        |
	 ************************************************************************/

	/**
	 * The following methods simply call other methods, 
	 * in other words they get the ball rolling. 
	 */

	public void solvePuzzle(){
		findWordsInBoard();
		findWordsInList();
		writeOutputCSV();
	}

	private void findWordsInBoard(){
		findWordsInRows();
		findWordsInColumns();
		findWordsInDiagonals();
	}

	/************************************************************************
	 * HELPER METHODS												|
	 ************************************************************************/

	/**
	 * Simple method to reverse a string.
	 * 
	 * @param The string you want to reverse
	 * @return The reversed string
	 */

	private String reverseString(String word){
		String reverse = new StringBuilder(word).reverse().toString();
		return reverse;
	}
	
	/**
	 * A method that parses a completed string built from a row, column or diagonal, using the substring method.
	 * If the substring is larger than 3 (smallest length of the average word) then it adds it to the list of potential 
	 * words.The reversed version of the word also gets added to the list. 
	 * 
	 * @param A complete line, i.e. a row, column, or diagonal
	 */
	private void parseString(String line){
		int length = line.length();
		for(int i=0; i<length; i++){
			for(int j=length; j>=i+3; j--){
				potentialWords.add(line.substring(i, j));
				potentialWords.add(reverseString(line.substring(i, j)));
			}
		}
	}

}
