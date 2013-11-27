/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorcmj;
import java.io.*;
import java.net.*;
import javax.swing.*;
/**
 *Clase ConexionServidor, contiene un Socket y los flujos de entrada/salida para comunicarse con
 * un cliente desde el servidor
 * @author ma0
 */
public class ConexionServidor implements Runnable{

    Socket media;
     ObjectOutputStream salida;
     ObjectInputStream entrada;
     String nombre,mensaje="",tipo="";
     Thread hilo;
     boolean conexionExitosa,llegoMensaje,estaActivado,meCerraron;
     /**
      * Crea una conexion desde el servidor hacia otro cliente
      * @param s Socket aceptado por el Server Socket
      * @param nombre Cadena para identificar la Conexion
      */
    public ConexionServidor(Socket s,String nombre)
    {
        media=s;
        this.nombre=nombre;
        conexionExitosa=llegoMensaje=estaActivado=meCerraron=false;
        try
        {
        entrada= new ObjectInputStream( media.getInputStream());
        
        salida= new ObjectOutputStream(media.getOutputStream());
        
        conexionExitosa=true;
        }catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Imposible conectar Flujos "+e.toString());
        }
        
        
    }


    /**
     * Modifica el booleano Llego mensaje
     * @param llegoMensaje booleano
     */
    public void setLlegoMensaje(boolean llegoMensaje) {this.llegoMensaje = llegoMensaje;}
    /**
     * Retorna el mensaje que ha llegado desde el cliente
     * @return String con un mensaje
     */
    public String getMensaje() {return mensaje;}
    /**
     * Pregunta si ha llegado algun mensaje
     * @return booleano con el estado de llegada de mensajes
     */
    public boolean isLlegoMensaje() {return llegoMensaje;}
    /**
     * Verifica si han cerrado un socket
     * @return booleano con el estado del socket
     */
    public boolean ismeCerraron() {return meCerraron;}

    /**
     * Retorna el nombre de la actual conexion al servidor
     * @return String con el nombre de la conexion
     */
    public String getNombre() {return nombre;}
    /**
     * asigna un nombre a la conexion
     * @param nombre String con un nombre
     */
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public String getTipo(){return tipo;}
    /**
     * Activa el hilo de Escucha de la conexion
     */
    public void iniciarConexion()
    {
        if(conexionExitosa)
        {
            hilo=new Thread(this,nombre);
            estaActivado=true;
            hilo.start();
        }
    }
 
    @Override
    public void run() 
    {
       
        while (estaActivado)
        {
            try
            {
                esperarMensaje();
            }catch ( ClassNotFoundException cnf )
                {JOptionPane.showMessageDialog(null, "Ni idea de que %& mandaron "+nombre);}
            catch ( IOException ioe )
                {JOptionPane.showMessageDialog(null, "Algo no llego de "+nombre+"\nDesconectando ...");
                cerrarConexion();}

              
        }
    }
    
    /**  
     * Metodo que espera mensajes de los Sockets del Cliente
     * @throws IOException En caso de que la conexion se interrumpa
     * @throws ClassNotFoundException en caso de que el objeto de llegada no sea un String
     */
    public void esperarMensaje()throws IOException, ClassNotFoundException
    {
        mensaje=(String)entrada.readObject();
        
        llegoMensaje=true;
    }
    /**
     * Envia un mensaje al socket correspondiente
     * @param mensaje 
     */
    public void mandaMensaje(String mensaje)
    {
        try 
        {
         salida.writeObject(mensaje);
         salida.flush();
        }
      catch ( IOException ioe ) 
      {
         JOptionPane.showMessageDialog(null, "Imposible mandar Mensaje "+ nombre);
         cerrarConexion();
      }
    }
    
    /**
     * cierra los puertos de entrada/salida del socket
     */
    public void cerrarConexion()
    {
        try
         {
            estaActivado=false;
            entrada.close(); 
            salida.close();
            media.close();
            meCerraron=true;
         }catch( IOException excepcionES ) {
         excepcionES.printStackTrace();
      }
    }
    
    public static void show(Object o){JOptionPane.showMessageDialog(null, o);}
    public static void cout(Object o){System.out.println(o);}
}
