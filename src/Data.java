import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class Data {
	
	public boolean errors;
	public String errormessage;
	private String fileName;
	public String name;
	public int[][] machinePenalties = new int[8][8];
	public int[][] forcedPartialAssignment = new int[2][8];
	public boolean[][] tooNearTasks = new boolean[8][8];
	public int[][] tooNearTasksSoft = new int[8][8];
	ArrayList<char[]> forbidden = new ArrayList<char[]>();
	
	//Machines are numbered 1-8, so when accessing arrays use (machine-1) to get index of that machine. 
	//Tasks are a number 0-7, so you can use their value directly for array access.
	
	//This is the disgusting ass constructor that doesn't even work properly
	
	public Data(String filename) {
		Scanner sc = null;
		
		try {
			String eol = System.getProperty("line.separator"); //get line separator because apparently windows and Linux have different ones
			sc = new Scanner(new FileInputStream(filename));
			sc.useDelimiter(eol);
			Pattern data = null;
			Pattern header = Pattern.compile("Name:");
			Pattern whitespace = Pattern.compile("");
			int i;
			//make sure that the name header is there
			if (sc.hasNext(header)) {
				sc.nextLine();	//skip the name header
				if (sc.hasNextLine()) {
					name = sc.nextLine();	//read in the name
					
					
					//read past any extra empty lines
					while (sc.hasNext("")) {
						sc.nextLine();
					}
					
					
					//make sure it has the next header
					if (sc.hasNext("forced partial assignment:")) {
						sc.next("forced partial assignment:");
						
						
						//read all data until the next header
						sc.useDelimiter("forbidden machine:");
						String temp[] = sc.next().split(eol); //split each line into it's own entry in an array
						i = 0;
						while (i < temp.length) {
							if (temp[i].trim().length() == 0) { //if it is an empty line ignore it
								i++;
								continue;
							}
							else if (temp[i].trim().length() != 6) { //if the length of the line isn't 6 then its obviously incorrect data.
								sc.close();
								throw new IOException("Error while parsing input file");
							}
							//make sure the data has the right format. I dont know how regex works obviously
							else if (temp[i].charAt(0) == '(' && temp[i].charAt(5) == ')' && temp[i].charAt(2) == ',' && temp[i].charAt(3) == ' ') {
								int machine = Character.getNumericValue((temp[i].charAt(1))); //read in the machine number
								int task = temp[i].charAt(4) - 65; //read in task letter and convert to int
//								System.out.println(machine);
//								System.out.println(task);
								
								//make sure the machine and tasks numbers are valid
								if (machine <= 8 && machine > 0 && task < 8 && task >= 0 && forcedPartialAssignment[0][task] == 0 && forcedPartialAssignment[1][machine - 1] == 0)  {
									forcedPartialAssignment[0][task] = machine; 
									forcedPartialAssignment[1][machine -1] = task;
								}
								else {
									sc.close();
									throw new IOException("partial assignment error");
								}
							}
							
							else {
								sc.close();
								throw new IOException("invalid machine/task");
							}
							i++;
						}
						
						//get rid of empty lines
						while (sc.hasNext("")) {
							sc.nextLine();
						}
						//make sure the next line is the correct header
						if (sc.nextLine().equalsIgnoreCase("forbidden machine:")) {
							//read all data until the next header
							sc.useDelimiter("too-near tasks:");
							String temp2[] = sc.next().split(eol);
							i = 0;
							while (i < temp2.length) {
								if (temp2[i].trim().length() == 0) {//ignore empty lines
									i++;
									continue;
								}	//if line length isnt 6 then it is incorrect data
								else if (temp2[i].trim().length() != 6) {
									sc.close();
									throw new IOException("Error while parsing input file");
								}
								//make sure the data is the correct format
								else if (temp2[i].charAt(0) == '(' && temp2[i].charAt(5) == ')' && temp2[i].charAt(2) == ',' && temp2[i].charAt(3) == ' ') {
									int machine = Character.getNumericValue((temp2[i].charAt(1)));
									int task = temp2[i].charAt(4) - 65;
//									System.out.println(machine);
//									System.out.println(task);
											if (machine <= 8 && machine > 0 && task < 8 && task >= 0) {
												if (forcedPartialAssignment[0][task] != machine) { //make sure the forcbidden task isnt already forced
														String t = Integer.toString(machine) + (char)(task + 65);
														forbidden.add(t.toCharArray());
												}
												
												else {
													//throw exception because forced and forbidden machines are the same
													sc.close();
													throw new IOException("No valid solution possible!");
												}
												
											}
											else {//invalid input data
												sc.close();
												throw new IOException("invalid machine/task");
											}
								}
								
								else {//invalid input data
									sc.close();
									throw new IOException("invalid machine/task");
								}
								i++;
							}
							//read any extra empty lines
							while (sc.hasNext("")) {
								sc.nextLine();
							}
							//make sure the next header is correct
							if (sc.nextLine().equalsIgnoreCase("too-near tasks:")) {
								sc.useDelimiter("machine penalties:");
								String temp3[] = sc.next().split(eol);
								
								i = 0;		
								while (i < temp3.length) {
									if (temp3[i].trim().length() == 0) {
										i++;
										continue;
									}
									
									else if (temp3[i].length() != 6){
										sc.close();
										throw new IOException("Error while parsing input file");
									}
									else if (temp3[i].charAt(0) == '(' && temp3[i].charAt(5) == ')' && temp3[i].charAt(2) == ',' && temp3[i].charAt(3) == ' ') {
										int task1 = temp3[i].charAt(1) - 65;
										int task2 = temp3[i].charAt(4) - 65;
										if (task1 < 8 && task1 >= 0 && task2 < 8 && task2 >= 0) {
											tooNearTasks[task1][task2] = true;
											
										}
										else {
											sc.close();
											throw new IOException("Invalid machine/task");
											
										}
										
									}
									else {
										sc.close();
										throw new IOException("invalid machine/task");
									}
									i++;
									}
									
								}
								
							else {
								sc.close();
								throw new IOException("Error while parsing input file");
							}
							
							
							while (sc.hasNext("")) {
								sc.nextLine();
							}
							if (sc.nextLine().equalsIgnoreCase("machine penalties:")) {
								sc.useDelimiter("too-near penalties:");
								String temp4[] = sc.next().split(eol);
								i = 0;
								int row = 0;
								String machinePenaltyString[][] = new String[8][8];
								while (i < temp4.length) {
									if (temp4[i].trim().equals("") || temp4[i].trim().equals(eol)) {
										i++;
										continue;
									}
									else if (temp4[i].trim().split(" ").length == 8) {
										machinePenaltyString[row] = temp4[i].trim().split(" ");
										for (int k = 0; k < 8; k++) {
											try {
//												System.out.println(row);
//												System.out.println(k);
												machinePenalties[row][k] = Integer.parseInt(machinePenaltyString[row][k]);
											}
											catch (NumberFormatException e){
												sc.close();
												throw new IOException("machine penalty error");
												
											}
											
										}
										row ++;
									}
									else {
										sc.close();
										throw new IOException("machine penalty error");
									}
									i ++;
								}
							}
							//CONTINUE FROM HERE
							
							while (sc.hasNext("")) {
								sc.nextLine();
							}
							if (sc.nextLine().equalsIgnoreCase("too-near penalties:")) {
								while (sc.hasNextLine()) {
									String temp5 = sc.nextLine();
									if (temp5.trim().equals("") || temp5.trim().equals(eol)) {
										continue;
									}
									else if (temp5.charAt(0) == '(' && temp5.charAt(2) == ',' && temp5.charAt(temp5.length() - 1) == ')' && temp5.charAt(5) == ',' && temp5.charAt(6) == ' '){
										int task1 = temp5.charAt(1) - 65;
										int task2 = temp5.charAt(4) - 65;
										int penalty = 0;
										try {
											
											penalty = Integer.parseInt(temp5.substring(7,temp5.length() - 1));
										}
										catch (NumberFormatException e) {
											sc.close();
											throw new IOException("invalid penalty");
										}
										if (task1 >= 0 && task1 < 8 && task2 >= 0 & task2 < 8) {
											tooNearTasksSoft[task1][task2] = penalty;
										}
										else {
											sc.close();
											throw new IOException("invalid penalty");
										}
										
									}
									else {
										sc.close();
										throw new IOException("Invalid soft too near task data.");
									}
								
								}
								
								
							}
							else {
								sc.close();
								throw new IOException("Error while parsing input file");
							}
						
							
							
						}
						else {
							sc.close();
							throw new IOException("Error while parsing input file");
						}
						
						
						
						
						
						
					}
					else {
						sc.close();
						throw new IOException("Error while parsing input file");
					}
					}
				else {
					sc.close();
					throw new IOException("Error while parsing input file");
					}
				}
				else {
					sc.close();
					throw new IOException("Error while parsing input file");
				}

		}
		catch (IOException e) {
			errors = true;
			errormessage = e.getMessage();
		}
		catch (NoSuchElementException e) {
			errors = true;
			errormessage = "Error while parsing input file";
		}
		
		
		sc.close();
		
	}
	
	
	public ArrayList<char[]> getForced(){
		ArrayList<char[]> forced = new ArrayList<char[]>();
		for (int i = 0; i < 8; i ++) {
			if (forcedPartialAssignment[0][i] != 0) {
				String temp =  Integer.toString(forcedPartialAssignment[0][i]) + (char)(i + 65) ;
				//System.out.println(temp);
				forced.add(temp.toCharArray());
			}
			
		}
	return forced;	
	}
	public ArrayList<char[]> getForbidden(){

		return forbidden;
	}
	
	public boolean[][] getTooNearTask() {
		return tooNearTasks;
	}
	
	public int[][] getTooNearPenalties(){
		return tooNearTasksSoft;
	}
	
	public int[][] getPenalties(){
		return machinePenalties;
	}
	
	
	
	
}
