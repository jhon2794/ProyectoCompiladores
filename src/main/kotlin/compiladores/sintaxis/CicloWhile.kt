package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error
class CicloWhile(var expRelacional: ExpresionRelacional?, var listaSentencias: ArrayList<Sentencia>?):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("Ciclo While")

        if (expRelacional != null){
            raiz.children.add(expRelacional!!.getArbolVisual())
        }


        var raizSentencias= TreeItem("Sentencias")

        if (listaSentencias != null) {
            for (s in listaSentencias!!) {
                raizSentencias.children.add(s.getArbolVisual())
            }
        }

        raiz.children.add(raizSentencias)
        return raiz
    }
    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if(listaSentencias != null){
            for(s in listaSentencias!!){
                s.llenarTablaSimbolos(tablaSimbolos, listaErrores, ambito)
            }
        }

    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        expRelacional?.analizarSemantica(tablaSimbolos,listaErrores,ambito)
        if(listaSentencias != null){
            for(s in listaSentencias!!){
                s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            }
        }
    }


    override fun getJavaCode(): String {
        var codigo = "while ("

        if (expRelacional != null){
            codigo += expRelacional!!.getJavaCode()
        }
        codigo += ") { \n"

        if (listaSentencias != null){
            for (sent in listaSentencias!!){
                codigo +=sent.getJavaCode()
            }
        }
        codigo += "} \n"
        return codigo
    }

}