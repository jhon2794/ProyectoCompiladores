package co.edu.uniquindio.compiladores.lexico

import co.edu.uniquindio.compiladores.lexico.Error

class AnalizadorLexico(var codigoFuente:String) {


    var posicionActual = 0
    var caracterActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0
    val operadoresAritmeticos = ArrayList<Char>()
    var listaErrores = ArrayList<Error>()
    var palabrasRes = ArrayList<String>()


    fun almacenarToken(lexema: String, categoria: Categoria, fila: Int, columna: Int) = listaTokens.add(Token(lexema, categoria, fila, columna))

    fun hacerBT(posicionInicial: Int, filaInicial: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = filaInicial
        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * funcion principal que permite analizar y isntanciar cada uno de las funciones respecto a la identificacion de tokens
     */
    fun analizar() {

        operadoresAritmeticos.add('+')
        operadoresAritmeticos.add('-')
        operadoresAritmeticos.add('*')
        operadoresAritmeticos.add('/')
        operadoresAritmeticos.add('%')

        palabrasRes.add("yes")
        palabrasRes.add("more")
        palabrasRes.add("lessons")
        palabrasRes.add("istf")
        palabrasRes.add("ist")
        palabrasRes.add("isf")
        palabrasRes.add("recent")
        palabrasRes.add("subject")
        palabrasRes.add("fix")
        palabrasRes.add("float")
        palabrasRes.add("long")
        palabrasRes.add("duplex")
        palabrasRes.add("ente")
        palabrasRes.add("cade")
        palabrasRes.add("car")
        palabrasRes.add("not")
        palabrasRes.add("time")
        palabrasRes.add("by")
        palabrasRes.add("open")
        palabrasRes.add("own")
        palabrasRes.add("care")
        palabrasRes.add("realize")
        palabrasRes.add("include")
        palabrasRes.add("reason")
        palabrasRes.add("task")
        palabrasRes.add("back")
        palabrasRes.add("never")
        palabrasRes.add("his")
        palabrasRes.add("pack")
        palabrasRes.add("bring")
        palabrasRes.add("obtain")
        palabrasRes.add("read")
        palabrasRes.add("print")
        palabrasRes.add("array")
        palabrasRes.add("in")



        while (caracterActual != finCodigo) {

            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSiguienteCaracter()
                continue
            }

            if (esEntero()) continue
            if (esDecimal()) continue
            if (esIdentificador()) continue
            if (esCaracter()) continue
            if (esCadena()) continue
            if (esAgrupador()) continue
            if (esComentarioBloque()) continue
            if (esComentarioLinea()) continue
            if (esOperadorAsignacion()) continue
            if (esOperadorIncremento()) continue
            if (esOperadorLogico()) continue
            if (esOperadorAritmeticos()) continue
            if (esOperadorRelacional()) continue
            if (esSeparador()) continue
            if (esPalabraReservada()) continue
            if (finLinea()) continue

            almacenarToken("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            reportarError("Caracter desconocido, lexema: " + caracterActual)
            obtenerSiguienteCaracter()

        }
    }

    /**
     * metodo que permite identificar los numeros decimales
     */
    fun esDecimal(): Boolean {
        if (caracterActual == '.' || caracterActual.isDigit()) {
            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual

            if (caracterActual == '.') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()

                }
            } else {
                lexema += caracterActual
                obtenerSiguienteCaracter()

                while (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                if (caracterActual == '.') {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }

            }
            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            almacenarToken(lexema, Categoria.DECIMAL, filaInicial, columnaInicial)
            return true

        }
        return false
    }

    /**
     *metodo que permite reconocer los numeros enteros
     */
    fun esEntero(): Boolean {


        if (caracterActual.isDigit()) {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual

            lexema += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual.isDigit()) {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            if (caracterActual == '.') {
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
            }

            almacenarToken(lexema, Categoria.ENTERO, filaInicial, columnaInicial)
            return true
        }
        return false
    }

    /**
     *este metodo permite la validacón del token Identificadores
     */
    fun esIdentificador(): Boolean {

        if (caracterActual == '$') {

            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual


            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual.isLowerCase() || caracterActual == 'P' ) {

                while (caracterActual != '$' && lexema.length < 11  ) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }

                if (caracterActual == '$'){
                    lexema+= caracterActual
                    almacenarToken(lexema, Categoria.IDENTIFICADOR, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }

            }
            lexema += caracterActual
            almacenarToken( lexema, Categoria.ERROR, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
        }

        return false
    }

    /**
     * Este metodo permite construir el token de caracter
     */
    fun esCaracter():Boolean{
        if(caracterActual+""=="'"){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if(caracterActual+""=="'"){
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            lexema += caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual+"" == "'"){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.CARACTER, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }

            return false
        }
        return false
    }

    /**
     ** Este metodo permite construir el token de agrupador
     */
    fun esAgrupador():Boolean{
        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual
        if (caracterActual=='{'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.LLAVE_IZQUIERDA, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual=='}'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.LLAVE_DERECHA, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual=='('){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.PARENTESIS_IZQUIERDO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual==')'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.PARENTESIS_DERECHO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual=='['){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.CORCHETE_IZQUIERDO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        if (caracterActual==']'){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.CORCHETE_DERECHO, filaInicial, columnaInicial);
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    /**
     * * Este metodo permite construir el token de Operaor de Incremento y decremento
     */
    fun esOperadorIncremento():Boolean{
        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (caracterActual=='+' || caracterActual=='-'){
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if(caracterActual=='+'&& lexema=="+"){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_INCREMENTO, filaInicial, columnaInicial);
                obtenerSiguienteCaracter()
                return true
            } else if(caracterActual=='-'&& lexema=="-"){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_DECREMENTO, filaInicial, columnaInicial);
                obtenerSiguienteCaracter()
                return true
            }else{
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }

        }
        return false
    }

