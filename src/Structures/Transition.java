/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures;

/**
 * Class that represent a Transition
 * @author Carlos
 */
public class Transition {
    private String id;
    private Node end;
    
    public Transition(String id){
        this.id = id;
    }
    
    //Getters
    public String getId(){ return this.id; }
    public Node getEnd(){ return this.end; }
    
    //Setters
    public void setId(String id){ this.id = id; }
    public void setEnd(Node end){ this.end = end; }
    
    public void redirectTransitionTo(Node node){
        end= node;        
    }
            
}
