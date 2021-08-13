
public class Edge {
	private Node firstNode;
	private Node secondNode;
	private int type;
	private String label;
	
	public Edge(Node u, Node v, int Type) {
		firstNode = u;
		secondNode = v;
		type = Type;
	}
	
	public Edge(Node u, Node v, int Type, String Label) {
		firstNode = u;
		secondNode = v;
		type = Type;
		label = Label;
	}
	
	public Node firstEndpoint() {
		return firstNode;
	}
	
	public Node secondEndpoint() {
		return secondNode;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int newType) {
		type = newType;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String newLabel) {
		label = newLabel;
	}
	
}