    /**
     * Este metodo permite construir el token de palabra reservada
     */
    fun esPalabraReservada():Boolean{

        if (caracterActual == '~') {
            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            var posicionInicial = posicionActual
            var palabra = ""

            lexema += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual.isLetter() ) {
                palabra+=caracterActual
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            if (palabrasRes.contains(palabra.toLowerCase())) {
                almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial)
                return true
            }
            almacenarToken(lexema, Categoria.ERROR, filaInicial, columnaInicial)
            reportarError("La palabra despues del signo ~ no es una palabra reservada")
            return false
        }
        return false
    }

    /**
     * Este metodo permite construir el token del separador, punto y dos puntos
     */
    fun esSeparador():Boolean{

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if(caracterActual==','){
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.SEPARADOR_COMA, filaInicial, columnaInicial);
            return true
        }

        if(caracterActual=='°'){
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.PUNTO, filaInicial, columnaInicial);
            return true
        }

        if(caracterActual==':'){
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(lexema, Categoria.DOSPUNTOS, filaInicial, columnaInicial);
            return true
        }

        return false
    }

    /**
     * Este metodo permite construir el token de cadena
     */
    fun esCadena():Boolean{

        if(caracterActual=='"') {
            var lexema = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual


            lexema += caracterActual
            obtenerSiguienteCaracter()


            while (caracterActual != '"') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual == '"') {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.CADENA, filaInicial, columnaInicial);
                return true
            }


        }

        return false
    }

    /**
     * Este metodo permite construir el token de operadores aritmeticos
     */
    fun esOperadorAritmeticos():Boolean{

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresAritmeticos.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (operadoresAritmeticos.contains(caracterActual) || caracterActual == '='){

                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false

            }
            almacenarToken(lexema, Categoria.OPERADOR_MATEMATICO, filaActual, columnaActual)

            return true
        }

        return false
    }

    /**
     * Este metodo permite construir el token de operador de asignacion
     */
    fun esOperadorAsignacion():Boolean{
        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresAritmeticos.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual=='=') {
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true

            } else{
                hacerBT(posicionInicial,filaInicial,columnaInicial)
                return false
            }

        }else if(caracterActual=='='){
            lexema+=caracterActual
            almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaActual, columnaActual)
            obtenerSiguienteCaracter()
            return true
        }

        return false
    }

    /**
     * Este metodo permite construir el token de operador logico
     */
    fun esOperadorLogico():Boolean{
        val operadoresLog = ArrayList<Char>()
        operadoresLog.add('&')
        operadoresLog.add('|')
        operadoresLog.add('!')

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresLog.contains(caracterActual)) {
            lexema+=caracterActual

            if (lexema.equals( "&") or lexema.equals("|")){

                almacenarToken(lexema, Categoria.OPERADOR_LOGICO_BINARIO, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }else{
                almacenarToken(lexema, Categoria.OPERADOR_LOGICO_UNARIO, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }
        }
        return false
    }

    /**
     * Este metodo permite construir el token de operador relacional
     */
    fun esOperadorRelacional():Boolean{
        val operadoresRelac = ArrayList<Char>()
        operadoresRelac.add('<')
        operadoresRelac.add('>')
        operadoresRelac.add('=')

        var lexema =""
        var filaInicial=filaActual
        var columnaInicial=columnaActual
        var posicionInicial=posicionActual

        if (operadoresRelac.contains(caracterActual)) {
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            if (caracterActual=='=') {
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }
            if(operadoresRelac.contains(caracterActual)){
                lexema+=caracterActual
                almacenarToken(lexema, Categoria.DESCONOCIDO, filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return true
            }
            if(lexema=="="){
                hacerBT(posicionInicial,filaInicial,columnaInicial)
            }else {
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaActual, columnaActual)
                return true
            }
        }

        return false
    }

    /**
     * Este metodo permite construir el token de fin de linea
     */
    fun finLinea():Boolean{
        if(caracterActual=='_'){
            almacenarToken(""+caracterActual,Categoria.FIN_SENTENCIA,filaActual,columnaActual)
            obtenerSiguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Este metodo permite construir el token de comentario de linea
     */
    fun esComentarioLinea():Boolean{
        if (caracterActual=='¿'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual

            lexema+=caracterActual

            obtenerSiguienteCaracter()


            while (caracterActual != '?'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }

            lexema+=caracterActual
            almacenarToken(lexema,Categoria.COMENTARIO_LINEA,filaInicial,columnaInicial);
            obtenerSiguienteCaracter()
            return true

        }
        return false
    }

    /**
     * Ente metodo permite determinar si un token es comentario de bloque
     */
    fun esComentarioBloque():Boolean{
        if(caracterActual=='¡'){
            var lexema =""
            var filaInicial=filaActual
            var columnaInicial=columnaActual
            var posicionInicial=posicionActual
            lexema+=caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual!='¡'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            if (caracterActual=='¡') {
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaInicial, columnaInicial);
                return true
            }

        }
        return false
    }




    /**
     * este metodo nos permite ir recorriendo y obteniendo el siguiente caracter
     */

    fun obtenerSiguienteCaracter(){
        if (posicionActual == codigoFuente.length-1){
            caracterActual = finCodigo
        }else{

            if (caracterActual == '\n'){
                filaActual++
                columnaActual = 0
            }else{
                columnaActual++
            }

            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }
    }

    /**
     * Este metodo permite agregar un error a la lista de errores
     */
    fun reportarError( mensaje:String){
        listaErrores.add(Error(mensaje,filaActual,columnaActual,Categoria.ERROR.toString()))
    }

}
