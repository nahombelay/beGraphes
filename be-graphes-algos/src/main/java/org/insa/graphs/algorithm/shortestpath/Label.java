package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class Label implements Comparable<Label>{
	
	private Node sommetC;
	private Arc sommetP;
	private double cout;
	private boolean marque;
	
	
	public Label(Node sommetC) {
		this.sommetC = sommetC;
		this.sommetP = null;
		this.marque = false;
		this.cout = Double.POSITIVE_INFINITY;	
	}
	
	
	public Node sommetCourant() {
		return this.sommetC;
	}
	
	public boolean estMarque() {
		return this.marque;
	}
	
	public void marquer() {
		this.marque = true;
	}
	
	public double getCout() {
		return this.cout;
	}
	
	public double getTotalCost() {
		return this.getCout();
	}
	public void setCout(double cout) {
		this.cout = cout;
	}
	
	public Arc getPere() {
		return this.sommetP;
	}
	
	public void setPere(Arc pere) {
		this.sommetP = pere;
	}


	@Override
	public int compareTo(Label other) {
		return Double.compare(this.getTotalCost(), other.getTotalCost());
	}
	
}
