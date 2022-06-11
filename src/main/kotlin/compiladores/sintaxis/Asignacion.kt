package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.Simbolo
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error

class Asignacion(): Sentencia() {
    var nombre:Token?=null
    var operador:Token?=null
    var expresion:Expresion?=null
    var invocacion:Sentencia?=null

    constructor( nombre:Token, operador:Token?,  expresion: Expresion?):this(){
        this.nombre=nombre
        this.operador=operador
        this.expresion=expresion
    }

    constructor( nombre:Token, operador:Token?,  invocacion: Sentencia):this(){
        this.nombre=nombre
        this.operador=operador
        this.invocacion=invocacion
    }

    override fun toString(): String {
        return "Asignacion(nombre=$nombre, operador=$operador, expresion=$expresion, invocacion=$invocacion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Asignacion")
        if(operador != null) {
            raiz.children.add(TreeItem("Nombre: ${nombre!!.lexema}"))
        }
        if(operador != null) {
            raiz.children.add(TreeItem("Operador: ${operador!!.lexema}"))
        }

        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        }else{
            raiz.children.add(invocacion!!.getArbolVisual())
        }

        return raiz
    }

    /**
     *
     */
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var simb: Simbolo?=null
        if (nombre != null) {
            simb = tablaSimbolos.buscarSimboloValor(nombre!!.lexema, ambito)
        }
        if (simb == null){

            listaErrores.add(Error("El campo ${nombre!!.lexema} no existe en el ambito $ambito",nombre!!.fila, nombre!!.columna,""))

        }else{
            var tipo=simb.tipo

            if (expresion != null){
                expresion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
                var tipoExp =expresion!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)

                if (tipoExp != tipo ){
                    listaErrores.add(Error("El tipo de dato de la expresion ${tipoExp} no coincide con el tipo de dato del campo " +
                            "${nombre!!.lexema} que es de tipo $tipo", nombre!!.fila,nombre!!.columna,""))
                }

            }

        }
    }
    override fun getJavaCode(): String {
        var codigo=nombre!!.getJavaCode() + operador!!.getJavaCode()

        if (expresion != null){
            codigo += expresion!!.getJavaCode()
        }else if (invocacion != null){
            codigo += invocacion!!.getJavaCode()
        }
        codigo += "; \n"
        return codigo
    }
}