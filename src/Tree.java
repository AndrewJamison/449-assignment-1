import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Tree {

	private Constraint constraint; // the Constraint object that has all the constraints
	private Node rootNode; // the rootNode of the tree; so that we can keep track of the other nodes
	private ArrayList<Character> finalSol; // the path to the final solution that we have found so far
	private int currentLowerBound; // the lower bound of the final solution that we have found so far
	private boolean errors;
	private String errormessage;
	
	/**
	 * Constructor for the Tree class
	 * @param filename the filename of the input file
	 */

	public Tree(String filename) {
		this.constraint = new Constraint(filename);
		if (this.constraint.errors) {
			errors = this.constraint.errors;
			errormessage = this.constraint.errormessage;
		}
		this.rootNode = new Node(null, -1, ' ');
		this.finalSol = new ArrayList<>(); 
	}
	
	public Tree(String filename, ArrayList<Character> finalSol, int currentLowerBound) {
		this.constraint = new Constraint(filename);
		if (this.constraint.errors) {
			errors = this.constraint.errors;
			errormessage = this.constraint.errormessage;
		}
		this.rootNode = new Node(null, -1, ' ');
		this.finalSol = finalSol;
		this.currentLowerBound = currentLowerBound;
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
			// Else, calculate the lower bounds of children node and set
			// next node as the child node with minimum lower bound
			if (tempNode.getChildren().size() == 0) {
				tempNode.setOpen(false);
				tempNode = tempNode.getParent();
			} 
			else {
				// Even if there are children, we still need to check
				// whether all the children are closed or not
				int numClosedChildren = 0;
				
				for (Node childNode : tempNode.getChildren()) {
					// calculate the lower bound for just the ones
					// that are open
					if (childNode.getOpen()) {
						calcLowerBound(childNode);
					}
					else {
						numClosedChildren = numClosedChildren + 1;
					}
				}
				
				if (numClosedChildren == tempNode.getChildren().size()) {
					// all children nodes are closed, so close this node and go to the parent node
					tempNode.setOpen(false);
					tempNode = tempNode.getParent();
				}
				else {
					// if there are still open children to be considered, set the tempNode to the node that
					// has the minimum lower bound
					tempNode = minLowerBound(tempNode.getChildren());
				}
			}
			
			// When it reaches the end node (last level), exit loop
			if (tempNode.getMachine() == 7) {
				break;
			}
			
			// If we haven't moved anywhere from the rootNode,
			// then it means that we couldn't create children.
			// Exit by returning -1.
			if (tempNode.getMachine() == -1) {
				return -1;
			}
		}

		// close the parent node because we've already checked it
		tempNode.getParent().setOpen(false);
		
		// Set the final solution to the history of the last node
		finalSol = tempNode.getHistory();
		
		// return the lower bound of the last node
		return tempNode.getLowerBound();
		
	}
	
	/**
	 * The search() function searches for the combination of tasks that has the lowest sum of the penalties.
	 * @author Esther Kim, Esther Chung
	 */
	public void search() {
		// Now get the initial list of tasks that are worthwhile to go down;
		// these are the ones that have smaller LB than the current LB.
		ArrayList<Node> openChildrenNodes = new ArrayList<Node>();
		
		// create children for the rootNode
		// Initial node = root node
		createChildren(rootNode);
		
		for (Node childNode : rootNode.getChildren()) {
			calcLowerBound(childNode);
			
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
			// Go to the node that has the smallest lower bound.
			tempNode = minLowerBound(openChildrenNodes);
			
			// create children for the current node (tempNode)
			// first check if tempNode already has the children created
			if (!tempNode.getHasChildren()) {
				// if there is no children, then create children 
				createChildren(tempNode);
				
				// calculate the lower bound for each of the children
				for (Node childNode : tempNode.getChildren()) {
					calcLowerBound(childNode);
				}
			}
		
			// if we are at the second last level and there are children,
			// first find which child has the smallest lower bound.
			// Then set the final solution to the child's lower bound
			// if the child's lower bound is smaller than the current lower bound.
			if (tempNode.getMachine() == 6 && !tempNode.getChildren().isEmpty()) {
				minChild = minLowerBound(tempNode.getChildren());
				
				if (currentLowerBound > minChild.getLowerBound()) {
					currentLowerBound = minChild.getLowerBound();
					finalSol = minChild.getHistory();
				}
				
			}
			else {
				// If we are at levels other than 6
				// or if we do not have any children
				// then execute the following.
				
				// if there are children that are available (we are at levels other than 6),
				// add these nodes to the openChildrenNodes array if they have
				// lower bounds that are smaller than the currentLowerBound.
				// if we do not have any children, the following loop will not execute.
				for (Node childNode : tempNode.getChildren()) {
					if ((childNode.getLowerBound() < currentLowerBound) && childNode.getOpen()) {
						openChildrenNodes.add(childNode);
						//System.out.println(childNode.getMachine());
					}
				}
			}
			
			// since we have already visited this node, close it.
			tempNode.setOpen(false);
			
			// remove the current node from the openChildrenNodes because we have already checked it.
			openChildrenNodes.remove(openChildrenNodes.indexOf(tempNode));
		}
	}


	/**
	 * Class minLowerBound returns the child node with minimum lower bound
	 * @author Ga Hyung Kim
	 * @param children An array of children nodes with same parent
	 * @return Node[index] Element of an array of children nodes with minimum lower bound
	 */
 	public Node minLowerBound(ArrayList<Node> children) {
 		// set the min to the largest integer value possible for the first time
 		// so that it's guaranteed to have a different min later
		int min = Integer.MAX_VALUE;
		int index = -1;
		
		// Go through each child and see which child has the smallest lower bound;
		// Compare only the open children
		for (int i=0; i < children.size(); i++) {
			if (children.get(i).getLowerBound() < min && children.get(i).getOpen()) {
				min = children.get(i).getLowerBound();
				index = i;
			}
		}
		
		// return the Node with the smallest lower bound
		return children.get(index);
 	}
	
 	/**
 	 * This method calculates the total lower bound for every node up to and including the current node and sets the passes in nodes lower bound to the sum
 	 * @param node the node to calculate the lower bound for 
 	 */
	public void calcLowerBound(Node node) {
		int[][] penalty = constraint.getPenalties(); //uses 2D penalty array from the constraint class
		int lowerbound = node.getLowerBound(); //initialize lowerbound to current nodes set lowerbound (zero or if there is a soft constraint it would take that penalty to start)
		
		if (node.getMachine() != -1) {
			lowerbound += penalty[node.getMachine()][convertInt(node.getTask())];
		}
		
		//set the lowerbound of the node
		node.setLowerBound(lowerbound);
	}
	
	/**
	 * createChildren method creates an array of children nodes which come from a parent node
	 * @author Esther Chung
	 * @param parent the parent Node from which the children come
	 */
	public void createChildren(Node parent) {
		// Create children only if there is no children
		if (!parent.getHasChildren()) {
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
				
				// if we are at the rootNode, we need to check the following:
				// 1. if any penalties fall into the forced/forbidden tasks
				// Note that we cannot check for the two near penalties at the rootNode
				if (parent.getMachine() == -1) {
					for (int i = 0; i < availableTasks.length; i++) {
						if (availableTasks[i] != Character.MIN_VALUE) {
							// if the child that we are about to create passes the forced and forbidden
							if (penalty[parentMachine + 1][convertInt(availableTasks[i])] != -1) {
								// create a child
								Node childNode = new Node(parent, parentMachine + 1, availableTasks[i]);
								
								// edit the historical path of this child
								ArrayList<Character> childHistory = parent.getHistory();
								childHistory.add(availableTasks[i]);
								childNode.setHistory(childHistory);
								childrenArray.add(childNode);
							}
						}
					}
				}
				else if (parent.getMachine() == 6) {
					// if we are at the end of the tree, we need to check the following:
					// 1. if any penalties fall into the forced/forbidden tasks
					// 2. if the two near constraints are passed for both (task_this, task_after) and (task_after, task_first)
					for (int i = 0; i < availableTasks.length; i++) {
						if (availableTasks[i] != Character.MIN_VALUE) { 
							if (penalty[parentMachine + 1][convertInt(availableTasks[i])] != -1
							&& !constraint.tooNearH(parent.getTask(), availableTasks[i])
							&& !constraint.tooNearH(availableTasks[i], parent.getHistory().get(0))) {
								// create a child
								Node childNode = new Node(parent, parentMachine + 1, availableTasks[i]);
								
								// if there is two near soft constraint, return the penalty, if not, return 0
								int tnsPenalty = parent.getLowerBound() + constraint.tooNearS(parent.getTask(), availableTasks[i]);
								
								// if there is two near soft constraint with the 1st level, add that soft constraint to the penalty
								tnsPenalty = tnsPenalty + constraint.tooNearS(availableTasks[i], parent.getHistory().get(0));
								childNode.setLowerBound(tnsPenalty);
								
								// edit the historical path of this child
								ArrayList<Character> childHistory = parent.getHistory();
								childHistory.add(availableTasks[i]);
								childNode.setHistory(childHistory);
								childrenArray.add(childNode);
							}
						}
					}
				}
				else {
					// if we are at any other nodes (other than root and the 6th level), we need to check the following:
					// 1. if any penalties fall into the forced/forbidden tasks
					// 2. if the two near constraints are passed for (task_this, task_after)
					for (int i = 0; i < availableTasks.length; i++) {
						if (availableTasks[i] != Character.MIN_VALUE) { 
							if (penalty[parentMachine + 1][convertInt(availableTasks[i])] != -1
							&& !constraint.tooNearH(parent.getTask(), availableTasks[i])) {
								// create a child
								Node childNode = new Node(parent, parentMachine + 1, availableTasks[i]);
								
								// if there is two near soft constraint, return the penalty, if not, return 0
								int tnsPenalty = parent.getLowerBound() + constraint.tooNearS(parent.getTask(), availableTasks[i]); //if there is too near soft constraint return penalty if not return 0
								childNode.setLowerBound(tnsPenalty);
								
								// edit the historical path of this child
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
	}
	
	public Constraint getConstraint() {
		return this.constraint;
	}
	
	public Node getRootNode() {
		return this.rootNode;
	}
	
	public ArrayList<Character> getFinalSol() {
		return this.finalSol;
	}
	
	public int getCurrentLowerBound() {
		return this.currentLowerBound;
	}
	
	public boolean getErrors() {
		return this.errors;
	}
	
	public String getErrorMessage() {
		return this.errormessage;
	}
	
	public void setCurrentLowerBound(int currentLowerBound) {
		this.currentLowerBound = currentLowerBound;
	}

	/**
	 * Convert a task (in char) to an int value
	 * @param task the character representing the task
	 * @return int value that corresponds to the task
	 */
	public static int convertInt(char task){
		return task - 65;
	}

	/**
	 * Convert an int value to a task (in char)
	 * @param task the integer representing the task
	 * @return char that corresponds to the task
	 */
	public static char convertChar(int task){
		return (char) (task+65);
	}
	
	/**
	 * Main method
	 */

	public static void main(String[] args) {
		String sol;
		
		Tree tree = new Tree(args[0]);
		if (tree.getErrors()) {
			
			try {	
				BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
				writer.write(tree.getErrorMessage());
					
				writer.close();
			} catch (IOException e) {
				System.err.println("Error writing file");
			}
		}
		else {
			tree.setCurrentLowerBound(tree.initSolution());
			if (tree.getCurrentLowerBound() == -1) {
				sol = "No valid solution possible!";
			}
			else {
				// set all the nodes to be open so that we don't skip them
				tree = new Tree(args[0], tree.getFinalSol(), tree.getCurrentLowerBound());
				
				tree.search();
				System.out.println(tree.getFinalSol().toString());
				System.out.println(tree.getCurrentLowerBound());
				
				sol = "Solution";
				for (char task: tree.getFinalSol()) {
					sol = sol + " " + task;
				}
				sol = sol + "; Quality: " + Integer.toString(tree.getCurrentLowerBound());
			}	
			try {	
				BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
				writer.write(sol);
					
				writer.close();
			} catch (IOException e) {
				System.err.println("Error writing file");
			}
		}
		
	}
}
