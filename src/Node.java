
public class Node {
	private int name;					//node number
	private boolean isMarked = false;	//if it has been marked, set to true
	
	public Node (int Name) {
		name = Name;
	}
	
	public void setMark(boolean mark) {
		isMarked = mark;
	}
	
	public boolean getMark() {
		return isMarked;
	}
	
	public int getName() {
		return name;
	}
}
