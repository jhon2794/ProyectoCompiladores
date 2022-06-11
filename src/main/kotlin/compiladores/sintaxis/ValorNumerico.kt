package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class ValorNumerico(var signo:Token, var numero:Token) {
    override fun toString(): String {
        return "ValorNumerico(signo=$signo, numero=$numero)"
    }

    fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Valor Numerico")
        raiz.children.add(TreeItem("Signo: ${signo.lexema}"))
        raiz.children.add(TreeItem("Numero: ${numero.lexema}"))

        return raiz
    }

    fun getJavaCode():String{
        var codigo=""
        if (signo.lexema=="+"){
            codigo +=numero.getJavaCode()
        }else {
            codigo += signo.getJavaCode() + numero.getJavaCode()
        }
        return codigo
    }

}