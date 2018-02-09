=================================================================
** Operating instructions
=================================================================
java Tree inputFile.txt outputFile.txt

=================================================================
** List of files included
=================================================================
*Data.java
- Reads the input file then stores the constraints and penalties.
- Prints the error message if the input file is invalid.

*Constraint.java
- Gets the constraints and penalties from the Data class. Applies 
forced and forbidden constraints and modifies the penalty array.

*Node.java
- Has all the attributes for the node created for the tree.

*Tree.java
- Creates the tree used for the algorithm. Initially it finds the 
possible solution by choosing the nodes with minimum lower bounds
then using the solution, it searches the tree if there is better 
solution. 
- Creates output file with the file name given as second command 
line argument. 

=================================================================
** How the program works
=================================================================
Using Branch-and-bound algorithm, this program finds the 
assignment of tasks to machines with minimal penalty value.

=================================================================
** Possible bugs
=================================================================
If the format of the input file is different, program might prompt 
error message even if the inputs are valid. 

=================================================================
** Credits to (alphabetical order)
=================================================================
Christilyn Arjona
Esther HaKyung Chung
Jona Mikhaela Grageda
Andrew Jamison
Esther Sunah Kim
Ga Hyung Kim