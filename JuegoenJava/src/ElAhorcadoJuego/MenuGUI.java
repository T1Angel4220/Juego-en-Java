package ElAhorcadoJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuGUI extends JFrame {

    private ArbolPalabras arbolPalabras;
    private static List<Jugador> jugadores = new ArrayList<>(); // Mantener los jugadores

    public MenuGUI() {
        arbolPalabras = new ArbolPalabras();
        initComponents();
    }

    private void initComponents() {
        setTitle("Menú Principal");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(7, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCargarArchivo = crearBoton("Cargar datos desde archivo");
        JButton btnIngresarDatos = crearBoton("Ingresar datos manualmente");
        JButton btnRegistrarJugadores = crearBoton("Registrar jugadores");
        JButton btnMostrarEliminarJugadores = crearBoton("Mostrar/Eliminar jugadores");
        JButton btnConfigurarJugar = crearBoton("Jugar");
        JButton btnLimpiarArchivo = crearBoton("Limpiar archivo de dificultad");
        JButton btnSalir = crearBoton("Salir");

        btnCargarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarOpcionesDeDificultad(true);
            }
        });

        btnIngresarDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarOpcionesDeDificultad(false);
            }
        });

        btnRegistrarJugadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarJugadores();
            }
        });

        btnMostrarEliminarJugadores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarEliminarJugadores();
            }
        });

        btnConfigurarJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configurarYJugar();
            }
        });

        btnLimpiarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarArchivoDeDificultad();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelBotones.add(btnCargarArchivo);
        panelBotones.add(btnIngresarDatos);
        panelBotones.add(btnRegistrarJugadores);
        panelBotones.add(btnMostrarEliminarJugadores);
        panelBotones.add(btnConfigurarJugar);
        panelBotones.add(btnLimpiarArchivo);
        panelBotones.add(btnSalir);

        JLabel lblTitulo = new JLabel("Adivina y Aprende!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelMain.add(lblTitulo, BorderLayout.NORTH);
        panelMain.add(panelBotones, BorderLayout.CENTER);

        getContentPane().add(panelMain);

        crearArchivosDeDificultad();
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setPreferredSize(new Dimension(200, 50));
        return boton;
    }

    private void mostrarOpcionesDeDificultad(boolean cargarDatos) {
        String[] opciones = {"Fácil", "Normal", "Difícil", "Aleatorio"};
        String opcion = (String) JOptionPane.showInputDialog(this, "Seleccione el nivel de dificultad:",
                "Opciones de Dificultad", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (opcion != null) {
            if (cargarDatos) {
                cargarDatosDesdeArchivo("Dificultad/" + opcion + ".txt");
            } else {
                ingresarDatosManual("Dificultad/" + opcion + ".txt");
            }
        }
    }

    private void cargarDatosDesdeArchivo(String rutaArchivoDestino) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoDestino, true))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(" ");
                    String palabra = partes[0];
                    List<Character> comodines = new ArrayList<>();
                    for (int i = 1; i < partes.length; i++) {
                        comodines.add(partes[i].charAt(0));
                    }
                    arbolPalabras.insertar(new Palabra(palabra, comodines));
                    bw.write(linea);
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(this, "Datos cargados exitosamente desde el archivo.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar datos desde el archivo.");
            }
        }
    }

    private void ingresarDatosManual(String rutaArchivoDestino) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoDestino, true))) {
            while (true) {
                String palabra = JOptionPane.showInputDialog(this, "Ingrese la palabra (o 'fin' para terminar):");
                if (palabra == null || palabra.equalsIgnoreCase("fin")) {
                    break;
                }
                String comodinesStr = JOptionPane.showInputDialog(this, "Ingrese los comodines separados por comas:");
                if (comodinesStr != null) {
                    List<Character> comodines = new ArrayList<>();
                    for (String comodin : comodinesStr.split(",")) {
                        comodines.add(comodin.trim().charAt(0));
                    }
                    arbolPalabras.insertar(new Palabra(palabra, comodines));
                    bw.write(palabra + " " + String.join(" ", comodinesStr.split(",")));
                    bw.newLine();
                    JOptionPane.showMessageDialog(this, "Palabra ingresada correctamente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al ingresar palabras.");
        }
    }

    private void registrarJugadores() {
        String nombre;
        while (true) {
            nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del jugador (o 'fin' para terminar):");
            if (nombre == null || nombre.equalsIgnoreCase("fin")) {
                break;
            }
            jugadores.add(new Jugador(nombre));
        }
    }

    private void mostrarEliminarJugadores() {
        if (jugadores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay jugadores registrados.");
            return;
        }

        StringBuilder listaJugadores = new StringBuilder();
        for (int i = 0; i < jugadores.size(); i++) {
            listaJugadores.append(i + 1).append(". ").append(jugadores.get(i).getNombre()).append("\n");
        }

        String opcion = JOptionPane.showInputDialog(this, "Jugadores registrados:\n" + listaJugadores + "\nIngrese el número del jugador a eliminar (o 'fin' para terminar):");
        if (opcion == null || opcion.equalsIgnoreCase("fin")) {
            return;
        }

        try {
            int indice = Integer.parseInt(opcion) - 1;
            if (indice >= 0 && indice < jugadores.size()) {
                jugadores.remove(indice);
                JOptionPane.showMessageDialog(this, "Jugador eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Número de jugador inválido.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
        }
    }

    private void configurarYJugar() {
        if (jugadores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe registrar jugadores antes de configurar el juego.");
            return;
        }

        String[] opciones = {"Fácil", "Normal", "Difícil", "Aleatorio"};
        String dificultad = (String) JOptionPane.showInputDialog(this, "Seleccione el nivel de dificultad:",
                "Modo de Juego", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (dificultad != null) {
            List<Palabra> palabras = cargarPalabrasPorModo(dificultad);

            if (palabras.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay palabras registradas para la dificultad " + dificultad);
                return;
            }

            int cantidadRondas;
            try {
                cantidadRondas = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese la cantidad de rondas:"));
            } catch (NumberFormatException e) {
                return; // Maneja el caso donde el usuario presiona cancelar o ingresa un valor inválido
            }

            int palabrasNecesarias = jugadores.size() * cantidadRondas;
            if (palabras.size() < palabrasNecesarias) {
                JOptionPane.showMessageDialog(this, "Palabras insuficientes en la dificultad seleccionada.");
                return;
            }

            String[] modalidades = {"Por tiempo", "Por intentos"};
            String modalidad = (String) JOptionPane.showInputDialog(this, "Seleccione la modalidad de juego:",
                    "Modalidad de Juego", JOptionPane.QUESTION_MESSAGE, null, modalidades, modalidades[0]);

            if (modalidad != null) {
                Juego juego;
                if (modalidad.equals("Por tiempo")) {
                    int tiempoPorRonda;
                    try {
                        tiempoPorRonda = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese el tiempo por ronda (en segundos):"));
                    } catch (NumberFormatException e) {
                        return; // Maneja el caso donde el usuario presiona cancelar o ingresa un valor inválido
                    }
                    juego = new Juego(palabras, jugadores, cantidadRondas, tiempoPorRonda, 0);
                } else {
                    int intentosPermitidos;
                    try {
                        intentosPermitidos = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese los intentos permitidos por palabra:"));
                    } catch (NumberFormatException e) {
                        return; // Maneja el caso donde el usuario presiona cancelar o ingresa un valor inválido
                    }
                    juego = new Juego(palabras, jugadores, cantidadRondas, 0, intentosPermitidos);
                }
                JuegoAhorcadoGUI gui = new JuegoAhorcadoGUI(juego);
                gui.setVisible(true);
                dispose(); // Cierra la ventana del menú principal
            }
        }
    }

    private void limpiarArchivoDeDificultad() {
        String[] opciones = {"Fácil", "Normal", "Difícil", "Aleatorio"};
        String dificultad = (String) JOptionPane.showInputDialog(this, "Seleccione el archivo de dificultad que desea limpiar:",
                "Limpiar Archivo", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (dificultad != null) {
            String rutaArchivo = "Dificultad/" + dificultad + ".txt";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
                bw.write("");
                JOptionPane.showMessageDialog(this, "El archivo " + rutaArchivo + " ha sido limpiado.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al limpiar el archivo " + rutaArchivo);
            }
        }
    }

    private List<Palabra> cargarPalabrasPorModo(String modo) {
        List<Palabra> palabras = new ArrayList<>();
        String rutaArchivo = "Dificultad/" + modo + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(" ");
                String palabra = partes[0];
                List<Character> comodines = new ArrayList<>();
                for (int i = 1; i < partes.length; i++) {
                    comodines.add(partes[i].charAt(0));
                }
                if (!palabra.trim().isEmpty()) {
                    palabras.add(new Palabra(palabra, comodines));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las palabras desde " + rutaArchivo);
        }

        return palabras;
    }
    private void crearArchivosDeDificultad() {
        try {
            new File("Dificultad/Fácil.txt").createNewFile();
            new File("Dificultad/Normal.txt").createNewFile();
            new File("Dificultad/Difícil.txt").createNewFile();
            new File("Dificultad/Aleatorio.txt").createNewFile();
        } catch (IOException e) {
            System.out.println("Error al crear los archivos de dificultad: " + e.getMessage());
        }
    }


}
