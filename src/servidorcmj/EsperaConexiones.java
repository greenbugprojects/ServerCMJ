/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorcmj;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * Clase encargada de esperar las conexiones al servidor
 * @author ma0
 */
public class EsperaConexiones extends Thread {
    ServerSocket serverSocket;
    Socket media;
    boolean esperamosConexiones,seConectaron;
    /**
     * Recibe el server socket del servidor y lo asigna para esperar conexiones
     * @param s Server Socket del servidor
     */
    public EsperaConexiones(ServerSocket s){
        serverSocket=s;
        esperamosConexiones=true;
        seConectaron=false;
        this.start();
    }

    /**
     * Retorna el estado de si alguien se ha conectado
     * @return booleano con dicho estado
     */
    public  boolean isSeConectaron() {return seConectaron;}
    
    @Override
    public void run() {
        while (esperamosConexiones)
        {
            try 
            {
                media=serverSocket.accept();
                seConectaron=true;
                Thread.sleep(1);
                
            }catch(InterruptedException e)
                {JOptionPane.showMessageDialog(null, "Imposible dormir");}
            catch ( IOException ioe )
                {JOptionPane.showMessageDialog(null, "Algo no llego ");}
        }
                
    }
    /**
     * Asigna un valor a la variable se conectaron, ideal para estar listo a esperar otra conexion
     * @param seConectaron booleano
     */
    public void setSeConectaron(boolean seConectaron) {this.seConectaron = seConectaron;}

    /**
     * Retorna el socket donde se ha asignado la conexion Socket
     * @return 
     */
    public Socket getMedia() {return media;}
    
}
