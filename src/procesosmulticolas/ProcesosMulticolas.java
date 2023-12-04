
package procesosmulticolas;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import javax.swing.*;
import java.awt.*;

public class ProcesosMulticolas extends JFrame implements Runnable ,ActionListener{

    JScrollPane scrollPane = new JScrollPane();
    JScrollPane scrollPane1 = new JScrollPane();
    
    JScrollPane scrollPane2 = new JScrollPane();
    JScrollPane scrollPane3 = new JScrollPane();
    
    JScrollPane scrollPane4 = new JScrollPane();
    JScrollPane scrollPane5 = new JScrollPane();
    
    JScrollPane scrollPane6 = new JScrollPane();
    JScrollPane scrollPane7 = new JScrollPane();
    
    JScrollPane scrollPane8 = new JScrollPane();
    JScrollPane scrollPane9 = new JScrollPane();
    
    JComboBox prioridad = new JComboBox();
    
    JLabel semaforo = new JLabel();
    
    JLabel label = new JLabel("Nombre del proceso: ");
    JLabel label1 = new JLabel("Nombre del proceso: ");
    JLabel label2 = new JLabel("Nombre del proceso: ");
    JLabel label3 = new JLabel("Proceso en ejecucion: Ninguno");
    JLabel label4 = new JLabel("Tiempo: ");
    JLabel label5 = new JLabel("Tabla de procesos (Round Robin - Limite de ejecucion: 5):");
    JLabel label6 = new JLabel("Diagrama de Gant:");
    JLabel label7 = new JLabel("Tabla de Bloqueados:");
    JLabel label8 = new JLabel("Rafaga restante del proceso: 0");
    JLabel label9 = new JLabel("Tabla de procesos (Rafaga más corta):");
    JLabel label10 = new JLabel("Tabla de procesos (Prioridades):");
    JLabel label11 = new JLabel("Prioridad:");
    
    JButton botonIngresarRoundRobin = new JButton("Ingresar Proceso");
    JButton botonIngresarCorta = new JButton("Ingresar Proceso");
    JButton botonIngresarPrioridad = new JButton("Ingresar Proceso");
    JButton botonIniciar = new JButton("Comenzar");
    JButton botonBloquear = new JButton("Bloquear proceso");
    
    JTextField tfNombre = new JTextField("A1");
    JTextField tfNombre2 = new JTextField("B1");
    JTextField tfNombre3 = new JTextField("C1");
    
    JTextField[][] tabla = new JTextField[100][7];
    JTextField[][] tabla2 = new JTextField[100][2];
    JTextField[][] tabla3 = new JTextField[100][3];
    JTextField[][] tablaBloqueados = new JTextField[100][3];
    JLabel[][] diagrama = new JLabel[50][200];  
    
    ListaCircular colaRoundRobin = new ListaCircular();
    ListaCircular colaCorta = new ListaCircular();
    ListaCircular colaPrioridad= new ListaCircular();
    
    Nodo nodoEjecutado;
    
    int filasRoundRobin = 0, filasCorta = 0, filasPrioridad = 0, rafagaTemporal;
    int tiempoGlobal = 0;
    int coorX = 0;
    
    Thread procesos;
    
    public static void main(String[] args) {

        /*ProcesosMulticolas pm = new ProcesosMulticolas(); 
        pm.setBounds(0, 0, 1320, 730);
        pm.setTitle("Procesos con multiples colas");
        pm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pm.setVisible(true);*/
        SwingUtilities.invokeLater(ProcesosMulticolas::new);
    }

