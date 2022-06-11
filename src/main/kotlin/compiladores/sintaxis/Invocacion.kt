package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import co.edu.uniquindio.compiladores.lexico.Error
import javafx.scene.control.TreeItem

class Invocacion(var nombre:Token, var listaArgumentos:ArrayList<Expresion>):Sentencia() {
    override fun toString(): String {
        return "Invocacion(nombre=$nombre, listaArgumentos=$listaArgumentos)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Invocacion Funcion")
        raiz.children.add(TreeItem("Nombre Funcion: ${nombre.lexema}"))
        var raizSentencias= TreeItem("Argumentos")
        for(arg in listaArgumentos){
            raizSentencias.children.add(arg.getArbolVisual())
        }
        raiz.children.add(raizSentencias)

        return raiz
    }
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String){
        var tipos:ArrayList<String> = obtenerTipoExpresion(tablaSimbolos, ambito, listaErrores)
        var s = tablaSimbolos.buscarSimboloFuncion(nombre.lexema, tipos)
        if(s==null){
            listaErrores.add(Error("La funcion ${nombre.lexema} no existe", nombre.fila, nombre.columna,""))
        }
    }

    fun obtenerTipoExpresion(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>): ArrayList<String>{
        var tipos:ArrayList<String> = ArrayList()
        for(e in listaArgumentos){
            if(e!=null){
                tipos.add(e.obtenerTipo(tablaSimbolos, ambito, listaErrores))
            }
        }
        return tipos
    }
    fun obtenerTipoFunc(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String):String?{
        var tipos:ArrayList<String> = obtenerTipoExpresion(tablaSimbolos, ambito, listaErrores)
        var s = tablaSimbolos.buscarSimboloFuncion(nombre.lexema, tipos)
        if(s==null){
            listaErrores.add(Error("La funcion ${nombre.lexema} no existe", nombre.fila, nombre.columna,""))
            return null
        }else{
            return s.tipo
        }
    }

    override fun getJavaCode(): String {
        var codigo ="\t \t"+ nombre.getJavaCode()+" ("

        if (listaArgumentos.isNotEmpty()) {
            for (arg in listaArgumentos) {
                codigo += arg.getJavaCode() + ", "
            }
            codigo = codigo.substring(0, codigo.length - 2)
        }
        codigo+= "); \n"
        return codigo
    }
}






