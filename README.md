# Advanced-Encryption-Standard
This repository contains implementation of AES cipher, also commonly known as Rijndael.


## Initialization

* Place all source files in one directory and create a Main class to run program.
* Program takes input two strings `key` and `text`.
* Both Strings `key` and `text` are in hexadecimal format where each character represents half byte and has value between __0__ and __F__ .

`AES aes= new AES(text,key);`


## Encryption

`aes.excrypt();`

Returns cipher text string in hexadecimal format.


## Decryption

`aes.decrypt();`

Returns plain text string in hexadecimal format.



