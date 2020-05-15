package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class LabelAstar extends Label {

	private double EstimatedCost;
	
	public LabelAstar(Node SommetCourant) {
		super(SommetCourant);
		this.EstimatedCost = Double.POSITIVE_INFINITY;
	}

	public double getEstimatedCost() {
		return EstimatedCost;
	}

	public void setEstimatedCost(double estimatedCost) {
		EstimatedCost = estimatedCost;
	}
	
	public double getTotalCost() {
		return EstimatedCost + this.cout;
	}
	
	public int compareTo(LabelAstar other) {
		return Double.compare(this.getTotalCost(), other.getTotalCost());
	}
	
}
