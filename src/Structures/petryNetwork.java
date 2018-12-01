/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author martin-trejo
 */


package Structures;


//Imports needed for Tarjan´s algorithm code
import java.util.ArrayList;
import java.util.List;
 
import static java.lang.Math.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
//import Mark.Index;
import lombok.*;

public class petryNetwork {
    
    private int[][] pre;
    private int[][] post;
    private int[][] incidence;
    private int[] m0;
    private int nPlaces;
    private int nTransitions;
    
    public petryNetwork(int nPlaces, int nTrans){
        // Create the PN object calling this constructor to set up the
        // number of places and transitions of the PN to dimention the pre and 
        // post matrices.
        pre = new int[nPlaces][nTrans];
        post = new int[nPlaces][nTrans];
        incidence = new int[nPlaces][nTrans];
        m0 = new int [nPlaces];
        this.nPlaces = nPlaces;
        this.nTransitions = nTrans;
    }
    
    //Computational procedures    
    private void computeIncidenceMatrix(){
        // This function is used internally by the set() methods.
        // Every time a value is set in the pre or post matrices, this metphod is
        // called ro update te incidence matrix.
        for (int i=0; i< nPlaces; i ++){
            for(int j=0; j< nTransitions; j++){
                incidence[i][j] = post[i][j]-pre[i][j];
            }
        }
    }
    
    public int[] computeNextMarking(int[] mk, int[] vk){
        // Implements the Estate equation of a PN to compute the next marking.
        // mk: Is the current markxing, mk length equals to nPlaces.
        // vk: Is the firing vector, vk lenght equals to nTransitions.
        
        /* How to use example: 
        int [] mk = {1,0,0};    //initial marking.
        int [] vk = {1,0,0};    //fire transition 1
        int [] vk2 = {0,1,0};   //fire transition 2
        
        int[] mkp1 = pn.computeNextMarking(mk,vk);      //Compute next marking from mk after firing transition 1.               
        int[] mkp12 = pn.computeNextMarking(mk,vk2);    //Compute next marking from mk after firing transition 2.            
        */
        
        int[] nextMarking;
        if (vk.length != this.nTransitions) throw new RuntimeException("Transitions firing vector (vk) of illigal size.");

        nextMarking = multiply(this.incidence , vk);
        nextMarking = addVectors(mk, nextMarking);
        return nextMarking;
    }
    
    public int[] computeActiveTransitions(int[] marking){
        // Returns the vector of active transitions with the marking passed as 
        // argument. The lenght of the vector returned is nTransitions specified
        // for the consutrction of the PN_Graph object.
        //T he markings vector should be of length nPlaces.
        
        /*How to use example:
            int [] mk = {1, 0, 0};            
            int [] actTrans = pn.computeActiveTransitions(mk);              
        */
        
        int[] activeTans= new int[nTransitions];             
               
        for (int i=0 ; i<nTransitions;i++){
            activeTans[i] = isVector_nk_LE_nr(marking, getMatrixColumn(pre, i)) ? 1 : 0; 
        }    
        
        boolean actTans = false;
        for (int i =0; i < this.nTransitions; i++){
            if (activeTans[i]==1)
                actTans = true;
        }
        
        if (actTans)
            return activeTans;
        else
            return null;
    }
    
    public boolean isVector_nk_LE_nr(int[] mk, int[] mr){
        //The function compares if vector mk is larger or equal than mr.
        //The function assumes the vectors length is equal to the number of places
        //input at the construction time of the NP_Graph object (nPlaces).
        //if mk is larger than mr, the function returns TRUE. It returns FALSE
        //otherwise.
        
        /* How to use example:         
            int [] mr = {1, 0, 0};            
            int [] mk = {1, 2, 0};            

            boolean result = pn.isVector_nk_LE_nr(mkp1, mk);                
            System.out.print("\nIs mk >= nr?: " + Integer.toString(result ? 1:0 ));         
        */
        
        boolean is_nk_LE_nr= true;
        for (int i=0; i < nPlaces; i++){
            if (mk[i]<mr[i]){
                is_nk_LE_nr = false;
                break;
            }           
        }    
        return is_nk_LE_nr;
    }
    
    //Test whether mz(i) >= mr(i) to determine where to place a "w".
    public boolean is_mz_ith_LE_mr_ith(int[] mz, int[] mr, int index){
        return mz[index] >= mr[index];
    }
    
