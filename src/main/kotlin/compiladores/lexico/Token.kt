package co.edu.uniquindio.compiladores.lexico

import co.edu.uniquindio.compiladores.lexico.Categoria

class Token (var lexema: String, var categoria: Categoria, var fila:Int, var columna:Int) {

    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna)"
    }

    fun getJavaCode():String{
        if (categoria == Categoria.PALABRA_RESERVADA){
            if (lexema.toLowerCase() == "~not"){
                return "void"
            }else if (lexema.toLowerCase() == "~isTF"){
                return "boolean"
            }else if (lexema.toLowerCase() == "~equalstring"){
                return "equalString"
            }else if (lexema.toLowerCase() == "~time"){
                return "while"
            }else if (lexema.toLowerCase() == "~cade"){
                return "String"
            }else if (lexema.toLowerCase() == "~duplex"){
                return "double"
            }else if (lexema.toLowerCase() == "~float"){
                return "float"
            }else if (lexema.toLowerCase() == "~long"){
                return "long"
            }else if (lexema.toLowerCase() == "~ente"){
                return "int"
            }else if (lexema.toLowerCase() == "~car"){
                return "char"
            }
        }

        if (categoria == Categoria.OPERADOR_RELACIONAL){

        }
        if (categoria == Categoria.FIN_SENTENCIA){
            return ";"
        }

        if (categoria == Categoria.IDENTIFICADOR){
            return lexema.replace("$","")
        }

        if (categoria == Categoria.OPERADOR_LOGICO_BINARIO){
            return "&&"
        }
        return lexema
    }

}