package ElAhorcadoJuego;

public class NodoCircular<T> {
    private T dato;
    private NodoCircular<T> siguiente;  //Referencia al siguiente

    public NodoCircular(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public NodoCircular<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCircular<T> siguiente) {
        this.siguiente = siguiente;
    }
}
