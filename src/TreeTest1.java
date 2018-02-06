import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TreeTest1 {

	private Tree tree;

	@BeforeEach
	void setUp() throws Exception {
		tree = new Tree();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testInitSolution() {
		// 26 with constraints
		// 19 with no constraints
		assertEquals(19, tree.initSolution());
	}
	
	@Test
	void testSearch() {
		// TODO
	}
	
	@Test
	void testMinLowerBound() {
		
	}

}
