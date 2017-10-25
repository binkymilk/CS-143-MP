/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 *
 * @author mk
 */
public class GraphProperties {

	public int[][] adjacencyMatrix;
	public int[][] distanceMatrix;
	public Vector<VertexPair> vpList;
	public int path[];

	/**
	 * This function searches for the vertex with the maximum degree.
	 * The degree of each vertex is divided by the maximum degree.
	 * @param vList Vector list of vertices
	 */
	public void computeNormalizedDegree(Vector<Vertex> vList) {
		float maxDegree = 0;
		for (Vertex v : vList) {
			if(v.getDegree() > maxDegree) maxDegree = v.getDegree();
		}
		for (Vertex v : vList) {
			v.setNormalizedDegree((float)v.getDegree()/maxDegree);
		}
	}

	/**
	 * @author Cess
	 * Betweenness
	 * @param vList
	 * @param eList
	 * @return 
	 * @return
	 */
	public void computeNormalizedBetweenness(Vector<Vertex> vList){
		for (int i=0; i<vList.size(); i++){
			float betweennessValueOfVertex = 0;
			int normalizationValue = 0;
			for(int j=0; j<vList.size(); j++){
				for(int k = j+1; k<vList.size(); k++){
					if(i!=j && j!=k && i!=k){
						normalizationValue++;
						VertexPair vp = new VertexPair(vList.get(j), vList.get(k));
						betweennessValueOfVertex += vp.getBetweenessCentrality(vList.get(i));
					} 
				} 
			} vList.get(i).setNormalizedBetweenness(betweennessValueOfVertex/normalizationValue);
		}
	}

	/**
	 * This function checks if the graph is a tree
	 * @param vList Vector list of vertices
	 * @param eList Vector list of edges
	 * @return true if the graph is connected and the number of edges is equal to the number of vertices - 1
	 * 		   false otherwise
	 */
	public boolean isTree(Vector<Vertex> vList, Vector<Edge> eList) {
		return graphConnectivity(vList) && eList.size() == vList.size() - 1;
	}

	public boolean isComplete(Vector<Vertex> vList, Vector<Edge> eList) {
		return eList.size() == (vList.size() * (vList.size() - 1)) / 2;
	}

	/** 
	 * This function checks if the given graph is hamiltonian--if it contains a hamiltonian cycle
	 * @param vList the list of vertices in the graph
	 * @param matrix the adjacency matrix obtained from the graph
	 * @return true if the graph is hamiltonian, false if not
	 */
	public boolean isHamiltonian(Vector<Vertex> vList, int[][] matrix) {
		// Array of indices of vertices
		path = new int[matrix.length];

		// Initialize hamiltonian path
		for(int i = 0; i < matrix.length; i++) {
			path[i] = -1;
		}

		// Set first vertex of path to vertex in vList with index 0
		path[0] = 0;

		if(recursiveHamiltonian(matrix, path, 1) == false) {
			System.out.println("Solution does not exist.");
			return false;
		}

		return true;
	}

	private boolean isSafe(int v, int[][] matrix, int path[], int currV) {
		// Check if the current vertex is adjacent to previous vertex
		if(matrix[path[currV - 1]][v] == 0) {
			return false;
		}

		// Check if the current vertex is already in the path
		for(int i = 0; i < currV; i++) {
			if(path[i] == v) return false;
		}

		return true;
	}

	private boolean recursiveHamiltonian(int[][] matrix, int[] path, int currV) {
		// If index of current vertex is the length of the matrix, end the recursion
		if(currV == matrix.length) {
			// If there is an edge between the last vertex and the starting vertex
			if(matrix[path[currV - 1]][path[0]] == 1) {
				return true;
			} else {
				return false;
			}
		}

		// Choose other vertices to test if graph is Hamiltonian.
		for(int i = 1; i < matrix.length; i++) {
			// Check if current vertex passes the test
			if(isSafe(i, matrix, path, currV)) {
				path[currV] = i;
				if(recursiveHamiltonian(matrix, path, currV + 1) == true) {
					return true;
				}

				// If adding vertex i doesn't lead to a solution, then remove it
				path[currV] = -1;
			}
		}

		// If no vertex can be added to Hamiltonian cycle, return false
		return false;
	}


	/**
	 * @author Cess
	 * This function determines if a graph is Eulerian based on the degree of each node.
	 * @param vList contains all vertices of the graph.
	 * @return The function returns true if the graph is Eulerian. 
	 */
	public boolean isEulerian(Vector<Vertex> vList){
		for(int i=0; i<vList.size(); i++){
			if(vList.get(i).getDegree() %2 == 0){
				continue;
			} else return false;
		} return true;
	}

