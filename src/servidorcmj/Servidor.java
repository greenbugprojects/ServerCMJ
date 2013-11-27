/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorcmj;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/**
 *Clase servidor que administra todos los mensajes entre usuarios, las autentificaciones
 * y hace de mediador con la base de datos
 * @author mandrescc
 */
public class Servidor extends JFrame implements Runnable{
    JTextArea area;
    JButton b;
    JPanel panel;
    ServerSocket servidor;
    EsperaConexiones ec;
    ArrayList <ConexionServidor> conexiones;
    private ArrayList <String> listaUsuarios;
    private int numeroSalas,cantidadConectados;
    private boolean estaPrendido,usuarioConectado,chatActivado,correoActivado,DNSactivado;
    Thread hilo;
    ConexionBD baseDatos;
    
    
    //Copy
    private JComboBox chatsDisponibles;
    private JLabel Usuarios;
    private JLabel nombreChat;
    private JLabel labelIp;
    private JList usuarios;
    private JMenu Archivo;
    private JMenu preferencias;
    private JMenuBar barraMenu;
    private JPanel paneln;
    private JScrollBar jScrollBar1;
    private JScrollPane scrollIp;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JTextArea chat;
    private JTextField jTextPane1;
    private InetAddress dir;
    private JMenuItem desactivarChat,desactivarCorreo,desactivarDNS,cerrarServidor, autentificarBD;
    

    /**
     * Constructor de servidor
     * @throws IOException EN caso de que no se pueda arrancar el servidor
     */
    public Servidor()throws IOException
    {
        super ("ServidorChat");
        servidor=new ServerSocket(5800,100);
        dir=InetAddress.getLocalHost();
        estaPrendido=chatActivado=correoActivado=DNSactivado=true;
        usuarioConectado=false;
        listaUsuarios=new ArrayList <>();
        numeroSalas=cantidadConectados=0;
        baseDatos=new ConexionBD();
        initComponents();
        setVisible(true);
        setSize(700,400);
        //configuracion config=new configuracion(this,baseDatos);
        arrancaServidor();
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                estaPrendido=false;
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }

    /**
     * Establece la conexion con la base de datos y comienza a esperar conexiones
     * @throws IOException 
     */
    private void arrancaServidor() throws IOException {
        chat.append("Arranca la Maquina de Maldad\n");
        chat.append("Conectando a la Base de Datos ...\n");
        chat.append(baseDatos.conectarBD());
        ec=new EsperaConexiones(servidor);
        hilo=new Thread(this,"Server");
        conexiones=new ArrayList <ConexionServidor>();
        hilo.start();

    }
    
