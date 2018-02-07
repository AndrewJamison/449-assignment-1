import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Data {
	
	private String fileName;
	public String name;
	public int[][] macPenArrInt = new int[8][8];
	public int[] forcedPartialAssignment = new int[8];
	
	
	//This is the disgusting ass constructor that doesn't even work properly
	
	public Data() {
		Scanner sc = null;
		
		try {
			String eol = System.getProperty("line.separator");
			sc = new Scanner(new FileInputStream("data.txt"));
			sc.useDelimiter(eol);
			Pattern data = null;
			Pattern header = Pattern.compile("Name:");
			Pattern whitespace = Pattern.compile("");
			if (sc.hasNext(header)) {
				sc.nextLine();
				if (sc.hasNextLine()) {
					name = sc.nextLine();	
					sc.useDelimiter("forced partial assignment:");
					if (sc.next().trim().length() == 0) {
						header = Pattern.compile("forced partial assignment:");
						if (sc.nextLine().equalsIgnoreCase("forced partial assignment:")) {
							
							sc.useDelimiter("forbidden machine:");
							String temp[] = sc.next().split(eol);
							int i = 0;
							while (i < temp.length) {
								
							}
							
							
							
							
						}
						else {
							sc.close();
							throw new IOException("Missing \"forced partial assignment\" header.");
						}
					}
					else {
						sc.close();
						throw new IOException("Extra characters in data file.");
					}
				}
				else {
					sc.close();
					throw new IOException("Missing name.");
				}
			}
			else {
				sc.close();
				throw new IOException("Missing \"name\" header.");
			}
		
			
		
		}
		catch (IOException e) {
			sc.close();
		}
		
		sc.close();
		System.out.println(name);
	}
	
	
	public int getPenalty(int Machine, char task_c) {
		int task = task_c - 65;
		return macPenArrInt[Machine][task];
	}
	
	
	
	
}
