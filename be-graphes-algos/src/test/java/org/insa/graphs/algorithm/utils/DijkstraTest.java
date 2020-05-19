package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraTest {
	
	// Small graph use for test
	//graphe routière
	private static Graph insa;
	
	//graphe routière
	private static Graph guadeloupe;
	
	//graphe non-routière
	private static Graph carre;
	
	//Liste de nodes pour le graphe haute-garonne
	private static List<Node> nodesINSA;
	
	//Liste de nodes pour le graphe guadeloupe
	private static List<Node> nodesGdlpe;	

	//Liste de nodes pour le graphe carre
	private static List<Node> nodesCarre;	
	
	//ArcInspector
	private static ArcInspector timeInsp, distanceInsp;
	
	//variables ShortestPathData et ShortestPathAlgorithm qu'on utilise lors de nos tests
	ShortestPathData cheminData;
	ShortestPathSolution cheminSolution, cheminSolutionOracle;
	
	//générateur de noeuds aléatoires
	Node randomOrigin, randomDest;
	Random getRandom = new Random();
	
	@SuppressWarnings("resource")
	@BeforeClass
	public static void initAll() throws IOException {
		//initialisation des graphes
		DataInputStream DIS1 = new DataInputStream(
	            new BufferedInputStream(new FileInputStream("../Cartes/guadeloupe.mapgr")));
		guadeloupe = new BinaryGraphReader(DIS1).read();
		
		DataInputStream DIS2 = new DataInputStream(
	            new BufferedInputStream(new FileInputStream("../Cartes/insa.mapgr")));
		insa = new BinaryGraphReader(DIS2).read();
		
		DataInputStream DIS3 = new DataInputStream(
	            new BufferedInputStream(new FileInputStream("../Cartes/carre.mapgr")));
		carre = new BinaryGraphReader(DIS3).read();
		
		
		//initialisation des nodes de guadeloupe
		nodesGdlpe = guadeloupe.getNodes(); 
		
		//initialisation des nodes de haute-garonne
		nodesINSA = insa.getNodes(); 
		
		//initialisation des nodes de Carre
		nodesCarre = carre.getNodes(); 
		
		//initialisation des ArcInspector
		timeInsp =  ArcInspectorFactory.getAllFilters().get(0);
		distanceInsp = ArcInspectorFactory.getAllFilters().get(2);
		
	}
	
	@Test
	public void cheminExistant() {
		//Test entre Noeuds dont on est sur qu'un chemin existe sur HG avec distance
		cheminData = new ShortestPathData(insa, insa.get(10), insa.get(20), distanceInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		assertTrue(cheminSolution.isFeasible());
		
		//Test entre Noeuds dont on est sur qu'un chemin existe sur guadeloupe avec temps
		cheminData = new ShortestPathData(guadeloupe, guadeloupe.get(32997), guadeloupe.get(32930), timeInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		assertTrue(cheminSolution.isFeasible());
		
	}
	
	@Test
	public void cheminInexistant() {
		//Test entre Noeuds dont on est sur qu'un chemin n'existe pas sur guadeloupe avec temps
		cheminData = new ShortestPathData(guadeloupe, guadeloupe.get(33685), guadeloupe.get(15117), timeInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		assertFalse(cheminSolution.isFeasible());
		
		//Test entre Noeuds dont on est sur qu'un chemin n'existe pas sur guadeloupe avec distance
		cheminData = new ShortestPathData(guadeloupe, guadeloupe.get(32997), guadeloupe.get(4048), distanceInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		assertFalse(cheminSolution.isFeasible());
		
	}
	
	@Test
	public void cheminLongueurNull(){
		//Test entre Noeuds dont on est sur qu'un chemin nul sur guadeloupe avec temps
		cheminData = new ShortestPathData(guadeloupe, guadeloupe.get(33685), guadeloupe.get(33685), timeInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		assertFalse(cheminSolution.isFeasible());
		
		
		//Test entre Noeuds dont on est sur qu'un chemin nul sur insa avec distance
		cheminData = new ShortestPathData(insa, insa.get(53), insa.get(53), distanceInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		assertFalse(cheminSolution.isFeasible());
	}
	
	
	@Test
	public void testOracle() {
		//Verifie que le résultat généré par Dijkstra est le même que celui généré par Bellman-Ford
		
		//chemins Randoms pour INSA avec temps et vérifie que la distance est égale
		do {
			randomOrigin = nodesINSA.get(getRandom.nextInt(nodesINSA.size()));
			randomDest = nodesINSA.get(getRandom.nextInt(nodesINSA.size()));
		} while ( randomOrigin.equals(randomDest));
		
		cheminData = new ShortestPathData(insa, randomOrigin, randomDest, timeInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		cheminSolutionOracle = new BellmanFordAlgorithm(cheminData).doRun();
		
		//vérifir chemin est feasible et valide
		assertTrue(cheminSolution.isFeasible());
		assertTrue(cheminSolution.getPath().isValid());
		
		
		//verifie que la distance est identique
		assertEquals(cheminSolutionOracle.getPath().getLength(), cheminSolution.getPath().getLength(), 0);
		
		//verifie que le tps minimum est indentique est identique
		assertEquals(cheminSolutionOracle.getPath().getMinimumTravelTime(), cheminSolution.getPath().getMinimumTravelTime(), 0);
		
		//chemins Randoms pour carre avec distance et vérifie que la distance est égale
		do {
			randomOrigin = nodesCarre.get(getRandom.nextInt(nodesCarre.size()));
			randomDest = nodesCarre.get(getRandom.nextInt(nodesCarre.size()));
		} while ( randomOrigin.equals(randomDest));
		
		cheminData = new ShortestPathData(carre, randomOrigin, randomDest, distanceInsp);
		cheminSolution = new DijkstraAlgorithm(cheminData).doRun();
		cheminSolutionOracle = new BellmanFordAlgorithm(cheminData).doRun();
		
		//vérifier chemin est feasible et valide
		assertTrue(cheminSolution.isFeasible());
		assertTrue(cheminSolution.getPath().isValid());
		
		
		//verifie que la distance est identique
		assertEquals(cheminSolutionOracle.getPath().getLength(), cheminSolution.getPath().getLength(), 0);
		
		//verifie que le tps minimum est indentique est identique
		assertEquals(cheminSolutionOracle.getPath().getMinimumTravelTime(), cheminSolution.getPath().getMinimumTravelTime(), 0);
		
	}
	
	@Test
	public void testSansOtacle() {
		//Verification de notre algo dijkstra est correcte, sans l'oracle cette fois-ci
	}
	
	
	
	
	
}
