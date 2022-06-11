package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Funcion (var nombreFuncion: Token, var tipoRetorno: Token, var listaParametros:ArrayList<Parametro>, var  listaSentencias: ArrayList<Sentencia>) {

    override fun toString(): String {
        return "Funcion(nombreFuncion=$nombreFuncion, tipoRetorno=$tipoRetorno, listaParametros=$listaParametros, listaSentencias=$listaSentencias)"
    }

    fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem("Funcion")

        raiz.children.add(TreeItem("Nombre: ${nombreFuncion.lexema}"))
        raiz.children.add(TreeItem("Tipo Retorno: ${tipoRetorno.lexema}"))

        var raizParametros = TreeItem("Parámetro")

        for (p in listaParametros) {
            raizParametros.children.add(p.getArbolVisual())
        }

        raiz.children.add(raizParametros)

        var raizSentencias = TreeItem("Sentencias")

        for (p in listaSentencias) {
            raizSentencias.children.add(p.getArbolVisual())
        }

        raiz.children.add(raizSentencias)

        return raiz

    }

    /**
     *
     */
    fun obtenerTiposParametros(): ArrayList<String> {

        var lista = ArrayList<String>()
        if (listaParametros != null) {
            for (p in listaParametros) {
                lista.add(p.tipoDato.lexema)
            }
        }
        return lista
    }

    /**
     *
     */
    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {

        tablaSimbolos.guardarSimboloFuncion(nombreFuncion.lexema, tipoRetorno.lexema, obtenerTiposParametros(), ambito, nombreFuncion.fila, nombreFuncion.columna)
        if (listaParametros != null) {
            for (p in listaParametros!!) {
                tablaSimbolos.guardarSimboloValor(p.nombre.lexema, p.tipoDato.lexema, true, nombreFuncion.lexema, p.nombre.fila, p.nombre.columna)
                //buscarSimboloValor(p.nombre.lexema, nombreFuncion.lexema)
            }
        }
        if (listaSentencias != null) {
            for (s in listaSentencias!!) {
                s.llenarTablaSimbolos(tablaSimbolos, listaErrores, nombreFuncion.lexema)
            }
        }
    }

    /**
     *
     */
    fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>) {
        if (listaSentencias != null) {
            for (s in listaSentencias!!) {
                s.analizarSemantica(tablaSimbolos, listaErrores, nombreFuncion.lexema)
            }

        }
    }

    fun getJavaCode():String {
        var codigo:String
        if (nombreFuncion.getJavaCode() == "main"){
            codigo = "public static void main (String[] args){ \n"
        }else{
            codigo = tipoRetorno.getJavaCode() + " " + nombreFuncion.getJavaCode() + "("
            for (p in listaParametros){
                codigo += p.getJavaCode() + ", "
            }
            codigo = codigo.substring(0, codigo.length - 2)
            codigo += "){ \n"
        }
        for (s in listaSentencias){
            codigo += s.getJavaCode()
        }
        codigo += "} \n"
        return codigo
    }
}