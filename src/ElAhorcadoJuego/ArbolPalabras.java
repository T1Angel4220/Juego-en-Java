package ElAhorcadoJuego;

import java.util.ArrayList;
import java.util.List;

public class ArbolPalabras {

    private NodoArbol raiz;

    public ArbolPalabras() {
        raiz = null;
    }

    public void insertar(Palabra palabra) {
        raiz = insertarRecursivo(raiz, palabra);  //Se llama al método de insertar recursivo para insertar la palabra (se le pasa la raíz y la palabra)
    }

    private NodoArbol insertarRecursivo(NodoArbol raiz, Palabra palabra) {
        if (raiz == null) {  //Si la raíz es igual a null
            raiz = new NodoArbol(palabra);      //La palabra se va a insertar en la raíz del árbol
            return raiz;      
        }
        if (palabra.getPalabra().compareTo(raiz.palabra.getPalabra()) < 0) { //Si la palabra a insertar es menor que la que está en la raíz(de acuerdo a orden lexicográfico) se manda a insertar en el subarbol izquierdo
            raiz.izquierda = insertarRecursivo(raiz.izquierda, palabra);
        } else if (palabra.getPalabra().compareTo(raiz.palabra.getPalabra()) > 0) { //Si la palabra a insertar es mayor a la que está en la raíz, se inserta en el subarbol derecho de la raíz
            raiz.derecha = insertarRecursivo(raiz.derecha, palabra);
        }
        return raiz;
    }

    public Palabra buscar(String palabra) {
        return buscarRecursivo(raiz, palabra); //Para buscar se llama al método buscar Recursivo para que haga la operación de búsqueda (se le pasa la palabra y la raíz)
    }

    private Palabra buscarRecursivo(NodoArbol raiz, String palabra) {
        if (raiz == null || raiz.palabra.getPalabra().equals(palabra)) {//Si la raíz es igual a null o la palabra de la raíz es igual a la palabra que se está buscando
            return raiz != null ? raiz.palabra : null;//Si la raíz no es igual a null la palabra que se está buscando es la raíz  sino es null
        }
        if (palabra.compareTo(raiz.palabra.getPalabra()) < 0) {  //si la palabra a buscar es menor que la palabra que está en la raíz se busca en el subarbol izquierdo 
            return buscarRecursivo(raiz.izquierda, palabra);     //Se llama nuevamente al método buscar recursivo para que haga de nuevo las comparaciones ahora en el subarbol izquierdo 
        }
        return buscarRecursivo(raiz.derecha, palabra);         //Se llama al método buscar recursivo y se le dice que busque en el subarbol derecho
    }

    public List<Palabra> obtenerTodasLasPalabras() { //Lista para obtener todas las palabras
        List<Palabra> lista = new ArrayList<>();     //Se crea una nueva lista 
        inorder(raiz, lista);                 //Se llama al método inorder
        return lista;        //Retorna la lista
    }

    private void inorder(NodoArbol raiz, List<Palabra> lista) {
        if (raiz != null) {    //Verifica si la raíz (el nodo actual) es diferente de null
            inorder(raiz.izquierda, lista);   // 1. Recorrer el subárbol izquierdo
            lista.add(raiz.palabra);         //2. Visitar el nodo actual    //Una vez que se han visitado todos los nodos del subárbol izquierdo, se añade la palabra del nodo actual a la lista lista
            inorder(raiz.derecha, lista);    //3. Recorrer el subárbol derecho
        }
    }
}
