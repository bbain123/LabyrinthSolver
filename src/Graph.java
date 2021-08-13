import java.util.*;

public class Graph implements GraphADT{
	private int numNodes;
	private Edge adjMat[][];  			//Adjacency matrix storing edges
	private Node nodesInGraph[];        //list of nodes in graph
	
	public Graph(int n) {
		adjMat = new Edge[n][n];
		nodesInGraph = new Node[n];
		
		for (int i = 0; i < n; i++) 			//store Nodes 0 to n-1
			nodesInGraph[i] = new Node(i);

	}
	
	public void insertEdge(Node nodeu, Node nodev, int type, String label) throws GraphException{
		Edge tempEdge = new Edge(nodeu, nodev, type, label);
		if(nodeu == null || nodev == null) {					//check if nodes exist
			throw new GraphException("node doesnt exist");
		}
		if(nodev.getName() < nodeu.getName())					//set smaller node as first in pair
			tempEdge = new Edge(nodev, nodeu, type, label);
		
		//place temp in [u][v] and [v][u] if its empty (smaller node, larger node)
		if(adjMat[nodeu.getName()][nodev.getName()] == null && adjMat[nodev.getName()][nodeu.getName()] == null) {
			adjMat[nodeu.getName()][nodev.getName()] = tempEdge;
			adjMat[nodev.getName()][nodeu.getName()] = tempEdge;
		}
		else
			throw new GraphException("cannot replace edge");		//if edge already stored, throw Graphexception
	}
	
	
	public void insertEdge(Node nodeu, Node nodev, int type) throws GraphException{  //same but without label
		Edge tempEdge = new Edge(nodeu, nodev, type);
		if(nodeu == null || nodev == null) 
			throw new GraphException("node doesnt exist");
		
		if(nodeu.getName() >= nodesInGraph.length || nodev.getName() >= nodesInGraph.length)
			throw new GraphException("invalid edge"); 
		
		if(nodev.getName() < nodeu.getName())
			tempEdge = new Edge(nodev, nodeu, type);
		
		//place temp in [u][v] and [v][u] if it doesnt have anything else
		if(adjMat[nodeu.getName()][nodev.getName()] == null && adjMat[nodev.getName()][nodeu.getName()] == null) {
			adjMat[nodeu.getName()][nodev.getName()] = tempEdge;
			adjMat[nodev.getName()][nodeu.getName()] = tempEdge;
		}
		else
			throw new GraphException("cannot replace edge");
	}
	
	
	public Node getNode(int u) throws GraphException{ //if u is not in range of 0 to n - 1, dont have node
	
		for(int i = 0; i < nodesInGraph.length; i++) {
			if (nodesInGraph[i].getName() == u)
				return nodesInGraph[i];
		}
	
		throw new GraphException("Node " + u + " is not in the graph");
	}
	
	
	public Iterator<Edge> incidentEdges(Node u) throws GraphException{ //returns edges incident to u
		getNode(u.getName()); 								//check if node exists
		ArrayList<Edge>incident = new ArrayList<Edge>();
		
		for(int i = 0; i < nodesInGraph.length; i++) { //from same column as u name, go row by row and add all that arent null
			if(adjMat[u.getName()][i] != null)
				incident.add(adjMat[u.getName()][i]);
		}
		Iterator<Edge>incidents = incident.iterator();
		return incidents;
	}
	
	
	public Edge getEdge(Node u, Node v) throws GraphException{    //return edge of u and v if it exists
		getNode(u.getName());
		getNode(v.getName());
		
		if(adjMat[u.getName()][v.getName()] != null)
			return adjMat[u.getName()][v.getName()];
		else
			throw new GraphException("no edge between node " + u.getName() + " and node "+v.getName());
	}
	
	
	public boolean areAdjacent(Node u, Node v) throws GraphException{  //for each node incident to u, check if nodes incident to that are paired with v
		getNode(u.getName());
		getNode(v.getName());
		Iterator<Edge>uEdges = incidentEdges(u);
		Node connectedU;          							//node that is connected to u
		
		while(uEdges.hasNext()) {
			Edge nextConnectU = uEdges.next();   						//next edge connected to u
			if(nextConnectU.firstEndpoint().getName() == u.getName()) //get the node from edge that is not u
				connectedU = nextConnectU.secondEndpoint();
			else
				connectedU = nextConnectU.firstEndpoint();
			
			Iterator<Edge>neighEdges = incidentEdges(connectedU); 	//get edges connected to edge connected to u
			while(neighEdges.hasNext()) {
				Edge nextConnectCU = neighEdges.next();  			 //node that is connected to connectedU
				Node connectedCU;
				if(nextConnectCU.firstEndpoint().getName() == connectedU.getName())
					connectedCU = nextConnectCU.secondEndpoint();
				else
					connectedCU = nextConnectCU.firstEndpoint();
				
				Iterator<Edge> vEdges = incidentEdges(connectedCU);  //get edges of neighEdges and look for v
				while(vEdges.hasNext()) {
					Edge vNext = vEdges.next();
					
					if(vNext.firstEndpoint().getName() == v.getName())
						return true;
					else if (vNext.secondEndpoint().getName() == v.getName())
						return true;
				}
			}
		}
		return false;
	}
}