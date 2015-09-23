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
import javax.swing.JOptionPane;

/**
 *
 * @author usuario
 */
public class ServidorHilo implements Runnable{
    private final Socket socketA;
    private final Socket socketB;
    private DataOutputStream outA;
    private DataInputStream inA;
    private DataOutputStream outB;
    private DataInputStream inB;
    private int figuraA=-1;
    private int figuraB=-1;
    private final int matriz[][]=new int[3][3];
    private boolean turno;
    private LinkedList<Socket> listaUsuarios = new LinkedList<>();

    ServidorHilo(Socket usuarioA, Socket usuarioB,int[] _numeros,String[] _letras) {
        this.socketA = usuarioA;
        this.socketB = usuarioB;
        
        //El orden de inicio es aleatorio
        
        iniciarTurnos();
       
            
        //this.listaUsuarios = usuarios;
        //this.figura = xo;
        vaciarMatriz();
    }
    
    void iniciarTurnos(){
        if(Math.random()<0.5){
            figuraA=1;
            figuraB=0;
            turno=true;
        }
        else{
            figuraA=0;
            figuraB=1;
            turno=false;
        }
    }

    @Override
    public void run() {
        try {
            inA = new DataInputStream(socketA.getInputStream());
            outA = new DataOutputStream(socketA.getOutputStream());
            inB = new DataInputStream(socketB.getInputStream());
            outB = new DataOutputStream(socketB.getOutputStream());
            
            String msg = "";
            if(figuraA==1){
                msg += "JUEGAS: " + ("X;"+true);
                outA.writeUTF(msg);
                msg="";
                msg += "JUEGAS: " + ("O;"+false);
                outB.writeUTF(msg);
            }
            else{
                msg += "JUEGAS: " + ("O;"+true);
                outA.writeUTF(msg);
                msg="";
                msg += "JUEGAS: " + ("X;"+false);
                outB.writeUTF(msg);
            }
            

            while (true) {
                //-------------------
                //---------TURNO A
                //-------------------
                if(turno){
                   
                    String recibidosA = inA.readUTF();
                    
                    if (recibidosA.equals("Reiniciar")) {
                        vaciarMatriz();                    
                        outA.writeUTF("Reiniciar");
                        outB.writeUTF("Reiniciar");
                        iniciarTurnos();
                    }
                    else {//Flujo normal de juego
                        String recibidoA[] = recibidosA.split(";");
                        

                        /*
                         recibido[0] : fila del tablero
                         recibido[1] : columna del tablero

                         */
                        int fila = Integer.parseInt(recibidoA[0]);
                        int columna = Integer.parseInt(recibidoA[1]);


                        /*
                         X : 1
                         O : 0
                         */
                        matriz[fila][columna] = figuraA;
                        System.out.println("Oh yeahh i:"+fila+" j:"+columna+" figura:"+figuraA);
                        String cad = "";
                        cad += figuraA + ";";
                        cad += fila + ";";
                        cad += columna + ";";

                        boolean ganador = comprobarGanador(figuraA);
                        boolean completo = comprobarTableroCompleto();

                        if (!ganador && !completo) {
                            cad += "NINGUNO";
                        } else if (!ganador && completo) {
                            cad += "EMPATE";
                        } else if (ganador) {
                            vaciarMatriz();
                            cad += figuraA == 1 ? "X" : "O";
                        }

                        
                        //Propaga el resultado
                        outA.writeUTF(cad);                           
                        outB.writeUTF(cad);
                        
                    }
                    turno=false;
                }
                //-------------------
                //---------TURNO B
                //-------------------
                else{
                    
                    String recibidosB = inB.readUTF();
                   
                    if (recibidosB.equals("Reiniciar")) {
                        vaciarMatriz();                    
                        outA.writeUTF("Reiniciar");
                        outB.writeUTF("Reiniciar");                    
                    }
                    else {//Flujo normal de juego                  
                        String recibidoB[] = recibidosB.split(";");

                        /*
                         recibido[0] : fila del tablero
                         recibido[1] : columna del tablero

                         */
                        int fila = Integer.parseInt(recibidoB[0]);
                        int columna = Integer.parseInt(recibidoB[1]);


                        /*
                         X : 1
                         O : 0
                         */
                        matriz[fila][columna] = figuraB;
                       
                        String cad = "";
                        cad += figuraB + ";";
                        cad += fila + ";";
                        cad += columna + ";";

                        boolean ganador = comprobarGanador(figuraB);
                        boolean completo = comprobarTableroCompleto();

                        if (!ganador && !completo) {
                            cad += "NINGUNO";
                        } else if (!ganador && completo) {
                            cad += "EMPATE";
                        } else if (ganador) {
                            vaciarMatriz();
                            cad += figuraB == 1 ? "X" : "O";
                        }

                        
                            
                            outA.writeUTF(cad);
                            outB.writeUTF(cad);
                        
                    }
                    
                    turno=true;
                }
                //---------------------------------------------------             
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
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
