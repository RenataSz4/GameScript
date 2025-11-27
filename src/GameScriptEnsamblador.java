import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameScriptEnsamblador {
    private ArrayList<Tupla> tuplas;
    private ArrayList<String> codigoEnsamblador;
    private Map<String, String> variablesEnMemoria;

    public GameScriptEnsamblador(ArrayList<Tupla> tuplas) {
        this.tuplas = tuplas;
        this.codigoEnsamblador = new ArrayList<>();
        this.variablesEnMemoria = new HashMap<>();
    }

    public void generarCodigo() {
        generarEncabezado();
        generarSeccionDatos();
        generarSeccionCodigo();
        generarPie();
    }

    private void generarEncabezado() {
        agregar("section .data");
    }

    private void generarSeccionDatos() {
        // Analizar las tuplas para encontrar mensajes y variables
        for (int i = 0; i < tuplas.size(); i++) {
            Tupla tupla = tuplas.get(i);

            if (tupla instanceof Print) {
                Print print = (Print) tupla;
                String mensaje = print.mensaje.getNombre().replace("\"", "");
                String etiquetaMensaje = "msg_" + i;
                agregar("    " + etiquetaMensaje + " db '" + mensaje + "', 0xA, 0");
                agregar("    " + etiquetaMensaje + "_len equ $ - " + etiquetaMensaje);
            }

            if (tupla instanceof Input) {
                Input input = (Input) tupla;
                String mensaje = input.mensaje.getNombre().replace("\"", "");
                String etiquetaMensaje = "input_msg_" + i;
                agregar("    " + etiquetaMensaje + " db '" + mensaje + "', 0");
                agregar("    " + etiquetaMensaje + "_len equ $ - " + etiquetaMensaje);
            }
        }

        // Buffer para entrada de usuario
        agregar("    input_buffer times 64 db 0");
        agregar("    input_buffer_len equ 64");

        // Formato para números
        agregar("    num_format db '%d', 0");
        agregar("    float_format db '%.2f', 0");
        agregar("    newline db 0xA, 0");

        agregar("");
        agregar("section .bss");

        // analizar todas las tuplas para identificar variables necesarias
        identificarVariables();

        // Reservar espacio para cada variable identificada
        for (Map.Entry<String, String> entry : variablesEnMemoria.entrySet()) {
            agregar("    " + entry.getValue() + " resd 1");
        }

        agregar("");
    }

    private void identificarVariables() {
        for (Tupla tupla : tuplas) {
            if (tupla instanceof Move) {
                Move move = (Move) tupla;
                String entidad = move.entidad.getNombre();
                getVariableMemoria(entidad + ".posX");
                getVariableMemoria(entidad + ".posY");
            } else if (tupla instanceof Set) {
                Set set = (Set) tupla;
                String variable = set.entidad.getNombre() + "." + set.atributo.getNombre();
                getVariableMemoria(variable);

                String valorStr = set.valor.getNombre();
                identificarVariablesEnExpresion(valorStr);
            } else if (tupla instanceof Input) {
                Input input = (Input) tupla;
                String variable = input.entidad.getNombre() + "." + input.atributo.getNombre();
                getVariableMemoria(variable);
            } else if (tupla instanceof If) {
                If ifTupla = (If) tupla;
                identificarVariablesEnExpresion(ifTupla.valor1.getNombre());
                identificarVariablesEnExpresion(ifTupla.valor2.getNombre());
            } else if (tupla instanceof While) {
                While whileTupla = (While) tupla;
                identificarVariablesEnExpresion(whileTupla.valor1.getNombre());
                identificarVariablesEnExpresion(whileTupla.valor2.getNombre());
            }
        }
    }

    private void identificarVariablesEnExpresion(String expresion) {
        if (expresion == null || expresion.isEmpty()) {
            return;
        }

        if (esNumero(expresion)) {
            return;
        }

        if (expresion.contains(".") && !expresion.contains(" ")) {
            getVariableMemoria(expresion);
            return;
        }

        if (expresion.contains("+") || expresion.contains("-") ||
                expresion.contains("*") || expresion.contains("/")) {

            String[] partes = expresion.split("\\s+");

            for (String parte : partes) {
                if (!parte.equals("+") && !parte.equals("-") &&
                        !parte.equals("*") && !parte.equals("/")) {

                    if (parte.contains(".") && !esNumero(parte)) {
                        getVariableMemoria(parte);
                    }
                }
            }
        }
    }

    private void generarSeccionCodigo() {
        agregar("section .text");
        agregar("    global _start");
        agregar("");
        agregar("_start:");
        agregar("    push ebp");
        agregar("    mov ebp, esp");
        agregar("    sub esp, 256");
        agregar("");

        // Generar código para cada tupla
        for (int i = 0; i < tuplas.size(); i++) {
            Tupla tupla = tuplas.get(i);

            agregar("L" + (i + 1) + ":");

            if (tupla instanceof Move) {
                generarMove((Move) tupla, i);
            } else if (tupla instanceof Print) {
                generarPrint((Print) tupla, i);
            } else if (tupla instanceof Input) {
                generarInput((Input) tupla, i);
            } else if (tupla instanceof Set) {
                generarSet((Set) tupla, i);
            } else if (tupla instanceof If) {
                generarIf((If) tupla, i);
            } else if (tupla instanceof While) {
                generarWhile((While) tupla, i);
            } else if (tupla instanceof EndGame) {
                generarEndGame();
            }

            agregar("");
        }
    }

    private void generarMove(Move move, int indice) {
        String entidad = move.entidad.getNombre();
        String varX = getVariableMemoria(entidad + ".posX");
        String varY = getVariableMemoria(entidad + ".posY");

        if (esNumero(move.x.getNombre())) {
            agregar("    mov eax, " + move.x.getNombre());
        } else {
            String varXOrigen = getVariableMemoria(move.x.getNombre());
            agregar("    mov eax, [" + varXOrigen + "]");
        }
        agregar("    mov [" + varX + "], eax");

        if (esNumero(move.y.getNombre())) {
            agregar("    mov ebx, " + move.y.getNombre());
        } else {
            String varYOrigen = getVariableMemoria(move.y.getNombre());
            agregar("    mov ebx, [" + varYOrigen + "]");
        }
        agregar("    mov [" + varY + "], ebx");

        if (indice + 1 < tuplas.size()) {
            agregar("    jmp L" + (indice + 2));
        } else {
            agregar("    jmp _exit");
        }
    }

    private void generarPrint(Print print, int indice) {
        agregar("    mov eax, 4");
        agregar("    mov ebx, 1");
        agregar("    mov ecx, msg_" + indice);
        agregar("    mov edx, msg_" + indice + "_len");
        agregar("    int 0x80");

        if (indice + 1 < tuplas.size()) {
            agregar("    jmp L" + (indice + 2));
        } else {
            agregar("    jmp _exit");
        }
    }

    private void generarInput(Input input, int indice) {
        // Limpiar buffer antes de leer
        agregar("    push ecx");
        agregar("    push edi");
        agregar("    mov ecx, input_buffer_len");
        agregar("    mov edi, input_buffer");
        agregar("    xor al, al");
        agregar("    rep stosb");
        agregar("    pop edi");
        agregar("    pop ecx");

        // Imprimir mensaje
        agregar("    mov eax, 4");
        agregar("    mov ebx, 1");
        agregar("    mov ecx, input_msg_" + indice);
        agregar("    mov edx, input_msg_" + indice + "_len");
        agregar("    int 0x80");

        // Leer entrada del usuario
        agregar("    mov eax, 3");
        agregar("    mov ebx, 0");
        agregar("    mov ecx, input_buffer");
        agregar("    mov edx, input_buffer_len");
        agregar("    int 0x80");

        // Convertir entrada a número y almacenar
        String variable = getVariableMemoria(input.entidad.getNombre() + "." +
                input.atributo.getNombre());
        agregar("    call str_to_int");
        agregar("    mov [" + variable + "], eax");

        if (indice + 1 < tuplas.size()) {
            agregar("    jmp L" + (indice + 2));
        } else {
            agregar("    jmp _exit");
        }
    }

    private void generarSet(Set set, int indice) {
        String variable = getVariableMemoria(set.entidad.getNombre() + "." +
                set.atributo.getNombre());
        String valorStr = set.valor.getNombre();

        if (valorStr.contains("+") || valorStr.contains("-") ||
                valorStr.contains("*") || valorStr.contains("/")) {
            generarExpresion(valorStr, variable);
        } else if (valorStr.contains(".")) {
            String varOrigen = getVariableMemoria(valorStr);
            agregar("    mov eax, [" + varOrigen + "]");
            agregar("    mov [" + variable + "], eax");
        } else if (esNumero(valorStr)) {
            agregar("    mov eax, " + valorStr);
            agregar("    mov [" + variable + "], eax");
        }

        if (indice + 1 < tuplas.size()) {
            agregar("    jmp L" + (indice + 2));
        } else {
            agregar("    jmp _exit");
        }
    }

    private void generarIf(If ifTupla, int indice) {
        cargarOperando(ifTupla.valor1.getNombre(), "eax");

        cargarOperando(ifTupla.valor2.getNombre(), "ebx");

        agregar("    cmp eax, ebx");

        // Salto condicional según operador
        String operador = ifTupla.operador.getNombre();
        String instruccionSalto = getInstruccionSalto(operador);

        int saltoVerdadero = ifTupla.getSaltoVerdadero() + 1;
        int saltoFalso = ifTupla.getSaltoFalso() + 1;

        agregar("    " + instruccionSalto + " L" + saltoVerdadero);
        agregar("    jmp L" + saltoFalso);
    }

    private void generarWhile(While whileTupla, int indice) {
        cargarOperando(whileTupla.valor1.getNombre(), "eax");

        cargarOperando(whileTupla.valor2.getNombre(), "ebx");

        // Comparar
        agregar("    cmp eax, ebx");

        String operador = whileTupla.operador.getNombre();
        String instruccionSalto = getInstruccionSalto(operador);

        int saltoVerdadero = whileTupla.getSaltoVerdadero() + 1;
        int saltoFalso = whileTupla.getSaltoFalso() + 1;

        agregar("    " + instruccionSalto + " L" + saltoVerdadero);
        agregar("    jmp L" + saltoFalso);
    }

    private void generarEndGame() {
        agregar("    jmp _exit");
    }

    private void generarPie() {
        agregar("");

        // convertir string a entero
        agregar("str_to_int:");
        agregar("    push ebx");
        agregar("    push ecx");
        agregar("    push edx");
        agregar("    xor eax, eax");
        agregar("    xor ebx, ebx");
        agregar("    mov ecx, 10");
        agregar(".loop:");
        agregar("    movzx edx, byte [input_buffer + ebx]");
        agregar("    cmp edx, 0xA");
        agregar("    je .done");
        agregar("    cmp edx, 0");
        agregar("    je .done");
        agregar("    sub edx, '0'");
        agregar("    imul eax, ecx");
        agregar("    add eax, edx");
        agregar("    inc ebx");
        agregar("    jmp .loop");
        agregar(".done:");
        agregar("    pop edx");
        agregar("    pop ecx");
        agregar("    pop ebx");
        agregar("    ret");
        agregar("");

        agregar("_exit:");
        agregar("    mov esp, ebp");
        agregar("    pop ebp");
        agregar("    mov eax, 1");
        agregar("    xor ebx, ebx");
        agregar("    int 0x80");
    }

    private void generarExpresion(String expresion, String varDestino) {
        String[] partes = expresion.split("\\s+");

        if (partes.length == 3) {
            String operando1 = partes[0];
            String operador = partes[1];
            String operando2 = partes[2];

            cargarOperando(operando1, "eax");

            cargarOperando(operando2, "ebx");

            // Ejecutar operación
            switch (operador) {
                case "+":
                    agregar("    add eax, ebx");
                    break;
                case "-":
                    agregar("    sub eax, ebx");
                    break;
                case "*":
                    agregar("    imul eax, ebx");
                    break;
                case "/":
                    agregar("    xor edx, edx");
                    agregar("    idiv ebx");
                    break;
            }

            agregar("    mov [" + varDestino + "], eax");
        }
    }

    private void cargarOperando(String operando, String registro) {
        if (esNumero(operando)) {
            agregar("    mov " + registro + ", " + operando);
        } else if (operando.contains(".")) {
            String var = getVariableMemoria(operando);
            agregar("    mov " + registro + ", [" + var + "]");
        } else {
            String var = getVariableMemoria(operando);
            agregar("    mov " + registro + ", [" + var + "]");
        }
    }

    private String getInstruccionSalto(String operador) {
        switch (operador) {
            case ">":
                return "jg";// jump if greater
            case "<":
                return "jl";// jump if less
            case ">=":
                return "jge";// jump if greater or equal
            case "<=":
                return "jle";// jump if less or equal
            case "==":
                return "je";// jump if equal
            case "!=":
                return "jne";// jump if not equal
            default:
                return "jmp";
        }
    }

    private String getVariableMemoria(String nombre) {
        if (!variablesEnMemoria.containsKey(nombre)) {
            String ubicacion = "var_" + variablesEnMemoria.size();
            variablesEnMemoria.put(nombre, ubicacion);
        }
        return variablesEnMemoria.get(nombre);
    }

    private boolean esNumero(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void agregar(String linea) {
        codigoEnsamblador.add(linea);
    }

    public ArrayList<String> getCodigoEnsamblador() {
        return codigoEnsamblador;
    }

    public void imprimir() {
        for (String linea : codigoEnsamblador) {
            System.out.println(linea);
        }
    }

    public void guardarEnArchivo(String nombreArchivo) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(nombreArchivo);
            for (String linea : codigoEnsamblador) {
                writer.write(linea + "\n");
            }
            writer.close();
            System.out.println("Código ensamblador guardado en: " + nombreArchivo);
        } catch (java.io.IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}
