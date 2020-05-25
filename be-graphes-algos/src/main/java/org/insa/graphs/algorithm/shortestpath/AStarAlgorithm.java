package org.insa.graphs.algorithm.shortestpath;

import java.util.List;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.algorithm.*;


public class AStarAlgorithm extends DijkstraAlgorithm {
	
    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    public ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        
        // Retrieve the graph.
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        
        //initialisation de la liste des labels
        LabelAstar[] labelArray = new LabelAstar[nbNodes];
        
        //initilaisation de la liste des nodes
        List<Node> nodes = graph.getNodes();
        
        double coutEstime;
        //l'initialisation des LabelAstar
        for (Node node: nodes) {
        	
            if(data.getMode() == AbstractInputData.Mode.TIME) {
                //fastest
            	coutEstime = node.getPoint().distanceTo(data.getDestination().getPoint())/(double)graph.getGraphInformation().getMaximumSpeed();
            } else {
                //shortest
            	coutEstime = node.getPoint().distanceTo(data.getDestination().getPoint());
            }
            
            labelArray[node.getId()] = new LabelAstar(node,coutEstime);
        }
        
        return djikstraRun(labelArray, data, graph);
        
    }
    
    
    
}
