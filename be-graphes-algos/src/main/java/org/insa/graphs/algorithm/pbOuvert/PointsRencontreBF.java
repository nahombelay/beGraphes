package org.insa.graphs.algorithm.pbOuvert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

@SuppressWarnings("unused")
public class PointsRencontreBF extends ProblemOuvertAlgorithm {
	private static double[] distances1 ;
    private static double[] distances2 ;
    private static Arc[] predecessorArcs1;
    private static Arc[] predecessorArcs2;
    private static ProblemOuvertSolution solution;
    private static Path chemin;
    
	public PointsRencontreBF(AbstractInputData data) {
		super(data);
	}

	@Override
	public ProblemOuvertSolution doRun() {
		
		// Retrieve the graph.
		ProblemOuvertData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of distances.
        distances1 = new double[nbNodes];
        distances2 = new double[nbNodes];
        Arrays.fill(distances1, Double.POSITIVE_INFINITY);
        Arrays.fill(distances2, Double.POSITIVE_INFINITY);
        distances1[data.getOrigin().getId()] = 0;
        

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        predecessorArcs1 = new Arc[nbNodes];
        predecessorArcs2 = new Arc[nbNodes];
        
        
        //________________________________________________________________
        //first cycle
        PointsRencontreBellman(graph, data, distances1,predecessorArcs1);
        
        data.permuteOriginDest();
        graph = data.getGraph();
        distances2[data.getOrigin().getId()] = 0;
        
        
        //________________________________________________________________//
        //second cycle
        PointsRencontreBellman(graph, data, distances2,predecessorArcs2);
        
        
        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs2[data.getDestination().getId()] == null) {
        	solution = new ProblemOuvertSolution(data, Status.INFEASIBLE);
            
        }
        else {
        	AfficherSol(graph, data, distances1, distances2,predecessorArcs2);
        	 solution = new ProblemOuvertSolution(data, Status.OPTIMAL, chemin);
        }
        
        return solution;
	}
	
	public void PointsRencontreBellman(Graph graph, ProblemOuvertData data, double[] distances, Arc[] predecessorArcs) {
		boolean found = false;
        for (int i = 0; !found && i < graph.size(); ++i) {
            found = true;
            for (Node node: graph.getNodes()) {
                for (Arc arc: node.getSuccessors()) {

                    // Small test to check allowed roads...
                    if (!data.isAllowed(arc)) {
                        continue;
                    }

                    // Retrieve weight of the arc.
                    double w = data.getCost(arc);
                    double oldDistance = distances[arc.getDestination().getId()];
                    double newDistance = distances[node.getId()] + w;


                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        found = false;
                        distances[arc.getDestination().getId()] = distances[node.getId()] + w;
                        predecessorArcs[arc.getDestination().getId()] = arc;
                    }
                }
            }
        }
	}

	public void AfficherSol(Graph graph,  ProblemOuvertData data, double[] distances1, double[] distances2, Arc[] predecessorArcs) {
		// The destination has been found, notify the observers.
        notifyDestinationReached(data.getDestination());

        // Create the path from the array of predecessors...
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc arc = predecessorArcs[data.getDestination().getId()];
        while (arc != null) {
            arcs.add(arc);
            arc = predecessorArcs[arc.getOrigin().getId()];
        }

        // Reverse the path...
        Collections.reverse(arcs);
        
        chemin = new Path(graph, arcs);
        double cheminLen = chemin.getLength();
        
		for (Node node: graph.getNodes()) {
        	if (Double.isFinite(distances1[node.getId()]) && Double.isFinite(distances2[node.getId()])) {
        		if (Math.abs(distances1[node.getId()]- distances2[node.getId()]) <= 0.15*(distances2[node.getId()] + distances2[node.getId()])/2 &&
        				(distances1[node.getId()] + distances2[node.getId()]) <= 1.30*cheminLen){
	        		notifyNodeReached(node);
	        	}
        	}
        }
	}
}
