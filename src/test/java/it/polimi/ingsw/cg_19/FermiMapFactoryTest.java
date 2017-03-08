package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import common.Coordinate;
import common.Sector;
import common.SectorType;
import factories.FermiGameMapFactory;
import factories.GameMapFactory;

public class FermiMapFactoryTest {

	/**
	 * Test for the FermiMapFactory, these test are valid for any specific map
	 * factory that extends MapFactory
	 * 
	 * @author Giorgio Pea
	 * @see MapFactory, Map, FermiMapFactory, Coordinate, Sector, SectorType
	 */
	private GameMapFactory fermiMapFactory = new FermiGameMapFactory();

	private GameMap fermiMapFromFile = new FermiGameMapFactory().makeMap();

	private UndirectedGraph<Sector, DefaultEdge> fermiMapGraphFromFile = fermiMapFromFile
			.getGraph();

	private UndirectedGraph<Sector, DefaultEdge> fermiMapGraphStatic = this
			.makeFermiMapStaticGraph();

	/**
	 * Makes the correct graph of FermiMap
	 * 
	 * @return the correct graph of FermiMap
	 */
	private UndirectedGraph<Sector, DefaultEdge> makeFermiMapStaticGraph() {
		// Graph init
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		// Sector creation
		Sector s1 = new Sector(new Coordinate('h', 10), SectorType.SAFE);
		Sector s2 = new Sector(new Coordinate('h', 11), SectorType.SAFE);
		Sector s3 = new Sector(new Coordinate('i', 6), SectorType.SAFE);
		Sector s4 = new Sector(new Coordinate('i', 7), SectorType.DANGEROUS);
		Sector s5 = new Sector(new Coordinate('i', 9), SectorType.DANGEROUS);
		Sector s6 = new Sector(new Coordinate('i', 10), SectorType.SAFE);
		Sector s7 = new Sector(new Coordinate('i', 12), SectorType.DANGEROUS);
		Sector s8 = new Sector(new Coordinate('j', 1), SectorType.OPEN_RESCUE);
		Sector s9 = new Sector(new Coordinate('j', 3), SectorType.DANGEROUS);
		Sector s10 = new Sector(new Coordinate('j', 4), SectorType.SAFE);
		Sector s11 = new Sector(new Coordinate('j', 5), SectorType.OPEN_RESCUE);
		Sector s12 = new Sector(new Coordinate('j', 7), SectorType.SAFE);
		Sector s13 = new Sector(new Coordinate('j', 8), SectorType.SAFE);
		Sector s14 = new Sector(new Coordinate('j', 10), SectorType.SAFE);
		Sector s15 = new Sector(new Coordinate('j', 11), SectorType.SAFE);
		Sector s16 = new Sector(new Coordinate('j', 12), SectorType.SAFE);
		Sector s17 = new Sector(new Coordinate('k', 2), SectorType.SAFE);
		Sector s18 = new Sector(new Coordinate('k', 3), SectorType.SAFE);
		Sector s19 = new Sector(new Coordinate('k', 4), SectorType.DANGEROUS);
		Sector s20 = new Sector(new Coordinate('k', 8), SectorType.SAFE);
		Sector s21 = new Sector(new Coordinate('k', 11), SectorType.SAFE);
		Sector s22 = new Sector(new Coordinate('k', 13), SectorType.DANGEROUS);
		Sector s23 = new Sector(new Coordinate('l', 3), SectorType.DANGEROUS);
		Sector s24 = new Sector(new Coordinate('l', 4), SectorType.DANGEROUS);
		Sector s25 = new Sector(new Coordinate('l', 5), SectorType.SAFE);
		Sector s26 = new Sector(new Coordinate('l', 6), SectorType.SAFE);
		Sector s27 = new Sector(new Coordinate('l', 7), SectorType.SAFE);
		Sector s28 = new Sector(new Coordinate('l', 8), SectorType.SAFE);
		Sector s29 = new Sector(new Coordinate('l', 9), SectorType.ALIEN);
		Sector s30 = new Sector(new Coordinate('l', 10), SectorType.HUMAN);
		Sector s31 = new Sector(new Coordinate('l', 11), SectorType.SAFE);
		Sector s32 = new Sector(new Coordinate('l', 12), SectorType.SAFE);
		Sector s33 = new Sector(new Coordinate('m', 2), SectorType.DANGEROUS);
		Sector s34 = new Sector(new Coordinate('m', 3), SectorType.SAFE);
		Sector s35 = new Sector(new Coordinate('m', 4), SectorType.DANGEROUS);
		Sector s36 = new Sector(new Coordinate('m', 8), SectorType.SAFE);
		Sector s37 = new Sector(new Coordinate('m', 11), SectorType.SAFE);
		Sector s38 = new Sector(new Coordinate('m', 13), SectorType.SAFE);
		Sector s39 = new Sector(new Coordinate('n', 1), SectorType.OPEN_RESCUE);
		Sector s40 = new Sector(new Coordinate('n', 3), SectorType.DANGEROUS);
		Sector s41 = new Sector(new Coordinate('n', 4), SectorType.SAFE);
		Sector s42 = new Sector(new Coordinate('n', 5), SectorType.OPEN_RESCUE);
		Sector s43 = new Sector(new Coordinate('n', 7), SectorType.DANGEROUS);
		Sector s44 = new Sector(new Coordinate('n', 8), SectorType.SAFE);
		Sector s45 = new Sector(new Coordinate('n', 10), SectorType.SAFE);
		Sector s46 = new Sector(new Coordinate('n', 11), SectorType.SAFE);
		Sector s47 = new Sector(new Coordinate('n', 12), SectorType.DANGEROUS);
		Sector s48 = new Sector(new Coordinate('o', 6), SectorType.SAFE);
		Sector s49 = new Sector(new Coordinate('o', 7), SectorType.SAFE);
		Sector s50 = new Sector(new Coordinate('o', 9), SectorType.SAFE);
		Sector s51 = new Sector(new Coordinate('o', 10), SectorType.DANGEROUS);
		Sector s52 = new Sector(new Coordinate('o', 12), SectorType.SAFE);
		Sector s53 = new Sector(new Coordinate('p', 10), SectorType.SAFE);
		Sector s54 = new Sector(new Coordinate('p', 11), SectorType.DANGEROUS);
		// Vertices creation
		graph.addVertex(s1);
		graph.addVertex(s2);
		graph.addVertex(s3);
		graph.addVertex(s4);
		graph.addVertex(s5);
		graph.addVertex(s6);
		graph.addVertex(s7);
		graph.addVertex(s8);
		graph.addVertex(s9);
		graph.addVertex(s10);
		graph.addVertex(s11);
		graph.addVertex(s12);
		graph.addVertex(s13);
		graph.addVertex(s14);
		graph.addVertex(s15);
		graph.addVertex(s16);
		graph.addVertex(s17);
		graph.addVertex(s18);
		graph.addVertex(s19);
		graph.addVertex(s20);
		graph.addVertex(s21);
		graph.addVertex(s22);
		graph.addVertex(s23);
		graph.addVertex(s24);
		graph.addVertex(s25);
		graph.addVertex(s26);
		graph.addVertex(s27);
		graph.addVertex(s28);
		graph.addVertex(s29);
		graph.addVertex(s30);
		graph.addVertex(s31);
		graph.addVertex(s32);
		graph.addVertex(s33);
		graph.addVertex(s34);
		graph.addVertex(s35);
		graph.addVertex(s36);
		graph.addVertex(s37);
		graph.addVertex(s38);
		graph.addVertex(s39);
		graph.addVertex(s40);
		graph.addVertex(s41);
		graph.addVertex(s42);
		graph.addVertex(s43);
		graph.addVertex(s44);
		graph.addVertex(s45);
		graph.addVertex(s46);
		graph.addVertex(s47);
		graph.addVertex(s48);
		graph.addVertex(s49);
		graph.addVertex(s50);
		graph.addVertex(s51);
		graph.addVertex(s52);
		graph.addVertex(s53);
		graph.addVertex(s54);
		// Edge creation
		graph.addEdge(s1, s2);
		graph.addEdge(s1, s6);
		graph.addEdge(s2, s7);
		graph.addEdge(s3, s4);
		graph.addEdge(s3, s11);
		graph.addEdge(s4, s12);
		graph.addEdge(s5, s6);
		graph.addEdge(s5, s13);
		graph.addEdge(s6, s14);
		graph.addEdge(s7, s15);
		graph.addEdge(s7, s16);
		graph.addEdge(s8, s17);
		graph.addEdge(s9, s10);
		graph.addEdge(s9, s18);
		graph.addEdge(s9, s19);
		graph.addEdge(s10, s11);
		graph.addEdge(s10, s19);
		graph.addEdge(s12, s13);
		graph.addEdge(s12, s20);
		graph.addEdge(s13, s20);
		graph.addEdge(s14, s15);
		graph.addEdge(s14, s21);
		graph.addEdge(s15, s16);
		graph.addEdge(s15, s21);
		graph.addEdge(s16, s22);
		graph.addEdge(s17, s18);
		graph.addEdge(s18, s19);
		graph.addEdge(s18, s23);
		graph.addEdge(s19, s23);
		graph.addEdge(s19, s24);
		graph.addEdge(s20, s27);
		graph.addEdge(s20, s28);
		graph.addEdge(s21, s30);
		graph.addEdge(s21, s31);
		graph.addEdge(s22, s32);
		graph.addEdge(s23, s24);
		graph.addEdge(s23, s34);
		graph.addEdge(s23, s35);
		graph.addEdge(s24, s25);
		graph.addEdge(s24, s35);
		graph.addEdge(s25, s26);
		graph.addEdge(s26, s27);
		graph.addEdge(s27, s28);
		graph.addEdge(s27, s36);
		graph.addEdge(s28, s29);
		graph.addEdge(s28, s36);
		graph.addEdge(s29, s30);
		graph.addEdge(s30, s31);
		graph.addEdge(s30, s37);
		graph.addEdge(s31, s32);
		graph.addEdge(s31, s37);
		graph.addEdge(s32, s38);
		graph.addEdge(s33, s34);
		graph.addEdge(s33, s39);
		graph.addEdge(s34, s35);
		graph.addEdge(s34, s40);
		graph.addEdge(s35, s40);
		graph.addEdge(s35, s41);
		graph.addEdge(s36, s43);
		graph.addEdge(s36, s44);
		graph.addEdge(s37, s45);
		graph.addEdge(s37, s46);
		graph.addEdge(s38, s47);
		graph.addEdge(s40, s41);
		graph.addEdge(s41, s42);
		graph.addEdge(s42, s48);
		graph.addEdge(s43, s49);
		graph.addEdge(s43, s44);
		graph.addEdge(s44, s50);
		graph.addEdge(s45, s46);
		graph.addEdge(s45, s51);
		graph.addEdge(s46, s47);
		graph.addEdge(s46, s52);
		graph.addEdge(s47, s52);
		graph.addEdge(s48, s49);
		graph.addEdge(s50, s51);
		graph.addEdge(s51, s53);
		graph.addEdge(s52, s54);
		graph.addEdge(s53, s54);

		return graph;
	}

