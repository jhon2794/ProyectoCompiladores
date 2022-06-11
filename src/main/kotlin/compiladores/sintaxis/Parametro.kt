package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Parametro( var tipoDato:Token, var nombre: Token ) {

    override fun toString(): String {
        return "Parametro( tipoDato=$tipoDato, nombre=$nombre)"
    }

    /**
     *
     */
    fun getArbolVisual(): TreeItem<String> {

        return TreeItem("${nombre.lexema} : ${tipoDato.lexema}")
    }

    fun getJavaCode():String{
        var codigo =""+tipoDato.getJavaCode()+" "+nombre.getJavaCode()

        return codigo
    }
}