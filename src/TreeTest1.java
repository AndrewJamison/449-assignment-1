import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * Tree test for input data.txt
 *
 */

class TreeTest1 {

	private Tree tree;

	@BeforeEach
	void setUp() throws Exception {
		tree = new Tree();
	}

	@AfterEach
	void tearDown() throws Exception {
		tree = null;
	}

	@Test
	void testInitSolution() {
		// 19 with no constraints
		// 26 with constraints
		assertEquals(26, tree.initSolution());
	}
	
	@Test
	void testSearch() {
		// TODO
	}
	
	@Test
	void testMinLowerBound() {
		
	}

}
