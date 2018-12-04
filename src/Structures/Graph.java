/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a Graph
 * @author Carlos
 */
public class Graph {
    private List<Node> nodes;
    private Node head; 
    
    public Graph(){
        nodes = new ArrayList<>();
        head = null;
    }
    
    /**
     * Add a node to the Graph and put this at the head if the boolean isHead is TRUE
     * @param node
     * @param isHead 
     */
    public void addNode(Node node, boolean isHead){
        this.nodes.add(node);
        if(isHead)
            this.setHead(node);
    }
    
    public boolean removeNode(Node node){
        
        for (int i=0; i < nodes.size(); i++){
            //If Id strings match.
            if(nodes.get(i).getId().compareTo(node.getId())== 0){
                nodes.remove(i);
                return true;
            }                
        }
        return false;
    }
    
    
   
    /**
     * Set a node HEAD of the Graph
     * @param node 
     */
    private void setHead(Node node){
        this.head = node;
    }
    
    /**
     * Get the first Node that haves the sender type
     * @param type
     * @return 
     */
    public Node getNodeType(TypeNode type){
        
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i).getType() == type)
                return this.nodes.get(i);
        }
        
        return null;
    }
    
    /**
     * Get a node that has TYPE other than Frontera and the mark is equal to the sender Node's mark
     * @param node
     * @return 
     */
    public Node getDuplicateNodeNotFrontera(Node node){
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i) != node && 
               this.nodes.get(i).getType() != TypeNode.FRONTERA &&
               this.nodes.get(i).hasThisMark(node.getMark()))
                return this.nodes.get(i);
        }
        
        return null;
    }

    
    public Node getDuplicateNodeExpandido(Node node){
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i) != node && 
               this.nodes.get(i).getType() == TypeNode.EXPANDIDO &&
               this.nodes.get(i).hasThisMark(node.getMark()))
                return this.nodes.get(i);
        }
        
        return null;
    }
        
        
    /**
     * Looks for a existing Node in the graph with a specific Marking.
    */    
    public Node getNodeByMarking(int[] mark){
        Node myNode;
        for (int i = 0; i < this.nodes.size(); i++) {
            myNode = this.nodes.get(i);
            if(myNode.hasThisMark(mark))
                return myNode;
        }        
        return null;
    }
    
    
    /**
     * Get the set of nodes
     * @return 
     */
    //For Tarjan
    public List<Node> getNodes(){
        return this.nodes;
    }
    
    /**
     * Get the max bound of the graph's nodes
     * @return 
     */
    public int getMaxBound(){
        int max = 0, maxAux;
 
        for (Node node : this.nodes) {
            maxAux = node.getMaxInMark();
            if (maxAux > max) {
                max =  maxAux;
            }
        }
        
        return max;
    }
    
    /**
     * Get the max bound of the graph's nodes
     * @return 
     */
    public boolean isEstrictlyConservative(){
        int sum, sumAnt = 0;
 
        if(this.nodes.size() > 0){
            sumAnt = this.nodes.get(0).getSumMark();
        }
        
        for (Node node : this.nodes) {
            sum = node.getSumMark();
            if (sum == Node.W) {
               return false;
            }
            if(sum != sumAnt){
               return false;
            }
        }
        
        return true;
    }
    
    /**
     * Find a terminal node in the graph
     * @return 
     */
    public boolean hasTerminalNodes(){
         for (Node node : this.nodes) {
             if(node.getType() == TypeNode.TERMINAL){
                 return true;
             }
         }
         
         return false;
    }
    
}