    /**
     * Inicializa Gui
     */
    private void initComponents() {

        paneln = new JPanel();
        jScrollPane3 = new JScrollPane();
        usuarios = new JList();
        Usuarios = new JLabel();
        jScrollPane4 = new JScrollPane();
        chat = new JTextArea();
        nombreChat = new JLabel();
        chatsDisponibles = new JComboBox();
        scrollIp = new JScrollPane();
        jTextPane1 = new JTextField(10);
        labelIp = new JLabel();
        jScrollBar1 = new JScrollBar();
        barraMenu = new JMenuBar();
        Archivo = new JMenu();
        preferencias = new JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Usuarios.setText("Usuarios");

        GroupLayout jPanel1Layout = new GroupLayout(paneln);
        paneln.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Usuarios)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Usuarios)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        chat.setColumns(40);
        chat.setRows(10);
        jScrollPane4=new JScrollPane(chat);

        nombreChat.setText("Chat General");

        chatsDisponibles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        chatsDisponibles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaDisponibles(evt);
            }
        });

        scrollIp.setViewportView(jTextPane1);
        StringTokenizer dire=new StringTokenizer(dir.toString(),"/");
        dire.nextToken();
        String direccionIP=dire.nextToken();
        jTextPane1.setText(direccionIP);
        
        labelIp.setText("Direccion IP");

        Archivo.setText("Archivo");
        barraMenu.add(Archivo);
        
        cerrarServidor=new JMenuItem("Cerrar Servidor");
        Archivo.add(cerrarServidor);
        
        cerrarServidor.addActionListener(
                new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        cerrarServidor();
                    }

                
                }
                );
        
        autentificarBD=new JMenuItem("Cambiar Acceso Base de Datos");
        Archivo.add(autentificarBD);
        
        autentificarBD.addActionListener(
                new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        config();
                    }
                }
                );

        preferencias.setText("Preferencias");
        barraMenu.add(preferencias);
               
        desactivarChat=new JMenuItem("Activar/Desactivar Chat");
         preferencias.add(desactivarChat);
         
         desactivarChat.addActionListener(
                new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        chatActivado=!chatActivado;
                    }
                    
                }
                );
        
        desactivarCorreo=new JMenuItem("Activar/Desactivar Correo");
         preferencias.add(desactivarCorreo);
         
          desactivarCorreo.addActionListener(
                new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        correoActivado=!correoActivado;
                    }
                    
                }
                );
         
         desactivarDNS=new JMenuItem("Activar/Desactivar DNS");
         preferencias.add(desactivarDNS);
         
         desactivarDNS.addActionListener(
                new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                        DNSactivado=!DNSactivado;
                    }
                    
                }
                );

        setJMenuBar(barraMenu);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombreChat)
                            .addComponent(chatsDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(labelIp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollIp, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(paneln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(paneln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrollIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelIp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(nombreChat)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(chatsDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
    
    public void config()
    {
        //configuracion config=new configuracion(this,baseDatos);
    }
    
    private void listaDisponibles(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    public static void main(String... a)
    {
        try{
            Servidor servidor = new Servidor();
        }catch(IOException e){
        e.printStackTrace();
        
        }
    }
    /**
     * Cierra el servidor, cierra las conexiones
     */
    private void cerrarServidor() 
    {
        try
        {
            for(int i=0;i<conexiones.size();i++)
            {
                ConexionServidor con = conexiones.get(i);
                con.cerrarConexion();
            }
        }catch(ArrayIndexOutOfBoundsException e)
            {
                show("Error Cerrando Conexiones");
            }finally
            {
                dispose();
                System.exit(0);
            }
    }
                

    /**
     * Hilo que administra las entradas al servidor
     */
    @Override
    public void run() {
        chat.append("\nY Arranca esta Joda :D");
        boolean cerrar=false;
        String nombreparaCerrar="";
        while(estaPrendido)
        {
            try            
            {
                Thread.sleep(15);
            }catch (InterruptedException e){}
            
            if(ec.isSeConectaron())
            {
                ec.setSeConectaron(false);
                chat.append("\nSe ha conectado alguien \n");
                Socket media=ec.getMedia();
                ConexionServidor nuevaConexion=new ConexionServidor (media,++cantidadConectados+"");
                if(esUsuarioRegistrado(nuevaConexion))
                {
                    nuevaConexion.mandaMensaje("/cmd/ Aceptada");
                    nuevaConexion.iniciarConexion();
                    conexiones.add(nuevaConexion);
                    chat.append("y lo hemos sabido introducir\n");
                    usuarioConectado=true;
                }else
                {nuevaConexion.mandaMensaje("/cmd/ Rechazada");
                nuevaConexion.cerrarConexion();}
                chat.append("Desconectamos la señal \n");

            }
            for (ConexionServidor c : conexiones) {
                try
                {
                    Thread.sleep(15);
                }catch (InterruptedException e){}
                
                if (c.isLlegoMensaje())
                {
                    boolean repartirMensaje=true;
                    String mensaje=c.getMensaje();
                    if(mensaje.length()>5)
                    {
                        if(mensaje.substring(0, 5).equals("/cmd/"))
                        {
                            llegaronComandos(mensaje,c);
                            repartirMensaje=false;
                        }
                    }
                    
                    c.setLlegoMensaje(false);
                }
                if(c.ismeCerraron())
                {
                    cerrar=true;
                    nombreparaCerrar=c.getNombre();
                }
            }
            if(usuarioConectado)
            {
                /*distribuirMensaje("/cmd/ actualizarUsuarios "+vector_a_Cadena(listaUsuarios),conexiones.get(0));
                usuarioConectado=false;*/
            }
            if(cerrar)
            {
                Iterator it2 = conexiones.iterator();
                int counter=0;
                while(it2.hasNext())
                {
                    ConexionServidor c=(ConexionServidor)it2.next();
                    if(c.getNombre().equals(nombreparaCerrar))
                    {
                        conexiones.remove(counter);
                        cerrar=false;
                        break;
                    }
                    counter+=1;
                }
            }
            
           
        }
         
    }

    /**
     * En caso de que lleguen palabras reservadas para comandos del ajedrez, se envia a 
     * este metodo que le dice al servidor que hacer
     * @param mensaje Mensaje con la peticion
     * @param c Conexion de donde proviene la peticion, para responderla
     */
    private void llegaronComandos(String mensaje,ConexionServidor c) {
        //chat.append("Comando"+"\n");
        StringTokenizer st=new StringTokenizer(mensaje);
        if(st.nextToken().equals("/cmd/"))
        {
            String token2=st.nextToken();
             if(token2.equals("solicitarConexion"))
             {
                 String token3=st.nextToken();
                 ConexionServidor con=buscarUsuario(token3, conexiones);
                 
                 if (con!=null)
                 {
                    
                    con.mandaMensaje("/cmd/ crearChat "+ numeroSalas);
                    c.mandaMensaje("/cmd/ crearChat "+ numeroSalas);
                 }
                                  
                 
             }else if(token2.equals("cerrarConexion"))
             {
                 c.cerrarConexion();
             }else if(token2.equals("crearUsuario"))
             {
                 if(c.getTipo().compareTo("administrador")==0)
                 {
                     if(baseDatos.crearUsuario(st.nextToken(), st.nextToken(), st.nextToken()))
                         c.mandaMensaje("/cmd/ usuarioCreado");
                     else
                         c.mandaMensaje("/cmd/ usuarioNoCreado");
                 }else
                 {
                     c.mandaMensaje("/cmd/ sinPermisos");
                 }
                 c.cerrarConexion();
             }
        }
        
    }


    private ConexionServidor buscarUsuario(String nombre, ArrayList<ConexionServidor> conexiones) {
        for (ConexionServidor c : conexiones) {
            if(c.getNombre().equals(nombre))
                return c;
        }
        return null;
    }


    public static void show(Object o){JOptionPane.showMessageDialog(null, o);}
    public static void cout(Object o){System.out.println(o);}

    /**
     * Conexion con la base de datos que verifica si un usuario esta registrado o no
     * @param con Conexion para enviar la replica
     * @return  Estado de aceptacion de la peticion
     */
    private boolean esUsuarioRegistrado(ConexionServidor con) 
    {
        chat.append("Esperando Mensaje de Autentificacion\n");
        try
        {
            if(con!=null){
                con.esperarMensaje();
                chat.append("Recibi mensaje\n");
            }
            else
            {show("nos llego conexion vacia");
            return false;}
        }catch(Exception e){show("error con la conexion " + e.toString());}
        String mensaje=con.getMensaje();
        StringTokenizer st= new StringTokenizer(mensaje);
        if(st.nextToken().equals("autentificar"))
        {
            String nombre=st.nextToken();
            String contraseña=st.nextToken();
            chat.append("\nautentificar "+ nombre+" "+contraseña+"\n" );
            if(autentificarUsuario(nombre,contraseña))
            {
                con.setNombre(nombre);
                chat.append("Autentificado como: "+baseDatos.getTipo(nombre)+"\n");
                con.setTipo(baseDatos.getTipo(nombre));
                
                return true;
            }
        }
        return false;
    }


    /**
     * Valida el ingreso al sistema
     * @param usuario String con el nombre de usuario
     * @param contraseña String con la contraseña
     * @return estadp de aceptacion de al peticion
     */
    private boolean autentificarUsuario(String usuario, String contraseña) {
        //chat.append(usuario + " "+ contraseña);
        //String[][] correos = baseDatos.obtenerInfo("usuario");
        //String[][] pass = baseDatos.obtenerInfo("contraseña");
        //if(baseDatos.validadorUsuario(correos, usuario)&& baseDatos.validadorUsuario(pass, contraseña))
        if(baseDatos.verificarInicioSesion(usuario, contraseña))
            {
                chat.append("Ingreso satisfactorio");
                return true;
            }else
                chat.append("El usuario o contraseña esta errada");
        return false;
    }

    
}
/**
 * Clase interna para configurar la Base de DAtos
 * @author ma0
 */
class configuracion extends JDialog{
    
    JTextField puerto, bd, usuario,contra;
    ConexionBD bdd;
    /**
     * Contrstructor d ela clase interna
     * @param baseDatos 
     */
    public configuracion(JFrame a,ConexionBD baseDatos)
    {
       super(a, true);
        
        JButton aceptar=new JButton("Aceptar");
        bdd=baseDatos;
        JPanel datos;
        puerto=new JTextField();
        bd=new JTextField();
        usuario=new JTextField();
        contra=new JTextField();
        setLayout(new BorderLayout());
        datos=new JPanel(new GridLayout(4,2));
        datos.add(new JLabel("Puerto"));
        datos.add(puerto);
        datos.add(new JLabel("Base de Datos"),BorderLayout.EAST);
        datos.add(bd);
        datos.add(new JLabel("Usuario"),BorderLayout.EAST);
        datos.add(usuario);
        datos.add(new JLabel("Contraseña"),BorderLayout.EAST);
        datos.add(contra);
        add(datos, BorderLayout.CENTER);
        add(aceptar,BorderLayout.SOUTH );
        aceptar.addActionListener(
         new ActionListener() {
            public void actionPerformed( ActionEvent evento )
            {
               bdd.configurar("localhost",puerto.getText(), bd.getText(), usuario.getText(), contra.getText());
               dispose();
            }
         }  
      ); 
        setSize(250,140);
        setVisible(true);
        
    }
}
