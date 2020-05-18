package org.insa.graphs.algorithm.pbOuvert;

import org.insa.graphs.algorithm.AbstractAlgorithm;
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;


public abstract class ProblemOuvertAlgorithm extends AbstractAlgorithm<ProblemOuvertObserver>{

	protected ProblemOuvertAlgorithm(AbstractInputData data) {
		super(data);
	}

	@Override
    public ProblemOuvertSolution run() {
        return (ProblemOuvertSolution) super.run();
    }

    @Override
    protected abstract ProblemOuvertSolution doRun();

    @Override
    public ProblemOuvertData getInputData() {
        return (ProblemOuvertData) super.getInputData();
    }
    

    public void notifyOriginProcessed(Node node) {
        for (ProblemOuvertObserver obs: getObservers()) {
            obs.notifyOriginProcessed(node);
        }
    }

    
    public void notifyNodeReached(Node node) {
        for (ProblemOuvertObserver obs: getObservers()) {
            obs.notifyNodeReached(node);
        }
    }

    public void notifyNodeMarked(Node node) {
        for (ProblemOuvertObserver obs: getObservers()) {
            obs.notifyNodeMarked(node);
        }
    }

    public void notifyDestinationReached(Node node) {
        for (ProblemOuvertObserver obs: getObservers()) {
            obs.notifyDestinationReached(node);
        }
    }

}
