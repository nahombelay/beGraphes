package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPerformance {

	// Small graph use for test
	//graphe routière
	private static Graph insa;
	
	//graphe routière
	private static Graph guadeloupe;
	
	//graphe non-routière
	private static Graph carre;
	
	
	//Array qui contient les nodes des trois cartes
	private static ArrayList<Graph> ListCartes;
	
	//ArcInspector
	private static ArcInspector timeInsp, distanceInsp;
	
	//variables ShortestPathData et ShortestPathAlgorithm qu'on utilise lors de nos tests
	ShortestPathData cheminData;
	ShortestPathSolution cheminSolutionDij, cheminSolutionBellman, cheminSolutionAstar;
	
	//générateur de noeuds aléatoires
	Node randomOrigin, randomDest;
	Random getRandom = new Random();
	
	//timer
	long timerStart, timerEnd;
	
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
		
		
		//initialise de NodesCartes
		ListCartes = new ArrayList<Graph>(Arrays.asList(insa,  guadeloupe));
		
		//initialisation des ArcInspector
		timeInsp =  ArcInspectorFactory.getAllFilters().get(0);
		distanceInsp = ArcInspectorFactory.getAllFilters().get(2);
		
	}
	
	@Test
	public void performance() {
		for (Graph carte: ListCartes) {
			for (int i = 0; i < 3; i++) {
				//test avec time et distance inspector
				do {
					randomOrigin = carte.getNodes().get(getRandom.nextInt(carte.size()-1));
					randomDest = carte.getNodes().get(getRandom.nextInt(carte.size()-1));
				} while ( randomOrigin.equals(randomDest));
				System.out.println("Node A: " + randomOrigin.getId() + " Node B: " + randomDest.getId());
				for (ArcInspector insp: (new ArrayList<ArcInspector>(Arrays.asList(timeInsp, distanceInsp)))) {
					//chemins Randoms pour INSA avec temps et vérifie que la distance est égale
					
					cheminData = new ShortestPathData(carte, randomOrigin, randomDest, insp);
					//test Dijkstra
					timerStart = System.currentTimeMillis();
					cheminSolutionDij = new DijkstraAlgorithm(cheminData).doRun();
					timerEnd = System.currentTimeMillis();
					
					System.out.println("Mode : Dijkstra  Carte: " + carte.getMapName() + " Durée d'éxecution: " + (timerEnd - timerStart)+ "ms");
					if (cheminSolutionDij.isFeasible()) {
						System.out.println("Mode: " + insp.toString() + " Cout: " + cheminSolutionDij.getPath().getLength() + cheminSolutionDij.getPath().getMinimumTravelTime());
					} else {
						System.out.println("Chemin non valide");
					}
					System.out.println("-----------------------------------------------------");
					//test A*
					timerStart = System.currentTimeMillis();
					cheminSolutionAstar = new AStarAlgorithm(cheminData).doRun();
					timerEnd = System.currentTimeMillis();
					
					System.out.println("Mode : A*  Carte: " + carte.getMapName() + " Durée d'éxecution: " + (timerEnd - timerStart)+ "ms");
					if (cheminSolutionAstar.isFeasible()) {
						System.out.println("Mode: " + insp.toString() + " Cout: " + cheminSolutionAstar.getPath().getLength() + cheminSolutionAstar.getPath().getMinimumTravelTime());
						assertEquals(cheminSolutionAstar.getPath().getLength(), cheminSolutionDij.getPath().getLength(), 0);
						assertEquals(cheminSolutionAstar.getPath().getMinimumTravelTime(), cheminSolutionDij.getPath().getMinimumTravelTime(), 0);
					} else {
						System.out.println("Chemin non valide");
					}
					System.out.println("-----------------------------------------------------");
					
					//test Bellman
					timerStart = System.currentTimeMillis();
					cheminSolutionBellman = new BellmanFordAlgorithm(cheminData).doRun();	
					timerEnd = System.currentTimeMillis();
					
					System.out.println("Mode : Bellman  Carte: " + carte.getMapName() + " Durée d'éxecution: " + (timerEnd - timerStart)+ "ms");
					if (cheminSolutionBellman.isFeasible()) {
						System.out.println("Mode: " + insp.toString() + " Cout: " + cheminSolutionBellman.getPath().getLength() + cheminSolutionBellman.getPath().getMinimumTravelTime());
						assertEquals(cheminSolutionAstar.getPath().getLength(), cheminSolutionBellman.getPath().getLength(), 0);
						assertEquals(cheminSolutionAstar.getPath().getMinimumTravelTime(), cheminSolutionBellman.getPath().getMinimumTravelTime(), 0);
					} else {
						System.out.println("Chemin non valide");
					}
					System.out.println("-----------------------------------------------------");
					System.out.println("-----------------------------------------------------");
					
					assertEquals(1,1);
				}
			}
		}
	}
}
