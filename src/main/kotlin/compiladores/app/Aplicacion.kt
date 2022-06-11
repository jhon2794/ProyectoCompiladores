package co.edu.uniquindio.compiladores.app

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * clase principal de la aplicación
 */
class Aplicacion : Application() {
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader( Aplicacion::class.java.getResource( "/Inicio.fxml") )
        val parent:Parent = loader.load()

        val scene = Scene (parent)

        primaryStage?.scene =scene
        primaryStage?.title = "Nuestro Compilador"
        primaryStage?.show()

    }

    /**
     * main principal para correr el proyecto
     */
    companion object{
        @JvmStatic
        fun main ( args: Array<String> ){
            launch( Aplicacion::class.java)
        }
    }

}