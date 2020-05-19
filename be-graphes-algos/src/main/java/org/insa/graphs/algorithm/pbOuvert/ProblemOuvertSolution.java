package org.insa.graphs.algorithm.pbOuvert;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Path;

public class ProblemOuvertSolution extends AbstractSolution {

	// Optimal solution.
    private final Path path;
    
    public ProblemOuvertSolution(ProblemOuvertData data, Status status) {  
		super(data, status);
		this.path = null;
    }
    
    public ProblemOuvertSolution(ProblemOuvertData data, Status status, Path path) {  
		super(data, status);
		this.path = path;
    }
    
    @Override
    public ProblemOuvertData getInputData() {
        return (ProblemOuvertData) super.getInputData();
    }

	public Path getPath() {
		return path;
	}
	
	@Override
    public String toString() {
        String info = null;
        if (!isFeasible()) {
            info = String.format("No path found from node #%d to node #%d",
                    getInputData().getOrigin().getId(), getInputData().getDestination().getId());
        }
        else {
            double cost = 0;
            for (Arc arc: getPath().getArcs()) {
                cost += getInputData().getCost(arc);
            }
            info = String.format("Found a path from node #%d to node #%d",
                    getInputData().getOrigin().getId(), getInputData().getDestination().getId());
            if (getInputData().getMode() == Mode.LENGTH) {
                info = String.format("%s, %.4f kilometers", info, cost / 1000.0);
            }
            else {
                info = String.format("%s, %.4f minutes", info, cost / 60.0);
            }
        }
        info += " in " + getSolvingTime().getSeconds() + " seconds.";
        return info;
    }
}
