package org.insa.graphs.algorithm.utils;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;

@SuppressWarnings("unused")
public class DijkstraTest {
	
	// Small graph use for tests
	//graphe non-routière
    private  Graph carre;
    //graphe routière
	private  Graph hauteGaronne;
	
	
	@SuppressWarnings("resource")
	public void initAll() throws IOException {
		DataInputStream DIS1 = new DataInputStream(
	            new BufferedInputStream(new FileInputStream("/Users/nahombelay/Downloads/Graphes/Carre/carre.mapgr")));
		this.carre = new BinaryGraphReader(DIS1).read();
		
		DataInputStream DIS2 = new DataInputStream(
	            new BufferedInputStream(new FileInputStream("/Users/nahombelay/Downloads/Graphes/haute-garonne/france/hauteGaronne.mapgr")));
		this.hauteGaronne = new BinaryGraphReader(DIS2).read();
	}
	
	
	
	
	
}
