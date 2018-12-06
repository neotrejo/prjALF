/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luis Humanoide
 */
public class Main {
    
    public static void main(String[] args){
        hacerGrafo();
    }
    
    /*
    metodo bonito para escribir un grafito en la carpetita del proyecto .w.
    */
    public static void hacerGrafo(){
        String nombreArchivo="nombreBonis";
        
        String content="digraph G{\n";
        /*
        se supone que aqui se pondria un ciclo for, como Harrison For jajajajajXD que buen chistazo
        */
        content=content+"\""+"a"+"\" -> "+"\""+"b"+"\"\n";
        content=content+"\""+"a"+"\" -> "+"\""+"a"+"\"\n";
        content=content+"\""+"b"+"\" -> "+"\""+"c"+"\"\n";
        content=content+"\""+"d"+"\" -> "+"\""+"a"+"\"\n";
        content=content+"\""+"d"+"\" -> "+"\""+"b"+"\"\n";
        
        content=content+"}";
        
        content = "digraph G {\n" +
    "  rankdir=LR;\n" +
    "\n" +
    "  subgraph cluster1 {\n" +
    "    fontsize = 20;\n" +
    "    label = \"Group 1\";\n" +
    "    rank=same;\n" +
    "    A  B  C  D [constraint=false];\n" +
    "    style = \"dashed\";\n" +
    "  }\n" +
    "\n" +
    "  subgraph cluster2 {\n" +
    "    fontsize = 20;\n" +
    "    label = \"Group 2\";\n" +
    "    rank=same;\n" +
    "    Z  Y  X  W [dir=back, constraint=false];\n" +
    "    style = \"dashed\";\n" +
    "  }\n" +
    "\n" +
    //"  O [shape=box];\n" +
    "  A -> Z A->Y A->X A->W \n" +
    "  B -> Z B->Y B->X B->W \n" +
    "  C -> Z C->Y C->X C->W \n" +
    "  D -> Z D->Y D->X D->W \n" +
    "}";
        
        FileUtils.write(nombreArchivo, content,"txt");
        FileUtils.generateImg(nombreArchivo, "png");
        new GraphFrame(nombreArchivo);   
    }
    
}
