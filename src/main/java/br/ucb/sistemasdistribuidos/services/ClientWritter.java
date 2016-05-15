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


public class ClientWritter implements Runnable{

    
    private Talk talk;
    
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

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public int getBufferBlockSize() {
        return bufferBlockSize;
    }

    public void setBufferBlockSize(int bufferBlockSize) {
        this.bufferBlockSize = bufferBlockSize;
    }
    public ClientWritter(Talk talk, Socket client, Socket other){
        setTalk(talk);
        setClient(client);
        setOtherClient(other);
        setActive(true);
        setBufferBlockSize(1024);
    }        
    public ClientWritter(Talk talk, Socket client, Socket other, int bufferBlockSize) {
        this(talk,client, other);
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
                Logger.getLogger(ClientWritter.class.getName()).log(Level.SEVERE, null, ex);
                setActive(false);
                if(getTalk() != null){
                    try {
                        getTalk().close();
                    } catch (IOException ex1) {
                        Logger.getLogger(ClientWritter.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
            
        }
    }
    
}
