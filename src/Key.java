
public class Key {				//class to keep track of the keys available in labyrinth
	private int pocket[];
	
	public Key() {				//initialize pocket (empty)
		pocket = new int[10];
		for (int i = 0; i < 10; i++) 
			pocket[i] = 0;	
	}
	
	
	public void addKey(int type, int numKeys) { //add numKeys of key type type
		if (!(type > 9)) 
			pocket[type] = pocket[type] + numKeys; 
	}
	
	public boolean useKey(int type, int numKeys) {  //use numKeys of key type type
		if(type <= 9 && pocket[type] - numKeys >= 0) {
			pocket[type] = pocket[type] - numKeys;
			return true;
		}
		else
			return false;
	}
	
	
	public int[] getKeys() {
		return pocket;
	}
													//returns the smallest possible key to use for the door
	public int smallestPossibleKey(int doorToOpen) { //negative if there is no key to use
		for(int i = doorToOpen; i < 10; i++) {
			if(pocket[i] >= 1)
				return i;
		}
		return -1;
		
	}

	
}