    ProcesosMulticolas(){
        
        // Configuración inicial del JFrame
        setTitle("Procesos con múltiples colas");
        setSize(1320, 730);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Espaciado entre componentes

        // Método para inicializar componentes
        initializeComponents();

        // Agregar paneles al JFrame
        add(createControlPanel(), BorderLayout.WEST);
        add(createDisplayPanel(), BorderLayout.CENTER);

        pack(); // Ajusta el tamaño de la ventana a los componentes
        setLocationRelativeTo(null); // Centrar la ventana
        setVisible(true);
        
        
        dibujarSemaforo("Verde.jpg");
       
    }
     
    
    private void initializeComponents() {
        // Inicializar los campos de texto para los nombres de los procesos
        tfNombre = new JTextField("A1");
        //tfNombre.setPreferredSize(new Dimension(200, 24)); // Ejemplo de tamaño preferido
        tfNombre2 = new JTextField("B1");
        //tfNombre2.setPreferredSize(new Dimension(200, 24)); // Ejemplo de tamaño preferido
        tfNombre3 = new JTextField("C1");
       // tfNombre3.setPreferredSize(new Dimension(200, 24)); // Ejemplo de tamaño preferido

        // Inicializar los botones con sus etiquetas
        botonIngresarRoundRobin = new JButton("Ingresar Proceso");
        botonIngresarCorta = new JButton("Ingresar Proceso");
        botonIngresarPrioridad = new JButton("Ingresar Proceso");
        botonIniciar = new JButton("Empezar");
        botonBloquear = new JButton("Bloquear proceso actual");

        // Inicializar el JComboBox para las prioridades
        prioridad = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        prioridad.setPreferredSize(new Dimension(200, 24)); // Ejemplo de tamaño preferido

        // Inicializar las etiquetas
        label = new JLabel("Nombre del proceso (RR):");
        label1 = new JLabel("Nombre del proceso (Ráfaga corta):");
        label2 = new JLabel("Nombre del proceso (Prioridad):");
        label3 = new JLabel("Proceso en ejecución: Ninguno");
        label4 = new JLabel("Tiempo: 0");
        label5 = new JLabel("Tabla de procesos (Round Robin - Límite de ejecución: 5):");
        label6 = new JLabel("Diagrama de Gantt:");
        label7 = new JLabel("Tabla de Bloqueados:");
        label8 = new JLabel("Ráfaga restante del proceso: 0");
        label9 = new JLabel("Tabla de procesos (Ráfaga más corta):");
        label10 = new JLabel("Tabla de procesos (Prioridades):");
        label11 = new JLabel("Prioridad:");

        // Inicializar JScrollPane para las tablas y otros componentes que se desplazarán
        scrollPane = new JScrollPane();
        scrollPane1 = new JScrollPane();
        scrollPane2 = new JScrollPane();
        scrollPane3 = new JScrollPane();
        scrollPane4 = new JScrollPane();
        scrollPane5 = new JScrollPane();
        scrollPane6 = new JScrollPane();
        scrollPane7 = new JScrollPane();
        scrollPane8 = new JScrollPane();
        scrollPane9 = new JScrollPane();
        
        // Configurar semáforo (si tienes una imagen, cámbiala por "path/to/traffic-light-image.png")
        semaforo = new JLabel();
        ImageIcon semaforoIcon = new ImageIcon("path/to/traffic-light-image.png");
        semaforo.setIcon(semaforoIcon);

        // Configurar JScrollPane y tablas internas, por ejemplo para el Round Robin:
        tabla = new JTextField[100][7]; // Suponiendo que ya tienes esta estructura definida
        // Aquí deberías añadir la tabla al scrollPane correspondiente y configurarla      
        scrollPane.setBounds(50, 350, 2500, 2500);
        scrollPane.setPreferredSize(new Dimension(1000, 600));  
        //scrollPane.setBackground(Color.WHITE);
        
        scrollPane1.setBounds(50, 350, 600, 200);
        scrollPane1.setPreferredSize(new Dimension(1150, 200)); 
        //scrollPane1.setBackground(Color.WHITE);
        
        scrollPane2.setBounds(50, 20, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(1000, 500));  
        //scrollPane2.setBackground(Color.WHITE);
        
        scrollPane3.setBounds(50, 20, 600, 300);
        scrollPane3.setPreferredSize(new Dimension(1000, 200)); 
        //scrollPane3.setBackground(Color.WHITE);
       

        // Configurar el tamaño preferido para los campos de texto y botones
        Dimension textFieldPreferredSize = new Dimension(30, 20);
        tfNombre.setPreferredSize(textFieldPreferredSize);
        tfNombre2.setPreferredSize(textFieldPreferredSize);
        tfNombre3.setPreferredSize(textFieldPreferredSize);

        Dimension buttonPreferredSize = new Dimension(80, 25); // Tamaño ajustado para botones
        botonIngresarRoundRobin.setPreferredSize(buttonPreferredSize);
        botonIngresarCorta.setPreferredSize(buttonPreferredSize);
        botonIngresarPrioridad.setPreferredSize(buttonPreferredSize);
        botonIniciar.setPreferredSize(buttonPreferredSize);
        botonBloquear.setPreferredSize(buttonPreferredSize);


        // Configurar los ActionListeners aquí
        botonIngresarRoundRobin.addActionListener(this);
        botonIngresarCorta.addActionListener(this);
        botonIngresarPrioridad.addActionListener(this);
        botonIniciar.addActionListener(this);
        botonBloquear.addActionListener(this);
    }
    

    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Control de Procesos"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Establecer el tamaño del panel de control para que sea más pequeño
        panel.setPreferredSize(new Dimension(300, 300)); // Cambia las dimensiones según tus necesidades

