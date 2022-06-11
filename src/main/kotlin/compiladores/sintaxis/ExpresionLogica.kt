package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Error


class ExpresionLogica() : Expresion() {
    var expresionRelacional:ExpresionRelacional?=null
    var operadorLogicoBinario:Token?=null
    var operadorLogicoUnario:Token?=null
    var expresionLogica:ExpresionLogica?=null


    constructor(operadorLogicoUnario:Token?,expresionRelacional:ExpresionRelacional?):this(){
        this.operadorLogicoUnario=operadorLogicoUnario
        this.expresionRelacional=expresionRelacional
    }
    constructor(expresionRelacional:ExpresionRelacional?, operadorLogicoBinaio:Token?, expresionLogica: ExpresionLogica?):this(){
        this.expresionRelacional=expresionRelacional
        this.operadorLogicoBinario=operadorLogicoBinaio
        this.expresionLogica=expresionLogica
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Condicion")
        if(expresionRelacional != null) {
            raiz.children.add(expresionRelacional!!.getArbolVisual())
        }
        if(expresionLogica != null) {
            raiz.children.add(expresionLogica!!.getArbolVisual())
        }

        return raiz
    }

    /**
     *
     */
    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito:String, listaErrores: ArrayList<Error>):String{
        return "~istf"
    }

    override fun getJavaCode(): String {
        var codigo=""

        if (expresionLogica != null && expresionLogica != null ){
            codigo =expresionRelacional!!.getJavaCode() +" " +operadorLogicoBinario!!.getJavaCode()+ " "+expresionLogica!!.getJavaCode()
        }else{
            if (operadorLogicoUnario != null && expresionRelacional != null){
                codigo =operadorLogicoUnario!!.getJavaCode() +" ("+expresionRelacional!!.getJavaCode()+")"
            }else
                if (expresionRelacional != null){
                    codigo =expresionRelacional!!.getJavaCode()
                }
        }

        return codigo
    }

}