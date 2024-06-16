package aed;

import java.util.ArrayList;

public class Trie<T> {
    private Nodo raiz;
    private int tamanio;

    // INVREP: ??? /no se si hay que escribir el del trie/ preguntar ???

    // DIAPOS LABO:
    //  No llego por dos claves al mismo nodo / los nodos tienen un
    // solo padre salvo la ra´ız (que no tiene padre) / es un ´arbol
    //  No hay nodos in´utiles o (bien dicho) los nodos, si no tienen
    // significado, tienen hijos.
    // QUE MAS:
    //  El "tamanio" coincide con la cantidad de claves definidas

    // Constructor de Trie
    public Trie() {
        raiz = null;
        tamanio = 0;
    }

    // Clase privada Nodo para implementar un Trie
    private class Nodo {
        // Usamos un array fijo de 256 caracteres, la posicion de cada caracter es la que le corresponde segun ASCII
        // Si en una posicion no hay nada es porque no hay una clave que se construya siguiendo por ese caracter.
        private ArrayList<Nodo> siguientes; 
        private T definicion;

        // Constructor de Nodo
        public Nodo(){
            this.siguientes = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                this.siguientes.add(null);
            } // Cada nodo tiene un arreglo fijo de tamaño 256 (caracteres de ASCII)
            this.definicion = null; 
        }

        // Metodo para verificar si un nodo tiene un hijo particular
        // Se usa para buscar (recorriendo los caracteres de una clave)
        public boolean contieneHijo(char c) {
            return this.siguientes.get(c) != null; // Si c es un hijo del nodo actual su posicion correspodiente deberia ser no vacia
        }

        // Metodo para obtener el nodo hijo de un caracter específico
        // Se usa para buscar (recorriendo los caracteres de una clave)
        public Nodo obtenerHijo(char c) {
            return this.siguientes.get(c); // Devuelve la posisicon correspondiente a dicho caracter
        }

        // Metodo para agregar un nuevo hijo a este nodo
        // Se usa para definir claves
        public void agregarHijo(char c, Nodo nodo) {
            this.siguientes.set(c, nodo); // Agrega un nodo en la posicion del caracter c
        }

        // Metodo para eliminar un hijo particular
        // Se usa para eliminar claves.
        public void eliminarHijo(char c) {
            this.siguientes.set(c, null); // Elimina el nodo en la posicion del caracter c
        }

        // Metodo para establecer la definición
        // Se usa para definir el valor de una clave una vez que llegamos al final de la misma
        public void setDefinicion(T definicion) {
            this.definicion = definicion; 
        }

        // Metodo para obtener la definición
        // Se usa para saber si estoy al final de una clave
        public T obtenerDefinicion() {
            return this.definicion;
        }

