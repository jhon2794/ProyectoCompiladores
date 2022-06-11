package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Decremento(var nombre:Token,var operador:Token):Sentencia() {
    override fun toString(): String {
        return "Decremento(nombre=$nombre, operador=$operador)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Operacion de Decremento")
        raiz.children.add(TreeItem("nombre: ${nombre.lexema}"))
        return raiz
    }

    override fun getJavaCode(): String {
        return nombre.getJavaCode()+"--; \n"
    }

}