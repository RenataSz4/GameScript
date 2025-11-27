section .data
    input_msg_0 db 'Ingresa vida inicial del jugador:', 0
    input_msg_0_len equ $ - input_msg_0
    input_msg_1 db 'Ingresa vida del enemigo:', 0
    input_msg_1_len equ $ - input_msg_1
    msg_2 db 'Bienvenido al juego!', 0xA, 0
    msg_2_len equ $ - msg_2
    msg_3 db 'Tu mision es recolectar items y evitar enemigos', 0xA, 0
    msg_3_len equ $ - msg_3
    msg_9 db 'Item recolectado!', 0xA, 0
    msg_9_len equ $ - msg_9
    msg_14 db 'Golpeado por enemigo!', 0xA, 0
    msg_14_len equ $ - msg_14
    msg_15 db 'Felicidades! Has ganado!', 0xA, 0
    msg_15_len equ $ - msg_15
    msg_17 db 'Game Over', 0xA, 0
    msg_17_len equ $ - msg_17
    msg_18 db 'Mejor suerte la proxima vez', 0xA, 0
    msg_18_len equ $ - msg_18
    input_buffer times 64 db 0
    input_buffer_len equ 64
    num_format db '%d', 0
    float_format db '%.2f', 0
    newline db 0xA, 0

section .bss
    var_1 resd 1
    var_6 resd 1
    var_0 resd 1
    var_4 resd 1
    var_5 resd 1
    var_7 resd 1
    var_3 resd 1
    var_2 resd 1

section .text
    global _start

_start:
    push ebp
    mov ebp, esp
    sub esp, 256

L1:
    push ecx
    push edi
    mov ecx, input_buffer_len
    mov edi, input_buffer
    xor al, al
    rep stosb
    pop edi
    pop ecx
    mov eax, 4
    mov ebx, 1
    mov ecx, input_msg_0
    mov edx, input_msg_0_len
    int 0x80
    mov eax, 3
    mov ebx, 0
    mov ecx, input_buffer
    mov edx, input_buffer_len
    int 0x80
    call str_to_int
    mov [var_0], eax
    jmp L2

L2:
    push ecx
    push edi
    mov ecx, input_buffer_len
    mov edi, input_buffer
    xor al, al
    rep stosb
    pop edi
    pop ecx
    mov eax, 4
    mov ebx, 1
    mov ecx, input_msg_1
    mov edx, input_msg_1_len
    int 0x80
    mov eax, 3
    mov ebx, 0
    mov ecx, input_buffer
    mov edx, input_buffer_len
    int 0x80
    call str_to_int
    mov [var_1], eax
    jmp L3

L3:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_2
    mov edx, msg_2_len
    int 0x80
    jmp L4

L4:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_3
    mov edx, msg_3_len
    int 0x80
    jmp L5

L5:
    mov eax, 0
    mov [var_2], eax
    jmp L6

L6:
    mov eax, 0
    mov [var_3], eax
    jmp L7

L7:
    mov eax, 5
    mov [var_2], eax
    mov ebx, 0
    mov [var_3], ebx
    jmp L8

L8:
    mov eax, [var_2]
    mov ebx, [var_4]
    cmp eax, ebx
    jge L9
    jmp L12

L9:
    mov eax, [var_5]
    mov ebx, [var_6]
    add eax, ebx
    mov [var_5], eax
    jmp L10

L10:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_9
    mov edx, msg_9_len
    int 0x80
    jmp L11

L11:
    mov eax, [var_4]
    mov ebx, 10
    add eax, ebx
    mov [var_4], eax
    jmp L12

L12:
    mov eax, 10
    mov [var_2], eax
    mov ebx, 0
    mov [var_3], ebx
    jmp L13

L13:
    mov eax, [var_2]
    mov ebx, [var_7]
    cmp eax, ebx
    jge L14
    jmp L16

L14:
    mov eax, [var_0]
    mov ebx, 10
    sub eax, ebx
    mov [var_0], eax
    jmp L15

L15:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_14
    mov edx, msg_14_len
    int 0x80
    jmp L16

L16:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_15
    mov edx, msg_15_len
    int 0x80
    jmp L17

L17:
    jmp _exit

L18:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_17
    mov edx, msg_17_len
    int 0x80
    jmp L19

L19:
    mov eax, 4
    mov ebx, 1
    mov ecx, msg_18
    mov edx, msg_18_len
    int 0x80
    jmp L20

L20:
    jmp _exit


str_to_int:
    push ebx
    push ecx
    push edx
    xor eax, eax
    xor ebx, ebx
    mov ecx, 10
.loop:
    movzx edx, byte [input_buffer + ebx]
    cmp edx, 0xA
    je .done
    cmp edx, 0
    je .done
    sub edx, '0'
    imul eax, ecx
    add eax, edx
    inc ebx
    jmp .loop
.done:
    pop edx
    pop ecx
    pop ebx
    ret

_exit:
    mov esp, ebp
    pop ebp
    mov eax, 1
    xor ebx, ebx
    int 0x80
