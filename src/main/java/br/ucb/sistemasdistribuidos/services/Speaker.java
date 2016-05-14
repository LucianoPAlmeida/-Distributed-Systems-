/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucb.sistemasdistribuidos.services;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author lucianoalmeida
 */
public class Speaker implements Runnable{
    
    public static Integer OUTPUT_BLOCK_SIZE = 8000;

    private Socket socket;

    private boolean active;
            
    public Speaker() {
        setActive(true);
    }
    
    public Speaker(Socket socket) {
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
            DataOutputStream out = new DataOutputStream(getSocket().getOutputStream());
            AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
            DataLine.Info microphoneInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine)AudioSystem.getLine(microphoneInfo);
            microphone.open(format);
            microphone.start();
            while(isActive()){
                byte buffer[] = new byte[OUTPUT_BLOCK_SIZE];
                int readedSize =  microphone.read(buffer, 0, buffer.length);
                if(readedSize > 0){
                    System.out.println("writing on socket "+readedSize + " bytes");
                    out.write(buffer, 0, readedSize);

                }
            }
            microphone.close();
            getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(Speaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Speaker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
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
