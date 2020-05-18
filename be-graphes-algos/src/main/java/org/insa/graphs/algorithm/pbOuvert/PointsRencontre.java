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
public class PointsRencontre extends ProblemOuvertAlgorithm {
	private static double[] distances1 ;
    private static double[] distances2 ;
    
	public PointsRencontre(AbstractInputData data) {
		super(data);
	}

	@Override
	public ProblemOuvertSolution doRun() {
		int erreur = 30;
		
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
        Arc[] predecessorArcs1 = new Arc[nbNodes];
        Arc[] predecessorArcs2 = new Arc[nbNodes];
        
        
        //________________________________________________________________
        //first cycle
        boolean found = false;
        for (int i = 0; !found && i < nbNodes; ++i) {
            found = true;
            for (Node node: graph.getNodes()) {
                for (Arc arc: node.getSuccessors()) {

                    // Small test to check allowed roads...
                    if (!data.isAllowed(arc)) {
                        continue;
                    }

                    // Retrieve weight of the arc.
                    double w = data.getCost(arc);
                    double oldDistance = distances1[arc.getDestination().getId()];
                    double newDistance = distances1[node.getId()] + w;


                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        found = false;
                        distances1[arc.getDestination().getId()] = distances1[node.getId()] + w;
                        predecessorArcs1[arc.getDestination().getId()] = arc;
                    }
                }
            }
        }
        
        data.permuteOriginDest();
        distances2[data.getOrigin().getId()] = 0;
        
        //________________________________________________________________//
        //second cycle
        found = false;
        for (int i = 0; !found && i < nbNodes; ++i) {
            found = true;
            for (Node node: graph.getNodes()) {
                for (Arc arc: node.getSuccessors()) {

                    // Small test to check allowed roads...
                    if (!data.isAllowed(arc)) {
                        continue;
                    }

                    // Retrieve weight of the arc.
                    double w = data.getCost(arc);
                    double oldDistance = distances2[arc.getDestination().getId()];
                    double newDistance = distances2[node.getId()] + w;


                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        found = false;
                        distances2[arc.getDestination().getId()] = distances2[node.getId()] + w;
                        predecessorArcs2[arc.getDestination().getId()] = arc;
                    }
                }
            }

        }
        
        //notifyDestinationReached(data.getDestination());
        
        ProblemOuvertSolution solution = null;
        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs2[data.getDestination().getId()] == null) {
        	System.out.println("if");
            solution = new ProblemOuvertSolution(data, Status.INFEASIBLE);
        }
        else {
        	System.out.println("else");
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs2[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs2[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            //solution = new ProblemOuvertSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            
            for (Node node: graph.getNodes()) {
            	if (Double.isFinite(distances1[node.getId()]) && Double.isFinite(distances2[node.getId()]))
    	        	//if (0.85*distances2[node.getId()] <= distances1[node.getId()]  && distances1[node.getId()] <= 1.15*distances2[node.getId()] 
    	        			//&& 0.85*distances1[node.getId()] <= distances2[node.getId()]  && distances2[node.getId()] <= 1.15*distances1[node.getId()]) 
            		if (Math.abs(distances1[node.getId()]- distances2[node.getId()]) <= 0.15*(distances2[node.getId()] + distances2[node.getId()])/2 &&
            				(distances1[node.getId()] + distances2[node.getId()]) <= 1.30*((new Path(graph, arcs).getLength()))){
    	        		notifyNodeReached(node);
    	        		System.out.println(distances1[node.getId()] + "|||" + distances2[node.getId()]);
    	        	}
            }
        }
        
        
        
        
		return new ProblemOuvertSolution(data, Status.INFEASIBLE);
	}
}
