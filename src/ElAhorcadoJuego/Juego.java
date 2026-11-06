package ElAhorcadoJuego;

import java.util.*;

public class Juego {

    private List<Palabra> palabras;
    private ListaCircular<Jugador> jugadores;
    private int cantidadRondas;
    private int tiempoPorRonda; // En segundos
    private int intentosPermitidos;
    private Set<Palabra> palabrasUsadas; // Set para rastrear las palabras usadas
    private int rondaActual;
    private int turnoActual;

    public Juego(List<Palabra> palabras, List<Jugador> jugadores, int cantidadRondas, int tiempoPorRonda, int intentosPermitidos) {
        this.palabras = filtrarPalabrasVacias(palabras);
        this.cantidadRondas = cantidadRondas;
        this.tiempoPorRonda = tiempoPorRonda;
        this.intentosPermitidos = intentosPermitidos;
        this.jugadores = new ListaCircular<>();
        this.palabrasUsadas = new HashSet<>();
        for (Jugador jugador : jugadores) {
            this.jugadores.insertar(jugador);
        }
        this.rondaActual = 1;
        this.turnoActual = 0;
    }

    private List<Palabra> filtrarPalabrasVacias(List<Palabra> palabras) {
        List<Palabra> palabrasFiltradas = new ArrayList<>();
        for (Palabra palabra : palabras) {    //Va iterando en las palabras y se verifica si la palabra obtendia con los espacios al inicio y al final eliminados no está vacía
            if (!palabra.getPalabra().trim().isEmpty()) { 
                palabrasFiltradas.add(palabra);   //Si no está vacía se añade a la lista de palabras filtradas
            }
        }
        return palabrasFiltradas;
    }

    public void siguienteTurno() {
        turnoActual++;  //Se va incrementando el turno
        if (turnoActual >= jugadores.getTotal()) {  //Si el turno actual es mayor o igual a la cantidad de jugadores
            turnoActual = 0;       //El turno se establece en cero
            rondaActual++;          //Y la ronda aumenta ya que se han acabado los turnos para todos los jugadores en la ronda
        }
        jugadores.avanzar();
    }

    public boolean juegoTerminado() {
        return rondaActual > cantidadRondas || palabrasUsadas.size() >= palabras.size(); //El juego termina si la ronda actual es mayor a la cantidad de rondas establecidas
    }                                                                                    // o si las palabras usadas es mayor o igual a la cantidad de palabras que existen

    public Palabra obtenerPalabraAleatoria() {
        if (palabrasUsadas.size() >= palabras.size()) {
            return null; // No hay más palabras disponibles
        }
        Random random = new Random();
        Palabra palabra;
        do {
            palabra = palabras.get(random.nextInt(palabras.size()));     //Se va a obtener una palabra aleatoria de acuerdo al número que ocupe en la lista de palabras
        } while (palabrasUsadas.contains(palabra));  // Repite hasta encontrar una palabra que no haya sido usada
        palabrasUsadas.add(palabra);         // La palabra se va a añadir a la lista de palabras usadas
        return palabra;
    }

    public String completarPalabra(Palabra palabra) {
        StringBuilder palabraCompleta = new StringBuilder();   //palabraCompleta se usará para construir la palabra completa
        int comodinIndex = 0;    //Se usa para recorrer la lista de comodines
        for (char c : palabra.getPalabra().toCharArray()) {      //Recorre cada caracter de la palabra original conviertiéndole en un array de caracteres 
            if (c == '*') {  //Si el caracter actual es igual a *
                palabraCompleta.append(palabra.getComodines().get(comodinIndex));   //Se añade al String Builder el caracter correspondiente a la lista de comodines
                comodinIndex++;  //Se va aumentando el indice de comodín 
            } else {
                palabraCompleta.append(c);   //Si el caracter actual no es un comodin se añade al StringBuilder
            }
        }
        return palabraCompleta.toString();
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public ListaCircular<Jugador> getJugadores() {
        return jugadores;
    }

    public int getTiempoPorRonda() {
        return tiempoPorRonda;
    }

    public int getIntentosPermitidos() {
        return intentosPermitidos;
    }

    public void reiniciarPuntajes() {
        Jugador inicio = jugadores.obtenerActual();
        Jugador temp = inicio;
        do {
            temp.reiniciarPuntos();
            jugadores.avanzar();
            temp = jugadores.obtenerActual();
        } while (temp != inicio);
    }
}
