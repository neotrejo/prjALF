/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Structures.Graph;
import Structures.Node;
import Structures.TypeNode;
import java.lang.reflect.Array;

/**
 *
 * @author Carlos
 */
public class AlgorithmPN {
    private int M0[];
    
    public AlgorithmPN(int[] M0, int n, int m){
        this.M0 = M0;
    }
    
    /**
     * Ejecuta el algoritmo para obtener el grafo de alcanzabilidad
     * a partir de la matriz pre, matriz post y M0
     */
    public void getCoverGraph(){
        Graph g = new Graph();
        int id = 0, j = 0;
        
        Node nz = new Node("n" + String.valueOf(id), M0);
        g.addNode(nz, true);
        id++;
        Node nk;
        int transitions[];
        int mz[];
        
        while((nk = g.getNodeType(TypeNode.FRONTERA)) != null){
            //Verificar que no sea duplicado
            if(g.getDuplicateNodeNotFrontera(nk) != null){
                nk.setType(TypeNode.DUPLICADO);
            }
            else{ //el nodo no es duplicado
                transitions = this.getEnabledTransitions(nk.getMark()); //buscar transiciones habilitadas
            
                if(transitions == null){ //no hay transiciones habilitadas para el nodo
                    nk.setType(TypeNode.TERMINAL);
                }
                else{                   //hay transiciones 
                    nk.setType(TypeNode.EXPANDIDO);
                    for(j = 0; j < Array.getLength(transitions) - 1; j++){
                        if(transitions[j] == 1){
                            //Se crea el nodo nz
                            mz = this.calculateNextMark(nk.getMark(), j);
                            nz = new Node("n" + String.valueOf(id), mz);// se crea como nodo frontera
                            g.addNode(nz, false);
                            id++;
                            //Se crea transicion para nk --> nz
                            nk.addTransition(nz, "t"+ String.valueOf(j));

                            //Buscar si no tiene Ws
                            nz.setWs();
                        }
                    }
                }
            }
            
        }
    }
    
    /**
     * Obtiene las transiciones que se habilitan con la marca enviada
     * @param mark
     * @return 
     */
    public int[] getEnabledTransitions(int[] mark){
        int n[]={0,0,1};
        return n;
    }
    
    /**
     * Calcual la siguiente marca a partir de una marca enviada y una transicion
     * @param mark
     * @param transitionIndex
     * @return 
     */
    public int[] calculateNextMark(int[] mark, int transitionIndex){
        int n[]={0,0,1};
        return n;
    }
    
}
