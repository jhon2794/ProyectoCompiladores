package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error

class UnidadDeCompilacion(var nombreClass: Token, var listaFunciones:ArrayList<Funcion>) {
    var erroresSemanticos: ArrayList<Error> = ArrayList()
    override fun toString(): String {
        return "UnidadCompilacion(listaFunciones=$listaFunciones), \n"
    }

    /**
     *
     */
    fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("Clase: ${nombreClass.lexema}")

        for (f in listaFunciones) {

            raiz.children.add(f.getArbolVisual())
        }
        return raiz
    }

    /**
     *
     */
    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>) {
        for (f in listaFunciones) {
            f.llenarTablaSimbolos(tablaSimbolos, listaErrores, "unidadDeCompilacion")
        }
    }

    /**
     *
     */
    fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>) {
        for (f in listaFunciones) {
            f.analizarSemantica(tablaSimbolos, listaErrores)

        }
    }

    fun getJavaCod():String{

        var codigo= "import javax.swing.*; \n" +
                "public class ${nombreClass.getJavaCode()} { \n"

        for (f in listaFunciones) {
            codigo += f.getJavaCode()
        }
        codigo += "}"

        /*var principal:Boolean=false

        for ( func in listaFunciones){
            if (func.nombreFuncion.lexema.toLowerCase() == "principal"){
                var principal=true
            }
        }
        if (!principal){
            erroresSemanticos.add(Error("No no se encontro la clase principal",0,0,""))
        }*/
        return codigo
    }
}