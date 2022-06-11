package co.edu.uniquindio.compiladores.controladores

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.AnalizadorSemantico
import co.edu.uniquindio.compiladores.sintaxis.AnalizadorSintactico
import co.edu.uniquindio.compiladores.sintaxis.UnidadDeCompilacion
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.io.File
import java.lang.Exception
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import co.edu.uniquindio.compiladores.lexico.Error

class InicioController : Initializable{
    @FXML lateinit var taCodigoFuente :TextArea

    //Tbala tokens
    @FXML lateinit var tblTokens :TableView<Token>
    @FXML lateinit var clmLexema:TableColumn<Token,String>
    @FXML lateinit var clmCategoria:TableColumn<Token,String>
    @FXML lateinit var clmFila:TableColumn<Token,Int>
    @FXML lateinit var clmColumna:TableColumn<Token,Int>

    //Tabla Errores Sintacticos
    @FXML lateinit var tblErroresSintacticos :TableView<Error>
    @FXML lateinit var clMensajeES:TableColumn<Error,String>
    @FXML lateinit var clFilaES:TableColumn<Error,Int>
    @FXML lateinit var clColumnaES:TableColumn<Error,Int>
    @FXML lateinit var clCategoriaES:TableColumn<Error,String>

    //Errores Lexicos
    @FXML lateinit var tblErroresLexicos :TableView<Error>
    @FXML lateinit var clMensajeEL:TableColumn<Error,String>
    @FXML lateinit var clFilaEL:TableColumn<Error,Int>
    @FXML lateinit var clColumnaEL:TableColumn<Error,Int>
    @FXML lateinit var clCategoriaEL:TableColumn<Error,String>
   //Errores Semanticos
    @FXML lateinit var tblErroresSemanticos :TableView<Error>
    @FXML lateinit var sMensaje:TableColumn<Error,String>
    @FXML lateinit var sFila:TableColumn<Error,Int>
    @FXML lateinit var sColumna:TableColumn<Error,Int>
    @FXML lateinit var sCategoria:TableColumn<Error,String>
    var unidadCompilacion:UnidadDeCompilacion?=null
    lateinit var semantica:AnalizadorSemantico
    lateinit var sintaxis:AnalizadorSintactico
    lateinit var lexico:AnalizadorLexico


    // Arbol Sintactico
    @FXML lateinit var tvArbol:TreeView<String>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        clmLexema.cellValueFactory=PropertyValueFactory("lexema")
        clmCategoria.cellValueFactory=PropertyValueFactory("categoria")
        clmFila.cellValueFactory=PropertyValueFactory("fila")
        clmColumna.cellValueFactory=PropertyValueFactory("columna")

        clMensajeES.cellValueFactory=PropertyValueFactory("error")
        clFilaES.cellValueFactory=PropertyValueFactory("fila")
        clColumnaES.cellValueFactory=PropertyValueFactory("columna")
        clCategoriaES.cellValueFactory=PropertyValueFactory("categoria")

        clMensajeEL.cellValueFactory=PropertyValueFactory("error")
        clFilaEL.cellValueFactory=PropertyValueFactory("fila")
        clColumnaEL.cellValueFactory=PropertyValueFactory("columna")
        clCategoriaEL.cellValueFactory=PropertyValueFactory("categoria")

        sMensaje.cellValueFactory=PropertyValueFactory("error")
        sFila.cellValueFactory=PropertyValueFactory("fila")
        sColumna.cellValueFactory=PropertyValueFactory("columna")
        sCategoria.cellValueFactory=PropertyValueFactory("categoria")

        leerDatos();
    }

    /**
     * Este metodo permite limpiar los campos y la tabla de la lista de tokens
     */
    @FXML
    fun Limpiar(){
        taCodigoFuente.text=""
        tblTokens.items=FXCollections.observableArrayList(ArrayList<Token>())
        tblErroresLexicos.items=FXCollections.observableArrayList(ArrayList<Error>())
        tblErroresSintacticos.items=FXCollections.observableArrayList(ArrayList<Error>())
        tblErroresSemanticos.items=FXCollections.observableArrayList(ArrayList<Error>())
        tvArbol.root=TreeItem()
    }

    /**
     * En este metodo se eenvia el codigo ingresado a analizar
     */
    @FXML
    fun AnalizarCodigo(e:ActionEvent){
        var codFuente=taCodigoFuente.text
        if (codFuente.length>0) {
            lexico=AnalizadorLexico(codFuente)
            lexico.analizar()

            tblTokens.items=FXCollections.observableArrayList(lexico.listaTokens)
             tblErroresLexicos.items=FXCollections.observableArrayList(lexico.listaErrores)

            if (lexico.listaErrores.isEmpty()) {
                sintaxis = AnalizadorSintactico(lexico.listaTokens)
                unidadCompilacion = sintaxis!!.esUnidadDeCompilacion()

                tblErroresSintacticos.items=FXCollections.observableArrayList(sintaxis!!.listaErrores)

                if (unidadCompilacion != null) {
                    tvArbol.root = unidadCompilacion!!.getArbolVisual()
                    semantica=AnalizadorSemantico(unidadCompilacion!!)
                    semantica!!.llenarTablaSimbolos()
                    semantica!!.analizarSemantica()
                    println(semantica!!.tablaSimbolos)
                    semantica.analizarSemantica()
                    tblErroresSemanticos.items=FXCollections.observableArrayList(semantica!!.listaErrores)
                    println(semantica!!.listaErrores)
                }
            }else{
                var alerta=Alert(Alert.AlertType.WARNING)

                alerta.headerText="Atención"
                alerta.contentText = "Hay errores lexicos en el codigo fuente"
            }
        }
        escribirDatos(codFuente)
    }


    @FXML
    fun traducirCodigo(e:ActionEvent){
        if (lexico.listaErrores.isEmpty() && sintaxis.listaErrores.isEmpty()) {
            var codigoTraducido= unidadCompilacion!!.getJavaCod()
            File("src/Principal.java").writeText(codigoTraducido)

            try {
                var run =Runtime.getRuntime().exec("javac src/Principal.java")
                run.waitFor()
                Runtime.getRuntime().exec("java Principal",null,File("src"))
            }catch ( ex:Exception){

            }

            print(codigoTraducido)
        }else{
            val alert=Alert(Alert.AlertType.ERROR)
            alert.headerText=null
            alert.contentText="El codigo no se puede traducir porque hay errores"
            alert.show()
        }

    }


    /**
     * Este metodo nos permite cargar datos que se hayan almacenado anteriormente
     */
    fun leerDatos(){
        try {
            var path:String=System.getProperty("java.io.tmpdir")+File.separator+"codFuente.txt"
            var archivo: File = File(path);

            if (archivo.exists()) {

                var scanner: Scanner = Scanner(archivo);
                var linea: String = "";
                while (scanner.hasNextLine()) {
                    linea += scanner.nextLine();

                }
                taCodigoFuente.text = linea
                scanner.close()
            }
        }
        catch (e:Exception){
            taCodigoFuente.text="";
        }
    }

    /**
     * Este metodo permite almacenar lo que se ha ingresado en la entrada de codigo
     */
    fun escribirDatos(datos:String){
        var path:String=System.getProperty("java.io.tmpdir")+File.separator+"codFuente.txt"

        File(path).writeText(datos)

    }
}