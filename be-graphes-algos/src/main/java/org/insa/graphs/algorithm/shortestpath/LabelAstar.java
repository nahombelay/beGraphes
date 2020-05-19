package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class LabelAstar extends Label {

	private double EstimatedCost;
	
	public LabelAstar(Node SommetCourant) {
		super(SommetCourant);
		this.EstimatedCost = Double.POSITIVE_INFINITY;
	}

	public double getEstimatedCost() {
		return this.EstimatedCost;
	}

	public void setEstimatedCost(double estimatedCost) {
		EstimatedCost = estimatedCost;
	}
	
	@Override
	public double getTotalCost() {
		return this.getEstimatedCost() + this.getCout();
	}
	
	public int compareTo(LabelAstar other) {
		return Double.compare(this.getEstimatedCost(), other.getTotalCost() - other.getCout());
	}
	
}
