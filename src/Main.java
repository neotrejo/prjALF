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
        
        FileUtils.write(nombreArchivo, content,"txt");
        FileUtils.generateImg(nombreArchivo, "png");
        new GraphFrame(nombreArchivo);   
    }
    
}
