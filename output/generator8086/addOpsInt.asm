org 100h
mov ax, -1
section .data
dw 4
DATA_0: db "2+3="
section .text
push ax
mov ax, DATA_0
call prints
push ax
mov ax, 2
push ax
mov ax, 3
mov bx, ax
pop ax
add ax, bx
call printi
call println
section .data
dw 5
DATA_1: db "2+-3="
section .text
push ax
mov ax, DATA_1
call prints
push ax
mov ax, 2
push ax
mov ax, 3
neg ax
mov bx, ax
pop ax
add ax, bx
call printi
call println
section .data
dw 6
DATA_2: db "2+3*4="
section .text
push ax
mov ax, DATA_2
call prints
push ax
mov ax, 2
push ax
mov ax, 3
push ax
mov ax, 4
mov bx, ax
pop ax
imul ax, bx
mov bx, ax
pop ax
add ax, bx
call printi
call println
section .data
dw 6
DATA_3: db "2*3+4="
section .text
push ax
mov ax, DATA_3
call prints
push ax
mov ax, 2
push ax
mov ax, 3
mov bx, ax
pop ax
imul ax, bx
push ax
mov ax, 4
mov bx, ax
pop ax
add ax, bx
call printi
call println
call println
section .data
dw 4
DATA_4: db "2-3="
section .text
push ax
mov ax, DATA_4
call prints
push ax
mov ax, 2
push ax
mov ax, 3
mov bx, ax
pop ax
sub ax, bx
call printi
call println
section .data
dw 5
DATA_5: db "2--3="
section .text
push ax
mov ax, DATA_5
call prints
push ax
mov ax, 2
push ax
mov ax, 3
neg ax
mov bx, ax
pop ax
sub ax, bx
call printi
call println
section .data
dw 6
DATA_6: db "2-3*4="
section .text
push ax
mov ax, DATA_6
call prints
push ax
mov ax, 2
push ax
mov ax, 3
push ax
mov ax, 4
mov bx, ax
pop ax
imul ax, bx
mov bx, ax
pop ax
sub ax, bx
call printi
call println
section .data
dw 6
DATA_7: db "2*3-4="
section .text
push ax
mov ax, DATA_7
call prints
push ax
mov ax, 2
push ax
mov ax, 3
mov bx, ax
pop ax
imul ax, bx
push ax
mov ax, 4
mov bx, ax
pop ax
sub ax, bx
call printi
call println
mov ax,0x4c00
int 0x21

printb: ; (AX)->()
        ; print a boolean to stdout
        call boo2str
        call prints
        ret

printc:	; (AL)->()
        ; print a char to stdout
        mov dl, al      ; load character
        mov ah, 2		; output char to stdout (ah: 02, dl: char)
        int 0x21		; DOS
.end:	ret

printi: ; (AX)->()
        ; print a signed integer (16 bit) to stdout
        call int2decimal
        call prints
        ret

prints:	; (AX)->()
        ; print a string to stdout
        ; string start address in AX
        ; string length at [AX-2]

        mov bx, ax
        mov cx, [bx-2]  ; length
        cmp cx, 0
        je  .end
.loop:
        mov dl,[bx]     ; load character
        mov ah,2		; output char to stdout (ah: 02, dl: char)
        int 0x21		; DOS
        inc bx
        loop .loop
.end:	ret

println:; ()->()
        ; put CR LF to stdout

        push ax ; save
        mov ax, .line
        call prints
        pop ax  ; restore
        ret
.size:  dw 2
.line:	db 0x0D, 0x0A

int2decimal:
        ; (AX)->(AX)
        ; convert a signed integer (16 bit) to a buffer
        mov dl, '+'	; sign
        cmp	ax,0
        jge .unsigned
        neg ax
        mov dl, '-'
.unsigned:
        mov bx, .buffer
        mov [bx], dl	; sign
        mov cx, .endbuf-1
.next:	mov dx, 0
        mov bx, 10
        div bx	; ax = (dx, ax) / bx
                ; dx = remainder
        mov bx, cx
        add dl, '0'
        mov [bx], dl	; digit
        dec cx
        cmp ax, 0
        jne .next
        ; move sign if necessary
        ; BX points to the first digit now
        mov dl, [.buffer]	; sign '+' or '-'
        cmp dl, '-'
        jne .end    ; no sign
        dec bx
        mov [bx], dl    ; copy sign
.end:   mov ax, .endbuf
        sub ax, bx      ; size
        mov [bx-2], ax
        mov ax, bx      ; pointer
        ret

section .data
        dw      0 ; size
.buffer	db		"-", "12345"
.endbuf:
section .text

boo2str:
        ; (AX)->(AX)
        ; convert a boolean to a string
        cmp ax, 0
        je .false
        mov ax, true
        ret
.false: mov ax, false
        ret

section .data
        dw      4 ; size
true:	db		"true"
        dw      5 ; size
false:	db		"false"
section .text