        // Agregar los campos de texto y botones para ingresar procesos Round Robin
        panel.add(new JLabel("Nombre del proceso (RR):"), gbc);
        panel.add(tfNombre, gbc);
        panel.add(botonIngresarRoundRobin, gbc);

        // Agregar los campos de texto y botones para ingresar procesos de ráfaga corta
        panel.add(new JLabel("Nombre del proceso (Ráfaga corta):"), gbc);
        panel.add(tfNombre2, gbc);
        panel.add(botonIngresarCorta, gbc);

        // Agregar los campos de texto y botones para ingresar procesos de prioridad
        panel.add(new JLabel("Nombre del proceso (Prioridad):"), gbc);
        panel.add(tfNombre3, gbc);
        panel.add(prioridad, gbc);
        panel.add(botonIngresarPrioridad, gbc);

        // Agregar botones de control de simulación
        panel.add(botonIniciar, gbc);
        panel.add(botonBloquear, gbc);

        // Configurar los botones y campos de texto
        botonIngresarRoundRobin.setActionCommand("Ingresar Round Robin");
        botonIngresarCorta.setActionCommand("Ingresar Ráfaga Corta");
        botonIngresarPrioridad.setActionCommand("Ingresar Prioridad");
        botonIniciar.setActionCommand("Iniciar Simulación");
        botonBloquear.setActionCommand("Bloquear Proceso");
        
