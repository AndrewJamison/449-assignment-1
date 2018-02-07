import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * temporary
 *
 */

public class Constraint {
	
    public final static int FORCED_PARTIAL_ASSIGNMENT = 0;
    public final static int FORBIDDEN_MACHINE = 1;
    public final static int TOO_NEAR_TASKS = 2;
	public final static int MACHINE_PENALTIES = 3;
    public final static int TOO_NEAR_PENALTIES = 4;
    
    private int type;
    
    private ArrayList<Object> forced; // {Integer, Character, Integer, Character ...}
    private ArrayList<Object> forbidden; 
    private ArrayList<Character> tooNearTasks;
    private int[][] machinePenalties;
    private ArrayList<Object> tooNearPenalties; // {Character, Character, Integer, Character, Character, Integer ...}
  
    
    public Constraint(int constraintType) {
		this.type = constraintType;
		switch (constraintType) {
		case FORCED_PARTIAL_ASSIGNMENT:
			this.forced = new ArrayList<>();
			break;
		case FORBIDDEN_MACHINE:
			this.forbidden = new ArrayList<>();
			break;
		case TOO_NEAR_TASKS:
			this.tooNearTasks = new ArrayList<>();
			break;
		case MACHINE_PENALTIES:
			this.machinePenalties = new int[8][8];
			break;
		default:
			this.tooNearPenalties = new ArrayList<>();
			break;
		}
	}
    
    public boolean isForced(int machine, char task) {
    	Integer intObj = new Integer(machine);
    	Character charObj = new Character(task);
    	for (int i=0; i<forced.size(); i++) {
    		if (forced.get(i) instanceof Integer) {
    			if (intObj.equals((Integer)forced.get(i)) && charObj.equals((Character)forced.get(i+1))) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public void setForcedConstraints(ArrayList<Object> forcedConstraints) {
    	forced = forcedConstraints;
    }
    
    public ArrayList<Object> getForcedConstraints() {
    	return new ArrayList<>(forced);
    }
    
    public void setForbiddenConstraints(ArrayList<Object> forbiddenConstraints) {
    	forbidden = forbiddenConstraints;
    }
    
    public ArrayList<Object> getForbiddenConstraints() {
    	return  new ArrayList<>(forbidden);
    }
    
    public void setTooNearTasks(ArrayList<Character> tooNearTasks) {
    	this.tooNearTasks = tooNearTasks;
    }
    
    public ArrayList<Character> getTooNearTasks() {
    	return new ArrayList<>(tooNearTasks);
    }

    public void setMachinePenalties(int[][] ar) {
    	machinePenalties = ar;
    }
    
    public int[][] getMachinePenalties() {
    	return machinePenalties;
    }
    
    public void setTooNearPenalties(ArrayList<Object> tooNearPenalties) {
    	this.tooNearPenalties = tooNearPenalties;
    }
    
    public ArrayList<Object> getTooNearPenalties() {
    	return new ArrayList<>(tooNearPenalties);
    }
    
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	// note: for the constraints, need to figure out eg. too near pair (A,B) 8 --> A and 1 --> B
	//(maybe in init solution and search?)
	// tooNearTask neads to be 2d array with true if there is too near else false
	public boolean tooNearH(char pTask, char cTask){
		return tooNearTask[pTask][cTask];
	}
	
	// tooNearPenalties need to be 2d array with penalty value if exist, else 0
	public int tooNearS(char pTask, char cTask) {
		return tooNearPenalties[pTask][cTask];
	}
    
}
