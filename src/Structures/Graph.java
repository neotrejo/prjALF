/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Class tha represents a Graph
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
     * Get a node that haves TYPE other than Frontera and the mark is equal to the sender Node's mark
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

    /**
     * Get the set of nodes
     * @return 
     */
    //For Tarjan
    public List<Node> getNodes(){
        return this.nodes;
    }
}