        // Ajustar el layout y tamaño del panel
        //panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // FlowLayout para mejor ajuste de componentes
        panel.setPreferredSize(new Dimension(300, panel.getPreferredSize().height)); // Anchura fija, altura flexible
        return panel;
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));  // Dos columnas principales.
        panel.setBorder(BorderFactory.createTitledBorder("Visualización de Procesos"));

        // Panel izquierdo para las tablas de procesos.
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));  // Alineación vertical para las tablas.

        // Agrega la tabla de procesos Round Robin al panel izquierdo.
        scrollPane1.setBorder(BorderFactory.createTitledBorder("Round Robin"));
        leftPanel.add(scrollPane1);

        // Agrega la tabla de procesos de ráfaga más corta al panel izquierdo.
        scrollPane7.setBorder(BorderFactory.createTitledBorder("Ráfaga más corta"));
        leftPanel.add(scrollPane7);

        // Agrega la tabla de procesos por prioridades al panel izquierdo.
        scrollPane9.setBorder(BorderFactory.createTitledBorder("Prioridades"));
        leftPanel.add(scrollPane9);

        // Panel derecho para el semáforo, Diagrama de Gantt y tabla de bloqueados.
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));  // Alineación vertical para el semáforo y el diagrama.

        // Agrega el semáforo al panel derecho.
        semaforo.setBorder(BorderFactory.createTitledBorder("Semaforo"));
        rightPanel.add(semaforo);

        // Agrega el Diagrama de Gantt al panel derecho.
        scrollPane3.setBorder(BorderFactory.createTitledBorder("Diagrama de Gantt"));
        rightPanel.add(scrollPane3);

        // Agrega la tabla de bloqueados al panel derecho.
        scrollPane5.setBorder(BorderFactory.createTitledBorder("Bloqueados"));
        rightPanel.add(scrollPane5);

        // Agrega los dos paneles al panel principal.
        panel.add(leftPanel);
        panel.add(rightPanel);

        return panel;
    }

    
    
    
    public void dibujarSemaforo(String color){     
    // Ajustar la escala de la imagen del semáforo
    ImageIcon imgIcon = new ImageIcon(getClass().getResource(color));
    Image imgEscalada = imgIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH); // Tamaño más pequeño para el semáforo
    Icon iconoEscalado = new ImageIcon(imgEscalada);
    semaforo.setIcon(iconoEscalado);
    semaforo.setPreferredSize(new Dimension(1000, 150)); // Tamaño preferido para el JLabel del semáforo
          
    }
    
    public void dibujarTablaRoundRobin(String nombre, int rafaga, int tiempo){
        
        scrollPane.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T_llegada");
        JLabel texto3 = new JLabel("Rafaga");
        JLabel texto4 = new JLabel("T_comienzo");
        JLabel texto5 = new JLabel("T_final");
        JLabel texto6 = new JLabel("T_retorno");
        JLabel texto7 = new JLabel("T_espera");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        texto4.setBounds(260, 20, 150, 20);
        texto5.setBounds(340, 20, 150, 20);
        texto6.setBounds(420, 20, 150, 20);
        texto7.setBounds(500, 20, 150, 20);
        
        scrollPane.add(texto1);
        scrollPane.add(texto2);
        scrollPane.add(texto3);
        scrollPane.add(texto4);
        scrollPane.add(texto5);
        scrollPane.add(texto6);
        scrollPane.add(texto7);
        
        for(int i = 0; i<filasRoundRobin; i++){
            
            for(int j = 0; j<7; j++){
            
                if(tabla[i][j] != null){
                    
                    scrollPane.add(tabla[i][j]);
                    
                } else {
                
                    tabla[i][j] = new JTextField();
                    tabla[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);
                    
                    scrollPane.add(tabla[i][j]);
                    
                }

            }
        
        }
        
        tabla[filasRoundRobin-1][0].setText(nombre);
        tabla[filasRoundRobin-1][1].setText(Integer.toString(tiempo));
        tabla[filasRoundRobin-1][2].setText(Integer.toString(rafaga));

        scrollPane.repaint();
        scrollPane1.setViewportView(scrollPane);
        
    }
    
    public void dibujarTablaCorta(ListaCircular cola, JScrollPane scrollPar, JScrollPane scrollImpar){
        
        scrollPar.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("Rafaga");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        
        scrollPar.add(texto1);
        scrollPar.add(texto2);
        
        if(cola.getCabeza() != null){
        
        Nodo temp = cola.getCabeza().getSiguiente();
        
            for(int i = 0; i<cola.getTamaño(); i++){

                for(int j = 0; j<2 ; j++){

                        tabla2[i][j] = new JTextField("");
                        tabla2[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);

                        scrollPar.add(tabla2[i][j]);

                }

                tabla2[i][0].setText(temp.getLlave());
                tabla2[i][1].setText(Integer.toString(temp.getRafaga()));
                
                temp = temp.getSiguiente();

            }
        
        }
        
        scrollPar.repaint();
        scrollImpar.setViewportView(scrollPar);
        
    }
    
    public void dibujarTablaPrioridad(ListaCircular cola, JScrollPane scrollPar, JScrollPane scrollImpar){
        
        scrollPar.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("Rafaga");
        JLabel texto3 = new JLabel("Prioridad");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        
        scrollPar.add(texto1);
        scrollPar.add(texto2);
        scrollPar.add(texto3);
        
        if(cola.getCabeza() != null){
        
        Nodo temp = cola.getCabeza().getSiguiente();
        
            for(int i = 0; i<cola.getTamaño(); i++){

                for(int j = 0; j<3 ; j++){

                        tabla3[i][j] = new JTextField("");
                        tabla3[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);

                        scrollPar.add(tabla3[i][j]);

                }

                tabla3[i][0].setText(temp.getLlave());
                tabla3[i][1].setText(Integer.toString(temp.getRafaga()));
                tabla3[i][2].setText(Integer.toString(temp.getPrioridad()));
                
                temp = temp.getSiguiente();

            }
        
        }
        
        scrollPar.repaint();
        scrollImpar.setViewportView(scrollPar);
        
    }
    
    public void llenarBloqueados( ListaCircular cola, JScrollPane scrollPar, JScrollPane scrollImpar){
        
        scrollPane4.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        
        scrollPane4.add(texto1);
        scrollPane4.add(texto2);
        scrollPane4.add(texto3);
        
        if(cola.getCabeza() != null){
        
        Nodo temp = cola.getCabeza().getSiguiente();
        
            for(int i = 0; i<cola.getTamaño()-1; i++){

                for(int j = 0; j<3 ; j++){

                        tablaBloqueados[i][j] = new JTextField("");
                        tablaBloqueados[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);

                        scrollPar.add(tablaBloqueados[i][j]);

                }

                tablaBloqueados[i][0].setText(temp.getLlave());
                tablaBloqueados[i][1].setText(tablaBloqueados[i][1].getText() + " " + Integer.toString(temp.getLlegada()));
                tablaBloqueados[i][2].setText(Integer.toString(temp.getRafaga()));
                
                temp = temp.getSiguiente();

            }
        
        }
        
        scrollPar.repaint();
        scrollImpar.setViewportView(scrollPar);
        
    }
    
    public void llenarRestante(){
        
        tabla[nodoEjecutado.getIndice()-1][3].setText(tabla[nodoEjecutado.getIndice()-1][3].getText() + "," + Integer.toString(nodoEjecutado.getComienzo()));
        tabla[nodoEjecutado.getIndice()-1][4].setText(tabla[nodoEjecutado.getIndice()-1][4].getText() + "," +Integer.toString(nodoEjecutado.getFinalizacion()));
        tabla[nodoEjecutado.getIndice()-1][5].setText(Integer.toString(nodoEjecutado.getFinalizacion() - nodoEjecutado.getLlegada()));
        
        String [] comienzos = tabla[nodoEjecutado.getIndice()-1][3].getText().split(","); 
        String [] finales = tabla[nodoEjecutado.getIndice()-1][4].getText().split(","); 
        finales[0] = "0";
        String cadena = "";
        
        for(int i = 1; i<comienzos.length; i++){
            
            cadena = cadena + (Integer.parseInt(comienzos[i]) - Integer.parseInt(finales[i-1])) + ",";
            
        }
          
        tabla[nodoEjecutado.getIndice()-1][6].setText(cadena);
        
        
    }
    
    public void dibujarDiagrama(String nombre, int coorX, int coorY){
        
        scrollPane2.removeAll();
        
        for(int i = 0; i<200; i++){
            
            diagrama[0][i] = new JLabel(Integer.toString(i));
            diagrama[0][i].setBounds(40 + (i*20), 20, 20, 20);

            scrollPane2.add(diagrama[0][i]);
            
        }
        
        diagrama[nodoEjecutado.getIndice()][0] = new JLabel("  " + nombre);
        diagrama[nodoEjecutado.getIndice()][0].setBounds(0, 20 + (nodoEjecutado.getIndice()*20), 50, 20);
        
        scrollPane2.add(diagrama[nodoEjecutado.getIndice()][0]);
        
        JLabel img = new JLabel();
        
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("barra.png"));

        Image imgEscalada = imgIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado = new ImageIcon(imgEscalada);
        
        for(int i = 1; i < filasRoundRobin + 1; i++){
            
            for(int j = 0; j < coorX+1; j++){
                
                if(diagrama[i][j] != null){
                
                    scrollPane2.add(diagrama[i][j]);
                    
                }
                
            }
            
        }
        
        diagrama[nodoEjecutado.getIndice()][coorX+1] = new JLabel();
        diagrama[nodoEjecutado.getIndice()][coorX+1].setBounds(40 + (coorX*20), 20 + (nodoEjecutado.getIndice()*20), 20, 20);
        diagrama[nodoEjecutado.getIndice()][coorX+1].setIcon(iconoEscalado);
        
        scrollPane2.add(diagrama[nodoEjecutado.getIndice()][coorX+1]);
        
        scrollPane2.repaint();
        scrollPane3.setViewportView(scrollPane2);
            
    }
    
   

    
    public void ingresar(ListaCircular cola, String nombre, int prioridad , int rafaga, int tiempo, int filas){
        
        cola.insertar(nombre, prioridad, rafaga, tiempo, filas);
        
    }
    
    public void ordenarRafagas(){
        
        int movimientos = 0;
        int contador = 0;
        
        Nodo temp = colaCorta.getCabeza().getSiguiente();
        
        int menorRaf = colaCorta.getCabeza().getRafaga();
        
        while(!(temp.equals(colaCorta.getCabeza()))){
    
            contador++;
            
            if(temp.getRafaga() < menorRaf){
            
                menorRaf = temp.getRafaga();
                movimientos = contador;
                
            }
            
            temp = temp.getSiguiente();
            
        }
        
        for(int i = 0; i < movimientos; i++){
            
            colaCorta.intercambiar(colaCorta.getCabeza());
            
        }
        
    }
    
    public int calcularRafaga(){
        
        return 1 + ((int) (Math.random()*12));
        
    }
    
    public void cortaLlenaRound(){
                
        if( (tiempoGlobal%3 == 0) ){

            for(int i = 0; i<3; i++){

                if(colaCorta.getCabeza() != null){

                    Nodo temp = colaCorta.getCabeza();
                    filasRoundRobin++;
                    ingresar(colaRoundRobin, temp.getLlave(), 0 , temp.getRafaga(), tiempoGlobal, filasRoundRobin);
                    dibujarTablaRoundRobin(temp.getLlave(), temp.getRafaga(), tiempoGlobal);

                    colaCorta.eliminar(colaCorta.getCabeza());

                }

            }

        }
        
        dibujarTablaCorta(colaCorta, scrollPane6, scrollPane7);
        
    }
    
    public void ordenarPrioridades(){
        
        int movimientos = 0;
        int contador = 0;
        
        Nodo temp = colaPrioridad.getCabeza().getSiguiente();
        
        int menorPrio = colaPrioridad.getCabeza().getPrioridad();
        
        while(!(temp.equals(colaPrioridad.getCabeza()))){
    
            contador++;
            
            if(temp.getPrioridad() < menorPrio){
            
                menorPrio = temp.getPrioridad();
                movimientos = contador;
                
            }
            
            temp = temp.getSiguiente();
            
        }
        
        for(int i = 0; i < movimientos; i++){
            
            colaPrioridad.intercambiar(colaPrioridad.getCabeza());
            
        }
        
    }
    
    public void prioLlenaCorta(){
                
        if( (tiempoGlobal%3 == 0) ){

            for(int i = 0; i<3; i++){

                if(colaPrioridad.getCabeza() != null){

                    Nodo temp = colaPrioridad.getCabeza();
                    filasCorta++;
                    ingresar(colaCorta, temp.getLlave(), 0 , temp.getRafaga(), tiempoGlobal, filasCorta);
                    dibujarTablaCorta(colaCorta, scrollPane6, scrollPane7);

                    colaPrioridad.eliminar(colaPrioridad.getCabeza());

                }

            }

        }
        
        dibujarTablaPrioridad(colaPrioridad, scrollPane8, scrollPane9);
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        if(e.getSource() == botonIngresarRoundRobin){
            
            filasRoundRobin++;
            
            String nombre = tfNombre.getText();
            rafagaTemporal = calcularRafaga();
            
            ingresar(colaRoundRobin, nombre, 0,rafagaTemporal, tiempoGlobal, filasRoundRobin);
            dibujarTablaRoundRobin(nombre, rafagaTemporal, tiempoGlobal);
            
            tfNombre.setText("A" + (filasRoundRobin + 1));
            
        } else if(e.getSource() == botonIngresarCorta){
        
            filasCorta++;
            
            String nombre = tfNombre2.getText();
            rafagaTemporal = calcularRafaga();
            
            ingresar(colaCorta, nombre, 0, rafagaTemporal, tiempoGlobal, filasCorta);
            dibujarTablaCorta(colaCorta, scrollPane6, scrollPane7);
            
            tfNombre2.setText("B" + (filasCorta + 1));
            
            ordenarRafagas();
            
        } else if(e.getSource() == botonIngresarPrioridad){
        
            filasPrioridad++;
            
            String nombre = tfNombre3.getText();
            String p = String.valueOf(prioridad.getSelectedItem());
            int prio = Integer.parseInt(p);
            rafagaTemporal = calcularRafaga();
            
            ingresar(colaPrioridad, nombre, prio, rafagaTemporal, tiempoGlobal, filasPrioridad);
            dibujarTablaPrioridad(colaPrioridad, scrollPane8, scrollPane9);
            
            tfNombre3.setText("C" + (filasPrioridad + 1));
            
            ordenarPrioridades();
            
        } else if(e.getSource() == botonIniciar){
        
            procesos = new Thread( this );
            procesos.start();  
            
        } else if(e.getSource() == botonBloquear){
        
            if(nodoEjecutado.getRafaga() != 0){
            
                filasRoundRobin++;
                ingresar(colaRoundRobin, nodoEjecutado.getLlave() + "*", 0,nodoEjecutado.getRafaga(), tiempoGlobal, filasRoundRobin);
                dibujarTablaRoundRobin(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                nodoEjecutado.setFinalizacion(tiempoGlobal);
                llenarRestante();
                colaRoundRobin.eliminar(colaRoundRobin.getCabeza());
                llenarBloqueados(colaRoundRobin, scrollPane4, scrollPane5);
                nodoEjecutado = colaRoundRobin.getCabeza();
                nodoEjecutado.setComienzo(tiempoGlobal);

            }
        }
        
    }
    
    @Override
    public void run() {
    
            try{

            while(colaRoundRobin.getTamaño() != 0){
                
                dibujarSemaforo("Rojo.jpg");
                
                nodoEjecutado = colaRoundRobin.getCabeza();
                nodoEjecutado.setComienzo(tiempoGlobal);
                
                int tiempoEjecutado = 0;
                
                while(nodoEjecutado.getRafaga() > 0 && tiempoEjecutado < 5){
                    
                    nodoEjecutado.setRafaga(nodoEjecutado.getRafaga()-1);
                    
                    label3.setText("Proceso en ejecucion: " + nodoEjecutado.getLlave());
                    label4.setText("Tiempo: " + String.valueOf(tiempoGlobal) + " Segundos.");
                    label8.setText("Rafaga restante del proceso: " + nodoEjecutado.getRafaga());
                    
                    dibujarDiagrama(nodoEjecutado.getLlave(), coorX, nodoEjecutado.getIndice());
                    llenarBloqueados(colaRoundRobin, scrollPane4, scrollPane5);
                    
                    tiempoGlobal++;
                    coorX++;
                    tiempoEjecutado++;
                    
                    cortaLlenaRound();
                    prioLlenaCorta();
                    
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProcesosMulticolas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                nodoEjecutado.setFinalizacion(tiempoGlobal);
                llenarRestante();
                
                if(nodoEjecutado.getRafaga() == 0){
                
                    colaRoundRobin.eliminar(colaRoundRobin.getCabeza());
                    
                } else if (tiempoEjecutado == 5){
                
                    colaRoundRobin.getCabeza().setLlave(colaRoundRobin.getCabeza().getLlave());
                    colaRoundRobin.intercambiar(colaRoundRobin.getCabeza());
                    
                }
                
            }

            dibujarSemaforo("Verde.jpg");
            label3.setText("Proceso en ejecucion: Ninguno");
            
        }catch(Exception e){
        
            System.out.print("No se que poner aca :D");
            
        }  
    
    }
    
}
