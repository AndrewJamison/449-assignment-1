import java.util.ArrayList;

public class Constraint {
	
    private ArrayList<char[]> forced; //array list that contains all the forced assignments
    private ArrayList<char[]> forbidden; //array list that contains all the forbidden assignments


	public boolean errors;
	public String errormessage;
    private int[][] penalties; //2D array that contains all penalty values
    private int[][] tooNearPenalties; //2D array that contains penalties for tooNear soft constraints
    private boolean[][] tooNearTask; //2D array that contains boolean values for whether or not a tooNearTask constraint exists

        
    private Data data;	//data object created to receive all input
    
    public Constraint(String filename) {
    	
    	// Get data from the class Data
    	this.data = new Data(filename);
    	if (this.data.errors) {
    		errors = true;
    		errormessage = this.data.errormessage;
    	}
    	
    	this.forced = data.getForced();
    	this.forbidden = data.getForbidden();
    	this.tooNearTask = data.getTooNearTask();
    	this.tooNearPenalties = data.getTooNearPenalties();
    	this.penalties = data.getPenalties();
    	
    	setPenaltySet();
		
	}
    
    private void setPenaltySet() {
    	
    	setForcedConstraints();
    	setForbiddenConstraints();
    	
    }
    
    /**
     * This method applies forced constraints to the penalty array.
     * If there is forced constraint (machine m, task t), 
     * Only element in index (m,t) of penalty array is valid. 
     * ( ,t) and (m, ) are set to null.
     */
    private void setForcedConstraints() {
    	
    	for (char[] c : forced) {
    		
    		int machine = Character.getNumericValue(c[0]) - 1; //gets the numeric value of that character and subtracts 1 because machines are read as 0 to 7
    		int task = c[1] - 65; //-65 to convert the char task into an int in order for it to be used as an index for the penalties 2D array
    		
    		// Penalty of the forced constraint
    		int penalty = penalties[machine][task];
    		
    		// Set elements in (machine, ) to null
    		for (int i = 0; i < penalties[machine].length; i++) {
    			penalties[machine][i] = -1;
    		}
    		
    		// Set elements in ( , task) to null
    		for (int i = 0; i < penalties.length; i++) {
    			penalties[i][task] = -1;
    		}
    		
    		// Set the penalty back 
    		penalties[machine][task] = penalty;
    	}	
    }
    
    /**
     * This method applies forbidden constraints to the penalty array.
     * If there is forbidden constraint (machine m, task t), 
     * an element in index (m,t) is set to null.
     */
    private void setForbiddenConstraints() {
    	
    	for (char[] c : forbidden) {
    		
    		int machine = Character.getNumericValue(c[0]) - 1; //gets the numeric value of that character and subtracts 1 because machines are read as 0 to 7
    		int task = c[1] - 65; //-65 to convert the char task into an int in order for it to be used as an index for the penalties 2D array
    		
    		penalties[machine][task] = -1; //access the index of the given machine and task in the 2D array and set the value to -1 so that it cannot be assigned
    	}
    }

    // tooNearTask neads to be 2d array with true if there is too near else false
    //-65 to convert the chars passed to ints to be used as the indices accessed for the 2D array
    public boolean tooNearH(char pTask, char cTask){
        return tooNearTask[pTask -65][cTask -65];
    }
 	
    // tooNearPenalties need to be 2d array with penalty value if exist, else 0
    //-65 to convert the chars passed to ints to be used as the indices accessed for the 2D array
    public int tooNearS(char pTask, char cTask) {
    	return tooNearPenalties[pTask -65][cTask -65];
    }
    
    public int[][] getPenalties() {
    	return penalties;
    }
    
}
