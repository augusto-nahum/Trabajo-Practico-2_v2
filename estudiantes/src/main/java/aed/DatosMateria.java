package aed;

import java.util.ArrayList;
// Agregamos la clase para usar min
import static java.lang.Math.min;

public class DatosMateria {

    // MateriaEquiv tiene el nombre equivalente y el trie de materias de la carrera correspondiente a dicha equivalencia
    public class MateriaEquiv{
        private String materia;
        private Materias trieMaterias;

        private MateriaEquiv(String materiaEquiv, Materias otraCarrera){
            this.materia = materiaEquiv;
            this.trieMaterias = otraCarrera;
        }

        public String getMateria(){
            return this.materia;
        }

        public Materias getTrieMaterias(){
            return this.trieMaterias;
        }
    }
    
    private ArrayList<MateriaEquiv> equivalencias;
    private int cantAlu;
    private Trie<String> alumnos;
    private int[] cargosDoc;

    public DatosMateria(){
    this.equivalencias = new ArrayList<MateriaEquiv> ();
    this.cantAlu = 0;
    this.alumnos = new Trie<String>();
    this.cargosDoc = new int[4]; // inicializo un array con cuatro ceros
    }
    
    public ArrayList<MateriaEquiv> equivalencias(){
        return this.equivalencias;
    }

    public int cantEquivalencias(){
        return this.equivalencias.size();
    }

    public void agregarEquivalencia(String materiaEquiv, Materias otraCarrera){
        MateriaEquiv par = new MateriaEquiv(materiaEquiv, otraCarrera);
        this.equivalencias.add(par);
    }

    public void agregarAlumno(String lu){
        this.cantAlu ++;
        this.alumnos.definir(lu, lu);
    }

    // cargo es un numero entre 0 y 3
    public void agregarCargo(int cargo){
        this.cargosDoc[cargo]++;
    }

    public int cantAlu(){
        return this.cantAlu;
    }

    public String[] alumnos(){
        return this.alumnos.obtenerClaves();
    }

    public int[] cargos(){
        return this.cargosDoc;
    }

    private int minimo(int c1,int c2,int c3,int c4){
        return min(min(c1,c2),min(c3,c4));
    }

    private int cupo(){
        int cupoProf = 250*this.cargosDoc[3];
        int cupoJtp = 100*this.cargosDoc[2];
        int cupoAy1 = 20*this.cargosDoc[1];
        int cupoAy2 = 30*this.cargosDoc[0];
        
        return minimo(cupoProf,cupoJtp,cupoAy1,cupoAy2);    
    }

    public boolean excedeCupo(){
        return this.cantAlu > cupo();
    }
}
