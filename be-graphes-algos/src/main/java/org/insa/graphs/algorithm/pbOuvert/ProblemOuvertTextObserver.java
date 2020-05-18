package org.insa.graphs.algorithm.pbOuvert;

import java.io.PrintStream;
import org.insa.graphs.model.Node;

public class ProblemOuvertTextObserver implements ProblemOuvertObserver {

	private final PrintStream stream;

    public ProblemOuvertTextObserver(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void notifyOriginProcessed(Node node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyNodeReached(Node node) {
        stream.println("Node " + node.getId() + " reached.");
    }

    @Override
    public void notifyNodeMarked(Node node) {
        stream.println("Node " + node.getId() + " marked.");
    }

    @Override
    public void notifyDestinationReached(Node node) {
        // TODO Auto-generated method stub

    }
}
