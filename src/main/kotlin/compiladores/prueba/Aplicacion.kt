package co.edu.uniquindio.compiladores.prueba

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.sintaxis.AnalizadorSintactico

fun main(){
   val lexico = AnalizadorLexico("~task ~ente s( a:~ente){} ")
   //34.34 casa 34585  ~hola$"hola"
    lexico.analizar()
    //print(lexico.listaTokens)

    val sintaxis = AnalizadorSintactico( lexico.listaTokens)
    print ( sintaxis.esUnidadDeCompilacion())
    print ( sintaxis.listaErrores)


}
