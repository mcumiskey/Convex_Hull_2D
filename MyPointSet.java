import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.Line2D;

public class MyPointSet extends Vector <MyPoint> {

    private int imin, imax, xmin, xmax;
    private boolean xySorted;
    private Vector<MyPoint>  theHull;
    public static final long serialVersionUID = 24362462L;
	
	public MyPointSet() {
		xySorted = false;
    }

    public void addPoint(int x, int y) {
		MyPoint p = new MyPoint(x,y);
		addElement(p);
		xySorted = false;
    }

    private int next(int i) {
		return (i = (i+1) % size());
    }

    private int previous(int i) {
		return (i = (i-1+size()) % size());
    }

    private int hullnext(int i) {
		return (i = (i+1) % theHull.size());
    }

    private int hullprevious(int i) {
		return (i = (i-1+theHull.size()) % theHull.size());
    }

    public void sortByXY() {
		int i;
		MyPoint p, q;
		boolean clean;

		// Task 1
		//bubble sort
		System.out.println("size " +size());

		for (int n = 0; n < size(); n++) {					//iterate through the vector, starting at 0
			for (int j = 0; j < size() - 1 - n; j++) {		//and compare one element to one next to it (making sure not to go out of bounds)
				p = elementAt(j);							//Set MyPoint p to the element at j
				q = elementAt(j + 1);						//Set Mypoint q to the element following j (aka j+1)
				if (p.x > q.x) {						// if the x value of p > q
						setElementAt(q, j); 				//set position j to q
						setElementAt(p, j+1); 				//set position j+1 to p (larger number goes to back)
				}
				if(p.x == q.x) {						//if the x values are the same, sort based on y
					if(p.y > q.y){						//if p.y > q.y
						setElementAt(q, j); 			//set position j to q
						setElementAt(p, j+1); 			//set position j+1 to p
					}
					if(p.x == q.y){						//if they are the same point
						System.out.println("Duplicate points! Only one will be added to the Vector.");
						setElementAt(q, j); 			//set position j to q
						//only add one of the points to avoid duplicates
					}
				}
			}
		}

		xySorted = true;

		//Here's some code that is useful for debugging 
		System.out.println("Is this sorted?");
		for (i = 0; i<size(); i++) {
			p = elementAt(i);
			System.out.println(i+": "+p.x+" "+p.y);
		}

		
		return;
    }

    private void enumerateHull() {  
	
		System.out.println("");
		System.out.print("Current chain is: ");
		for (int index=0; index<theHull.size(); index++) {
			MyPoint tmppoint;
			tmppoint = theHull.elementAt(index);
			System.out.print(" ("+tmppoint.x+", "+tmppoint.y+")");
		}
		System.out.println("");
    }

    private int removeChain(int bottom, int top) {
	// removes the chain between bottom+1 and top-1 inclusive
	// N.B. the size of the hull decreases by 1 at each step
	// returns the index of the last valid element

	int i, howmany;
	MyPoint q;

	if (bottom == top) return bottom; // nothing to remove

	System.out.println("  Removing chain between "+bottom+" and "+top+
			   " in hull of size "+theHull.size());

	if (bottom < top) {
	    howmany = top-bottom-1;
	    System.out.println("   0 I want "+howmany+" elements");
	    for (i=0; i<howmany; i++) {
		q = theHull.elementAt(bottom+1);
		System.out.println("   0 Removing element at "+bottom+1+": ("+q.x+", "+q.y+")");
		theHull.removeElementAt(bottom+1);
	    }
	}

	else { // top < bottom so wrap along chain end
	    System.out.println(" \n  WRAPPING AROUND THE END \n");
	    howmany = theHull.size()-bottom-1;
	    System.out.println("   1 I want "+howmany+" elements between "+(bottom+1)+" and "+(theHull.size()-1)+" inclusive");
	    for (i=0; i<howmany; i++) {
		q = theHull.elementAt(bottom+1);
		System.out.println("   1 Removing element at "+(bottom+1)+": ("+q.x+", "+q.y+")");
		theHull.removeElementAt(bottom+1);
	    }
	    howmany = top;
	    System.out.println("   plus "+howmany+" elements between "+0+" and "+(top-1)+" inclusive");
	    for (i=0; i<howmany; i++) {
		// could remove top-1 but then need to change top
		q = theHull.elementAt(0);
		System.out.println("   2 Removing element at "+0+": ("+q.x+", "+q.y+")");
		theHull.removeElementAt(0);
	    }

	    if (bottom >= theHull.size()) bottom = theHull.size()-1;
	}

	return bottom; // index of last valid element
	}
    
