package co.edu.uniquindio.compiladores.semantica

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.sintaxis.UnidadDeCompilacion

class AnalizadorSemantico(var unidadDeCompilacion:UnidadDeCompilacion) {

    val listaErrores: ArrayList<Error> = ArrayList()
    val tablaSimbolos:TablaSimbolos = TablaSimbolos(listaErrores)

    /**
     *
     */
    fun llenarTablaSimbolos (){
        unidadDeCompilacion.llenarTablaSimbolos(tablaSimbolos,listaErrores)
    }

    /**
     *
     */
    fun analizarSemantica(){
        unidadDeCompilacion.analizarSemantica(tablaSimbolos, listaErrores)
    }

}