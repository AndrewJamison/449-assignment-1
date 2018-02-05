import java.util.ArrayList;

public class Tree {
//		Constraint constraint;
		Node rootNode;
		ArrayList<Node> finalSol;
		static int currentLowerBound;
		
	public Tree() {
//		this.constraint = new Constraint();
		this.rootNode = new Node(null, -1, ' ');
		this.finalSol = new ArrayList<>(); 
	}

	public static void main(String[] args) {
		Tree tree = new Tree();
		currentLowerBound = tree.initSolution();
		search();
	}
	
	// no constraints yet
	public int initSolution() {
		Node tempNode = rootNode;
		calcLowerBound(tempNode);
		for (int i=0;i<8;i++) {
			createChildren(tempNode);
			for (Node childNode : tempNode.getChildren()) {
				calcLowerBound(childNode);
			}
			tempNode = minLowerBound(tempNode.getChildren());
		}
		tempNode.getParent().setOpen(false);
		finalSol = tempNode.getHistory();
		return tempNode.getLowerBound();
	}
	
	//check if open
	// 	if closed go to parent
	// create children --> check if has children already
	public void search() {
		ArrayList<Node> openChildrenNodes = new ArrayList<Node>();
		for (Node childNode : rootNode.getChildren()) {
			if ((childNode.getLowerBound() < currentLowerBound) && childNode.getOpen()) {
				openChildrenNodes.add(childNode);
			}
		}
		Node tempNode;
		Node minChild;
		while (openChildrenNodes.size() != 0) {
			Node tempNode = minLowerBound(openChildrenNodes);
			// create children for node
			if (!tempNode.hasChildren()) {
				createChildren(tempNode);
				for (Node childNode : tempNode.getChildren()) {
					calcLowerBound(childNode);
				}
			}
			// when at the end of tree 
			if (tempNode.getMachine() == 6) {
				Node minChild = minLowerBound(tempNode.getChildren());
				if (currentLowerBound > minChild.getLowerBound()) {
					currentLowerBound = minChild.getLowerBound();
					finalSol = minChild.getHistory();
				}
				tempNode.setOpen(false);
				openChildrenNodes.remove(openChildrenNodes.indexOf(tempNode));
				for (Node childNode : tempNode.getChildren()) {
					if (currentLowerBound < childNode.getLowerBound()) {
						openChildrenNodes.remove(openChildrenNodes.indexOf(childNode));	
					}
				}
			}
			// when at middle of tree
			else {
				for (Node childNode : tempNode.getChildren()) {
					if (currentLowerBound > childNode.getLowerBound()) {
						openChildrenNodes.add(childNode);	
					}
					else {
						childNode.setOpen(false);
					}
				}
			}
			
			
			
			//Check if node has children, if not, generate them and calc LB
			//if current node is node 6, then find best child, find LB for each child
			//update CLB if possible, and close and remove all nodes in openChildrenNodes 
			//with LB > CLB
			//close parent
			
			//else:
			//add all new children with LB < CLB to openChildrenNode
			
			
		}
	}
	
	/* 	
	 * Node[] openChildrenNodes;
	 * 
	 * 		if less than curentLB
	 * 			check if have children
	 * 				if no children --> create children
	 * 					store all children in openChildrenNodes
	 * 					
	 * 				else has children 
	 * 					check if child is closed
	 * 					store all open children in openChildrenNodes 
	 * 		else greater than currentLB
	 * 			close the node 
	 */

	/**
	 * Class minLowerBound returns the child node with minimum lower bound
	 * @author Ga Hyung Kim
	 * @param children An array of children nodes with same parent
	 * @return Node[index] Element of an array of children nodes with minimum lower bound
	 */
 	public Node minLowerBound(ArrayList<Node> children) {
		
		int min = Integer.MAX_VALUE;
		int index = -1;
		for (int i=0; i < children.size(); i++) {
			if (children.get(i).getLowerBound() < min) {
				min = children.get(i).getLowerBound();
				index = i;
			}
		}
		return children.get(index);
 	}
	
	public void calcLowerBound(Node node) {

		Node calcNode = node; //the node that we are calculating lower bound for
		int[][] penalty = constraint.getMachinePenalties(); //gets penalty 2D array from constraint class
		ArrayList<Character> tasksTaken = new ArrayList<>();
		tasksTaken.add(calcNode.getTask());
		char[] history = calcNode.getHistory(); //assuming calcNode machine/task in calcNode history
		int lowerbound = calcNode.getLowerBound();

		//get the sum of the parent nodes machine/task penalty
		char tempTask;
		Node tempNode = node;
		for (int i = calcNode.getMachine(); i>1; i--) {
			tempNode = tempNode.getParent();
			tempTask = tempNode.getTask();
			tasksTaken.add(tempTask); // task set in parent nodes no longer available
			lowerbound += penalty[tempNode.getMachine()][convertInt(tempNode.getTask())]; //task is char so need to convert to int to get penalty (convert method in Node for task)
			history[i] = tempTask;
		}

		// get the sum of the rest of machine/task penalty (lowest penalty possible for each machine)
		// note: need to figure out how to check for/implement the constraints (too near, forbidden, forced)
		double tempPenalty = Double.POSITIVE_INFINITY;
		for (int i = calcNode.getMachine() + 1;i<=8; i++) {
			for (int j = 0;j<=8;i++) {
				if (i,j).forced(){
					break;
				}
				else if (!taskTaken.contains(j) && (i,j).hardTooNear() && !(i,j).forbidden()){ //again tasksTaken has char while j is int
					if tempPenalty >= penalty[i][j] + (i,j).softTooNear(){
						tempPenalty = penalty[i][j]
					}
				}
			lowerbound += penalty[i][j];
			history[i] = j.convertChar();
			}

		calcNode.setLowerBound(lowerbound);
		calcNode.setHistory(history);
		}

	}

	
	/**
	 * createChildren method creates an array of children nodes which come from a parent node
	 * @author Esther Chung
	 * @param parent the parent Node from which the children come
	 */
	public void createChildren(Node parent) {
		// create an array of nodes
		Node[] childrenArray;
		
		// variables needed
		int parentMachine = parent.getMachine(); // get the parent's machine #
		char[] availableTasks = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']; // the available tasks
		char[] takenTasks = parent.getHistory();  // get the history of the tasks that have been taken so far
		
		// Take out the tasks that are already taken from the availableTasks array
		for (int i = 0; i < parentMachine; i++) {
			for (int j = 0; j < availableTasks.length; j++) {
				if (takenTasks[i] == availableTasks[j]) {
					availableTasks[j] = '';
					break; 
				}
			}
		}
		
		// initialize the children nodes; create nodes for only the available tasks
		for (int i = 0; i < availableTasks.length; i++) {
			if (availableTasks[i] != '') {
				childrenArray.add(new Node(parent, parentMachine + 1, availableTasks[i]));
			}
		}
		
		// pass the children array to the parent node
		parent.setChildren(childrenArray);
		parent.setHasChildren(true);
	}

	public int convertInt(char task){
		return task - 65;
	}

	public char convertChar(int task){
		return char(task+65);
	}
}
