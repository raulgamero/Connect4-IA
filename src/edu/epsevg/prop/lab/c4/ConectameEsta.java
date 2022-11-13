package edu.epsevg.prop.lab.c4;

import static java.lang.Math.random;
import java.util.Random;

/**
 * Jugador creado para jugar automáticamente a Connecta 4, PROP 2021-2022
 * @author Lucas Efrain Espinola y Javier Delgado
 */
public class ConectameEsta
  implements Jugador, IAuto
{
    private String nom;

    private int colorNostre=1;
    private int prof;
    private boolean primerMove;
    private int contaNodes;
  
    /**
     * Tabla que define las puntuaciones de las posiciones del tablero, puntuaciones
     * escogidas dependiendo del número de combinaciones que se pueden realizar
     * para conectar 4 fichas y ganar.
     */
    public int[][] tablaPuntuacio = {
        {3, 4, 5, 7, 7, 5, 4, 3},
        {4, 6, 8,10,10, 8, 6, 4},
        {5, 8,11,13,13,11, 8, 5},
        {7,10,13,16,16,13,10, 7},
        {7,10,13,16,16,13,10, 7},
        {5, 8,11,13,13,11, 8, 5},
        {4, 6, 8,10,10, 8, 6, 4},
        {3, 4, 5, 7, 7, 5, 4, 3}
    }
                                                                ;
     /**
     * Constructor del jugador automático.
     * @param pprof define la profundidad máxima que puede alcanzar nuestro árbol
     * de búsqueda.
     */
      public ConectameEsta(int pprof)
      {
        nom = "ConectameEsta3";
        prof = pprof;
        contaNodes = 0;
        primerMove = true;
      }

    /**
     * Método que retorna la siguiente columna en la que pondremos nuestra ficha.
     * @param t tablero actual.
     * @param color hace referencia al color de la ficha.
     * @return nextCol devuelve la columna decidida para poner la ficha.
     */
      public int moviment(Tauler t, int color)
      {
        colorNostre = color;
        int nextCol = minimax(t,prof);
        System.out.println("Nodes total = "+ contaNodes);
        return nextCol;
      }

     /**
     * Método que nos permite saber el nombre de nuestro jugador.
     * @return nombre del jugador.
     */
      public String nom()
      {
        return nom;
      }

    /**
     * Función que inicia la ejecución del algoritmo minimax y decide la siguiente
     * columna en la que pondremos nuestra ficha.
     * @param pt Tablero actual, sería la raíz de nuestro árbol.
     * @param pprof profundiad máxima a la que llegará nuestro algoritmo creando
     * el árbol.
     * @return retornamos la columna siguiente en la que pondremos nuestra ficha.
     */
    public int minimax(Tauler pt, int pprof){
        int valor = Integer.MIN_VALUE;
        int col = 0;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        for (int i = 0; i < 8; i++){
            if(pt.movpossible(i)){

                Tauler move = new Tauler(pt);
                move.afegeix(i, colorNostre);
                int valorNou = movMin(move, i ,pprof-1,alpha,beta);
                if(valorNou > valor){
                    valor = valorNou;
                    col = i;
                }

            } 
        }  
        return col;
    }

    /**
     * Función que nos devuelve el movimiento con mayor valor heurístico de todos los
     * movimientos estudiados.
     * @param pt tablero resultante de poner una ficha en una determinada columna.
     * @param lastcol columna en la que hemos puesto ficha para estudiar el tablero.
     * @param pprof número de niveles restantes que le queda al algoritmo por
     * analizar.
     * @param alpha variable que determina el alfa para realizar la poda alfa-beta.
     * @param beta variable que determina la beta para realizar la poda alfa-beta.
     * @return retorna el valor heurístico máximo entre todas las posibilidades
     * comprobadas.
     */
    public int movMax(Tauler pt, int lastcol ,int pprof,int alpha,int beta){

        if(pt.solucio(lastcol,colorNostre*-1)){
            return -1000000;
        }else if(pt.solucio(lastcol,colorNostre)){
               return 1000000;
        }else if (pprof == 0 || !(pt.espotmoure())){
            return Heuristica(pt);
        }

        int value = Integer.MIN_VALUE;
        for(int i = 0; i < 8; i++){
            if(pt.movpossible(i)){

                Tauler move = new Tauler(pt);
                move.afegeix(i,colorNostre);
                value = Math.max(value, movMin(move,i,pprof-1,alpha ,beta));
                alpha = Math.max(value,alpha);
                if(alpha>=beta)
                {
                    break;
                }
             }
        }
        return value;
    }

    /**
     * Función que nos devuelve el movimiento con menor valor heurístico de todos los
     * movimientos estudiados.
     * @param pt tablero resultante de poner una ficha en una determinada columna.
     * @param lastcol columna en la que hemos puesto ficha para estudiar el tablero.
     * @param pprof número de niveles restantes que le queda al algoritmo por
     * analizar.
     * @param alpha variable que determina el alfa para realizar la poda alfa-beta.
     * @param beta variable que determina la beta para realizar la poda alfa-beta.
     * @return retorna el valor heurístico máximo entre todas las posibilidades
     * comprobadas.
     */

    public int movMin(Tauler pt,int lastcol, int pprof,int alpha, int beta){
        if(pt.solucio(lastcol,colorNostre*-1)){
           return -100000;
        }else if(pt.solucio(lastcol,colorNostre)){
               return 100000;
        }else if (pprof == 0 || !(pt.espotmoure())){
            return Heuristica(pt);
        }

        int value = Integer.MAX_VALUE;
        for(int i = 0; i < 8; i++){
            if(pt.movpossible(i)){
                Tauler move = new Tauler(pt);
                move.afegeix(i, colorNostre*-1);
                value = Math.min(value, movMax(move,i,pprof -1,alpha,beta));
                beta = Math.min(value,beta);
                if(alpha>=beta)
                {
                    break;
                }
             }
        }
        return value;
    }

    /**
     * Método que calcula el valor heurístico de un tablero dado, cuyo cálculo
     * se realiza sumando el valor de la tabla de valores de una posición fila-columna
     * si nuestra ficha se encuentra en esa posición dentro del tablero, en caso contrario
     * si es la del rival, el valor de la tabla se resta al valor heurístico final.
     * @param pt tablero resultante de realizar los movimientos posibles antes
     * de sobrepasar la profundidad.
     * @return retorna un valor heurístico calculado dependiendo de si ganamos o
     * perdemos.
     */

    public int Heuristica(Tauler pt){
        contaNodes = contaNodes + 1;

        int valorHeur = 0;
        for (int i = 0; i < 8; i++) {
            if(pt.getColor(i, 0)==0 && pt.getColor(i,1)==0  &&
                pt.getColor(i, 2)==0 && pt.getColor(i,3)==0 &&
                 pt.getColor(i, 4)==0 && pt.getColor(i,5)==0 &&
                     pt.getColor(i, 6)==0 && pt.getColor(i,7)==0){
                break;
            }
            for (int j = 0; j < 8; j++) {
                if (pt.getColor(i, j) == colorNostre) {
                    valorHeur += tablaPuntuacio[i][j];
                } else if (pt.getColor(i, j) == colorNostre*-1) {
                    valorHeur -= tablaPuntuacio[i][j];
                }
            }
        }
        return valorHeur;

    }
}
