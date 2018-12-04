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
 * Class that represents a Node
 * @author Carlos
 */
public class Node {
    public static final int W = Integer.MAX_VALUE;
    private TypeNode type;
    private int mark[];
    private List<Transition> transitions;
    private Node parent;
    private String id;
    
    //For Tarjan
    public int index;
    public int lowlink;
    public boolean visited;
    public boolean onStack;
    //
    
    public Node(){
        //For Tarjan
        index = Integer.MAX_VALUE;
        lowlink = Integer.MAX_VALUE;
        visited = false;
        onStack = false;    
    }
    
    public Node(String id){
        this();
        this.id = id;
        this.transitions = new ArrayList<>();
        this.type = TypeNode.FRONTERA;
        this.parent = null;
    }
    
    public Node(String id, int[] mark){
        this(id);
        this.mark = mark;
    }
    
    //Getters
    public TypeNode getType(){ return this.type; }
    public int[] getMark(){ return this.mark; }
    public Node getParent(){ return this.parent; }
    public List<Transition> getTransitions(){ return this.transitions; }
    public String getId(){ return this.id; }
    
    //Setters
    public void setType(TypeNode type){ this.type = type; }
    public void setMark(int[] m){ this.mark = m; }
    public void setParent(Node node){ this.parent = node; }
    public void setId(String id){ this.id = id; }
    
    /**
     * Add a transition with the sender name and the sender node
     * @param node
     * @param nameT 
     */
    public void addTransition(Node node, String nameT){
        Transition t = new Transition(nameT);
        t.setEnd(node);
        this.transitions.add(t);
        node.setParent(this);
    }
    
    /**
     * Compare the mark of the node with the sender mark, and decide if is iquals
     * @param mark
     * @return 
     */
    public boolean hasThisMark(int[] mark){
        if(Array.getLength(this.mark) != Array.getLength(mark))
            return false;
        
        for(int i = 0; i < Array.getLength(mark); i++){
            if(this.mark[i] != mark[i])
                return false;
        }
        
        return true;
    }
    /**
     * Find if the mark of the node have W's
     */
    public void setWs(){
        if(this.parent != null){
            this.copyW(this.parent.mark); //Copy all ws of the parent
            
            Node auxParent = this.parent;
            while(auxParent != null){
                if(isVector_nk_LE_nr(this.mark, auxParent.mark)){
                    for(int i = 0; i < Array.getLength(mark); i++){
                        if(this.mark[i] > auxParent.mark[i]){
                            this.mark[i] = Node.W;
                        }
                    }
                }
                auxParent = auxParent.getParent();
            }
        }
    }
    
    /**
     * 
     * @param m mark to copy
     * @return 
     */
    public void copyW(int[] m){
        for(int i = 0; i < Array.getLength(m); i++){
            if(m[i] == Node.W)
                this.mark[i] = Node.W;
        }
    }
    
    /**
     * The function compares if vector mk is larger or equal than mr.
     * The function assumes the vectors length is equal to the number of places
     * input at the construction time of the NP_Graph object (nPlaces).
     * if mk is larger than mr, the function returns TRUE. It returns FALSE
     * otherwise.
     * @param mk
     * @param mr
     * @return 
     */
    public boolean isVector_nk_LE_nr(int[] mk, int[] mr){

        boolean is_nk_LE_nr= true;
        for (int i=0; i < Array.getLength(mk); i++){
            if (mk[i]<mr[i]){
                is_nk_LE_nr = false;
                break;
            }           
        }    
        return is_nk_LE_nr;
    }
    
    //For Tarjan
    /**
     * Get the neighboors of the node
     * @return 
     */
    public List<Node> getSucessorNodes(){
        List<Node> succNodes = new ArrayList();
                
        transitions.stream().forEach((transition) -> {
            succNodes.add(transition.getEnd());
        });
        return succNodes;
    }
    /**
     * 
     * @return 
     */
    public String getMarkString(){
        String markString = id+": ";
        
        for(int i=0; i < Array.getLength(mark); i++){
            if(this.mark[i]==0||this.mark[i]==1){
                markString += String.valueOf(this.mark[i]) + " ";
            }else{
                markString += "w ";
            }
        }
        if(this.getType()==TypeNode.DUPLICADO){
                markString+="(D)";
            }else if(this.getType()==TypeNode.EXPANDIDO){
                markString+="(E)";
            }else if(this.getType()==TypeNode.TERMINAL){
                markString+="(T)";
            }

        return markString;
    }
}
