/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucb.sistemasdistribuidos.main;

import br.ucb.sistemasdistribuidos.services.Listener;
import br.ucb.sistemasdistribuidos.services.Speaker;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author lucianoalmeida
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 2000);
            
            //Listener
            Listener listener = new Listener(socket);
            Thread t = new Thread(listener);
            t.start();
            
            //Speaker
            Speaker speaker = new Speaker(socket);
            Thread t2 = new Thread(speaker);
            t2.start();
            
            System.out.println("Waiting for other to talk\nPress enter to disconnect...");
            scanner.nextLine();
            listener.setActive(false);
            speaker.setActive(false);
            socket.close();
            
        } catch (IOException ex) {
            System.out.println("Disconnected");
        }
    }
}
