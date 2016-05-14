/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucb.sistemasdistribuidos.services;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucianoalmeida
 */
public class Talk implements Observer,Closeable{
    private Socket client;
    private Socket otherClient;

    public Talk(Socket client, Socket otherClient) {
        this.client = client;
        this.otherClient = otherClient;
    }

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
    
    public void start(){
        startTransferFromClientToOther(getClient(), getOtherClient());
        startTransferFromClientToOther(getOtherClient(), getClient());
    }
    
    private void startTransferFromClientToOther(Socket from, Socket to){
        ClientWritter writter = new ClientWritter(from, to);
        writter.addObserver(this);
        Thread t = new Thread(writter);
        t.start();
    }
    
    public boolean containsClient(Socket client){ 
        if(getClient() != null && getClient().equals(client)){
            return true;
        }else if (getOtherClient() != null && getOtherClient().equals(client)){
            return true;
        }
        return false; 
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            close();
        } catch (IOException ex) {
            Logger.getLogger(Talk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("closing talk");
        if(getClient()!= null ){
            getClient().close();
        }
        if(getOtherClient()!= null){
           getOtherClient().close();
        }
        
        
    }

    
    
    
}
