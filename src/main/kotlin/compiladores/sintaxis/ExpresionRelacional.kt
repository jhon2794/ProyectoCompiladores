package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error


class ExpresionRelacional(var expresion1:Expresion,var operadorRelacional:Token,var expresion2:Expresion ):Expresion() {
    override fun toString(): String {
        return "ExpresionRelacional(expresion1=$expresion1, operadorRelacional=$operadorRelacional, expresion2=$expresion2)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Expresion Relacional")
        raiz.children.add(expresion1.getArbolVisual())
        raiz.children.add(TreeItem("Op Relacional: ${operadorRelacional.lexema}"))
        raiz.children.add(expresion2.getArbolVisual())
        return raiz
    }

    /**
     *
     */
    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String, listaErrores: ArrayList<Error>):String{
        return "~istf"
    }
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if(expresion1!=null && expresion2!=null){
            expresion1.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            expresion2.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = expresion1.getJavaCode()+" "+operadorRelacional.getJavaCode()+ " "+expresion2.getJavaCode()

        return codigo
    }
}