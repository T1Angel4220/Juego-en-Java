package ElAhorcadoJuego;

public class ListaCircular<T> {
    private NodoCircular<T> primero;
    private NodoCircular<T> actual;

    public ListaCircular() {
        this.primero = null;   
        this.actual = null;
    }

    public void insertar(T dato) {
        NodoCircular<T> nuevoNodo = new NodoCircular<>(dato);
        if (primero == null) {   //Si la lista está vacía 
            primero = nuevoNodo;  //El primero va a ser el nuevo nodo
            primero.setSiguiente(primero);  //El siguiente apunta a sí mismo para ser una lista circular
            actual = primero;  //El actual va a ser el primero 
        } else {
            NodoCircular<T> temp = primero; 
            while (temp.getSiguiente() != primero) {  //Busca al último nodo(el nodo cuyo siguiente apunte al primero)
                temp = temp.getSiguiente();     
            }
            temp.setSiguiente(nuevoNodo);
            nuevoNodo.setSiguiente(primero);    //El siguiente del nuevo nodo se establace para que apunte al primero
        }
    }

    public T obtenerActual() {
        if (actual != null) {
            return actual.getDato();
        }
        return null;
    }

    public void avanzar() {
        if (actual != null) {
            actual = actual.getSiguiente();
        }
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int getTotal() {
        if (primero == null) {
            return 0;
        }
        int total = 1;
        NodoCircular<T> temp = primero;
        while (temp.getSiguiente() != primero) {
            total++;
            temp = temp.getSiguiente();
        }
        return total;
    }
}
