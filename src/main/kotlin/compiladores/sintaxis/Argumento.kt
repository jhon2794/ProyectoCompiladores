package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token

class Argumento (var nombre: Token){
    override fun toString(): String {
        return "Argumento(nombre=$nombre)"
    }
}