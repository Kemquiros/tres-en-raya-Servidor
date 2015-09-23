/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triquisocketsservidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author esteban.catanoe
 */
public class SocketServidor implements Runnable {

    private ServerSocket servidor = null;
    private int puerto = 3020;
    private final int noConexiones = 2;
    private final LinkedList<Socket> usuarios = new LinkedList<>();
    private final Boolean turno = true;
    //private final int matriz[][] = new int[3][3];
    private int turnos = 1;
    private ArrayList partidas;
    ServidorMenu pantallaServidor;
    int[] primos = new int[1001];
    String[] letras={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};

    public SocketServidor(int _puerto, ServidorMenu _pantallaServidor) {
        this.puerto = _puerto;
        this.pantallaServidor = _pantallaServidor;
        generarClaves();
    }
    
    //Funcion que cierra el socket del servdiro
    public void cerrarServidor(){
        try {
            servidor.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pantallaServidor, "Error al cerrar el servidor: "+e.getMessage());
        }
        
    }

     //Funcion para que el servidor empieze a recibir conexiones de clientes
    //El servidor escucha por posibles clientes
    @Override
    public void run() {
        try {
            
            
            
            try {
                servidor = new ServerSocket(puerto, noConexiones);
                
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "El servidor no puede iniciar:" + e.getMessage());
                return;
            }

            pantallaServidor.enviarMensaje("Escuchando en ip:" + servidor.getInetAddress() + " puerto:" + servidor.getLocalPort());
            pantallaServidor.enviarMensaje("Esperando jugadores...");

            while (true) {
                Socket cliente = servidor.accept();
                pantallaServidor.enviarMensaje("Se conecta ip:" + cliente.getInetAddress() + " puerto:" + cliente.getPort());
                
                usuarios.add(cliente);
                if(usuarios.size()>=2){
                    //int fig = turnos % 2 == 0 ? 1 : 0;
                    //turnos++;
                    //Recupera y remueve a los dos primeros usuarios de la lista
                    //FIFO (First Input First Output)
                    Socket usuarioA=usuarios.pollFirst();
                    Socket usuarioB=usuarios.pollFirst();
                    Runnable  partida = new ServidorHilo(usuarioA,usuarioB,primos,letras);
                    Thread hilo = new Thread(partida);
                    hilo.start();
                }
                    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean esPrimo(int numero){
      int contador = 2;
      boolean primo=true;
      while ((primo) && (contador!=numero)){
        if (numero % contador == 0)
          primo = false;
        contador++;
      }
      return primo;
    }
    
    void generarClaves(){
        //Se calculan los 1000 primeros numeros primos
        int num=0;
        int i=2;
        while(num<1000){
            if(esPrimo(i)){
                primos[num]=i;
                num++;
            }
            i++;
        }
        //26 Letras
        //10 Números
        //36 caracteres en total
        //letras
    }
}