	// Just for tests purposes, a searchable graph is created using
	// fermiMapGraphStatic method
	private NeighborIndex<Sector, DefaultEdge> index = new NeighborIndex<Sector, DefaultEdge>(
			this.fermiMapGraphStatic);

	/**
	 * This test check if all the edges of fermi's map graph from file are
	 * present in the fermi's map graph generated statically by the
	 * makeFermiMapStaticGraph, which represent the correct graph
	 */
	@Test
	public void connectionsCompositionTest() {
		Iterator<Sector> iterator = fermiMapGraphStatic.vertexSet().iterator();
		while (iterator.hasNext()) {
			Sector source = iterator.next();
			Sector target = fermiMapFromFile.getSectorByCoords(source
					.getCoordinate());
			Iterator<Sector> targetIterator = fermiMapFromFile
					.getSearchableGraph().neighborListOf(target).iterator();
			while (targetIterator.hasNext()) {
				assertTrue(index.neighborListOf(source).contains(
						targetIterator.next()));
			}
		}
	}

	/**
	 * This test check if all the vertices of fermi's map graph from file are
	 * present in the fermi's map graph generated statically by the
	 * makeFermiMapStaticGraph, which represent the correct graph. A case in
	 * which no vertices are present is considered as well
	 */

	@Test
	public void sectorCompositionTest() {
		Iterator<Sector> iterator = fermiMapGraphStatic.vertexSet().iterator();
		while (iterator.hasNext()) {
			Sector current = iterator.next();
			assertEquals(current,
					fermiMapFromFile.getSectorByCoords(current.getCoordinate()));
		}
		assertEquals(0, fermiMapFactory.makeGraph(new File("empty_file.txt"))
				.vertexSet().size());
		assertEquals(0, fermiMapFactory.makeGraph(new File("empty_file.txt"))
				.edgeSet().size());
	}

	/**
	 * This test check if the number vertices and edges of fermi's map graph
	 * from file is the same as in the fermi's map graph generated statically by
	 * the makeFermiMapStaticGraph, which represent the correct graph.
	 */
	@Test
	public void sizeTest() {
		assertEquals(fermiMapGraphStatic.vertexSet().size(),
				fermiMapGraphFromFile.vertexSet().size());
		assertEquals(fermiMapGraphStatic.edgeSet().size(),
				fermiMapGraphFromFile.edgeSet().size());
	}

}