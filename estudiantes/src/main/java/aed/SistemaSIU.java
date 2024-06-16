package aed;

import java.util.ArrayList;

public class SistemaSIU {

    private Trie<Integer> estudiantes;
    private Trie<Materias> carreras;

    enum CargoDocente{
        // Los tuvimos que dar vuelta porque estaban al revez que en los test
        PROF,
        JTP,
        AY1,
        AY2,
    }

    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias){
        this.estudiantes = new Trie<Integer>();
        // Agregamos a todos los estudiantes el sistema (con 0 inscripciones)
        for (int i = 0; i < libretasUniversitarias.length; i++) {  
            this.estudiantes.definir(libretasUniversitarias[i], 0);	   
        } 
        // Agregamos las carreras
        this.carreras = new Trie<Materias>();

        for (int i = 0; i < infoMaterias.length; i++){

            DatosMateria datos = new DatosMateria(); // Definimos los datos para todas las equivalencias de una "misma" materia

            for (int j = 0; j < infoMaterias[i].getParesCarreraMateria().length; j++){

                String materia = infoMaterias[i].getParesCarreraMateria()[j].getNombreMateria();
                String carrera = infoMaterias[i].getParesCarreraMateria()[j].getCarrera();

                if(!this.carreras.estaDefinido(carrera)){ // Si la carrera no esta definida aun
                    Materias materiasCarreraNueva = new Materias();
                    this.carreras.definir(carrera, materiasCarreraNueva);
                    materiasCarreraNueva.definir(materia, datos);
                    datos.agregarEquivalencia(materia, materiasCarreraNueva);

                } else {
                    Materias materiasCarrera = this.carreras.definicion(carrera);
                    materiasCarrera.definir(materia, datos);
                    datos.agregarEquivalencia(materia, materiasCarrera);
                }
            }
        }
    }

    public void inscribir(String estudiante, String carrera, String materia){
        Materias materias = this.carreras.definicion(carrera);
        DatosMateria datos = materias.definicion(materia);
        datos.agregarAlumno(estudiante);

        // agregamos esto porque sino no sumaban en el Trie de estudiantes
        int old = this.estudiantes.definicion(estudiante);
        this.estudiantes.definir(estudiante, old+1);
    }

    public void agregarDocente(CargoDocente cargo, String carrera, String materia){
        Materias materias = this.carreras.definicion(carrera);
        DatosMateria datos = materias.definicion(materia);
        datos.agregarCargo(cargo.ordinal());	    
    }     

    public int[] plantelDocente(String materia, String carrera){
        Materias  materias = this.carreras.definicion(carrera);
        DatosMateria datos = materias.definicion(materia);
        return datos.cargos();
    }

    public void cerrarMateria(String materia, String carrera){
        Materias materias = this.carreras.definicion(carrera);
        DatosMateria datos = materias.definicion(materia);
        String[] alumnos = datos.alumnos();
        int cantAlu = datos.cantAlu();   

        // Restamos una inscripcion a cada estudiante que estaba inscripto en la materia a cerrar
        for(int i = 0; i < cantAlu; i++){
            int old = this.estudiantes.definicion(alumnos[i]);
            this.estudiantes.definir(alumnos[i], old-1);
        }
        for(int j = 0; j < datos.cantEquivalencias(); j++){
            // cambiamos esto para que se cumpla el tipo Materias.
            // adentro de cada array esta el nombre correcto de la materia a partir de esa carrera. ESE TENGO QUE BORRAR
            Materias raizMaterias =  datos.equivalencias().get(j).getTrieMaterias();
            String materiaABorrar =  datos.equivalencias().get(j).getMateria();

            raizMaterias.eliminar(materiaABorrar);
        }

        // el error que tenemos ahora es que no se resta la materia (CREO!!!!!)

    }

    public int inscriptos(String materia, String carrera){
        Materias materias = this.carreras.definicion(carrera);
        DatosMateria datos = materias.definicion(materia);
        return datos.cantAlu();    
    }

    public boolean excedeCupo(String materia, String carrera){
        Materias materias = this.carreras.definicion(carrera);
        DatosMateria datos = materias.definicion(materia);
        return datos.excedeCupo();    
    }

    public String[] carreras(){
        return this.carreras.obtenerClaves();	    
    }

    public String[] materias(String carrera){
        Materias materias = this.carreras.definicion(carrera);   
        return materias.obtenerMaterias(); 
    }

    public int materiasInscriptas(String estudiante){
        return this.estudiantes.definicion(estudiante);
    }
}
