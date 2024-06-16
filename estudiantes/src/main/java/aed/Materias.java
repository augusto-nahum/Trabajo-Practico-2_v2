package aed;

public class Materias {

    private Trie<DatosMateria> materias;

    public Materias(){
        this.materias = new Trie<DatosMateria>();
    }

    public Trie<DatosMateria> getmaterias(){
        return this.materias;
    }

    public void definir(String materia,DatosMateria datos){
        this.materias.definir(materia,datos);
    }

    public DatosMateria definicion(String materia){
        return this.materias.definicion(materia);
    }

    public String[] obtenerMaterias(){
        return this.materias.obtenerClaves();
    }

    public void eliminar(String materia){
        this.materias.eliminar(materia);
    }

}
