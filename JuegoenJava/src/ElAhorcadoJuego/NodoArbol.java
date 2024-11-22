package ElAhorcadoJuego;

public class NodoArbol {

    Palabra palabra;    //Instancia de palabra que almacena la palabra y los comodines 
    NodoArbol izquierda, derecha;  //Referencia al izquierdo y al derecho

    public NodoArbol(Palabra palabra) {
        this.palabra = palabra;
        izquierda = derecha = null;
    }
}
