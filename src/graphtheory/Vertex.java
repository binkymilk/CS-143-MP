/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Vector;
import java.awt.Graphics;

/**
 *
 * @author mk
 */
public class Vertex implements Comparable {

    public String name;
    public Point location;
    public float normalizedDegree;
    public float normalizedBetweenness;
    public float normalizedCloseness;
    public boolean wasFocused;
    public boolean wasClicked;
    public boolean isDegree;
    public boolean isBetweenness;
    public boolean isCloseness;
    private int size1 = 30;
    private int size2 = 40;
    public Vector<Vertex> connectedVertices;

    public Vertex(String name, int x, int y) {
        this.name = name;
        location = new Point(x, y);
        connectedVertices = new Vector<Vertex>();
    }

    public void addVertex(Vertex v) {
        connectedVertices.add(v);
    }

    public boolean hasIntersection(int x, int y) {
        double distance = Math.sqrt(Math.pow((x - location.x), 2) + Math.pow((y - location.y), 2));

        if (distance > size2 / 2) {
            return false;
        } else {
            return true;
        }
    }

    public boolean connectedToVertex(Vertex v) {
        if (connectedVertices.contains(v)) {
            return true;
        }
        return false;
    }

    public int getDegree() {
        return connectedVertices.size();
    }
    
    public void setNormalizedDegree(float normalizedDegree) {
    	this.normalizedDegree = normalizedDegree;
    }
    
    public float getNormalizedDegree() {
    	return this.normalizedDegree;
    }
    
    public void setNormalizedBetweenness(float normalizedBetweenness){
    	this.normalizedBetweenness = normalizedBetweenness;
    }
    
    public float getNormalizedBetweenness(){
    	return this.normalizedBetweenness;
    }
    
    public void setNormalizedCloseness(float normalizedCloseness){
    	this.normalizedCloseness = normalizedCloseness;
    }
    
    public float getNormalizedCloseness(){
    	return this.normalizedCloseness;
    }

    public int compareTo(Object v) {
        if (((Vertex) v).getDegree() > getDegree()) {
            return 1;
        } else if (((Vertex) v).getDegree() < getDegree()) {
            return -1;
        } else {
            return 0;
        }
    }

    public void draw(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
            g.drawString(name, location.x, location.y);
        } else if (wasFocused) {
        	//System.out.println("was focused");
            g.setColor(Color.blue);
            g.drawString(name, location.x, location.y);
        } else if (isDegree) {		// set size of circle to normalized degree
        	int normalized_size2 = size2 + (int)(this.normalizedDegree*10*2);
        	int normalized_size1 = size1 + (int)(this.normalizedDegree*10*2);
        	g.fillOval(location.x - normalized_size2 / 2, location.y - normalized_size2 / 2, normalized_size2, normalized_size2);
            g.setColor(Color.WHITE);
            g.fillOval(location.x - normalized_size1 / 2, location.y - normalized_size1 / 2, normalized_size1, normalized_size1);
            g.setColor(Color.BLACK);
            
        	g.drawString(String.format("%.2f", this.normalizedDegree), location.x - 10, location.y + 5);
        } else if (isBetweenness){
        	int normalized_size2 = size2 + (int)(this.normalizedBetweenness*10*2);
        	int normalized_size1 = size1 + (int)(this.normalizedBetweenness*10*2);
        	g.fillOval(location.x - normalized_size2 / 2, location.y - normalized_size2 / 2, normalized_size2, normalized_size2);
            g.setColor(Color.WHITE);
            g.fillOval(location.x - normalized_size1 / 2, location.y - normalized_size1 / 2, normalized_size1, normalized_size1);
            g.setColor(Color.BLACK);
            
        	g.drawString(String.format("%.2f", this.normalizedBetweenness), location.x - 10, location.y + 5);
        } else if (isCloseness){
        	int normalized_size2 = size2 + (int)(this.normalizedCloseness*10*2);
        	int normalized_size1 = size1 + (int)(this.normalizedCloseness*10*2);
        	g.fillOval(location.x - normalized_size2 / 2, location.y - normalized_size2 / 2, normalized_size2, normalized_size2);
            g.setColor(Color.WHITE);
            g.fillOval(location.x - normalized_size1 / 2, location.y - normalized_size1 / 2, normalized_size1, normalized_size1);
            g.setColor(Color.BLACK);
            
        	g.drawString(String.format("%.2f", this.normalizedCloseness), location.x - 10, location.y + 5);
        }
        
        if(!isBetweenness && !isDegree && !isCloseness){
	        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2, size2);
	        g.setColor(Color.WHITE);
	        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1, size1);
	        g.setColor(Color.BLACK);
	        g.setColor(Color.black);
	        g.drawString(name, location.x, location.y);
        }
    }
}