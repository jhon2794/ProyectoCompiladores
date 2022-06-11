package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error
class Imprimir(var expresion: Expresion):Sentencia() {
    override fun toString(): String {
        return "Imprimir(expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Imprimir")
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        expresion.analizarSemantica(tablaSimbolos, listaErrores, ambito)
    }

    override fun getJavaCode(): String {
        return "JOptionPane.showMessageDialog(null,"+expresion.getJavaCode()+"); \n"
    }

}