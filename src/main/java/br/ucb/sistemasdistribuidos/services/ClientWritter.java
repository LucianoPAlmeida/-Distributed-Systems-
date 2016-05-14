/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucb.sistemasdistribuidos.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucianoalmeida
 */


public class ClientWritter extends Observable implements Runnable{

    private Socket client;
    private Socket otherClient;
    
    private int bufferBlockSize;
    
    private boolean active;
    
    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public Socket getOtherClient() {
        return otherClient;
    }

    public void setOtherClient(Socket otherClient) {
        this.otherClient = otherClient;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }  

    public int getBufferBlockSize() {
        return bufferBlockSize;
    }

    public void setBufferBlockSize(int bufferBlockSize) {
        this.bufferBlockSize = bufferBlockSize;
    }
    public ClientWritter(Socket client, Socket other){
        setClient(client);
        setOtherClient(other);
        setActive(true);
        setBufferBlockSize(1024);
    }        
    public ClientWritter(Socket client, Socket other, int bufferBlockSize) {
        this(client, other);
        setBufferBlockSize(bufferBlockSize);
    }

    
    
    
    @Override
    public void run() {
        while(isActive()){
            DataInputStream dataInput = null;
            DataOutputStream dataOutPut = null;
            try {
                dataInput = new DataInputStream(getClient().getInputStream());
                byte audioBytes [] = new byte[getBufferBlockSize()];
                int bytesReaded = dataInput.read(audioBytes, 0, audioBytes.length);
                
                dataOutPut = new DataOutputStream(getOtherClient().getOutputStream());
                dataOutPut.write(audioBytes, 0, bytesReaded);
            } catch (IOException ex) {
                setActive(false);
                notifyObservers();
                Logger.getLogger(ClientWritter.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
}
