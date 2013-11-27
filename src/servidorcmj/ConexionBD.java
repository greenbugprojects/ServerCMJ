/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorcmj;



import java.io.UnsupportedEncodingException;
import java.sql.*;
import javax.swing.JOptionPane;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author REEE PATO
 */
public class ConexionBD {
    
    //private String driver = "com.postgresql.jdbc.Driver";
    private String URL = "jdbc:postgresql://pgsql/mandrescc";
    private String usuario = "mandrescc";
    private String contraseña = "mandrescc";
    private String nombreTabla="usuarios";
    private boolean conectado;
    
    private Connection conexion;
    private Statement statement;
    
    public ConexionBD(){
       
    }
    
    public void configurar(String host,String puerto,String bd,String usuario,String contra)
    {
        URL="jdbc:postgresql://"+host+":"+puerto+"/"+bd;
        this.usuario=usuario;
        contraseña=contra;
        //driver = "com.mysql.jdbc.Driver";
        nombreTabla="usuarios";
    }
    
    public String conectarBD(){
         
        URL="jdbc:postgresql://localhost/cmj";
        usuario="acceso";
        contraseña = "acceso";   
        try 
        {
            conexion = DriverManager.getConnection(URL,usuario,contraseña);
            statement = conexion.createStatement();
            conectado = true;
             return "Usted esta conectado a la base de datos";
        } catch (Exception e) 
        {
            conectado = false;
            return "No se pudo conectar a la base de datos\n" +(e.getMessage());            
        }
    }
    


    public boolean verificarInicioSesion(String nombre, String contraseña){
        contraseña=convertirMD5(contraseña);
        try{
             ResultSet  rs = statement.executeQuery("SELECT * FROM "+nombreTabla+" WHERE usuario ='"+nombre+"' AND pass ='"+contraseña+"';");
             
             while(rs.next())
             {
                if(nombre.equals(rs.getString(1)) && contraseña.equals(rs.getString(2)) ){ 
                    //System.out.println(rs.getString(1)+" "+rs.getString(2));
                    return true;
                }
             }
            
            
            return false;
            
        }catch(SQLException e){
            System.out.println("Error al iniciar sesion");
        }
        return false;
    }
   
    public String getTipo(String Usuario)
    {
        try{System.out.println("SELECT * FROM "+nombreTabla+" WHERE usuario ='"+Usuario+"';");
             ResultSet  rs = statement.executeQuery("SELECT tipo FROM "+nombreTabla+" WHERE usuario ='"+Usuario+"';");
             System.out.println(rs.getFetchSize());
             System.out.println(rs.next());
             return rs.getString(1);
             
        }catch(SQLException e){
            System.out.println("Error al Buscar el tipo");
            e.printStackTrace();
        }
        return "No Encontrado";
    }
    
    public boolean crearUsuario(String Usuario, String Contraseña, String tipo)
    {
        try {
            statement.execute("INSERT INTO "+nombreTabla+" VALUES ('"+Usuario+"','"+
                    convertirMD5(Contraseña)+"','"+tipo+"');");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
     
     public static String convertirMD5(String texto)
    {
        try 
        {
            byte[] bytesOfMessage = texto.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            byte[] thedigest = md.digest(bytesOfMessage);
            StringBuilder h = new StringBuilder(thedigest.length);
            for (int i = 0; i < thedigest.length; i++) 
            {
                int u = thedigest[i] & 255;
                if (u < 16) 
                    h.append("0").append(Integer.toHexString(u));                    
                else                                              
                    h.append(Integer.toHexString(u));
            }
             return h.toString();
        }catch ( UnsupportedEncodingException | NoSuchAlgorithmException ex) 
        {
        }
        return "Error en la Conversión";      
    }
}
