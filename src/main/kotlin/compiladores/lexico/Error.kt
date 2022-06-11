package co.edu.uniquindio.compiladores.lexico

class Error(var error:String, var fila:Int, var columna:Int, var categoria:String ){
    override fun toString(): String {
        return "Error(error='$error', fila=$fila, columna=$columna, categoria=$categoria), \n"
    }

}