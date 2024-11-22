package ElAhorcadoJuego;

public class Jugador {

    private String nombre;   //Nombre
    private double puntos;   //Puntos

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPuntos() {
        return puntos;
    }

    public void incrementarPuntos(double puntos) {  //Método para incrementar puntos cuando jugador acierte alguna palabra
        this.puntos += puntos;
    }

    public void reiniciarPuntos() {          //Método para reiniciar los puntos al finalizar una partida
        this.puntos = 0;
    }

    @Override
    public String toString() {
        return nombre + ": " + puntos + " puntos";
    }
}
