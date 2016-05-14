/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucb.sistemasdistribuidos.main;

import br.ucb.sistemasdistribuidos.services.Talk;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucianoalmeida
 */
public class Main {
    public static void main(String args[]){
        try {
            ServerSocket server = new ServerSocket(2000);
            System.out.println("Server started at " + new Date());
            Socket waitingClient = null;
            while(true){
                Socket socket = server.accept();
                System.out.println("Cliente connected.");
                
                if(waitingClient == null || waitingClient.isClosed()){
                    waitingClient = socket;
                    System.out.println("Cliente waiting.");
                    
                }else{
                    System.out.println("Talking initiated.");
                    
                    Talk talk = new Talk(socket, waitingClient);
                   // manager.addTalk(talk);
                    talk.start();
                    
                    waitingClient = null;
                }
            }
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
