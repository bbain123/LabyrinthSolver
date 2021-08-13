import java.io.*;
import java.util.*;

public class Labyrinth {
	private Graph graph;
	private Key key;
	private Node start, end;
	
	
	public Labyrinth(String inputFile) throws LabyrinthException {
		try {

			BufferedReader inFile = new BufferedReader(new FileReader(inputFile)); 
			
		    String line = inFile.readLine();  									//skip scale value
		    int graphWidth = Integer.parseInt(inFile.readLine());
		    int graphLength = Integer.parseInt(inFile.readLine());
		    
		    StringTokenizer theKeys = new StringTokenizer(inFile.readLine(), " ");//add keys to pocket
		    int type = 0;
		    key = new Key();
		    while(theKeys.hasMoreTokens()) {
		    	key.addKey(type, Integer.parseInt(theKeys.nextToken()));
		    	type++;
		    }
		    
		    graph = new Graph(graphWidth*graphLength);  //make graph with width x length nodes
		   
		    //HORIZONTAL CONNECTIONS
		    for(int row = 0; row < graphLength; row++){ //nodes in row and col, add width*row + col. row = 1, add width*row 
		    	String workingLine = inFile.readLine();
		    	Node first,second;
		    	String label = null;
		   
		 
		    	if(workingLine.charAt(0) == 's')			//check if first node of line is start or exit
		    		start = graph.getNode(row*graphWidth);
		    	else if (workingLine.charAt(0) == 'x')
		    		end = graph.getNode(row*graphWidth);
		    	
		    	first = graph.getNode(row*graphWidth);     //initialize first node of workingLine 
		    	
		    	for(int col = 1; col < graphWidth*2-1; col++) {
		    		if (col%2 == 1 && Character.isDigit(workingLine.charAt(col))) { //if connection is a number
		    			type = Character.getNumericValue(workingLine.charAt(col));
		    			label = "door";
		    		}
		    		
		    		else if (col%2 == 1 && workingLine.charAt(col) == 'w' || workingLine.charAt(col) == 'c') { //if connection is a wall or corridor
		    			type = -1;
		    			if(workingLine.charAt(col) == 'w')
		    				label = "wall";
		    			if(workingLine.charAt(col) == 'c')
		    				label = "corridor";  
		    		}
		    		
		    		else if (col%2 == 0) {       //its a node
		    		
		    			if(workingLine.charAt(col) == 's')			//check if current node of line is start or exit
				    		start = graph.getNode(row*graphWidth + col/2);
		    			else if (workingLine.charAt(col) == 'x')
				    		end = graph.getNode(row*graphWidth + col/2);
		    			
		    			second = graph.getNode(row*graphWidth + col/2); //initialize second node
		    			graph.insertEdge(first, second, type, label);   //insert edge from first to second node
		    			first = graph.getNode(row*graphWidth + col/2);
		    		}
		    	}
		    	workingLine = inFile.readLine();
		    }
		    inFile.close();
		    
		    BufferedReader sameFile = new BufferedReader(new FileReader(inputFile)); //start from top of same file
		    String fromNodes;
		    for(int i = 0; i < 5; i++) {
		    	fromNodes = sameFile.readLine();       //skip 4 lines down 
		    }
		    
		    //VERTICAL CONNECTIONS
		    String connections = sameFile.readLine();
		    String toNodes = sameFile.readLine();
		    Node first, second;
		    String label = null;
		    
		    for(int row = 0; row < graphLength - 1; row++) {
		    	
		    	
		    	for(int col = 0; col < graphWidth*2; col = col + 2) {
		    		first = graph.getNode(row*graphWidth + col/2);
		    		
		    		if(connections.charAt(col) == 'w') {
		    			label = "wall";
		    			type = -1;
		    		}
		    		else if (connections.charAt(col) == 'c') {
		    			label = "corridor";
		    			type = -1;
		    		}
		    		else if (Character.isDigit(connections.charAt(col))) {
		    			label = "door";
		    			type = Character.getNumericValue(connections.charAt(col));
		    		}
		    		
		    		second = graph.getNode((row+1)*graphWidth + col/2);
		    		graph.insertEdge(first, second, type, label);    //insert the edge
		    		
		    	}
		    	fromNodes = toNodes;
		    	connections = sameFile.readLine();
		    	toNodes = sameFile.readLine();
		    }
		    sameFile.close();
			
		}
		catch(FileNotFoundException s) {
			throw new LabyrinthException("error with reading file " + s);
		}
		catch(IOException e ) {
			System.out.println(e);
		}
		catch(GraphException d) {
			System.out.println("something went wrong with the graph " + d);
		}
	}
	
	public Graph getGraph() throws LabyrinthException {
		if (graph == null)
			throw new LabyrinthException("Graph is null");
		return graph;
	}
	
	public Iterator<Node> solve(){
		ArrayList<Node>path = new ArrayList<Node>();
		return solveGraph(start, end, path);
	}
	
	private Iterator<Node> solveGraph(Node current, Node end, ArrayList<Node> path){
		int keyTypeUsed = -1;
		Node goingTo;
		Iterator<Node>solution;
		
		try {
			current.setMark(true);					//mark current and add to list
			path.add(current);
			
			if(current == end)						//if reached the end, return path
				return path.iterator();
			
			Iterator<Edge> possiblePaths = graph.incidentEdges(current);
			
			while(possiblePaths.hasNext()) {
				Edge currentChoice = possiblePaths.next();
				
				if(currentChoice.getLabel().equals("corridor")){   //if its a corridor, other side unmarked
					if(currentChoice.firstEndpoint() == current) {
						if(!currentChoice.secondEndpoint().getMark()) { //if new node is second endpoint
							goingTo = currentChoice.secondEndpoint();
							solution = solveGraph(goingTo, end, path);
							if(solution != null)
								return solution;
						}
					}
					else {
						if(!currentChoice.firstEndpoint().getMark()) { //if new node is first endpoint
							goingTo = currentChoice.firstEndpoint();
							solution = solveGraph(goingTo, end, path);
							if(solution != null)
								return solution;
								
						}
					}
				}
				
				if(currentChoice.getLabel().equals("door")) { //if theres a door we can open with and other side unmarked
					if(key.smallestPossibleKey(currentChoice.getType()) >= 0) { 
						
						if(currentChoice.firstEndpoint() == current) {
							if(!currentChoice.secondEndpoint().getMark()) { 
								keyTypeUsed = key.smallestPossibleKey(currentChoice.getType());
								key.useKey(keyTypeUsed, 1); 					//use the smallest possible key
								goingTo = currentChoice.secondEndpoint();
								solution = solveGraph(goingTo, end, path);
								if(solution != null)
									return solution;
								else
									if(keyTypeUsed != -1)						//if that was dead end, get the key back
										key.addKey(keyTypeUsed, 1);
							}
						}
						
						else {
							if(!currentChoice.firstEndpoint().getMark()) {
								keyTypeUsed = key.smallestPossibleKey(currentChoice.getType());
								key.useKey(keyTypeUsed, 1);
								goingTo = currentChoice.firstEndpoint();
								solution = solveGraph(goingTo, end, path);
								if(solution != null)
									return solution;
								else
									if(keyTypeUsed != -1)					//dead end, get key back
										key.addKey(keyTypeUsed, 1);
							}
						}
					}
				}
			}
			//there are no more paths
			if(current == start)  
				return null;
			
			current.setMark(false);
			path.remove(current);
				
		}
		catch(GraphException e) {
			System.out.println("Something went wrong with the graph " + e);
		}
		return null;
	}

}
