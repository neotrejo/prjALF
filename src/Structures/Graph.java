/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos
 */
public class Graph {
    private List<Node> nodes;
    private Node head; 
    
    public Graph(){
        nodes = new ArrayList<>();
        head = null;
    }
    
    public void addNode(Node node, boolean isHead){
        this.nodes.add(node);
        if(isHead)
            this.setHead(node);
    }
    
    private void setHead(Node node){
        this.head = node;
    }
    
    public Node getNodeType(TypeNode type){
        
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i).getType() == type)
                return this.nodes.get(i);
        }
        
        return null;
    }
    
    public Node getDuplicateNodeNotFrontera(Node node){
        for (int i = 0; i <= this.nodes.size() - 1; i++) {
            if(this.nodes.get(i) != node && 
               this.nodes.get(i).getType() != TypeNode.FRONTERA &&
               this.nodes.get(i).hasThisMark(node.getMark()))
                return this.nodes.get(i);
        }
        
        return null;
    }

    //For Tarjan
    public List<Node> getNodes(){
        return this.nodes;
    }
}
