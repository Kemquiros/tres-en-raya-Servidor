/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triquisocketsservidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author usuario
 */
public class TriquiSocketsServidor {
    //Inicializamos el puerto
    private final int puerto = 2027;
    //Numero maximo de conexiones (el tictactoe es un juego para 2)
    private final int noConexiones = 2;
    //Creamos una lista de sockets para guardar el socket de cada jugador
    private final LinkedList<Socket> usuarios = new LinkedList<>();
    //Variable para controlar el turno de cada jugador
    private final Boolean turno = true;
    //Matriz donde se guardan los movimientos 
    private final int G[][] = new int[3][3];
    //Numero de veces que se juega...para controlar las X y O
    private int turnos = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TriquiSocketsServidor servidor= new TriquiSocketsServidor();
        servidor.escuchar();
    }
    
     //Funcion para que el servidor empieze a recibir conexiones de clientes
    public void escuchar(){
        try {
            //Inicializamos la matriz del juego con -1
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    G[i][j] = -1;
                }
            }
            //Creamos el socket servidor
            ServerSocket servidor = new ServerSocket(puerto,noConexiones);
            //Ciclo infinito para estar escuchando por nuevos jugadores
            System.out.println("Esperando jugadores....");
            while(true){
                    //Cuando un jugador se conecte guardamos el socket en nuestra lista
                    Socket cliente = servidor.accept();
                    //Se agrega el socket a la lista
                    usuarios.add(cliente);
                    //Se le genera un turno X o O 
                    int xo = turnos % 2 == 0 ? 1 : 0;
                    turnos++;
                    //Instanciamos un hilo que estara atendiendo al cliente y lo ponemos a escuchar
                    Runnable  run = new ServidorHilo(cliente,usuarios,xo,G);
                    Thread hilo = new Thread(run);
                    hilo.start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
