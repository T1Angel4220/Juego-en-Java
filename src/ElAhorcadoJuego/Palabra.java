package ElAhorcadoJuego;

import java.util.List;

public class Palabra {

    private String palabra;
    private List<Character> comodines;  //Lista de comodines

    public Palabra(String palabra, List<Character> comodines) {
        this.palabra = palabra;
        this.comodines = comodines;
    }

    public String getPalabra() {
        return palabra;
    }

    public List<Character> getComodines() { //Se obtienen los comodines
        return comodines;
    }

    @Override
    public String toString() {
        return palabra + " " + comodines.toString();
    }
}
// ES UN ARBOL DE BUSQUEDA BINARIA Y LEXIVOGRAFICO