    // matrix vector multiplication (y = C * vk)
    private static int[] multiply(int[][] C, int[] vk) {
        int m = C.length;
        int n = C[0].length;
        if (vk.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        int[] y = new int[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += C[i][j] * vk[j];
        return y;
    }
    
    // Vectors addition mk+1 = mk + Cvk
    private static int[] addVectors(int[] mk, int[] Cvk) {
        int m = mk.length;
        int n = Cvk.length;
        if (m != n) throw new RuntimeException("Illegal vectors dimensions.");
        int[] mk_plus_1 = new int[m];
        for (int i = 0; i < m; i++)
            mk_plus_1[i] = mk[i] + Cvk[i];
        return mk_plus_1;
    }
    
    
    // Retives a column from a matrix for example pre(place_i).
    public int[] getMatrixColumn(int[][] matrix, int colIndex){
        int[] column = new int[nPlaces];
        for(int i=0; i<nPlaces; i++){
           column[i] = matrix[i][colIndex];
        }
        return column;
    }
       
    // Determinacion de propiedades de RP .......... //    
    public boolean isPNBounded(Graph gcPN){
        //Ask the graph whether it contains a w in its markings.
        return true;
    }       
    public boolean isPNBlockageFree(Graph gcPN){        
        //Ask the graph whether it contains an terminal nodes.
        return true;
    }    
    public boolean isPNEstrictlyConservative (Graph gcPN){
        // Ask the Graph whether the it is bounded and the sum_of_marks is constant across all nodes.
        return true;
    } 
    public boolean isPNRepetitive(Graph gcPN){
        //Implement tarjan algorithm to find this.
        //Whether the graph has a directed circuir with all transitions in it.
        return true;
    }  
     public boolean isPNReversible(Graph gcPN){
        //Implement tarjan algorithm to find this.
        //Whether all each node is contained in a directed circuit that contains the node n0.
        //(basically, the graph is a Strongly coneceted graph)
        return true;
    }  
    
     
    // Set members
    public void setPreValue(int rowIndex, int colIndex, int value){
        pre[rowIndex][colIndex]=value;
        computeIncidenceMatrix();
    }
    public void setPostValue(int rowIndex, int colIndex, int value){
        post[rowIndex][colIndex]=value;
        computeIncidenceMatrix();
    }
    private void setIncidence(int rowIndex, int colIndex, int value){
        incidence[rowIndex][colIndex]=value;
        computeIncidenceMatrix();
    }
    public void setm0(int rowIndex, int value){
        m0[rowIndex]=value;
        computeIncidenceMatrix();
    }
    
    // Get members
    public int[][] getPre(){return pre;}
    public int[][] getPost(){return post;}
    public int[][] getIncidence(){return incidence;}
    public int[]   getm0(){return m0;}
    
    // Print Members
    
    public void printPreMatrix(){
        System.out.print("\nPre Matrix:\n");
        for(int rowIndex=0; rowIndex < this.nPlaces; rowIndex++){
            for(int colIndex=0; colIndex < this.nTransitions; colIndex++){            
                System.out.print(Integer.toString(pre[rowIndex][colIndex]) + " ");
            }
            System.out.print("\n");
        }
    }
    
    public void printPostMatrix(){
        System.out.print("\nPost Matrix:\n");
        for(int rowIndex=0; rowIndex < this.nPlaces; rowIndex++){
            for(int colIndex=0; colIndex < this.nTransitions; colIndex++){            
                System.out.print(Integer.toString(post[rowIndex][colIndex]) + " ");
            }
            System.out.print("\n");
        }
    }
    
    public void printm0Matrix(){
        System.out.print("\n m0 Matrix:\n");
        for(int rowIndex=0; rowIndex < this.nPlaces; rowIndex++){            
            System.out.print(Integer.toString(m0[rowIndex]) + "\n");
        }        
    }
    
    public void printIncidenceMatrix(){
        System.out.print("\nIncidence Matrix:\n");
        for(int rowIndex=0; rowIndex < this.nPlaces; rowIndex++){
            for(int colIndex=0; colIndex < this.nTransitions; colIndex++){            
                System.out.print(Integer.toString(incidence[rowIndex][colIndex]) + " ");
            }
            System.out.print("\n");
        }
    }
    
}
