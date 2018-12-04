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
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class petryNetwork {
    
    private int[][] pre;
    private int[][] post;
    private int[][] incidence;
    private int[] m0;
    private int nPlaces;
    private int nTransitions;
    
    //Grpah cover
    private Graph graphCover;
    private Graph gCTarjan;
    //For tarjan
    private int index;
    private Stack<Node> S;
    //
    
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
        this.graphCover = null;
        
        //For Tarjan
        S = new Stack<>();
        index = 0;
    }
    
    /**
     * Computes Coverage Graph for Tarjan Algorithm Processing.
     */
    private void computeTarjanCoverGraph(){
        gCTarjan = new Graph();
        int id = 1, j;
        
        Node nz = new Node("n" + String.valueOf(id), m0);
        gCTarjan.addNode(nz, true);
        id++;
        Node nk, dupNode;
        int transitions[];
        int mz[];
        
        
        while((nk = gCTarjan.getNodeType(TypeNode.FRONTERA)) != null){
            //Verificar que no sea duplicado
            if(gCTarjan.getDuplicateNodeNotFrontera(nk) != null){
                nk.setType(TypeNode.DUPLICADO);                                
            }
            else{ //el nodo no es duplicado
                transitions = this.computeActiveTransitions(nk.getMark()); //buscar transiciones habilitadas
            
                if(transitions == null){ //no hay transiciones habilitadas para el nodo
                    nk.setType(TypeNode.TERMINAL);
                }
                else{                   //hay transiciones 
                    nk.setType(TypeNode.EXPANDIDO);
                    for(j = 0; j < Array.getLength(transitions); j++){
                        if(transitions[j] == 1){
                            //Se crea el nodo nz
                            mz = this.computeNextMarking(nk.getMark(), computeVk(j));
                            nz = new Node("n" + String.valueOf(id), mz);// se crea como nodo frontera
                            gCTarjan.addNode(nz, false);
                            id++;
                            //Se crea transicion para nk --> nz
                            Transition t = nk.addTransition(nz, "t"+ String.valueOf(j+1));
                            //Buscar si no tiene Ws
                            nz.setWs();                           
                            
                            /*dupNode = gCTarjan.getDuplicateNodeNotFrontera(nz);
                            
                            if(dupNode != null){                                
                                t.redirectTransitionTo(dupNode);
                                if(!gCTarjan.removeNode(nz))
                                    System.out.print("Error: Could not remove node with Id:" + nz.getId());
                                else
                                    System.out.print("Seccessfully remove node with Id:" + nz.getId());
                                dupNode.setType(TypeNode.DUPLICADO);
                            } */              
                        }
                    }
                }
            }
            
        }
        connectTarjanDuplicatesCoverGraph();
    }
    private void connectTarjanDuplicatesCoverGraph(){
        Node dupNode;        
        Node parentNode;
        Transition redirTrans;        
        List<Node> allNodes;
        List<Node> duplicateNodes = new ArrayList<>();
        
        if(gCTarjan== null){            
            this.computeTarjanCoverGraph();            
        }        
        allNodes = gCTarjan.getNodes();
        //Get duplicate Nodes only
        
        for(Node myNode : allNodes){ 
            if(myNode.getType()==TypeNode.DUPLICADO)
                duplicateNodes.add(myNode);
        }
        
        for(Node myNode : duplicateNodes){   
            dupNode = gCTarjan.getDuplicateNodeExpandido(myNode);
            //Find pre Transition that connect to myNode                    
            parentNode = myNode.getParent();
            for (int j=0; j< parentNode.getTransitions().size(); j++){
                //Identify Transition that connects to myNode at its end
                redirTrans = parentNode.getTransitions().get(j);
                if (myNode.getId().compareTo(redirTrans.getEnd().getId())==0){
                    redirTrans.redirectTransitionTo(dupNode); //Redirects transition to matching node                    
                    gCTarjan.removeNode(myNode);
                }
            }

            
        }   
    }
    /**
     * Calculate the graph cover with the matrix pre, post and m0
    */
    private void computeCoverGraph(){
        graphCover = new Graph();
        int id = 1, j;
        
        Node nz = new Node("n" + String.valueOf(id), m0);
        graphCover.addNode(nz, true);
        id++;
        Node nk;
        int transitions[];
        int mz[];
        
        while((nk = graphCover.getNodeType(TypeNode.FRONTERA)) != null){
            //Verificar que no sea duplicado
            if(graphCover.getDuplicateNodeNotFrontera(nk) != null){
                nk.setType(TypeNode.DUPLICADO);
            }
            else{ //el nodo no es duplicado
                transitions = this.computeActiveTransitions(nk.getMark()); //buscar transiciones habilitadas
            
                if(transitions == null){ //no hay transiciones habilitadas para el nodo
                    nk.setType(TypeNode.TERMINAL);
                }
                else{                   //hay transiciones 
                    nk.setType(TypeNode.EXPANDIDO);
                    for(j = 0; j < Array.getLength(transitions); j++){
                        if(transitions[j] == 1){
                            //Se crea el nodo nz
                            mz = this.computeNextMarking(nk.getMark(), computeVk(j));
                            nz = new Node("n" + String.valueOf(id), mz);// se crea como nodo frontera
                            graphCover.addNode(nz, false);
                            id++;
                            //Se crea transicion para nk --> nz
                            Transition t = nk.addTransition(nz, "t"+ String.valueOf(j+1));
                            nz.addPreTransition(t);
                            //Buscar si no tiene Ws
                            nz.setWs();
                        }
                    }
                }
            }
            
        }
    }
    
    /**
     * Calculate the vector vk from position
     * returns an array with 0's, and only a 1 in the position of the array 
     * @param position
     * @return 
     */
    private int[] computeVk(int position){
       int[] vk = new int[this.nTransitions];  
       
       for (int i = 0 ; i<nTransitions; i++){
           vk[i] = (i == position)?1:0;
       }
       
       return vk;
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
        
        this.graphCover = null;
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
    public boolean isPNBounded(){
        //Ask the graph whether it contains a w in its markings.

        int maxBound;
        if(this.graphCover != null){
            return (this.graphCover.getMaxBound() != Node.W);
        }
        return false;
    }      
    // Determinacion de propiedades de RP .......... //    
    public int getMaxBoundValue(){
    
        if(this.graphCover != null){
            return this.graphCover.getMaxBound();
        }
        //Ask the graph whether it contains a w in its markings.
        return 0;
    } 
    public boolean isPNBlockageFree(){        
        if(this.graphCover != null){
            return !(this.graphCover.hasTerminalNodes());
        }
        
        return true;
    }    
    public boolean isPNEstrictlyConservative (){
        // Ask the Graph whether the it is bounded and the sum_of_marks is constant across all nodes.
        return graphCover.isEstrictlyConservative();
    } 
    public boolean isPNRepetitive(){
        //Implement tarjan algorithm to find this.
        //Whether the graph has a directed circuir with all transitions in it.
        return true;
    }  
     public boolean isPNReversible(){
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
    /**
     * Get the graph cover and if the value is equal to null, calculate the new
     * cover graph from the algorithm 
     * @return 
     */
    public Graph getCoverGraph(){
        if(this.graphCover == null){
            this.computeCoverGraph();
        }
        return this.graphCover;
    }
    
    /**
     * Computes the CoverageGraph With Loops to duplicate nodes (for Tarjan Algorithms)
     */
     public Graph getTarjanCoverGraph(){
        if(this.gCTarjan == null){
            this.computeTarjanCoverGraph();            
        }
        return this.gCTarjan;
    }
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
    
    // Basic Implementation of Tarjan Algorithm.
               
    public List<List<Node>> Tarjan(Graph G){
        //output: set of strongly connected components (sets of vertices) 
        List<List<Node>> sccAll = new ArrayList<>();
                   
        this.index=1;
        S.clear();
             
        //Iterate through all nodes not visited yet.
        System.out.print("\nTotal number of nodes: " + Integer.toString(G.getNodes().size()));
        G.getNodes().stream().forEach((v) -> {
            if (v.index == Integer.MAX_VALUE){
                List<Node> scc = strongconnect(v);
                if(scc != null){                    
                    sccAll.add(scc);
                }
            }
        });
        return sccAll;
    }

    public List<Node> strongconnect(Node v){        
        List<Node> scc;     // List to hold the nodes of any SCC found.
        Node wscc;          
        
        // Set the depth index for node v to the smallest unused index
        v.index = index;
        v.lowlink = index;
        index = index + 1;
        S.push(v);
        v.onStack = true;
           
        // Consider successors of v
        //for each (v, w) in E do
        v.getSucessorNodes().stream().forEach((w) -> {          
            if (w.index == Integer.MAX_VALUE){
              // Successor w has not yet been visited; recurse on it
              strongconnect(w);
              v.lowlink  = min(v.lowlink, w.lowlink);
            }
            else if (w.onStack){
              // Successor w is in stack S and hence in the current SCC
              // If w is not on stack, then (v, w) is a cross-edge in the DFS tree and must be ignored
              // Note: The next line may look odd - but is correct.
              // It says w.index not w.lowlink; that is deliberate and from the original paper
              v.lowlink  = min(v.lowlink, w.index);
            }
        });

        // If v is a root node, pop the stack and generate an SCC
        if (v.lowlink == v.index) {
            //start a new strongly connected component
            scc = new ArrayList();
            do{
              wscc = S.pop();
              wscc.onStack = false;
              //add w_scc to current strongly connected component
              scc.add(wscc);
            }while (!wscc.equals(v)); //Test it is not the same node (w != v).
            //output the current strongly connected component
            return scc;
        }
        return null;               
        
        
    }
   
    public boolean isSCCCyclic(List<Node> sCC){
        return sCC.size() > 1;                    
    }
    
}
