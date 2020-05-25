package org.insa.graphs.algorithm.pbOuvert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

@SuppressWarnings("unused")
public class PointsRencontreDijkstra extends ProblemOuvertAlgorithm {
	
	private static ProblemOuvertSolution solution;
    private Path chemin;
    private static Label x;
    private static Node origin, destination;
    private static Label originL, destinationL;
    
	public PointsRencontreDijkstra(AbstractInputData data) {
		super(data);
	}

	@Override
	protected ProblemOuvertSolution doRun() {
		
		ProblemOuvertData data = getInputData();
		
		// Retrieve the graph.
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        
        //initialisation de la liste des labels
        Label[] labelArray1 = new Label[nbNodes];
        Label[] labelArray2 = new Label[nbNodes];
        
        //initilaisation de la liste des nodes
        List<Node> nodes = graph.getNodes();
        //l'initialisation des labels
        
        for (Node node: nodes) {
        	labelArray1[node.getId()] = new Label(node);
        	labelArray2[node.getId()] = new Label(node);
        }
        
        //________________________________________________________________
        //first cycle
        dijkstra(labelArray1, data, graph);
        
        data.permuteOriginDest();
        graph = data.getGraph();
        
        //________________________________________________________________//
        //second cycle
        dijkstra(labelArray2, data, graph);
        
        Node origin = data.getOrigin();
        Label originL = labelArray2[data.getOrigin().getId()];
        Node destination = data.getDestination();
        Label destinationL = labelArray2[data.getDestination().getId()];
        Label x = null;
        if(destinationL.estMarque() == false || destinationL.getCout() == 0) {
        	solution = new ProblemOuvertSolution(data, Status.INFEASIBLE);
        } else {
        	
        	this.chemin = AfficherSol(graph, data, labelArray1, labelArray2);
        	solution = new ProblemOuvertSolution(data, Status.OPTIMAL, chemin);
	    	
        }

		return solution;
	}
	

	protected void dijkstra(Label[] labelArray, ProblemOuvertData data, Graph graph) {
		
		// Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
		//variables pas nécessaire mais plus propre
        origin = data.getOrigin();
        originL = labelArray[data.getOrigin().getId()];
        destination = data.getDestination();
        destinationL = labelArray[data.getDestination().getId()];
        
		//initialisation du tas
        BinaryHeap<Label> heap = new BinaryHeap<>();
        
        //on insere origin dans le tas
        labelArray[origin.getId()].setCout(0);
        heap.insert(labelArray[origin.getId()]);
        
		x = null;
		while (!labelArray[destination.getId()].estMarque() && !heap.isEmpty() ) {
        	x = heap.findMin();
        	heap.remove(x);
        	x.marquer();
        	
        	for (Arc iter: x.sommetCourant().getSuccessors()) {
        		if (data.isAllowed(iter)) {
	        		Label y = labelArray[iter.getDestination().getId()];
	        		
	        		
	        		if(! y.estMarque()) {
	        			if (y.getCout() > x.getCout() + data.getCost(iter)) {
	        				try {
	        					heap.remove(y);
	        				} catch(ElementNotFoundException e) {
	        					;
	    		    		}
	        				y.setCout(x.getCout() + data.getCost(iter));
	        				y.setPere(iter);
	        				heap.insert(y);
	        			}
	        		}
        		}
        	}        	
        }
		
	}

	public Path AfficherSol(Graph graph,  ProblemOuvertData data, Label[] labelArray1, Label[] labelArray2 ) {
		// The destination has been found, notify the observers.
        notifyDestinationReached(data.getDestination());
            
         // Create the path from the array of predecessors...
    	ArrayList<Arc> arcs = new ArrayList<>();
    	
    	while(!x.equals(originL)) {
    		arcs.add(x.getPere());
    		x = labelArray2[x.getPere().getOrigin().getId()];
    		//verification bien Dij: oui décroissant 
    		//System.out.println("cout label: " + x.getCout());
    	}
    	// Reverse the path...
        Collections.reverse(arcs);
        
        //critere de comparasion, décommenter selon le critere choisi
        double critere;
        
        //critere de durrée
        critere = 0.15;
        //critere de distance
        //critere = 0.30;
        
        Path path = new Path(graph, arcs);
        double cheminLen = path.getLength();
        
        
        double cout1, cout2 = 0;
        for (int i = 0; i < graph.size(); i++) {
        	cout1 = labelArray1[i].getCout();
        	cout2 = labelArray2[i].getCout();
        	if (Double.isFinite(cout1) && (Double.isFinite(cout2))) {
        		//if ((1-critere)*cout2 <= (1+critere)*cout1 && (1+critere)*cout2 >= (1-critere)*cout1){
        		if (((1-critere)*cout1 <= cout2 && cout2 <=(1+critere)*cout1) && ((1-critere)*cout2 <= cout1 && cout1 <=(1+critere)*cout2)){
        			System.out.println("cout1 : " + cout1 + " cout2: " + cout2);
        			notifyNodeReached(labelArray1[i].sommetCourant());
        		}
        	}
        }
        return path;
	}
}
