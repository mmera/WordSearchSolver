package assignment;
public class Main {
	public static void main(String[] args){
		if(args.length < 3){
			System.err.println("Must input 3 files in the following order: puzzle, dictionary, output");
			System.exit(1);
		}
		
		for(int i=0; i<args.length;i++){
			if(checkExtension(args[i])==false){
				System.err.println("All files must be of CSV format");
				System.exit(1);
			}
		}
		
		WordPuzzle search = new WordPuzzle(args[0], args[1], args[2]);
		search.solvePuzzle();
	}
	public static boolean checkExtension(String file){
		String extension = file.substring(file.lastIndexOf(".") + 1, file.length());
		String csv = "csv";
		if(!extension.equalsIgnoreCase(csv)){
			return false;
		}
		return true;
	}
}
