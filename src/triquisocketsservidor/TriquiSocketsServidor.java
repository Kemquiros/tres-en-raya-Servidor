/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triquisocketsservidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author esteban.catanoe
 */
public class TriquiSocketsServidor implements Runnable {

    private int puerto = 3020;
    private final int noConexiones = 2;
    private final LinkedList<Socket> usuarios = new LinkedList<>();
    private final Boolean turno = true;
    private final int matriz[][] = new int[3][3];
    private int turnos = 1;
    PantallaServidor pantallaServidor;

    public TriquiSocketsServidor(int _puerto, PantallaServidor _pantallaServidor) {
        this.puerto = _puerto;
        this.pantallaServidor = _pantallaServidor;
    }
    /*
     public static void main(String[] args) {
     // TODO code application logic here
     TriquiSocketsServidor servidor= new TriquiSocketsServidor();
     servidor.escuchar();
     }
     */

     //Funcion para que el servidor empieze a recibir conexiones de clientes
    //El servidor escucha por posibles clientes
    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    matriz[i][j] = -1;
                }
            }
            
            ServerSocket servidor = null;
            try {
                servidor = new ServerSocket(puerto, noConexiones);
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "El servidor no puede iniciar:" + e.getMessage());
                System.exit(0);
            }

            pantallaServidor.enviarMensaje("Escuchando en ip:" + servidor.getInetAddress() + " puerto:" + servidor.getLocalPort());
            pantallaServidor.enviarMensaje("Esperando jugadores...");

            while (true) {
                Socket cliente = servidor.accept();
                pantallaServidor.enviarMensaje("Se conecta ip:" + cliente.getInetAddress() + " puerto:" + cliente.getPort());
                usuarios.add(cliente);
                    int fig = turnos % 2 == 0 ? 1 : 0;
                    turnos++;
                    Runnable  run = new ServidorHilo(cliente,usuarios,fig,matriz);
                    Thread hilo = new Thread(run);
                    hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
