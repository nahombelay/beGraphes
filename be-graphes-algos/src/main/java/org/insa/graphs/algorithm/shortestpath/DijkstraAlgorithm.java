package org.insa.graphs.algorithm.shortestpath;

import java.util.*;


import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;

@SuppressWarnings("unused")
public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	public int sommetIn, sommetOut = 0;
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    public ShortestPathSolution doRun() {
    	
        final ShortestPathData data = getInputData();
        
        // Retrieve the graph.
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        
        //initialisation de la liste des labels
        Label[] labelArray = new Label[nbNodes];
        
        //initilaisation de la liste des nodes
        List<Node> nodes = graph.getNodes();
        //l'initialisation des labels
        
        for (int i = 0; i < nbNodes; i++) {
        	labelArray[i] = new Label(nodes.get(i));
        }
        
        return djikstraRun(labelArray, data, graph);
     
    }
    
   
    protected ShortestPathSolution djikstraRun(Label[] labelArray, ShortestPathData data, Graph graph) {
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

       
        //variables pas nécessaire mais plus propre
        Node origin = data.getOrigin();
        Label originL = labelArray[data.getOrigin().getId()];
        Node destination = data.getDestination();
        Label destinationL = labelArray[data.getDestination().getId()];
        
        //variables pour compter 
        
        int cptArc = 0;
		int cptIter = 0;
        
        //initialisation du tas
        BinaryHeap<Label> heap = new BinaryHeap<>();
        
        //on insere origin dans le tas
        labelArray[origin.getId()].setCout(0);
        heap.insert(labelArray[origin.getId()]);
        sommetIn++;
        
        Label x = null;
        
        while (!labelArray[destination.getId()].estMarque() && !heap.isEmpty() ) {
        	//System.out.println(heap.isValid(0));
        	x = heap.findMin();
        	heap.remove(x);
        	sommetOut++;
        	x.marquer();
        	notifyNodeMarked(x.sommetCourant());
        	
        	for (Arc iter: x.sommetCourant().getSuccessors()) {
        		if (data.isAllowed(iter)) {
	        		Label y = labelArray[iter.getDestination().getId()];
	        		notifyNodeReached(iter.getDestination());
	        		
	        		if(! y.estMarque()) {
		        			if (y.getCout() > x.getCout() + data.getCost(iter)) {
		        				try {
		        					heap.remove(y);
		        					sommetOut++;
		        				} catch(ElementNotFoundException e) {
		        					;
		    		    		}
		        				y.setCout(x.getCout() + data.getCost(iter));
		        				y.setPere(iter);
		        				heap.insert(y);
		        				sommetIn++;
		        			}
	        		}
        		}
        		cptArc++;
        	}
        	cptIter++;
        }
        // Destination has no predecessor, the solution is infeasible...
        
        ShortestPathSolution solution = null;
		if(destinationL.estMarque() == false || destinationL.getCout() == 0) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	// The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
         // Create the path from the array of predecessors...
        	ArrayList<Arc> arcs = new ArrayList<>();
        	
        	while(!x.equals(originL)) {
        		arcs.add(x.getPere());
        		x = labelArray[x.getPere().getOrigin().getId()];
        		//verification bien Dij: oui décroissant 
        		//System.out.println("cout label: " + x.getCout());
        	}
        	// Reverse the path...
            Collections.reverse(arcs);
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        //verification nbr arc et iterations
        //System.out.println("Nbr arcs: " + cptArc + " NbrIer : " + cptIter);
		
		//nombre empilation et dépilation
		System.out.println("Sommet ajoutés dans le tas: " + sommetIn + " Sommet enlevés du tas: " + sommetOut);
        return solution;
    }

}
