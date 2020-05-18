package org.insa.graphs.algorithm.pbOuvert;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

public class ProblemOuvertData extends AbstractInputData {
	
	// Origin and destination nodes.
    private Node origin, destination;

	public ProblemOuvertData (Graph graph, Node origin, Node destination, ArcInspector arcInspector) {
		super(graph, arcInspector);
        this.origin = origin;
        this.destination = destination;
	}
	
	public void permuteOriginDest() {
		Node interm = destination;
		this.destination = this.origin;
		this.origin = interm;
	}
    
    public Node getOrigin() {
		return origin;
	}



	public Node getDestination() {
		return destination;
	}



	@Override
    public String toString() {
        return "Shortest-path from #" + origin.getId() + " to #" + destination.getId() + " ["
                + this.arcInspector.toString().toLowerCase() + "]";
    }

}
