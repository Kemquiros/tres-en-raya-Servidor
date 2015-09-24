/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triquisocketsservidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean turnoA;
    
    private char[] letras;
    private int[] primos;
    
    

    ServidorHilo(Socket usuarioA, Socket usuarioB,int[] _numeros,char[] _letras) {
        System.out.println("Constructor ServidorHilo");
        this.socketA = usuarioA;
        this.socketB = usuarioB;
        
        this.letras=_letras;
        this.primos=_numeros;
        //El orden de inicio es aleatorio
        
        iniciarTurnos();
       
            
        //this.listaUsuarios = usuarios;
        //this.figura = xo;
        vaciarMatriz();
    }
    
    //Encripta un mensaje con una clave
    String encriptar(int clave, String mensaje){
        System.out.println("-------------------Encripta-----------------------");
        System.out.println("Ingresa a encriptar mensaje: "+mensaje);
        mensaje=mensaje.toLowerCase();
        char[] m = mensaje.toCharArray();
        //try {
            
        
        
        //System.out.println("Ingresa a Descomposición prima con clave:"+clave);
        int god = descomposicionPrima(clave);
        //System.out.println("Clave: "+clave+"  DescomPrima: "+god);
        for(int i=0;i<m.length;i++){
            //El separador  lo deja intacto
            if(m[i]!=';'){
               //System.out.println("-Indice original: "+i + " Caracter:"+m[i]);
               int indice=buscarIndice(m[i]);
               if(indice==-1){
                   System.out.println("Error: no se encuentra el indice:"+indice);
                   
               }
                //System.out.print("  Indice busqueda: "+indice+" Caracter Busqueda: "+letras[indice]);
                //Porque hay 36 caracteres
               
                int indice1=((indice+god)%letras.length);
               // System.out.println(" Nuevo indice: "+indice1+" Nuevo Caracter:"+letras[indice1]+" god:"+god+" i+g:"+(indice+god)+" leng:"+letras.length+" mod:" +((indice+god)%letras.length));
                m[i]= letras[indice1]; 
            }
            
        //}
        
        
        //} catch (Exception e) {
        //    System.out.println("Herror:"+e.getMessage());
        }
        System.out.println("Sale de encriptar mensaje: "+new String(m));
        System.out.println("-------------------Encripta-----------------------");
        return new String(m);
    }
    
    int buscarIndice(char c){
        //System.out.println("Busca: "+c);
        for(int i=0;i<letras.length;i++){
            //Encuentra el indice
            //System.out.println("Compara "+letras[i]+" con "+c);
            if(letras[i]==c){
                //System.out.println(">>  Encuentra: "+letras[i]);
                return i;
            }
        }
        return -1;
    }
    
    //Descomposicion en factores primos
    int descomposicionPrima(int clave){
        int sum=0;
        int indice=0;
        //System.out.println(">>>Comienza itracion descomposicion prima:");
        while(clave>1){
            //Si es divisible entre el primo
            //sumelo
            //System.out.println("Clave:"+clave+" Indice:"+indice+"  Primo:"+primos[indice]);
            if(clave%primos[indice]==0){
                sum+=primos[indice];
                //System.out.println("Clave:"+clave+" Indice:"+indice+"  Primo:"+primos[indice]);
                clave=clave/primos[indice];
                
                
            }
            else{//De lo contrario pase al siguiente primo
                //System.out.print("  -FALSE");
                indice++;
            }            
        }
        return sum;
    }
    
    //Genera numero aleatorio no primo
    int generarClave(){
        //System.out.println("Ingresa a generarClave en servidorHilo");
        int num=-1;
        //System.out.println("Generación de clave:");
        while(num==-1){
           // System.out.print("  >Ingresa");
            num=calcularNumeroAleatorioNoPrimo();
            
        }
        //System.out.println("Sale de generarClave en servidorHilo");
        return num;
    }
    
    int calcularNumeroAleatorioNoPrimo(){
        int range = (1000 - 100) + 1;     
        int num=(int)(Math.random() * range) + 100;
        //System.out.print(" >Ingresa a Numero Aleatorio:"+num+"< ");
        if(!esPrimo(num)){
            return num;
        }
        return -1;
    }
    
    public static boolean esPrimo(int numero){
      int contador = 2;
      boolean primo=true;
      while ((primo) && (contador!=numero)){
        if (numero % contador == 0)
          primo = false;
        contador++;
      }
        //System.out.println("Retorna primo:"+primo);
      return primo;
    }
    
  
        
        String desencriptacion(int clave, String mensaje){
        System.out.println("-------------------Desencripta-----------------------");
        System.out.println("<<Ingresa a desencriptar mensaje:"+mensaje);
        mensaje=mensaje.toLowerCase();
        char[] m = mensaje.toCharArray();
        //try {
            
        
        
        //System.out.println("Ingresa a Descomposición prima con clave:"+clave);
        int god = descomposicionPrima(clave);
        //System.out.println("Clave: "+clave+"  DescomPrima: "+god);
        for(int i=0;i<m.length;i++){
            //El separador  lo deja intacto
            if(m[i]!=';'){
               //System.out.println("-Indice original: "+i + " Caracter:"+m[i]);
               int indice=buscarIndice(m[i]);
               if(indice==-1){
                   System.out.println("Error: no se encuentra el indice:"+indice);
                   
               }
                //System.out.print("  Indice busqueda: "+indice+" Caracter Busqueda: "+letras[indice]);
                //Porque hay 36 caracteres
                try {
                     int indice1=((indice+(letras.length-god))%letras.length);
                     if(indice1<0){
                         indice1=letras.length+indice1;
                     }
                     if(indice1>letras.length){
                         indice1=indice1%letras.length;
                     }
                     //System.out.println("Indice 1: "+indice1+"  tamaño letras: "+letras.length);
                     
                     /*try {
                        System.out.println(" Nuevo indice: "+indice1+" Nuevo Caracter:"+letras[indice1]+" god:"+god+" i+g:"+(indice+god)+" leng:"+letras.length+" mod:" +((indice+god)%letras.length));
                        
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error mensaje: "+e.toString());
                    }
                     */
                     try {
                        m[i]= letras[indice1];
                    } catch (Exception e) {
                         JOptionPane.showMessageDialog(null, "Ultimo Error: "+e.toString());
                    }
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "nada que hacer"+e.toString());
                    System.out.println(e.getMessage());
                    System.out.println(e.toString());
                }
                
            }
            
        //}
        
        
        //} catch (Exception e) {
        //    System.out.println("Herror:"+e.getMessage());
        }
        System.out.println("<<Mensaje desencriptado:"+new String(m));
        System.out.println("-------------------Desencripta-----------------------");
        return new String(m);
    }
    
    
    void iniciarTurnos(){
        if(Math.random()<0.5){
            figuraA=1;
            figuraB=0;
            turnoA=true;
           
        }
        else{
            figuraA=0;
            figuraB=1;
            turnoA=false;
            
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Llega al Run del hilo servidor");
            inA = new DataInputStream(socketA.getInputStream());
            outA = new DataOutputStream(socketA.getOutputStream());
            inB = new DataInputStream(socketB.getInputStream());
            outB = new DataOutputStream(socketB.getOutputStream());
            
            String msg = "";
            int clave;
            if(figuraA==1){
                
                msg += "JUEGAS: " + ("X;"+true);
                //System.out.println("A-Sin Enc: "+msg);
                clave= generarClave();
                msg=encriptar(clave, msg.toLowerCase())+";"+clave;
                //System.out.println("A-Enc: "+msg);
                outA.writeUTF(msg);
                msg="";
                msg += "JUEGAS: " + ("O;"+false);
                //System.out.println("B-Sin Enc: "+msg.toLowerCase());
                clave= generarClave();
                msg=encriptar(clave, msg.toLowerCase())+";"+clave;
                //System.out.println("A-Enc: "+msg.toLowerCase());
                outB.writeUTF(msg);
                //System.out.println("Letras tamaño "+letras.length);
            }
            else{
                msg += "JUEGAS: " + ("O;"+true);
                clave= generarClave();
                msg=encriptar(clave, msg.toLowerCase())+";"+clave;
                outA.writeUTF(msg);
                msg="";
                msg += "JUEGAS: " + ("X;"+false);
                clave= generarClave();
                msg=encriptar(clave, msg.toLowerCase())+";"+clave;
                outB.writeUTF(msg);
            }
            

            while (true) {
                if(turnoA){
                //-------------------
                //---------TURNO A
                //-------------------
                if(turno ){
                   System.out.println(">>>>>>>>>>");
                    System.out.println("Espera turno de A");                    
                    System.out.println(">>>>>>>>>>");
                    String recibidosA = inA.readUTF();
                    System.out.println(">>>>>>>>>>");
                    System.out.println("recibe turno de A");                    
                    System.out.println(">>>>>>>>>>");
                    String[] recibidoA = recibidosA.split(";");
                    
                    //--Desencriptación
                    //desencriptacion(clave, recibidoA[0]+";"+recibidoA[1]);
                    //--
                    
                    if (recibidoA.length==2) {
                        vaciarMatriz();
                        msg="reiniciar";
                        clave= generarClave();
                        msg=encriptar(clave, msg)+";"+clave;
                        outA.writeUTF(msg);
                        clave= generarClave();
                        msg="reiniciar";
                        msg=encriptar(clave, msg)+";"+clave;
                        outB.writeUTF(msg);
                        iniciarTurnos();
                    }
                    else {//Flujo normal de juego
                        //String recibidoA[] = recibidosA.split(";");
                        System.out.println(">>Encriptado:"+ recibidoA[0]+";"+recibidoA[1]);
                        recibidoA[0]=desencriptacion(Integer.parseInt(recibidoA[2]), recibidoA[0]);
                        recibidoA[1]=desencriptacion(Integer.parseInt(recibidoA[2]), recibidoA[1]);
                        System.out.println(">>Desencriptado:"+ recibidoA[0]+";"+recibidoA[1]);
                        /*
                         recibido[0] : fila del tablero
                         recibido[1] : columna del tablero
                         recibido[2] : clave
                         */
                        int fila = Integer.parseInt(recibidoA[0]);
                        int columna = Integer.parseInt(recibidoA[1]);


                        /*
                         X : 1
                         O : 0
                         */
                        //System.out.println("Oh yeahh i:"+fila+" j:"+columna+" figura:"+figuraA);
                        matriz[fila][columna] = figuraA;
                        
                        String cad = "";
                        cad += figuraA + ";";
                        cad += fila + ";";
                        cad += columna + ";";

                        boolean ganador = comprobarGanador(figuraA);
                        boolean completo = comprobarTableroCompleto();

                        if (!ganador && !completo) {
                            cad += "ninguno";
                        } else if (!ganador && completo) {
                            cad += "empate";
                        } else if (ganador) {
                            vaciarMatriz();
                            cad += figuraA == 1 ? "X" : "O";
                        }

                        
                        //Propaga el resultado
                        clave= generarClave();
                        System.out.println("++Encripta para A con clave:"+clave);
                        String cad1=encriptar(clave, cad)+";"+clave+";";
                        outA.writeUTF(cad1);
                        clave= generarClave();
                        System.out.println("++Encripta para B con clave:"+clave);
                        cad1=encriptar(clave, cad)+";"+clave+";";
                        outB.writeUTF(cad1);
                        
                    }
                    turno=!turno;
                    
                }
                //-------------------
                //---------TURNO B
                //-------------------
                else{
                    System.out.println(">>>>>>>>>>");
                    System.out.println("Espera turno de B");                    
                    System.out.println(">>>>>>>>>>");
                    String recibidosB = inB.readUTF();
                    System.out.println(">>>>>>>>>>");
                    System.out.println("recibe turno de B");                    
                    System.out.println(">>>>>>>>>>");
                    String[] recibidoB = recibidosB.split(";");
                    if (recibidoB.length==2) {
                        vaciarMatriz();
                        msg="Reiniciar";
                        clave= generarClave();
                        msg=encriptar(clave, msg)+";"+clave;
                        outA.writeUTF(msg);
                        msg="Reiniciar";
                        clave= generarClave();
                        msg=encriptar(clave, msg)+";"+clave;
                        outB.writeUTF(msg);                    
                    }
                    else {//Flujo normal de juego                  
                        //String recibidoB[] = recibidosB.split(";");
                        recibidoB[0]=desencriptacion(Integer.parseInt(recibidoB[2]), recibidoB[0]);
                        recibidoB[1]=desencriptacion(Integer.parseInt(recibidoB[2]), recibidoB[1]);
                        /*
                         recibido[0] : fila del tablero
                         recibido[1] : columna del tablero
                         recibido[2] : clave
                         */
                        int fila = Integer.parseInt(recibidoB[0]);
                        int columna = Integer.parseInt(recibidoB[1]);


                        /*
                         X : 1
                         O : 0
                         */
                        System.out.println("Oh yeahh i:"+fila+" j:"+columna+" figura:"+figuraB);
                        matriz[fila][columna] = figuraB;
                       
                        String cad = "";
                        cad += figuraB + ";";
                        cad += fila + ";";
                        cad += columna + ";";

                        boolean ganador = comprobarGanador(figuraB);
                        boolean completo = comprobarTableroCompleto();

                        if (!ganador && !completo) {
                            cad += "ninguno";
                        } else if (!ganador && completo) {
                            cad += "empate";
                        } else if (ganador) {
                            vaciarMatriz();
                            cad += figuraB == 1 ? "X" : "O";
                        }

                        
                            clave= generarClave();
                            String cad1=encriptar(clave, cad)+";"+clave;
                            outA.writeUTF(cad1);
                            clave= generarClave();
                            cad1=encriptar(clave, cad)+";"+clave;
                            outB.writeUTF(cad1);
                        
                    }
                    
                    turno=!turno;
                    
                }
            }
            else{
                  if(turno ){
                   System.out.println(">>>>>>>>>>");
                    System.out.println("Espera turno de B");                    
                    System.out.println(">>>>>>>>>>");
                    String recibidosB = inB.readUTF();
                    System.out.println(">>>>>>>>>>");
                    System.out.println("recibe turno de B");                    
                    System.out.println(">>>>>>>>>>");
                    String[] recibidoB = recibidosB.split(";");
                    
                    //--Desencriptación
                    //desencriptacion(clave, recibidoA[0]+";"+recibidoA[1]);
                    //--
                    
                    if (recibidoB.length==2) {
                        vaciarMatriz();
                        msg="reiniciar";
                        clave= generarClave();
                        msg=encriptar(clave, msg)+";"+clave;
                        outA.writeUTF(msg);
                        clave= generarClave();
                        msg="reiniciar";
                        msg=encriptar(clave, msg)+";"+clave;
                        outB.writeUTF(msg);
                        iniciarTurnos();
                    }
                    else {//Flujo normal de juego
                        //String recibidoA[] = recibidosA.split(";");
                        System.out.println(">>Encriptado:"+ recibidoB[0]+";"+recibidoB[1]);
                        recibidoB[0]=desencriptacion(Integer.parseInt(recibidoB[2]), recibidoB[0]);
                        recibidoB[1]=desencriptacion(Integer.parseInt(recibidoB[2]), recibidoB[1]);
                        System.out.println(">>Desencriptado:"+ recibidoB[0]+";"+recibidoB[1]);
                        /*
                         recibido[0] : fila del tablero
                         recibido[1] : columna del tablero
                         recibido[2] : clave
                         */
                        int fila = Integer.parseInt(recibidoB[0]);
                        int columna = Integer.parseInt(recibidoB[1]);


                        /*
                         X : 1
                         O : 0
                         */
                        //System.out.println("Oh yeahh i:"+fila+" j:"+columna+" figura:"+figuraA);
                        matriz[fila][columna] = figuraB;
                        
                        String cad = "";
                        cad += figuraB + ";";
                        cad += fila + ";";
                        cad += columna + ";";

                        boolean ganador = comprobarGanador(figuraB);
                        boolean completo = comprobarTableroCompleto();

                        if (!ganador && !completo) {
                            cad += "ninguno";
                        } else if (!ganador && completo) {
                            cad += "empate";
                        } else if (ganador) {
                            vaciarMatriz();
                            cad += figuraB == 1 ? "X" : "O";
                        }

                        
                        //Propaga el resultado
                        clave= generarClave();
                        System.out.println("++Encripta para B con clave:"+clave);
                        String cad1=encriptar(clave, cad)+";"+clave+";";
                        outB.writeUTF(cad1);
                        clave= generarClave();
                        System.out.println("++Encripta para A con clave:"+clave);
                        cad1=encriptar(clave, cad)+";"+clave+";";
                        outA.writeUTF(cad1);
                        
                    }
                    turno=!turno;
                    
                }
                //-------------------
                //---------TURNO A
                //-------------------
                else{
                    System.out.println(">>>>>>>>>>");
                    System.out.println("Espera turno de A");                    
                    System.out.println(">>>>>>>>>>");
                    String recibidosA = inA.readUTF();
                    System.out.println(">>>>>>>>>>");
                    System.out.println("recibe turno de A");                    
                    System.out.println(">>>>>>>>>>");
                    String[] recibidoA = recibidosA.split(";");
                    
                    if (recibidoA.length==2) {
                        vaciarMatriz();
                        msg="Reiniciar";
                        clave= generarClave();
                        msg=encriptar(clave, msg)+";"+clave;
                        outA.writeUTF(msg);
                        msg="Reiniciar";
                        clave= generarClave();
                        msg=encriptar(clave, msg)+";"+clave;
                        outB.writeUTF(msg);                    
                    }
                    else {//Flujo normal de juego                  
                        //String recibidoB[] = recibidosB.split(";");
                        recibidoA[0]=desencriptacion(Integer.parseInt(recibidoA[2]), recibidoA[0]);
                        recibidoA[1]=desencriptacion(Integer.parseInt(recibidoA[2]), recibidoA[1]);
                        /*
                         recibido[0] : fila del tablero
                         recibido[1] : columna del tablero
                         recibido[2] : clave
                         */
                        int fila = Integer.parseInt(recibidoA[0]);
                        int columna = Integer.parseInt(recibidoA[1]);


                        /*
                         X : 1
                         O : 0
                         */
                        System.out.println("Oh yeahh i:"+fila+" j:"+columna+" figura:"+figuraA);
                        matriz[fila][columna] = figuraA;
                       
                        String cad = "";
                        cad += figuraA + ";";
                        cad += fila + ";";
                        cad += columna + ";";

                        boolean ganador = comprobarGanador(figuraA);
                        boolean completo = comprobarTableroCompleto();

                        if (!ganador && !completo) {
                            cad += "ninguno";
                        } else if (!ganador && completo) {
                            cad += "empate";
                        } else if (ganador) {
                            vaciarMatriz();
                            cad += figuraA == 1 ? "X" : "O";
                        }

                        
                            clave= generarClave();
                            String cad1=encriptar(clave, cad)+";"+clave;
                            outA.writeUTF(cad1);
                            clave= generarClave();
                            cad1=encriptar(clave, cad)+";"+clave;
                            outB.writeUTF(cad1);
                        
                    }
                    
                    turno=!turno;
                    
                }  
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
