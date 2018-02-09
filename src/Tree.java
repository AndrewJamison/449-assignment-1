import java.util.ArrayList;

public class Tree {
		static Constraint constraint;
		static Node rootNode;
		static ArrayList<Character> finalSol;
		static int currentLowerBound;
		
	public Tree(String filename) {
		this.constraint = new Constraint(filename);
		this.rootNode = new Node(null, -1, ' ');
		this.finalSol = new ArrayList<>(); 
	}

	public static void main(String[] args) {
		Tree tree = new Tree("data.txt");
		currentLowerBound = tree.initSolution();
		search();
		System.out.println(finalSol.toString());
		System.out.println(currentLowerBound);
	}
	
	/**
	 * This class searches for the possible viable solution by finding 
	 * the valid child node with minimum lower bound. 
	 * @author Ga Hyung Kim
	 * 
	 * @return tempNode.getLowerBound() Current best lower bound
	 */
	public int initSolution() {
		
		// Initial node = root node
		Node tempNode = rootNode;
		calcLowerBound(tempNode);
		
		// Branching down to the leaf node
		while (true) {
			
			// Create children of tempNode
			createChildren(tempNode);
			
			// If there are no children for the tempNode, close the current
			// node and move back to the parent node and find another child
			// Else calculate the lower bounds of children node and set
			// next node as child node with minimum lower bound
			if (tempNode.getChildren().size() == 0) {
				tempNode.setOpen(false);
				tempNode = tempNode.getParent();
			} else {
				for (Node childNode : tempNode.getChildren()) {
					calcLowerBound(childNode);
				}
				tempNode = minLowerBound(tempNode.getChildren());
			}
			
			// When it reaches at the end node, exit loop
			if (tempNode.getMachine() == 7) break;
			
		}

		tempNode.getParent().setOpen(false);
		finalSol = tempNode.getHistory();
		
		return tempNode.getLowerBound();
	}
	
	/**
	 * The search() function searches for the combination of tasks that has the lowest sum of the penalties.
	 * @author Esther Kim, Esther Chung
	 */
	public static void search() {
		// Now get the initial list of tasks that are worthwhile to go down;
		// these are the ones that have smaller LB than the current LB.
		ArrayList<Node> openChildrenNodes = new ArrayList<Node>();
		for (Node childNode : rootNode.getChildren()) {
			if ((childNode.getLowerBound() < currentLowerBound) && childNode.getOpen()) {
				openChildrenNodes.add(childNode);
			}
			else {
				childNode.setOpen(false);
			}
		}
		
		// Now traverse around the tree
		// start off with the node that has the lowest LB
		// out of the list of the nodes that are worthwhile to go down.
		Node tempNode;
		Node minChild;
		while (!openChildrenNodes.isEmpty()) {
			tempNode = minLowerBound(openChildrenNodes);
			// create children for the current node (tempNode)
			// first check if tempNode already has the children created
			if (!tempNode.getHasChildren()) {
				// if there is no children, then create children 
				createChildren(tempNode);
				for (Node childNode : tempNode.getChildren()) {
					calcLowerBound(childNode);
				}
				
			}
		
			if (tempNode.getMachine() == 6 && !tempNode.getChildren().isEmpty()) {
				minChild = minLowerBound(tempNode.getChildren());
				
				if (currentLowerBound > minChild.getLowerBound()) {
					currentLowerBound = minChild.getLowerBound();
					finalSol = minChild.getHistory();
				}
				
			}
			else {
				
				// if there are children that are available,
				// add these nodes to the openChildrenNodes array if they have
				// lower bounds that are smaller than the currentLowerBound.
				for (Node childNode : tempNode.getChildren()) {
					if ((childNode.getLowerBound() < currentLowerBound) && childNode.getOpen()) {
						openChildrenNodes.add(childNode);
						System.out.println(childNode.getMachine());
					}
				}
				
				// if we are at the last node, select the child node that has the
				// smallest lower bound. Compare that lower bound with the current
				// lower bound. If it is smaller than the current lower bound, 
				// update the current lower bound, and update the final solution.
		
				
				// remove the current tempNode and move to the best node
				
			}
			tempNode.setOpen(false);
			openChildrenNodes.remove(openChildrenNodes.indexOf(tempNode));
		}
	}


