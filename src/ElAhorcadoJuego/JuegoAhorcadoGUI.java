package ElAhorcadoJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class JuegoAhorcadoGUI extends JFrame {

    private Juego juego;
    private JTextField inputField;
    private JTextArea outputArea;
    private JLabel labelPalabra;
    private JLabel labelRonda;
    private JLabel labelTurno;
    private JButton btnSubmit;
    private Jugador jugadorActual;
    private Palabra palabraActual;
    private Timer timer;
    private int tiempoRestante;
    private int intentosRestantes;
    private boolean primerIntento;

    public JuegoAhorcadoGUI(Juego juego) {
        this.juego = juego;
        setTitle("Adivina y Aprende!");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        startGame();
    }

    private void initComponents() {
        inputField = new JTextField(20);
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        labelPalabra = new JLabel("Palabra: ");
        labelPalabra.setFont(new Font("Arial", Font.BOLD, 18));
        labelRonda = new JLabel("Ronda: 1");
        labelRonda.setFont(new Font("Arial", Font.BOLD, 18));
        labelTurno = new JLabel("Turno del jugador: ");
        labelTurno.setFont(new Font("Arial", Font.BOLD, 18));
        btnSubmit = new JButton("Enviar");

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarRespuesta();
            }
        });

        JPanel panelInput = new JPanel();
        panelInput.add(new JLabel("Ingrese su respuesta: "));
        panelInput.add(inputField);
        panelInput.add(btnSubmit);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        JPanel panelNorth = new JPanel();
        panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
        panelNorth.add(labelRonda);
        panelNorth.add(labelTurno);

        panelMain.add(panelNorth, BorderLayout.NORTH);
        panelMain.add(labelPalabra, BorderLayout.CENTER);
        panelMain.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        panelMain.add(panelInput, BorderLayout.SOUTH);

        getContentPane().add(panelMain);
    }

    private void startGame() {
        mostrarSiguienteTurno(true);
    }

    private void iniciarCronometro() {
        if (timer != null) {
            timer.cancel();
        }
        tiempoRestante = juego.getTiempoPorRonda();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (tiempoRestante > 0) {  //Si el tiempo restante es mayor a 0, se va restando el tiempo restante
                    tiempoRestante--;
                    setTitle("Adivina y Aprende! - Tiempo restante: " + tiempoRestante + " segundos");
                } else {
                    timer.cancel();
                    tiempoAgotado();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000); // Ejecuta cada segundo
    }

    private void mostrarSiguienteTurno(boolean nuevaPalabra) {
        if (juego.juegoTerminado()) {
            if (timer != null) {
                timer.cancel(); // Detener el temporizador si el juego ha terminado
            }
            mostrarResultados();
            return;
        }

        jugadorActual = juego.getJugadores().obtenerActual();
        if (nuevaPalabra) {
            palabraActual = juego.obtenerPalabraAleatoria();
            if (palabraActual == null) {
                mostrarResultados();
                return;
            }
            intentosRestantes = juego.getIntentosPermitidos();
            primerIntento = true;
        }
        labelPalabra.setText("Palabra: " + mostrarPalabraConComodines(palabraActual));
        labelRonda.setText("Ronda: " + juego.getRondaActual());
        labelTurno.setText("Turno del jugador: " + jugadorActual.getNombre());
        outputArea.append("Turno de " + jugadorActual.getNombre() + "\n");
        inputField.setText("");
        inputField.requestFocus();
        if (juego.getTiempoPorRonda() > 0) {
            iniciarCronometro();
        }
    }

    private String mostrarPalabraConComodines(Palabra palabra) {
        StringBuilder palabraConComodines = new StringBuilder();
        for (char c : palabra.getPalabra().toCharArray()) {
            if (c == '*') {
                palabraConComodines.append('_'); // Representación del comodín
            } else {
                palabraConComodines.append(c);
            }
        }
        return palabraConComodines.toString();
    }

    private void verificarRespuesta() {
        String respuesta = inputField.getText().trim();
        String palabraCompleta = completarPalabra(palabraActual);
        if (respuesta.equals(palabraCompleta)) {
            if (primerIntento) {
                JOptionPane.showMessageDialog(this, "Correcto!");
                jugadorActual.incrementarPuntos(1);
            } else {
                JOptionPane.showMessageDialog(this, "Correcto, pero en el segundo intento o más! Puntos: 0.5");
                jugadorActual.incrementarPuntos(0.5);
            }
            juego.siguienteTurno();
            mostrarSiguienteTurno(true); // Nueva palabra
        } else {
            intentosRestantes--;
            if (intentosRestantes > 0) {
                primerIntento = false;
                JOptionPane.showMessageDialog(this, "Incorrecto! Te quedan " + intentosRestantes + " intentos.");
                mostrarSiguienteTurno(false); // Misma palabra
            } else {
                JOptionPane.showMessageDialog(this, "Incorrecto! La palabra correcta era: " + palabraCompleta);
                juego.siguienteTurno();
                mostrarSiguienteTurno(true); // Nueva palabra
            }
        }
    }

    private String completarPalabra(Palabra palabra) {
        StringBuilder palabraCompleta = new StringBuilder();
        int comodinIndex = 0;
        for (char c : palabra.getPalabra().toCharArray()) {
            if (c == '*') {
                palabraCompleta.append(palabra.getComodines().get(comodinIndex));
                comodinIndex++;
            } else {
                palabraCompleta.append(c);
            }
        }
        return palabraCompleta.toString();
    }

    private void tiempoAgotado() {
        if (juego.juegoTerminado()) {
            mostrarResultados();
        } else {
            JOptionPane.showMessageDialog(this, "Tiempo agotado!");
            btnSubmit.setEnabled(false);
            inputField.setEnabled(false);
            JButton btnRegresar = new JButton("Regresar al menú principal");
            btnRegresar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new MenuGUI().setVisible(true);
                    dispose();
                }
            });
            JPanel panelMain = (JPanel) getContentPane();
            panelMain.add(btnRegresar, BorderLayout.SOUTH);
            panelMain.revalidate();
            panelMain.repaint();
        }
    }

    private void mostrarResultados() {
        JOptionPane.showMessageDialog(this, "El juego ha terminado!\n" + obtenerResultados());
        juego.reiniciarPuntajes(); // Reinicia los puntajes de los jugadores
        new MenuGUI().setVisible(true);
        dispose();
    }

    private String obtenerResultados() {
        StringBuilder resultados = new StringBuilder();
        Jugador inicio = juego.getJugadores().obtenerActual();
        Jugador temp = inicio;
        do {
            resultados.append(temp).append("\n");
            juego.getJugadores().avanzar();
            temp = juego.getJugadores().obtenerActual();
        } while (temp != inicio);
        return resultados.toString();
    }
}
