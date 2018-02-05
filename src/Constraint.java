import java.util.ArrayList;

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
    
    private ArrayList<Object> forced;
    private ArrayList<Object> forbidden;
    private ArrayList<Character> tooNearTask;
    private int[][] machinePenalties;
    private ArrayList<Object> tooNearPenalties;
    
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
			this.tooNearTask = new ArrayList<>();
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
    	return (Integer)forced.get(0) == machine && ((Character)forced.get(1)).equals(task);
    }
    
    public void setForcedConstraint(int machine, char task) {
    	forced.add(machine);
    	forced.add(task);
    }
    
    public ArrayList<Object> getForcedConstraint() {
    	ArrayList<Object> forcedCopy = new ArrayList<>();
    	forcedCopy.add(forced.get(0));
    	forcedCopy.add(forced.get(1));
    	return forcedCopy;
    }
    
    public void setForbiddenConstraint(int machine, char task) {
    	forbidden.add(machine);
    	forbidden.add(task);
    }
    
    public ArrayList<Object> getForbiddenConstraint() {
    	ArrayList<Object> forbiddenCopy = new ArrayList<>();
    	forbiddenCopy.add(forbidden.get(0));
    	forbiddenCopy.add(forbidden.get(1));
    	return forbiddenCopy;
    }
    
    public void setTooNearTask(char task1, char task2) {
    	tooNearTask.add(task1);
    	tooNearTask.add(task2);
    }
    
    public ArrayList<Character> getTooNearTask() {
    	ArrayList<Character> tooNearTaskCopy = new ArrayList<>();
    	tooNearTaskCopy.add(tooNearTask.get(0));
    	tooNearTaskCopy.add(tooNearTask.get(1));
    	return tooNearTaskCopy;
    }

    public void setMachinePenalties(int[][] ar) {
    	machinePenalties = ar;
    }
    
    public int[][] getMachinePenalties() {
    	return machinePenalties;
    }
    
    public void setTooNearPenalties(char task1, char task2, int penalty) {
    	tooNearPenalties.add(task1);
    	tooNearPenalties.add(task2);
    	tooNearPenalties.add(penalty);
    }
    
    public ArrayList<Object> getTooNearPenalties() {
    	ArrayList<Object> tooNearPenaltiesCopy = new ArrayList<>();
    	tooNearPenaltiesCopy.add(tooNearPenalties.get(0));
    	tooNearPenaltiesCopy.add(tooNearPenalties.get(1));
    	tooNearPenaltiesCopy.add(tooNearPenalties.get(2));
    	return tooNearPenaltiesCopy;
    }
    
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
    
}