	/**
	 * Class minLowerBound returns the child node with minimum lower bound
	 * @author Ga Hyung Kim
	 * @param children An array of children nodes with same parent
	 * @return Node[index] Element of an array of children nodes with minimum lower bound
	 */
 	public static Node minLowerBound(ArrayList<Node> children) {
		
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
	
 	/**
 	 * This method calculates the total lower bound for every node up to and including the current node and sets the passes in nodes lower bound to the sum
 	 * @param node the node to calculate the lower bound for 
 	 */
	public static void calcLowerBound(Node node) {
		Node calcNode = node; //the node whose LB is being calculated
		int[][] penalty = constraint.getPenalties(); //uses 2D penalty array from the constraint class
		ArrayList<Character> history = calcNode.getHistory(); //list of the tasks assigned prior to this current node and including this current nodes task
		int lowerbound = calcNode.getLowerBound(); //initialize lowerbound to current nodes set lowerbound (zero or if there is a soft constraint it would take that penalty to start)
		
		//calculate the sum of the penalties for the tasks assigned in the given history ArrayList
		char tempTask;
		int tempMachine;
		for (int i = 0; i < history.size(); i++) {
			tempMachine = i;
			tempTask = history.get(i);
			lowerbound += penalty[tempMachine][convertInt(tempTask)];
		}
		
		//set the lowerbound of the node
		calcNode.setLowerBound(lowerbound);
	}
	
	/**
	 * createChildren method creates an array of children nodes which come from a parent node
	 * @author Esther Chung
	 * @param parent the parent Node from which the children come
	 */
	public static void createChildren(Node parent) {
		// getting penalty array from constraint class
				int[][] penalty = constraint.getPenalties();
				// create an array of nodes
				ArrayList<Node> childrenArray = new ArrayList<Node>();
				
				// variables needed
				int parentMachine = parent.getMachine(); // get the parent's machine #
				char[] availableTasks = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'}; // the available tasks
				ArrayList<Character> takenTasks = parent.getHistory();  // get the history of the tasks that have been taken so far
				
				// Take out the tasks that are already taken from the availableTasks array
				for (int i = 0; i <= parentMachine; i++) {
					for (int j = 0; j < availableTasks.length; j++) {
						if (takenTasks.get(i) == availableTasks[j]) {
							availableTasks[j] = Character.MIN_VALUE;
							break; 
						}
					}
				}
				// if root node --> cannot check too near constraints
				if (parent.getMachine() == -1) {
					for (int i = 0; i < availableTasks.length; i++) {
						if (availableTasks[i] != Character.MIN_VALUE) { 
							if (penalty[parentMachine + 1][convertInt(availableTasks[i])] != -1) {

								Node childNode = new Node(parent, parentMachine + 1, availableTasks[i]);
								ArrayList<Character> childHistory = parent.getHistory();
								childHistory.add(availableTasks[i]);
								childNode.setHistory(childHistory);
								childrenArray.add(childNode);
							}
						}
					}
				}
				else if (parent.getMachine() == 6) {
					for (int i = 0; i < availableTasks.length; i++) {
						if (availableTasks[i] != Character.MIN_VALUE) { 
							if (penalty[parentMachine + 1][convertInt(availableTasks[i])] != -1 //checking if penalty spot is null
							&& !constraint.tooNearH(parent.getTask(), availableTasks[i])
							&& !constraint.tooNearH(availableTasks[i], parent.getHistory().get(0))) { 		//checking if there is too near hard constraint
							
								Node childNode = new Node(parent, parentMachine + 1, availableTasks[i]);
								int tnsPenalty = constraint.tooNearS(parent.getTask(), availableTasks[i]); //if there is too near soft constraint return penalty if not return 0
								tnsPenalty = tnsPenalty + constraint.tooNearS(availableTasks[i], parent.getHistory().get(0));
								childNode.setLowerBound(tnsPenalty);
								ArrayList<Character> childHistory = parent.getHistory();
								childHistory.add(availableTasks[i]);
								childNode.setHistory(childHistory);
								childrenArray.add(childNode);
							}
						}
					}
				}
				else {
					// initialize the children nodes; create nodes for only the available tasks
					for (int i = 0; i < availableTasks.length; i++) {
						if (availableTasks[i] != Character.MIN_VALUE) { 
							if (penalty[parentMachine + 1][convertInt(availableTasks[i])] != -1 //checking if penalty spot is null
							&& !constraint.tooNearH(parent.getTask(), availableTasks[i])) { 		//checking if there is too near hard constraint
							
								Node childNode = new Node(parent, parentMachine + 1, availableTasks[i]);
								int tnsPenalty = constraint.tooNearS(parent.getTask(), availableTasks[i]); //if there is too near soft constraint return penalty if not return 0
								childNode.setLowerBound(tnsPenalty);
								ArrayList<Character> childHistory = parent.getHistory();
								childHistory.add(availableTasks[i]);
								childNode.setHistory(childHistory);
								childrenArray.add(childNode);
							}
						}
					}
				}
				
				// pass the children array to the parent node
				parent.setChildren(childrenArray);
				parent.setHasChildren(true);
	}

	public static int convertInt(char task){
		return task - 65;
	}

	public char convertChar(int task){
		return (char) (task+65);
	}
}
