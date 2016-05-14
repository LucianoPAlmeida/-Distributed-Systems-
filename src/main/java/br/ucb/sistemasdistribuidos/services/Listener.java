/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucb.sistemasdistribuidos.services;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author lucianoalmeida
 */
public class Listener implements Runnable{
    public static Integer INPUT_BLOCK_SIZE = 8000;
    private Socket socket;

    private boolean active;
            
    public Listener() {
        setActive(true);
    }
    
    public Listener(Socket socket) {
        this();
        setSocket(socket);
    }
    
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    
    
    @Override
    public void run() {
        try {
            AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class,format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(format);
            speaker.start();
            System.out.println("Start speaker, buffer size = "+ speaker.getBufferSize());
            while(isActive()){
                DataInputStream dataInput = new DataInputStream(getSocket().getInputStream());
                byte audioBytes [] = new byte[INPUT_BLOCK_SIZE];
                int bytesReaded = dataInput.read(audioBytes, 0, audioBytes.length);
                if(bytesReaded != -1){
                    System.out.println("Listening...");
                    speaker.write(audioBytes, 0, bytesReaded);
                }
            }
            speaker.close();
        } catch (LineUnavailableException | IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (getSocket() != null){
                try {
                    getSocket().close();
                } catch (IOException ex) {
                     Logger.getLogger(Speaker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    
        }
    }
}
