import java.util.ArrayList;

public class Node {
	private Node parent;
	private int lowerBound;
	private ArrayList<Node> children;
	private int machine;
	private char task;
	private char[] history = new char[8]; //all the machine task assignments for penalty sum 
	private boolean open;
	private boolean hasChildren;
	
	Node(Node parent, int machine, char task){
		this.parent = parent;
		this.machine = machine;
		this.task = task;
		this.open = true; //false means closed
		this.hasChildren = false;
		children = new ArrayList<>();
	}
	
	public boolean getHasChildren() {
		return hasChildren;
	}
	
	public void  setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	public boolean getOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBount(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
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