        // Metodo para verificar si el nodo tiene varios hijos
        // Se usa a la hora de eliminar claves para guardar la ultima clave util: Un criterio es que tenga mas de un hijo
        public boolean tieneHijos() {
            int contador = 0;  // Voy a contar la cantidad de hijos
            for (Nodo hijo : this.siguientes) {  // Recorro todos los nodos en el array "siguientes"
                if (hijo != null) {  // Si el nodo es disinto de null, es un hijo
                    contador++;  // Sumo 1
                    if (contador > 1) {  // Si al final hay mas de un hijo devuelvo true
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // Metodo para evaluar si una clave pertenece
    public boolean estaDefinido(String clave) {
        if(this.raiz == null){ 
            return false;  // Si la raiz es nula no está lo que busco
        }
        Nodo actual = this.raiz; // Comienzo en la raiz
        for (int i = 0; i < clave.length(); i++) { // Recorro la palabra clave
            char c = clave.charAt(i);
            if (!actual.contieneHijo(c)) { // Si en algun momento se "corta" mi palabra clave, devuelvo false
                return false;
            }
            actual = actual.obtenerHijo(c); // Avanzo lo que pueda sobre la clave
        }
        return (actual.obtenerDefinicion() != null); // Si logro llegar al final, chequeo que efectivamente este definida;
    }

    // Metodo para definir una palabra en el Trie
    public void definir(String clave, T definicion) {
        if(this.raiz == null){ // Si el trie estaba vacio
            this.raiz = new Nodo(); // Creo el primer Nodo
        }
        Nodo actual = this.raiz; // Comienzo en la raiz
        for (int i = 0; i < clave.length(); i++) { // Recorro la palabra clave
            char c = clave.charAt(i);
            if (!actual.contieneHijo(c)) { // Si un caracter no está a partir de ese punto agrego el resto
                actual.agregarHijo(c, new Nodo());
            }
            actual = actual.obtenerHijo(c); // Avanzo hasta recorrer toda la clave
        }
        actual.setDefinicion(definicion); // Una vez que agregue mi clave, en su caracter final la defino
        this.tamanio++;
    }

    // Metodo para obtener la definición de una palabra en el Trie
    public T definicion(String clave) {
        Nodo actual = this.raiz; // Comienzo en la raiz
        if(this.raiz == null){ // Si la raiz es nula no está lo que busco
            return null;
        }
        for (int i = 0; i < clave.length(); i++) { // Recorro la palabra clave
            char c = clave.charAt(i);
            if (!actual.contieneHijo(c)) { // Si en algun momento se "corta" mi palabra clave, devuelvo null
                return null;
            }
            actual = actual.obtenerHijo(c); // Avanzo lo que pueda sobre la clave
        }
        return actual.obtenerDefinicion(); // Si logro llegar al final, obtengo la definicion de mi clave
    }

    // Metodo para eliminar una clave ///////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void eliminar(String clave) {
        Nodo actual = this.raiz;  // Comienzo en la raiz
        Nodo ultimoNodoUtil = null;  // La idea es guardar el ultimo nodo util para borrar la referencia al resto de la clave vieja
        char ultimoIndice = ' '; // Aqui guardamos la posicion del ultimo caracter util
        //Nodo ultimoFinDePalabra = null;

        if(this.raiz == null){ // Si la raiz es nula no hago nada
            return;
        }

        for (int i = 0; i < clave.length(); i++) {
            char c = clave.charAt(i);  // Recorro la palabra clave

            ////ACA TENGO EL PROBLEMA PARA MI; 
            /// CASO: ESTOY EN EL 1 DE ALGORITMOS1 , MI UltimoNodoUtil y ultimoIndice es S. 
            /// ENTRO A EL FOR, C = 1, actual es el array que tiene el 1, 2 , etc....
            if (!actual.contieneHijo(c)) {  // Si no tiene algun caracter de mi clave no elimino nada
                ultimoIndice = c;
                break; // No modifica el trie (ANTES DECIA RETURN)
            }

            
            // Guardo el ultimo nodo (distinto del ultimo nodo de mi clave) con varios hijos o con definicion como ultimoNodoUtil
            if ((i <= clave.length() - 1) && (actual.tieneHijos() || actual.obtenerDefinicion() != null)) {  /// modifique de i != clave.length()
                ultimoNodoUtil = actual;
                ultimoIndice = c; /// PASO DE UN INT (INDICE) A UNA LETRA???
            }
            
            actual = actual.obtenerHijo(c); // Paso al nodo del caracter
        }

        //ultimoNodoUtil.obtenerHijo(ultimoIndice).setDefinicion(null); // Elimino la definicion de mi clave

        this.tamanio--;

        // if (actual.tieneHijos()) { // Si mi clave tiene hijos no hago nada mas porque el nodo de la clave es prefijo de otra
        //     return; 
        // }

        if (ultimoNodoUtil != null) { // Si encontramos un UltimoNodoUtil elimino su hijo "c"
            ultimoNodoUtil.eliminarHijo(ultimoIndice);
        }

        // } else { // Si es null es porque la clave era la unica en el diccionario
        //     this.raiz = null; // vacio mi trie
        // }
    }

    // Metodo para obetener un Array con todas las claves
    public String[] obtenerClaves() {
        ArrayList<String> claves = new ArrayList<>(); // Declaro un ArrayList vacio
        obtenerClaves(this.raiz, "", claves); // Lo lleno con todas las claves en orden ASCII
        return claves.toArray(new String[this.tamanio]);
    }

    // Metodo auxiliar para obtener las claves
    private void obtenerClaves(Nodo nodo, String prefijo, ArrayList<String> claves) {
        if (nodo == null) { // Si llego a un nodo nulo devuelvo lo que tengo
            return;
        }
        if (nodo.obtenerDefinicion() != null) { // Si llegue a un nodo con definicion es porque encontre una clave
            claves.add(prefijo); // Agrego la clave
        }
        for (char c = 0; c < 256; c++) { // Para el nodo actual "sigo el camino" de todos los caracteres posibles en orden ASCII
            if (nodo.contieneHijo(c)) { // Si el nodo tiene el hijo c entonces hago recursion agregando al prefijo c
                obtenerClaves(nodo.obtenerHijo(c), prefijo + c, claves);
            }
        }
    }
    

    // Método para obtener el tamaño del Trie (la cantidad de claves)
    public int getTamanio() {
        return tamanio;
    }

}
