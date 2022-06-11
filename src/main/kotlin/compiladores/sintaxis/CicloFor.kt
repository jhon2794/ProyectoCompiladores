package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class CicloFor(var lista:Token, var item:Token, var tipoDato:Token, var listaSentencias:ArrayList<Sentencia>?): Sentencia(){
    override fun toString(): String {
        return "CicloForEach(lista=$lista, item=$item, tipoDato=$tipoDato, listaSentencias=$listaSentencias)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Ciclo For")

        raiz.children.add(TreeItem("Lista: ${lista.lexema}"))
        raiz.children.add(TreeItem("Item: ${item.lexema}"))
        if(tipoDato != null) {
            raiz.children.add(TreeItem("Tipo: ${tipoDato!!.lexema}"))
        }

        var raizSentencias= TreeItem("Sentencias")

        if(listaSentencias != null) {
            for (s in listaSentencias!!) {
                raizSentencias.children.add(s.getArbolVisual())
            }
        }

        raiz.children.add(raizSentencias)

        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloValor(item.lexema,tipoDato.lexema,false,ambito,item.fila,item.columna)
        if (listaSentencias != null) {
            for (sent in listaSentencias!!){
                sent.llenarTablaSimbolos(tablaSimbolos,listaErrores,ambito)
            }

        }
    }
    override fun getJavaCode(): String {
        var codigo="for ("+tipoDato.getJavaCode() +" "+item.getJavaCode()+": "+lista.getJavaCode() +"){"

        if (listaSentencias != null){
            for (sent in listaSentencias!!){
                codigo+= sent.getJavaCode()
            }
        }

        codigo+="} \n"

        return codigo
    }

}