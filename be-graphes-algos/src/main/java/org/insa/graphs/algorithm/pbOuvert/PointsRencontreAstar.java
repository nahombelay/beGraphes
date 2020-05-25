package org.insa.graphs.algorithm.pbOuvert;

import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.shortestpath.LabelAstar;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

@SuppressWarnings("unused")
public class PointsRencontreAstar extends PointsRencontreDijkstra {

	private static ProblemOuvertSolution solution;
    private Path chemin;
    private static Label x;
    private static Node origin, destination;
    private static Label originL, destinationL;

	public PointsRencontreAstar(AbstractInputData data) {
		super(data);
	}

	@Override
	protected ProblemOuvertSolution doRun() {
		ProblemOuvertData data = getInputData();
		
		// Retrieve the graph.
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        
        //initialisation de la liste des labels
        LabelAstar[] labelArray1 = new LabelAstar[nbNodes];
        LabelAstar[] labelArray2 = new LabelAstar[nbNodes];
        
        double coutEstime;
        
        //initilaisation de la liste des nodes
        List<Node> nodes = graph.getNodes();
        //l'initialisation des labels
        
        for (Node node: nodes) {
        	
            if(data.getMode() == AbstractInputData.Mode.TIME) {
                //fastest
            	coutEstime = node.getPoint().distanceTo(data.getDestination().getPoint())/(double)graph.getGraphInformation().getMaximumSpeed();
            } else {
                //shortest
            	coutEstime = node.getPoint().distanceTo(data.getDestination().getPoint());
            }
            
            labelArray1[node.getId()] = new LabelAstar(node, coutEstime);
        	labelArray2[node.getId()] = new LabelAstar(node, coutEstime);
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

	

}