	/**
	 * @author Cess
	 * This function visits each vertex and determines if a cycle can be formed from that vertex.
	 * @param vList contains all vertices of the graph.
	 * @return The function returns true if the graph contains a cycle.
	 */
	public boolean isCyclic(Vector<Vertex> vList){
		Boolean visited[] = new Boolean[vList.size()];
		for (int i=0; i<vList.size(); i++){
			visited[i] = false;
		}

		for(int i=0; i<vList.size(); i++){
			if(!visited[i]){
				if(isCyclicUtil(vList, i, visited, -1)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @author Cess
	 * This function is a recursive function used by determineIfGraphIsCyclic() to determine if a vertex is part of cycle.
	 * @param vList contains all vertices of the graph.
	 * @param v is the position of the vertex in vList.
	 * @param visited[] is a boolean array. visited[] is true if a vertex is already checked if it contains a cycle.
	 * @param parent is the position of the vertex which was recently visited that is adjacent to the current vertex. 
	 * @return The function returns true if a cycle is found.
	 */
	private boolean isCyclicUtil(Vector<Vertex> vList, int v, Boolean[] visited, int parent){
		visited[v] = true;

		for (int i=0; i<vList.size(); i++){
			if(vList.get(i).connectedToVertex(vList.get(v))){
				if(!visited[i]){
					if(isCyclicUtil(vList, i, visited, v)){
						return true;
					}
				}
				else if (i != parent)
					return true;
			}

		} return false;
	}

	public int[][] generateAdjacencyMatrix(Vector<Vertex> vList, Vector<Edge> eList) {
		adjacencyMatrix = new int[vList.size()][vList.size()];

		for (int a = 0; a < vList.size(); a++)//initialize
		{
			for (int b = 0; b < vList.size(); b++) {
				adjacencyMatrix[a][b] = 0;
			}
		}

		for (int i = 0; i < eList.size(); i++) {
			adjacencyMatrix[vList.indexOf(eList.get(i).vertex1)][vList.indexOf(eList.get(i).vertex2)] = 1;
			adjacencyMatrix[vList.indexOf(eList.get(i).vertex2)][vList.indexOf(eList.get(i).vertex1)] = 1;
		}
		return adjacencyMatrix;
	}

	public int[][] generateDistanceMatrix(Vector<Vertex> vList) {
		distanceMatrix = new int[vList.size()][vList.size()];

		for (int a = 0; a < vList.size(); a++)//initialize
		{
			for (int b = 0; b < vList.size(); b++) {
				distanceMatrix[a][b] = 0;
			}
		}

		VertexPair vp;
		int shortestDistance;
		for (int i = 0; i < vList.size(); i++) {
			for (int j = i + 1; j < vList.size(); j++) {
				vp = new VertexPair(vList.get(i), vList.get(j));
				shortestDistance = vp.getShortestDistance();
				distanceMatrix[vList.indexOf(vp.vertex1)][vList.indexOf(vp.vertex2)] = shortestDistance;
				distanceMatrix[vList.indexOf(vp.vertex2)][vList.indexOf(vp.vertex1)] = shortestDistance;
			}
		}
		return distanceMatrix;
	}

	public void displayContainers(Vector<Vertex> vList) {
		vpList = new Vector<VertexPair>();
		int[] kWideGraph = new int[10];
		for (int i = 0; i < kWideGraph.length; i++) {
			kWideGraph[i] = -1;
		}



		VertexPair vp;

		for (int a = 0; a < vList.size(); a++) {    // assign vertex pairs
			for (int b = a + 1; b < vList.size(); b++) {
				vp = new VertexPair(vList.get(a), vList.get(b));
				vpList.add(vp);
				int longestWidth = 0;
				System.out.println(">Vertex Pair " + vList.get(a).name + "-" + vList.get(b).name + "\n All Paths:");
				vp.generateVertexDisjointPaths();
				for (int i = 0; i < vp.VertexDisjointContainer.size(); i++) {//for every container of the vertex pair
					int width = vp.VertexDisjointContainer.get(i).size();
					Collections.sort(vp.VertexDisjointContainer.get(i), new descendingWidthComparator());
					int longestLength = vp.VertexDisjointContainer.get(i).firstElement().size();
					longestWidth = Math.max(longestWidth, width);
					System.out.println("\tContainer " + i + " - " + "Width=" + width + " - Length=" + longestLength);

					for (int j = 0; j < vp.VertexDisjointContainer.get(i).size(); j++) //for every path in the container
					{
						System.out.print("\t\tPath " + j + "\n\t\t\t");
						for (int k = 0; k < vp.VertexDisjointContainer.get(i).get(j).size(); k++) {
							System.out.print("-" + vp.VertexDisjointContainer.get(i).get(j).get(k).name);
						}
						System.out.println();
					}

				}
				//d-wide for vertexPair
				for (int k = 1; k <= longestWidth; k++) { // 1-wide, 2-wide, 3-wide...
					int minLength = 999;
					for (int m = 0; m < vp.VertexDisjointContainer.size(); m++) // for each container with k-wide select shortest length
					{
						minLength = Math.min(minLength, vp.VertexDisjointContainer.get(m).size());
					}
					if (minLength != 999) {
						System.out.println(k + "-wide for vertexpair(" + vp.vertex1.name + "-" + vp.vertex2.name + ")=" + minLength);
						kWideGraph[k] = Math.max(kWideGraph[k], minLength);
					}
				}
			}
		}

		for (int i = 0; i < kWideGraph.length; i++) {
			if (kWideGraph[i] != -1) {
				System.out.println("D" + i + "(G)=" + kWideGraph[i]);
			}
		}


	}

	public void drawAdjacencyMatrix(Graphics g, Vector<Vertex> vList, int x, int y) {
		int cSize = 20;
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y-30, vList.size() * cSize+cSize, vList.size() * cSize+cSize);
		g.setColor(Color.black);
		g.drawString("AdjacencyMatrix", x, y - cSize);
		for (int i = 0; i < vList.size(); i++) {
			g.setColor(Color.RED);
			g.drawString(vList.get(i).name, x + cSize + i * cSize, y);
			g.drawString(vList.get(i).name, x, cSize + i * cSize + y);
			g.setColor(Color.black);
			for (int j = 0; j < vList.size(); j++) {
				g.drawString("" + adjacencyMatrix[i][j], x + cSize * (j + 1), y + cSize * (i + 1));
			}
		}
	}

	public void drawDistanceMatrix(Graphics g, Vector<Vertex> vList, int x, int y) {
		int cSize = 20;
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y-30, vList.size() * cSize+cSize, vList.size() * cSize+cSize);
		g.setColor(Color.black);
		g.drawString("ShortestPathMatrix", x, y - cSize);
		for (int i = 0; i < vList.size(); i++) {
			g.setColor(Color.RED);
			g.drawString(vList.get(i).name, x + cSize + i * cSize, y);
			g.drawString(vList.get(i).name, x, cSize + i * cSize + y);
			g.setColor(Color.black);
			for (int j = 0; j < vList.size(); j++) {
				g.drawString("" + distanceMatrix[i][j], x + cSize * (j + 1), y + cSize * (i + 1));
			}
		}
	}

	public Vector<Vertex> vertexConnectivity(Vector<Vertex> vList) {
		Vector<Vertex> origList = new Vector<Vertex>();
		Vector<Vertex> tempList = new Vector<Vertex>();
		Vector<Vertex> toBeRemoved = new Vector<Vertex>();
		Vertex victim;


		origList.setSize(vList.size());
		Collections.copy(origList, vList);

		int maxPossibleRemove = 0;
		while (graphConnectivity(origList)) {
			Collections.sort(origList, new ascendingDegreeComparator());
			maxPossibleRemove = origList.firstElement().getDegree();

			for (Vertex v : origList) {
				if (v.getDegree() == maxPossibleRemove) {
					for (Vertex z : v.connectedVertices) {
						if (!tempList.contains(z)) {
							tempList.add(z);
						}
					}
				}
			}

			while (graphConnectivity(origList) && tempList.size() > 0) {
				Collections.sort(tempList, new descendingDegreeComparator());
				victim = tempList.firstElement();
				tempList.removeElementAt(0);
				origList.remove(victim);
				for (Vertex x : origList) {
					x.connectedVertices.remove(victim);
				}
				toBeRemoved.add(victim);
			}
			tempList.removeAllElements();
		}

		return toBeRemoved;
	}

	/**
	 * from private to public
	 * @param vList
	 * @return
	 */

	public boolean graphConnectivity(Vector<Vertex> vList) {

		Vector<Vertex> visitedList = new Vector<Vertex>();

		recurseGraphConnectivity(vList.firstElement().connectedVertices, visitedList); //recursive function
		if (visitedList.size() != vList.size()) {
			return false;
		} else {
			return true;
		}
	}

	private void recurseGraphConnectivity(Vector<Vertex> vList, Vector<Vertex> visitedList) {
		for (Vertex v : vList) {
			{
				if (!visitedList.contains(v)) {
					visitedList.add(v);
					recurseGraphConnectivity(v.connectedVertices, visitedList);
				}
			}
		}
	}

	private class ascendingDegreeComparator implements Comparator {

		public int compare(Object v1, Object v2) {

			if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
				return 1;
			} else if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	private class descendingDegreeComparator implements Comparator {

		public int compare(Object v1, Object v2) {

			if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
				return -1;
			} else if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private class descendingWidthComparator implements Comparator {

		public int compare(Object v1, Object v2) {

			if (((Vector<Vertex>) v1).size() > (((Vector<Vertex>) v2).size())) {
				return -1;
			} else if (((Vector<Vertex>) v1).size() < (((Vector<Vertex>) v2).size())) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
