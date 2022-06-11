package co.edu.uniquindio.compiladores.semantica
import co.edu.uniquindio.compiladores.lexico.Error

class TablaSimbolos (var listaErrores:ArrayList<Error> ) {


    var listaSimbolos:ArrayList<Simbolo> = ArrayList()

    /**
     * Permite guardar un simbolo que representa una variables o una constante o un parametro, arreglo
     */
    fun guardarSimboloValor(nombre:String, tipodato:String, modificable:Boolean ,ambito:String, fila:Int , columna:Int ){

        var s = buscarSimboloValor(nombre, ambito)

        if(s==null) {
            listaSimbolos.add(Simbolo(nombre,tipodato, modificable, ambito, fila, columna))
        }else {
            listaErrores.add( Error(" El campo $nombre ya existe dentro del ambito $ambito", fila, columna, ""))
        }

    }

    /**
     * permite guardar un simbolo que represente una función o metodo
     */
    fun guardarSimboloFuncion(nombre: String, tipoRetorno: String, tipoParametros: ArrayList<String>, ambito: String, fila:Int, columna: Int){
        var s=buscarSimboloFuncion(nombre, tipoParametros)

        var simb:Simbolo?=buscarSimboloFuncion(nombre,tipoParametros)

        if (s == null){
            listaSimbolos.add(Simbolo(nombre, tipoRetorno, tipoParametros, ambito))
        }else{
            listaErrores.add(Error("La funcion con nombre $nombre, ya existe dentro del ambito $ambito", fila+1, columna+1,""))
        }
    }


    /**
     * Permite buscar un valor dentro de la tabla de simbolos
     */
    fun buscarSimboloValor(nombre:String, ambito:String):Simbolo? {
        for (s in listaSimbolos) {
            if (s.tiposParametros == null) {
                if (s.nombre == nombre && s.ambito == ambito) {
                    return s
                }
            }
        }
            return null
        }

    fun buscarFuncAmbito(ambito: String): Simbolo?{
        for(s in listaSimbolos){
            if(s.nombre == ambito){
                return s
            }
        }
        return null
    }
        /**
         * Permite buscar una funcion dentro de la tabla de simbolos
         */
        fun buscarSimboloFuncion(nombre: String, tipoParametros: ArrayList<String>): Simbolo? {
            for (s in listaSimbolos) {
                if (s.tiposParametros != null) {
                    if (s.nombre == nombre && s.tiposParametros == tipoParametros) {
                        return s
                    }
                }
            }
            return null
        }

    override fun toString(): String {
        return "TablaSimbolos( listaSimbolos=$listaSimbolos)"
    }


}