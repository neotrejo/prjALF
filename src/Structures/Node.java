/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carlos
 */
public class Node {
    public static final int W = -1;
    private TypeNode type;
    private int mark[];
    private List<Transition> transitions;
    private Node parent;
    private String id;
    
    public Node(String id){
        this.id = id;
        this.transitions = new ArrayList<>();
        this.type = TypeNode.FRONTERA;
        this.parent = null;
    }
    
    public Node(String id, int[] mark){
        this(id);
        this.mark = mark;
    }
    
    public TypeNode getType(){
        return this.type;
    }
    
    public void setType(TypeNode type){
        this.type = type;
    }
    
    public int[] getMark(){
        return this.mark;
    }
    
    public void setMark(int[] m){
        this.mark = m;
    }
    
    public void setParent(Node node){
        this.parent = node;
    }
 
    public Node getParent(){
        return this.parent;
    }
            
    public void addTransition(Transition t){
        this.transitions.add(t);
    }
    
    public void addTransition(Node node, String nameT){
        Transition t = new Transition(nameT);
        t.setEnd(node);
        this.transitions.add(t);
        node.setParent(this);
    }
    
    public List<Transition> getTransitions(){
        return this.transitions;
    }
    
    public String getId(){
        return this.id;
    }
    
    public void setId(String id){
        this.id = id;
    }
    
    public boolean hasThisMark(int[] mark){
        if(Array.getLength(this.mark) != Array.getLength(mark))
            return false;
        
        for(int i = 0; i < Array.getLength(mark); i++){
            if(this.mark[i] != mark[i])
                return false;
        }
        
        return true;
    }
    
    public void setWs(){
        //comparar 2 vectores para saber si uno es mayor igual que otro
    }
}
