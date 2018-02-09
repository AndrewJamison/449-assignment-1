import java.util.ArrayList;

/**
 * 
 * Implements individual Node on the Tree generated through branch and bound algorithm
 *
 */

public class Node {
	
	private Node parent;
	private int lowerBound;
	private ArrayList<Node> children; //all the children of that parent node will be stored in this array list
	private int machine;
	private char task;
	private ArrayList<Character> history = new ArrayList<Character>(); //all the machine task assignments for penalty sum 
	private boolean open; //boolean to check if the node hasn't been closed
	private boolean hasChildren; //boolean to check if children have been created for that node
	
	/**
	 * Constructor 
	 * 
	 * @param parent Parent node 
	 * @param machine machine assigned
	 * @param task task assigned
	 */
	Node(Node parent, int machine, char task){
		this.parent = parent;
		this.machine = machine;
		this.task = task;
		this.open = true; //false means closed
		this.hasChildren = false;
		children = new ArrayList<>();
		this.lowerBound = 0;
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

	public void setLowerBound(int lowerBound) {
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

	public ArrayList<Character> getHistory() {
		return new ArrayList<>(history);
	}

	public void setHistory(ArrayList<Character> history) {
		this.history = history;
	}
	
	
}

