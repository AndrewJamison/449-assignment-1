import java.util.ArrayList;

/**
 * 
 * temporary
 *
 */

public class Constraint {
	
    private ArrayList<char[]> forced; 
    private ArrayList<char[]> forbidden; 
    private ArrayList<char[]> tooNearTasks;
    private ArrayList<char[]> tooNearPenalties; 
    private int[][] penalties;
    
    private Data data;
    
    public Constraint() {
    	
    	// Get data from the class Data
    	this.data = new Data();
    	this.forced = data.getForced();
    	this.forbidden = data.getForbidden();
    	this.tooNearTasks = data.getTooNearTasks();
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
    		
    		int machine = Character.getNumericValue(c[0]) - 1;
    		int task = c[1] - 65;
    		
    		// Set elements in (machine, ) to null
    		for (int i = 0; i < penalties[machine].length; i++) {
    			penalties[machine][i] = -1;
    		}
    		
    		// Set elements in ( , task) to null
    		for (int i = 0; i < penalties.length; i++) {
    			penalties[i][task] = -1;
    		}
    	}	
    }
    
    /**
     * This method applies forbidden constraints to the penalty array.
     * If there is forbidden constraint (machine m, task t), 
     * an element in index (m,t) is set to null.
     */
    private void setForbiddenConstraints() {
    	
    	for (char[] c : forbidden) {
    		
    		int machine = Character.getNumericValue(c[0]) - 1;
    		int task = c[1] - 65;
    		
    		penalties[machine][task] = -1;
    	}
    }
    
    public ArrayList<char[]> getTooNearTasks() {
    	return new ArrayList<>(tooNearTasks);
    }
    
    public ArrayList<char[]> getTooNearPenalties() {
    	return new ArrayList<>(tooNearPenalties);
    }
    
    public int[][] getPenalties() {
    	return penalties;
    }
    
}
