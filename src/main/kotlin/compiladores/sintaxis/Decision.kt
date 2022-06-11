package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Decision(var expresionLogica:ExpresionLogica, var listaSentencia:ArrayList<Sentencia>, var listaSentenciaElse: ArrayList<Sentencia>?): Sentencia() {

    override fun toString(): String {
        return "Decision(expresionLogica=$expresionLogica, listaSentencia=$listaSentencia, listaSentenciaElse=$listaSentenciaElse)"
    }

    override fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem("Decisión Simple")

        var condicion = TreeItem("Condición")
        raiz.children.add(expresionLogica.getArbolVisual())

        raiz.children.add(condicion)

        var raizTrue = TreeItem("Sentencias Verdaderas")

        for (s in listaSentencia) {
            raizTrue.children.add(s.getArbolVisual())
        }

        raiz.children.add(raizTrue)

        if (listaSentenciaElse != null) {

            var raizFalse = TreeItem("Sentencias falsas")

            for (s in listaSentenciaElse!!) {
                raizFalse.children.add(s.getArbolVisual())
            }
            raiz.children.add(raizFalse)
        }

        return raiz
    }

    /**
     *
     */
    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {

    for (s in listaSentencia!!){
        s.llenarTablaSimbolos(tablaSimbolos,listaErrores,ambito)
     }


    if (listaSentenciaElse!=null){
        for (sElse in listaSentenciaElse!!){
            sElse.llenarTablaSimbolos(tablaSimbolos,listaErrores,ambito)
        }
    }
    }
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if(expresionLogica!=null){
            expresionLogica.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
        for(s in listaSentencia){
            s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
        if(listaSentenciaElse != null){
            for(s in listaSentenciaElse!!){
                s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            }
        }
    }


    override fun getJavaCode(): String {
        var codigo = "if ("

        if (expresionLogica != null){
            codigo += expresionLogica!!.getJavaCode()
        }
        codigo += ") { \n"

        if (listaSentencia != null){
            for (sent in listaSentencia!!){
                codigo +=sent.getJavaCode()
            }
        }
        codigo +="} \n"

        if (listaSentenciaElse != null){
            codigo += "else { \n"
            if (listaSentenciaElse != null){
                for (sent in listaSentenciaElse!!){
                    codigo +=sent.getJavaCode()
                }
            }

            codigo +="} \n"
        }

        return codigo
    }
}
