package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import  co.edu.uniquindio.compiladores.lexico.Error
class Leer(var variable:Token):Sentencia() {
    override fun toString(): String {
        return "Leer(variable=$variable)"
    }
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var s = tablaSimbolos.buscarSimboloValor(variable!!.lexema, ambito)
        if(s==null){
            listaErrores.add(Error("El campo (${variable!!.lexema}) no existe en el ambito $ambito", variable!!.fila, variable!!.columna,""))
        }
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Leer")
        raiz.children.add(TreeItem("Variable: ${variable.lexema}"))
        return raiz
    }
}