package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Arreglo(var nombre:Token, var tipoArreglo:Token,var listaExpresiones:ArrayList<Expresion>?):Sentencia() {

    /**
     *
     */
    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem<String>("Arreglo")
        raiz.children.add(TreeItem("Nombre: ${nombre.lexema}"))
        raiz.children.add(TreeItem("Tipo: ${tipoArreglo.lexema}"))
        var raizExp = TreeItem<String>("Argumentos")

        if (listaExpresiones != null) {
            for (exp in listaExpresiones!!) {

                raizExp.children.add(exp!!.getArbolVisual())
            }
            raiz.children.add(raizExp)
        }

        return raiz
    }

    /**
     *
     */
    override fun toString(): String {
        return "Arreglo(nombre=$nombre, tipoArreglo=$tipoArreglo, listaExpresiones=$listaExpresiones)"
    }

    /**
     *
     */
    override fun llenarTablaSimbolos(tablaSimbolos:TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloValor(nombre.lexema, tipoArreglo.lexema,true,ambito,nombre.fila,nombre.columna)

    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (listaExpresiones!= null) {
            for (exp in listaExpresiones!!){
                var tipo = exp.obtenerTipo(tablaSimbolos,ambito,erroresSemanticos)

                if (tipo == null){
                }else if (tipo != tipoArreglo.lexema){
                    erroresSemanticos.add(Error("El tipo de dato de la expresion ($tipo) no coincide co el tipo de datos del arreglo (${tipoArreglo.lexema})",
                            nombre.fila,nombre.columna,""))
                }
            }
        }
    }

    override fun getJavaCode(): String {
        var codigo= tipoArreglo.getJavaCode()+"[] "+nombre.getJavaCode()+" "

        if (listaExpresiones!=null){
            codigo += "= {"
            for (expr in listaExpresiones!!){
                codigo += expr.getJavaCode()+", "
            }
            codigo = codigo.substring(0,codigo.length-2)+"}"
        }
        codigo+= "; \n"
        print(codigo)
        return codigo
    }

}