    private void hullIncremental() {    
		int k, i, size, howmany;
		MyPoint p, q, r;
		MyPoint topelem, nextelem, botelem, prevelem;
		int top, bottom;
		
		size = size();

		if (size < 3) { //less than 3 points is just a line segment or a point
			System.out.println("\u0007Can't compute convex hull");
			return;
		}

		theHull = new Vector<MyPoint>(size, size);

		if (!xySorted) {
			sortByXY();
		} 
		//Task 2
		// YOUR CODE GOES HERE
		// prepare first three points
		//Add first three points to theHull
		p = elementAt(0);
		theHull.insertElementAt(p, 0); //leftmost point is 0

		q = elementAt(1);
		r = elementAt(2);

		if((q.x > r.x) && (q.y < r.y)){
			//if q.x is righter than r.x but above it, it should be the third point to be counter clock wise 
			theHull.insertElementAt(r, 1);
			theHull.insertElementAt(q, 2);
			top = bottom = theHull.indexOf(q);

		} if ((q.x < r.x) && (q.y < r.y)) {
			//if q.x is lefter than r.x but above it, it should be the third point to be counter clock wise 
			theHull.insertElementAt(r, 1);
			theHull.insertElementAt(q, 2);
			// initialise the ends of the chain-to-be-removed 
			// as the rightmost point in the hull
			// That is, top=bottom= "index of rightmost point in hull"
			top = bottom = theHull.indexOf(r);
		top = bottom = 2;
		} else {
			//otherwise insert in order to be counter clock wise
			theHull.insertElementAt(q, 1);
			theHull.insertElementAt(r, 2);
				// initialise the ends of the chain-to-be-removed 
			// as the rightmost point in the hull
			// That is, top=bottom= "index of rightmost point in hull"
			top = bottom = theHull.indexOf(r); 	
		}

		//Task 4
		// OPTIONAL CODE HERE TOO

		
		//Task 2
		// MORE OF YOUR CODE GOES HERE

		// at this point hull is counter-clockwise and 
		// its last point is visible from the next light-source

		//Task 3
		// YOUR CODE GOES HERE

		//Loop through the remaining points

			// //next light-source is p = CH[k]
			// p = elementAt(3);

			// // find lit chain

			// // remove lit chain
			// bottom = removeChain(bottom, top);
			// //Add the next point
			// theHull.insertElementAt(p, (bottom+1));
			// enumerateHull();

			// // construct next chain starting at last inserted element
			// if (top != bottom) top = bottom = bottom+1; 

		//End loop
    }

    public Polygon hullDraw() {
		int i;
		MyPoint q;
		Polygon p;

		p = new Polygon();
		System.out.println("The Hull has size "+theHull.size());
		System.out.println("The Hull is:");
		for (i=0; i<theHull.size(); i++) {
			q = theHull.elementAt(i);
			System.out.println("-> ("+q.x+", "+q.y+")");
			p.addPoint(q.x,q.y);
		}	
		System.out.println("===================================");
		return p;
    }

    public Polygon hullThePolygon() {
		int i;

		Polygon chp;

		sortByXY();
		hullIncremental();
		chp = hullDraw();

		return chp;
    }
}
