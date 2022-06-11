package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Declaracion(var modAcceso:Token?,var tipoDato:Token, var nombre:Token,var asignacion:Asignacion?):Sentencia() {
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem<String>("Declaracion")

        if (modAcceso != null){
            raiz.children.add(TreeItem("Mod Acceso: ${modAcceso!!.lexema}"))
        }
        raiz.children.add(TreeItem("Tipo Dato: ${tipoDato!!.lexema}"))
        raiz.children.add(TreeItem("Nombre: ${nombre!!.lexema}"))

        if (asignacion != null){
            raiz.children.add(asignacion!!.getArbolVisual())
        }

        return raiz
    }

    /**
     *
     */
    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var acceso=""
        if (modAcceso!=null) acceso=modAcceso!!.lexema
        tablaSimbolos.guardarSimboloValor(nombre.lexema,tipoDato.lexema,true,ambito,nombre.fila,nombre.columna)
    }

    /**
     *
     */
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if (asignacion != null){
            asignacion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode():String{
        var codigo = "\t"
        /* if (modAcceso != null){
             codigo += modAcceso!!.getJavaCode()+" "
         }*/
        codigo += tipoDato.getJavaCode()+" "

        if (asignacion != null){
            codigo += asignacion!!.getJavaCode()
        }else{
            codigo += nombre.getJavaCode()+";"
        }

        codigo+= "\n"

        return codigo
    }

}