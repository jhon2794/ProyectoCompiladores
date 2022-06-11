package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error

class ExpresionCadena(var cadena: Token, var expresion:Expresion?):Expresion() {
    override fun toString(): String {
        return "ExpresionCadena(cadena=$cadena, expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expesion Cadena")
        raiz.children.add(TreeItem("Cadena: ${cadena.lexema}"))
        if (expresion != null) {
            raiz.children.addAll(expresion!!.getArbolVisual())
        }
        return raiz
    }

    /**
     *
     */
    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String, listaErrores: ArrayList<Error>): String {

    return "~cade"
    }

    override fun getJavaCode(): String {
        var codigo = cadena.getJavaCode()

        if (expresion != null){
            codigo +="+" +expresion!!.getJavaCode()+";"
        }
        return codigo
    }

}