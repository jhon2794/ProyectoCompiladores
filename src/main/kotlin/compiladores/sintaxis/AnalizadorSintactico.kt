package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.lexico.Error

class AnalizadorSintactico (var listaTokens:ArrayList<Token>){

    var posicionActual=0
    var tokenActual = listaTokens[0]
    var listaErrores = ArrayList<Error>()

    /**
     *
     */
    fun obtenerSiguienteToken(){

        posicionActual++

        if(posicionActual < listaTokens.size){
            tokenActual = listaTokens[posicionActual]
        }
    }

    /**
     *
     */
    fun reportarError(mensaje:String,categoria: String){
        listaErrores.add( Error(mensaje,tokenActual.fila, tokenActual.columna, categoria))

    }

    /**
     * BNF para reresentar la unidad de compilacion
     * <UnidadDeCompilacion> ::= ~lessons  Identificador “{”  <VariablesGlobales> <ListaFunciones> “}”
     */
    fun esUnidadDeCompilacion (): UnidadDeCompilacion? {


        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~lessons") {
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombreClase = tokenActual
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                    obtenerSiguienteToken()
                } else {
                    reportarError("Falta la llave de apertura de la clase","Unidad de compilacion")
                }
                var listaFunciones = esListaFunciones()
                while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                if (tokenActual.categoria != Categoria.LLAVE_DERECHA) {

                    reportarError(
                            "Falta la  llave de cierre de la undad de compilacion", "unidad de compilacion")
                }
                if (listaFunciones.size > 0) {
                    return UnidadDeCompilacion(nombreClase,listaFunciones)
                } else {
                    reportarError("Falta la lista de funciones", "unidad de compilacion")
                }
                while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()



            }else{
                reportarError("Falta un nombre de la clase valido", "unidad de compilacion")
            }
        }else{
            reportarError("Falta la palabra reservada 'lesson' ", "unidad de compilacion")
        }

        return null
    }


    /**
     * <InvocacionFuncion> ::= Indentificador “(“ [<ListaArgumentos>] “)”
     */
    fun esInvocacionFuncion(): Invocacion? {
        // while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_ASIGNACION  && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_DECREMENTO
                    && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_INCREMENTO){


                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    val nombreFuncion = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                    } else {
                        reportarError("Falta paréntesis izquierdo","Invocacion Funcion")
                    }
                    val listaArgumentos: ArrayList<Expresion> = esListaArgumentos()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                    } else {
                        reportarError("Falta paréntesis derecho","Invocacion Funcion")
                    }
                    if (tokenActual.categoria != Categoria.FIN_SENTENCIA) {
                        reportarError("Falta fin de sentencia","Invocacion Funcion")
                    }
                    return Invocacion(nombreFuncion, listaArgumentos)

                }
            }
        }catch (ex:Exception){

        }
        return null
    }

    /**
     * <ListaArgumentos> ::= <Expresion> [“,” <ListaArgumentos>]
     */
    fun esListaArgumentos():ArrayList<Expresion>{
        var expresion=esExpresion()
        var listaExpresiones =ArrayList<Expresion>()
        while (expresion !=null){
            listaExpresiones.add(expresion)

            if (tokenActual.categoria == Categoria.SEPARADOR_COMA){
                obtenerSiguienteToken()
                expresion=esExpresion()
            }else{
                if (tokenActual.categoria != Categoria.PARENTESIS_DERECHO && tokenActual.categoria != Categoria.PARENTESIS_IZQUIERDO){
                    reportarError("Falta el separador en la lista de expresiones de la invocacion de la funcion","Lista de Argumentos")
                }
                break
            }
        }
        return listaExpresiones
    }


    /**
     * <ListaFunciones> ::= <Funcion> [<ListaFunciones>]
     */
    fun esListaFunciones(): ArrayList<Funcion>{

        var listaFunciones = ArrayList<Funcion>()
        var funcion = esFuncion()

        while(funcion!=null ){
            listaFunciones.add(funcion)
            funcion = esFuncion()
        }

        return listaFunciones
    }

    /**
     *<Funcion> ::= ~task <TipoRetorno> identificador "("[<ListaParametros>]")" <BloqueSentencias>
     */
    fun esFuncion(): Funcion?{

        if(tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~task" ){
            obtenerSiguienteToken()

            var tipoRetorno = esTipoRetorno()

            if(tipoRetorno!= null){
                obtenerSiguienteToken()

                if(tokenActual.categoria == Categoria.IDENTIFICADOR){
                    var nombreFuncion = tokenActual
                    obtenerSiguienteToken()

                    if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO ){
                        obtenerSiguienteToken()

                        var listaParametros = esListaParametros()

                        if( tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                            obtenerSiguienteToken()

                            var bloqueSentencia = esBloqueSentencias()

                            if(bloqueSentencia!=null){
                                return Funcion(nombreFuncion, tipoRetorno, listaParametros, bloqueSentencia)
                            }else{
                                reportarError( "el bloque de sentencias esta vacio","Funciones")
                            }

                        }else{
                            reportarError("falta el parentesis derecho","Funciones")
                        }
                    }else{
                        reportarError("falta el parentesis izquierdo","Funciones")
                    }
                }else{
                    reportarError("falta el nombre de la funcion","Funciones")
                }
            }else{
                reportarError("falta el tipo de retorno de la funcion","Funciones")
            }
        }
        return null
    }



    /**
     * <TipoRetorno> ::= ~ente | ~duplex | ~cade | ~isTF | ~car | ~not
     */
    fun esTipoRetorno():Token?{
        if( tokenActual.categoria == Categoria.PALABRA_RESERVADA){
            if(tokenActual.lexema == "~ente" || tokenActual.lexema == "~duplex" || tokenActual.lexema == "~cade" ||
                    tokenActual.lexema == "~isTF" || tokenActual.lexema == "~car" || tokenActual.lexema == "~not"){
                return  tokenActual
            }
        }
        return null
    }

    /**
     * BNF y palabra reservada para retonar
     * <Retorno> ::= ~back  [ Identificador ] [<TipoDato>] [<Expresion>]
     */
    fun esRetorno():Retorno?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "~back"){
            obtenerSiguienteToken()

            var exp=esExpresion()
            if (exp == null){
                reportarError("Falta la expresion de retorno de la fincion","Retorno funcion")
            }
            return Retorno(exp)
        }
        return null
    }

    /**
     *<ListaParametros> ::= <Parametro>[","<ListaParametros>]
     */
    fun esListaParametros(): ArrayList<Parametro>{
        var listaParametros = ArrayList<Parametro>()
        var parametro = esParametro()

        while(parametro!=null ) {
            listaParametros.add(parametro)
            if (tokenActual.categoria == Categoria.SEPARADOR_COMA) {
                obtenerSiguienteToken()
                parametro = esParametro()

            } else {
                if (tokenActual.categoria != Categoria.PARENTESIS_DERECHO) {
                    reportarError("falta una coma en la lista de parametros","Lista de Parametros")

                }
                break
            }
        }
        return listaParametros

    }

    /**
     * <Parametro> ::= <TipoDato>  Identificador
     */
    fun esParametro():Parametro?{
        val tipoDato = esTipoDato();
        if(tipoDato != null){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                val nombre = tokenActual
                obtenerSiguienteToken()
                val nombres = "hola"
                return Parametro(tipoDato, nombre)
            }else{
                reportarError("falta el identificador del parametro","Parametros")
                val nombres = arrayOf("Luis", "María José", "Fernando")

            }
        }
        return null
    }



        /**
         * <TipoRetorno> ::= ~ente | ~duplex | ~cade | ~isTF | ~car |
         */
        fun esTipoDato():Token?{
            if( tokenActual.categoria == Categoria.PALABRA_RESERVADA){
                if(tokenActual.lexema == "~ente" || tokenActual.lexema == "~duplex" || tokenActual.lexema == "~cade" ||
                        tokenActual.lexema == "~isTF" || tokenActual.lexema == "~car" ){
                    return  tokenActual
                }
            }
            return null
        }

    /**
     ** BNF para reresentar un tipo de parametro
     * <TipoRetorno> ::= ~ente | ~duplex | ~cade | ~isTF | ~car |
     */
    fun esTipoParametro(): Token?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            //obtenerSiguienteToken()
            if(tokenActual.lexema == "~cad" || tokenActual.lexema == "~ente"
                    ||tokenActual.lexema == "~duplex"||tokenActual.lexema.toLowerCase() == "~car"
                    ||tokenActual.lexema.toLowerCase() == "list" ||tokenActual.lexema.toLowerCase() == "int" ){
                return tokenActual
            }
        } else{
            if(tokenActual.categoria==Categoria.IDENTIFICADOR && listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO){
                return tokenActual
            }
        }
        return null
    }

    /**
     *
     */


    /**
     *<BloqueSentencia> ::= "{"[<ListaSentencias>]"}"
     */
    fun esBloqueSentencias():ArrayList<Sentencia>?{

        if(tokenActual.categoria == Categoria.LLAVE_IZQUIERDA){
            obtenerSiguienteToken()

            var listaSentencias = esListaSentencias()

            if(tokenActual.categoria == Categoria.LLAVE_DERECHA){
                obtenerSiguienteToken()

                return listaSentencias
            }else {
                reportarError("falta la llave derecha de la funcion","Sentencia")
            }
            }else{

                reportarError("falta la llave izquierda de la funcion","Sentencia")

            }

        return null
    }

    /**
     * * BNF para reresentar una sentencia
     * <ListaSentencias>::=  <Sentecia> [<ListaSentencias> ]
     */
    fun esListaSentencias():ArrayList<Sentencia>{
        var listaSentencias =ArrayList<Sentencia>()
        var sentecia=esSentencia()

        while (sentecia !=null){
            listaSentencias.add(sentecia)
            obtenerSiguienteToken()
            sentecia=esSentencia()
        }
        return listaSentencias
    }


    /**
     * Sentencia> ::= <DesicionSimple>  | <Ciclo> |  <Impresion> | <Leer>  | <Asignacion> | <DeclaracionVariable> | TipoRetorno   |  <Incremento> | <Decremento> | <InvocacionFuncion>
     */
    fun esSentencia():Sentencia? {

        var sentencia: Sentencia? = esCicloFor()
        if (sentencia != null) {
            return sentencia
        }
        sentencia = esCicloWhile()
        if (sentencia != null) {
            return sentencia
        }
       sentencia = esArreglo()
        if (sentencia != null){
            return sentencia
        }

        sentencia = esAsignacion()
        if (sentencia != null){
            return sentencia
        }
        sentencia=esDecicionSimple()
        if (sentencia != null) {
            return sentencia
        }

        sentencia=esDeclaracion()
        if (sentencia != null) {
            return sentencia
        }
        sentencia=esInvocacionFuncion()
        if (sentencia != null) {
            return sentencia
        }
        sentencia=esIncremento()
        if (sentencia != null) {
            return sentencia
        }
        sentencia=esDecremento()
        if (sentencia != null) {
            return sentencia
        }

        sentencia=esInterrupcion()
        if (sentencia != null) {
            return sentencia
        }
        sentencia=esRetorno()
        if (sentencia != null) {
            return sentencia
        }
        sentencia=esImpresion()
        if (sentencia != null) {
            return sentencia
        }
        sentencia=esLeer()
        if (sentencia != null) {
            return sentencia
        }
        return null
    }

    /**
     * <Expresion> ::= <ExpresionLogica> | <ExpresionRelacional> | <ExpresionAritmetica> | <ExpresionCadena>
     */
    fun esExpresion():Expresion?{
        var expresion:Expresion?=esExpresionAritmetica()
        if (expresion!= null) return expresion

        expresion=esExpresionRelacional()
        if (expresion!= null) return expresion

        expresion=esExpresionCadena()
        if (expresion!= null) return expresion

        return null
    }

    /**
     *<ExpAritmetica> ::= “(” <ExpAritmetica> “)”  [OperadorAritmetico < ExpAritmetica >] | <ValorNumerico>  [OperadorAritmetico < ExpAritmetica >]
     */
    fun esExpresionAritmetica():ExpresionAritmetica?{

        if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
            obtenerSiguienteToken()
            val expAritmetica: ExpresionAritmetica? = esExpresionAritmetica()

            if (expAritmetica != null) {
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    obtenerSiguienteToken()
                }else{
                    reportarError("Falta el parentesis derecho de la expresion Aritmetica","Expresion Aritmetica")
                }

                if (tokenActual.categoria == Categoria.OPERADOR_MATEMATICO){
                    var operador =tokenActual
                    obtenerSiguienteToken()

                    val expAritmetica2: ExpresionAritmetica? = esExpresionAritmetica()

                    if (expAritmetica2 != null){
                        return ExpresionAritmetica(expAritmetica, operador,expAritmetica2)
                    }
                }else{
                    return ExpresionAritmetica(expAritmetica)
                }

            }
        }else{
            var numerico=esValorNumerico()

            if (numerico != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_MATEMATICO) {
                    var operador = tokenActual
                    obtenerSiguienteToken()

                    val expAritmetica2: ExpresionAritmetica? = esExpresionAritmetica()

                    if (expAritmetica2 == null) {
                        reportarError("Falta la expresion 2 de la Expresion Aritmetica","Expresion Aritmetica")

                    }
                    return ExpresionAritmetica(numerico, operador, expAritmetica2!!)

                } else {
                    return ExpresionAritmetica(numerico)
                }
            }
        }
        return null
    }

    /**
     * <ValorNumerico> ::= [<Signo>] real | [<Signo>]  entero | [<Signo>]  idetificador
     */

    fun esValorNumerico():ValorNumerico?{
        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        var  signo=""
        if (tokenActual.lexema == "+" || tokenActual.lexema == "-") {
            signo =tokenActual.lexema
            obtenerSiguienteToken()
        }
        if (signo =="") signo="+"

        var tSigno= Token(signo,Categoria.SIGNO,tokenActual.fila,tokenActual.columna)


        if (tokenActual.categoria == Categoria.ENTERO || tokenActual.categoria == Categoria.DECIMAL ||
                (tokenActual.categoria == Categoria.IDENTIFICADOR && listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO)){
            return ValorNumerico(tSigno,tokenActual)
        }
        return null
    }

    /**
     * <ExpresionRelacional>::= <ExpresionAritmetica> <OperadorRelacional> <ExpresionAritmetica>
     */
    fun esExpresionRelacional():ExpresionRelacional?{
        try{
            var expresion1=esExpresionAritmetica()
            if (expresion1 != null){
                //obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL){
                    var operadorRelacional=tokenActual
                    obtenerSiguienteToken()
                    var expresion2=esExpresionAritmetica()

                    if (expresion2 != null) {
                        return ExpresionRelacional(expresion1,operadorRelacional,expresion2)
                    }else{
                        reportarError("Falta la operacion 2 de la expresion relacional","Expresion Relacional")
                    }
                }else{
                    reportarError("Falta el operador relacional","Expresion Relacional")
                }
            }
        }
        catch (ex:java.lang.Exception){
            print("Exception Metodo de creacion de Expresion Relacional")
        }
        return null
    }

    /**
     * <ExpresionCadena> ::= cadenaDeCaracteres ["+"<Expresion> ] | identificador
     */
    fun esExpresionCadena():ExpresionCadena?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria== Categoria.CADENA ){
            var cadena=tokenActual
            obtenerSiguienteToken()
            var expresion:Expresion?= null

            if (tokenActual.lexema == "+"){
                obtenerSiguienteToken()
                expresion=esExpresion()
            }
            return ExpresionCadena(cadena,expresion)
        }
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var variable = tokenActual
                obtenerSiguienteToken()
                return ExpresionCadena(variable,null)
            }

        return null
    }


    /**
     * <LecturaDatos> ::= ~read Identificador
     */
    fun esLeer():Sentencia?{

        if (tokenActual.categoria== Categoria.PALABRA_RESERVADA && tokenActual.lexema== "~read") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var variable = tokenActual
                obtenerSiguienteToken()

                return Leer(variable)
            }
        }
        return null
    }

    /**
     * <Imprimir> ::= ~print  “  “ “ ” Mensaje “ ” ”  [“+”<DeclaracionVariable>] [ “+”<Expresion>]
     */
    fun esImpresion():Sentencia?{
        if (tokenActual.categoria==Categoria.PALABRA_RESERVADA && tokenActual.lexema =="~print"){
            obtenerSiguienteToken()
            var imprimir=esExpresion()
            if (imprimir != null){

                return Imprimir(imprimir)
            }
        }
        return null
    }

    /**
     * palabra reservada del break con el ~
     */
    fun esInterrupcion():Sentencia?{
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema =="~break"){
            var interrupcion=tokenActual
            obtenerSiguienteToken()
            return Interrupcion(interrupcion)
        }
        return null
    }


    /**
     * <Incremento> ::= Identificador  "++"
     */
    fun esIncremento():Incremento?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO
                    && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_DECREMENTO){

                if (tokenActual.categoria== Categoria.IDENTIFICADOR){
                    var nombre=tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria== Categoria.OPERADOR_INCREMENTO){
                        var operador = tokenActual
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                            return Incremento(nombre,operador)
                        }else{
                            reportarError("Falta el fin de sentencia en la operacion de Incremento","Sentencia de Incremento")
                        }
                    }
                }

            }
        }catch (ex:Exception){

        }
        return null
    }

    /**
     *<Decremento> ::= Identificador  "--"
     */
    fun esDecremento():Decremento?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO
                    && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_INCREMENTO){

                if (tokenActual.categoria== Categoria.IDENTIFICADOR){
                    var nombre=tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria== Categoria.OPERADOR_DECREMENTO){
                        var operador = tokenActual
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                            return Decremento(nombre,operador)
                        }else{
                            reportarError("Falta el fin de sentencia de la expresion de decremente","Sentencia de Decremento")
                        }
                    }
                }

            }
        }catch (ex:Exception){

        }
        return null
    }


    fun Acceso ():Token?{
        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if(tokenActual.lexema.toLowerCase() == "public" || tokenActual.lexema.toLowerCase() == "private"
                    ||tokenActual.lexema.toLowerCase() == "protected"){
                return tokenActual
            }
        }
        return null
    }

    /***
     *<Declaracion> ::=  <TipoDato> <Identificador> [<OperadorAsignacion> <TipoAsignacion>]
     */
    fun esDeclaracion():Declaracion?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try{
            if ( listaTokens[posicionActual+1].categoria != Categoria.PARENTESIS_IZQUIERDO && tokenActual.lexema.toLowerCase() != "back"
                    && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_INCREMENTO && listaTokens[posicionActual+1].categoria != Categoria.OPERADOR_DECREMENTO) {

                var modAcceso = Acceso()
                if (modAcceso != null) obtenerSiguienteToken()

                var tipoDato = esTipoParametro()

                if (tipoDato != null) {
                    obtenerSiguienteToken()

                    if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                        var nombre = tokenActual
                        obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                            var opAsignacion = tokenActual
                            obtenerSiguienteToken()
                            var expresion = esExpresion()
                            var invocacion = esInvocacionFuncion()
                            var asignacion:Asignacion


                            if (expresion != null || invocacion != null) {
                                if (expresion!= null) {
                                    asignacion = Asignacion(nombre, opAsignacion, expresion)
                                }else{
                                    asignacion = Asignacion(nombre, opAsignacion, invocacion!!)
                                }
                                return Declaracion(modAcceso, tipoDato, nombre, asignacion)
                            } else {
                                reportarError("Falta la expresion de asignacion de la declaracion", "Declaracion")
                                asignacion = Asignacion(nombre, opAsignacion, null)
                                return Declaracion(modAcceso, tipoDato, nombre, asignacion)
                            }

                        } else {
                            return Declaracion(modAcceso, tipoDato, nombre, null)
                        }

                    } else {
                        reportarError("Falta el identificador de la declaracion", "Declaracion")
                    }

                }
            }
        }catch (ex:Exception){

        }
        return null
    }

    /**
     * <<CicloFor> ::= ~by “(” TipoDato Identificador  in Identificador“)”  “{” <ListaSentencia> “}”
     */
    fun esCicloFor():CicloFor?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema.toLowerCase() == "~by" ){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
            }
            else{
                reportarError("Falta parentesis izquierdo en el For","Ciclo For")
                obtenerSiguienteToken()
            }

            var tipoDato = esTipoParametro()

            if (tipoDato != null) {
                obtenerSiguienteToken()
            } else {
                reportarError("Tipo de dato invalido en el For","Ciclo For")
                tipoDato= Token("Error",Categoria.ERROR,tokenActual.fila,tokenActual.columna)
                obtenerSiguienteToken()
            }

            var lista:Token?=null
            if (tokenActual.categoria != Categoria.IDENTIFICADOR) {
                reportarError("Lista no valida en el For","Ciclo For")
                lista = Token("Sin Lista",Categoria.ERROR,tokenActual.fila,tokenActual.columna)
                finCodigo(Categoria.PARENTESIS_DERECHO)

            }else{
                lista = tokenActual
                obtenerSiguienteToken()
            }

            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~in") {
                obtenerSiguienteToken()
            } else {
                reportarError("Falta la palabra reservada 'in' en el For","Ciclo For")
                obtenerSiguienteToken()
            }
            var item:Token?=null
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                item =tokenActual
                obtenerSiguienteToken()
            } else {
                reportarError("Identificador del item invalido en el For","Ciclo For")
                finCodigo(Categoria.PARENTESIS_DERECHO)
                item= Token("Sin Item",Categoria.ERROR,tokenActual.fila,tokenActual.columna)
            }

            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                obtenerSiguienteToken()
            } else {
                reportarError("Falta parentesis derecho en el For","Ciclo For")
                obtenerSiguienteToken()
            }
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                obtenerSiguienteToken()
            } else {
                reportarError("Falta llave izquierda en el For","Ciclo For")
                obtenerSiguienteToken()
            }
            var listaSentencias = esListaSentencias()
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                return CicloFor( lista,item, tipoDato,  listaSentencias )

            } else {
                reportarError("Falta llave derecha en el For","Ciclo For")
                obtenerSiguienteToken()
            }

        }
        return null
    }

    /***
     * <CicloWhile> ::= ~time “(“ <ExpresionRelacional> “)” “{”<ListaSentencias> “}”
     */
    fun esCicloWhile():CicloWhile?{
        try {
            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~time") {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()

                    var expRelacional = esExpresionRelacional()

                    if (expRelacional == null) {

                        reportarError("Falta la expresion relacional del ciclo while", "Ciclo While")
                        finCodigo(Categoria.PARENTESIS_DERECHO)
                    }
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                            obtenerSiguienteToken()

                            var sentencias = esListaSentencias()

                            while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                                obtenerSiguienteToken()
                                return CicloWhile(expRelacional, sentencias)

                            } else {
                                reportarError("Falta la llave derecha del ciclo while", "Ciclo While")
                            }
                        } else {
                            reportarError("Falta la llave izquierda del ciclo while", "Ciclo While")
                        }
                    } else {
                        reportarError("Falta el parentesis derecho del ciclo while", "Ciclo While")
                    }

                } else {
                    reportarError("Falta el parentesis izquierdo del ciclo while", "Ciclo While")
                }
            }

        }
        catch (ex:java.lang.Exception){
            print("Exception Metodo de creacion de ciclo While")
        }
        return null
    }

    /**
     * <Asignacion> ::= Identificador “=” <Expresion>
     */
    fun esAsignacion():Sentencia?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()
        try {
            if (listaTokens[posicionActual + 1].categoria != Categoria.PARENTESIS_IZQUIERDO && listaTokens[posicionActual + 1].categoria != Categoria.OPERADOR_DECREMENTO
                    && listaTokens[posicionActual + 1].categoria != Categoria.OPERADOR_INCREMENTO
            ) {

                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var nombre = tokenActual
                    obtenerSiguienteToken()

                    var opAsignacion:Token?=null

                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION) {
                        opAsignacion = tokenActual
                        obtenerSiguienteToken()
                    } else {
                        reportarError("Falta el operador de asinacion de la asignacion", "Asignacion")
                        obtenerSiguienteToken()
                    }
                    var expresion = esExpresion()
                    var invocacion: Sentencia? = esInvocacionFuncion()

                    if (expresion != null || invocacion != null) {

                        //obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                            //obtenerSiguienteToken()
                            if (expresion != null) {
                                return Asignacion(nombre, opAsignacion, expresion)
                            } else {
                                return Asignacion(nombre, opAsignacion, invocacion!!)
                            }
                        } else {
                            reportarError("Falta el fin de sentencia de la asignacion", "Asignacion")
                        }
                    } else {
                        reportarError("Falta la expresion de la asignacion", "Asignacion")
                        finCodigo(Categoria.FIN_SENTENCIA)
                        return Asignacion(nombre, opAsignacion,null)
                    }


                }
            }
        }catch ( ex:java.lang.Exception){

        }
        return null
    }

    /**
     * <DesicionSimple> ::= ~yes “(“ <EspresionLogica> “)” “{”<ListaSentencias> “}” [<Else>]
     *  <Else> ::= “~more”  “{”<ListaSentencias> “}”
     */
    fun esDecicionSimple():Decision?{
        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~yes"){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                obtenerSiguienteToken()
            }else{
                reportarError("Falta el parentesis izquierdo del if","Decision")
                obtenerSiguienteToken()
            }
            var expLogica: ExpresionLogica?=esExpresionLogica()

            if (expLogica != null){
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                    obtenerSiguienteToken()
                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                    if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                        obtenerSiguienteToken()
                        var sentencias = esListaSentencias()
                        while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()


                        if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                            obtenerSiguienteToken()

                            if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~more") {
                                obtenerSiguienteToken()
                                while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                                    obtenerSiguienteToken()
                                    var sentenciasElse = esListaSentencias()
                                    while (tokenActual.categoria == Categoria.FIN_SENTENCIA) obtenerSiguienteToken()

                                    if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {

                                        if (sentenciasElse != null) {
                                            obtenerSiguienteToken()
                                            return Decision(expLogica,sentencias, sentenciasElse)
                                        }else{
                                            reportarError("Falta lista de sentencias del else","Decision")
                                        }
                                    }else{
                                        reportarError("Falta el parentesis derecho del else","Decision")
                                    }
                                }else{
                                    reportarError("Falta el parentesis izquierdo del else","Decision")
                                }
                            } else{
                                return Decision(expLogica, sentencias, null)
                            }

                        }else{
                            reportarError("Falta la llave derecha del if","Decision")
                        }
                    }else{
                        reportarError("Falta la lleve izquierda del if","Decision")
                    }
                }else{
                    reportarError("Falta el parentesis derecho del if","Decision")
                }
            }else{
                reportarError("Falta la expresion logica del if","Decision")
            }
        }
        return null
    }

    /**
     * <ExpresionLogica> <ExpresionRelacional>  [<OperadorLogico><ExpresionLogica>]
     */
    fun esExpresionLogica():ExpresionLogica?{
        var operadorUnario:Token?=null

        if (tokenActual.categoria == Categoria.OPERADOR_LOGICO_UNARIO) {
            operadorUnario=tokenActual
            obtenerSiguienteToken()
        }

        var expRelacional:ExpresionRelacional?=esExpresionRelacional()

        if (expRelacional != null){
            //obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.OPERADOR_LOGICO_BINARIO){
                var operadorBinario=tokenActual
                obtenerSiguienteToken()
                var expresionLogica=esExpresionLogica()

                if (expresionLogica != null){
                    return ExpresionLogica(expRelacional,operadorBinario, expresionLogica)
                }else{
                    reportarError("Falta la expresion logica despues del operador logico","Expresion Logica")
                }
            }else{
                return ExpresionLogica(operadorUnario,expRelacional)
            }

        }else{
            reportarError("Falta la expresion relacional de la expresion logica","Expresion Logica")
        }
        return null
    }

    /**
     *
     */
    fun finCodigo( parada:Categoria){
        while (tokenActual.categoria != parada && tokenActual.categoria!= Categoria.FIN_CODIGO){

            obtenerSiguienteToken()
        }
    }

    /**
     *
     * <Arreglo>:: ~array <TipoDato> identificadorv ["="[" <ListaArgumentos> "]"] "_"
     *
     */
    fun esArreglo(): Arreglo? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "~array") {
            obtenerSiguienteToken()
            val tipoDato = esTipoDato()
            if (tipoDato != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    var nombre = tokenActual
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ASIGNACION && tokenActual.lexema == "=") {
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.CORCHETE_IZQUIERDO) {
                            obtenerSiguienteToken()
                            var expresion = esListaArgumentos()
                            if (tokenActual.categoria == Categoria.CORCHETE_DERECHO)
                                obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                                obtenerSiguienteToken()
                                return Arreglo(nombre, tipoDato, expresion)
                            }else{
                                reportarError(" Falta operador terminal", "Expresion arreglo")
                                println("Falta operador terminal")
                            }
                        }else{
                            reportarError(" Falta corchete derecho", "Expresion arreglo")
                            println("Falta corchete derecho")
                        }
                    } else {
                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                            obtenerSiguienteToken()
                            return Arreglo(nombre, tipoDato, null)
                        }else{
                            reportarError(" Falta operador terminal", "Expresion arreglo")
                            println("Falta operador terminal")
                        }
                    }
                }else{
                    reportarError(" Falta operador terminal", "Expresion arreglo")
                    println("Falta operador terminal")
                }
            }
        }
        return null
    }

}