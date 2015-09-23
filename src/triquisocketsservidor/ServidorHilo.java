/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triquisocketsservidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author usuario
 */
public class ServidorHilo implements Runnable{
    private final Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private final int figura;
    private final int matriz[][];
    private boolean turno;
    private LinkedList<Socket> listaUsuarios = new LinkedList<>();

    ServidorHilo(Socket cliente, LinkedList<Socket> usuarios, int xo, int[][] ma) {
        this.socket = cliente;
        this.listaUsuarios = usuarios;
        this.figura = xo;
        this.matriz = ma;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            turno = figura == 1;
            String msg = "";
            msg += "JUEGAS: " + (turno ? "X;" : "O;");
            msg += turno;
            out.writeUTF(msg);

            while (true) {
                String recibidos = in.readUTF();
                if (recibidos.equals("Reiniciar")) {
                    vaciarMatriz();
                    for (Socket usuario : listaUsuarios) {
                        out = new DataOutputStream(usuario.getOutputStream());
                        out.writeUTF("Reiniciar");
                    }
                } else {
                    String recibido[] = recibidos.split(";");

                    /*
                     recibido[0] : fila del tablero
                     recibido[1] : columna del tablero
                
                     */
                    int fila = Integer.parseInt(recibido[0]);
                    int columna = Integer.parseInt(recibido[1]);
                    
                    
                    /*
                     X : 1
                     O : 0
                     */
                    matriz[fila][columna] = figura;

                    String cad = "";
                    cad += figura + ";";
                    cad += fila + ";";
                    cad += columna + ";";

                    boolean ganador = comprobarGanador(figura);
                    boolean completo = comprobarTableroCompleto();

                    if (!ganador && !completo) {
                        cad += "NINGUNO";
                    } else if (!ganador && completo) {
                        cad += "EMPATE";
                    } else if (ganador) {
                        vaciarMatriz();
                        cad += figura == 1 ? "X" : "O";
                    }

                    for (Socket usuario : listaUsuarios) {
                        out = new DataOutputStream(usuario.getOutputStream());
                        out.writeUTF(cad);
                    }
                }
            }
        } catch (Exception e) {

           for (int i = 0; i < listaUsuarios.size(); i++) {
                if (listaUsuarios.get(i) == socket) {
                    listaUsuarios.remove(i);
                    break;
                }
            }
            vaciarMatriz();
        }
    }
    
    public boolean comprobarGanador(int n) {
        for (int i = 0; i < 3; i++) {
            boolean gano = true;
            for (int j = 0; j < 3; j++) {
                gano = gano && (matriz[i][j] == n);
            }
            if (gano) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            boolean gano = true;
            for (int j = 0; j < 3; j++) {
                gano = gano && (matriz[j][i] == n);
            }
            if (gano) {
                return true;
            }
        }

        if (matriz[0][0] == n && matriz[1][1] == n && matriz[2][2] == n) {
            return true;
        }

        return matriz[0][2] == n && matriz[1][1] == n && matriz[2][0] == n;
    }
    
    public boolean comprobarTableroCompleto() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matriz[i][j] == -1) {
                    return false;
                }
            }
        }

        vaciarMatriz();
        return true;
    }

    public void vaciarMatriz() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matriz[i][j] = -1;
            }
        }
    }
    
}
