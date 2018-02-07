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
								if (temp[i].trim().length() == 0) {
									i++;
									continue;
								}
								else if (temp[i].length() != 6) {
									sc.close();
									throw new IOException("Invalid forced partial assignment data.");
								}
								else if (temp[i].charAt(0) == '(' && temp[i].charAt(5) == ')' && temp[i].charAt(2) == ',' && temp[i].charAt(3) == ' ') {
									int machine = Character.getNumericValue((temp[i].charAt(1)));
									int task = temp[i].charAt(4) - 65;
									//System.out.println(machine);
									//System.out.println(task);
									if (machine < 8 && machine > 0 && task < 8 && task > 0 && forcedPartialAssignment[task] == 0) {
										forcedPartialAssignment[task] = machine;
									}
									else {
										sc.close();
										throw new IOException("Invalid Forced Partial Assignment Data.");
									}
								}
								
								else {
									sc.close();
									throw new IOException("Invalid characters in forced partial assignment data.");
								}
								i++;
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
			System.out.println(name);
			for (int i = 0; i < 8; i ++) {
				System.out.println(forcedPartialAssignment[i]);
			}
			
		
		}
		catch (IOException e) {
			e.printStackTrace();
			sc.close();
		}
		
		sc.close();
		
	}
	
	
	public int getPenalty(int Machine, char task_c) {
		int task = task_c - 65;
		return macPenArrInt[Machine][task];
	}
	
	
	
	
}
