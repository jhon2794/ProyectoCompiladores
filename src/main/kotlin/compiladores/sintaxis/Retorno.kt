package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Retorno(var expresion: Expresion?):Sentencia() {
    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Retorno")
        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        }
        return raiz
    }


    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var s = tablaSimbolos.buscarFuncAmbito(ambito)
        if(expresion!=null){
            expresion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            if(s!=null){
                var tipoR = expresion!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
                if( tipoR!= s.tipo){
                    listaErrores.add(Error("El retorno($tipoR) no es del mismo tipo de la funcion ${s!!.nombre}(${s.tipo})", s.fila, s.columna,""))
                }
            }
        }

    }

    override fun getJavaCode(): String {
        var codigo=""
        if (expresion != null) {
            codigo= "return "+expresion!!.getJavaCode()+"; \n"
        }

        return codigo
    }

}