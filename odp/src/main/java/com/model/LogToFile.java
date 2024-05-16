package com.example.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class LogToFile {
    File file;

    public LogToFile(String filename) {
        if (!filename.endsWith(".log")){
            filename+=".log";
        }
        this.file = new File(filename);
        try {
            if (!file.exists()) {
                //El archivo no existe. Creando nuevo archivo.
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.log("Opened filelog "+filename);
    }
     public void log(String logline){
        try {   
        // Abrir el archivo en modo de escritura para agregar contenido
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, true))) {
            bw.write(LocalDateTime.now()+":"+logline+'\n');
            bw.close();
        }
    } catch (IOException e) {
        System.err.println("Error de E/S: " + e.getMessage());
    }
     }


}
