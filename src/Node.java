import java.util.ArrayList;

public class Node {
	private Node parent;
	private int lowerBound;
	private ArrayList<Node> children;
	private int machine;
	private char task;
	private char[] history; //all the machine task assignments for penalty sum 
	private boolean open;
	private boolean hasChildren;
	
	Node(Node parent, int machine, char task){
		this.parent = parent;
		this.children = new ArrayList<Node>();
		this.machine = machine;
		this.task = task;
		this.history = new char[8];
		this.open = true; //false means closed
		this.hasChildren = false;
	}
	
	public boolean getHasChildren() {
		return hasChildren;
	}
	
	public setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	public boolean getOpen() {
		return open;
	}
	
	public setOpen(boolean open) {
		this.open = open;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getPenaltySum() {
		return penaltySum;
	}

	public void setPenaltySum(int penaltySum) {
		this.penaltySum = penaltySum;
	}

	public Node[] getChildren() {
		return children;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public int getMachine() {
		return machine;
	}

	public void setMachine(int machine) {
		this.machine = machine;
	}

	public char getTask() {
		return task;
	}

	public void setTask(char task) {
		this.task = task;
	}

	public char[] getHistory() {
		return history;
	}

	public void setHistory(char[] history) {
		this.history = history;
	}
	
	
}
