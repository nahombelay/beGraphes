package org.insa.graphs.algorithm.shortestpath;

import java.util.*;


import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // TODO:
        
        // Retrieve the graph.
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of distances.
        double[] distances = new double[nbNodes];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[data.getOrigin().getId()] = 0;
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        //initialisation de la liste des labels
        Label[] labelArray = new Label[nbNodes];
        
        //initilaisation de la liste des nodes
        List<Node> nodeArray = graph.getNodes();
        
        //initialisation des labels
        for (int i = 0; i < nbNodes; i++) {
        	labelArray[i] = new Label(nodeArray.get(i));
        }
        
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        Label originLabel = labelArray[data.getOrigin().getId()];
        Label destinationLabel = labelArray[data.getDestination().getId()];
        labelArray[origin.getId()].setCout(0);
        
        
        //initialisation du tas
        BinaryHeap<Label> heap = new BinaryHeap<>();
        
        //on insere origin dans le tas
        heap.insert(labelArray[origin.getId()]);
        
        Label x = null;
        
        while (!labelArray[destination.getId()].estMarque()) {
        	x = heap.deleteMin();
        	x.estMarque();
        	
        	for (Arc iter: x.sommetC.getSuccessors()) {
        		
        		Label y = labelArray[iter.getDestination().getId()];
        		notifyNodeReached(iter.getDestination());
        		
        		if(! y.estMarque()) {
        			
        			if (y.getCout() > x.getCout() + data.getCost(iter)) {
        				
        				
        				try {
        					heap.remove(y);
        					
        				} catch(ElementNotFoundException e) {
        					
    		    		}
        				
        				y.setCout(x.getCout() + data.getCost(iter));
        				y.setPere(iter);
        				heap.insert(y);
        			}
        		}
        	}
        	
        }

        // Destination has no predecessor, the solution is infeasible...
        
        if(destinationLabel.estMarque() == false) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	// The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
            // Create the path from the array of predecessors...
        	ArrayList<Arc> arcs = new ArrayList<Arc>();
        	while(!x.equals(originLabel)) {
        		arcs.add(x.getPere());
        		x = labelArray[x.getPere().getOrigin().getId()];
        	}
        	// Reverse the path...
            Collections.reverse(arcs);
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        
        
        
        return solution;
    }

}
