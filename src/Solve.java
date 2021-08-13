import java.util.*;
import java.io.*;

public class Solve {

    public static void main (String[] args) {
	Node u, v;
	DrawLab display;
	int delay = 0;


	if (args.length != 1 && args.length != 2)
	    System.out.println("Usage: java Solve labyrithFile OR java Solve labyrinthFile speed");
	else {
	    if (args.length == 2) delay = Integer.parseInt(args[1]);
	    display = new DrawLab(args[0]);
	    try {
	    	BufferedReader in;
	    	String line;

	    	Labyrinth lab = new Labyrinth(args[0]);

	    	Iterator solution = lab.solve();

	    	if (solution != null) {
	    		if (solution.hasNext()) u = (Node)solution.next();
	    		else return;
	    		while (solution.hasNext()) {
	    			v = (Node)solution.next();
	    			Thread.sleep(delay);
	    			display.drawEdge(u,v);
	    			u = v;
	    		}
	    	}
		else {
		    System.out.println("No solution was found");
		    System.out.println("");
		}

		try {
			in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Press RET to finish");
	        line = in.readLine();
	    }
		catch (IOException inn) {
			System.out.println("Error reading from keyboard");
		    }
	    
	    }
	    catch (LabyrinthException e) {
			System.out.println("Error reading labyrinth");
		    }
	    catch (Exception ex) {
		System.out.println(ex.getMessage());
	    }
		

	    display.dispose();
	    System.exit(0);
	   }
    }
}
