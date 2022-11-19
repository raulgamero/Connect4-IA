package edu.epsevg.prop.lab.c4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author raulg
 */
public class C4Panther
  implements Jugador, IAuto
{
    private String nom;
    
    private int prof, cont_jugades = 0, color;
    public int[][] taulaHeurisitca = null;
    private Timer timer;
    
    public C4Panther(int profunditat)
    {
        nom = "C4Panther";
        prof = profunditat;
        timer = new Timer(5500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("TIME OUT");
                //variable_Booleana_De_La_Clase = true; //en el minimax poner un semaforo de que si esto esta activo devuelva la mejor que tenga actualmente
            }
        });

    }
    
    public String nom()
    {
        return nom;
    }
    
    /**
     * Método que retorna la siguiente columna en la que pondremos nuestra ficha.
     * @param t tablero actual.
     * @param color hace referencia al color de la ficha.
     * @return nextCol devuelve la columna decidida para poner la ficha.
     */
    @Override
    public int moviment(Tauler t, int color)
    {
        // empezamos el contador para que no pase de 1 minuto
        //timer.start();

        // creamos tabla dinamica para la heuristica y hacemos print
        if (taulaHeurisitca == null) {
            this.color = color;
            creaTaulaHeurisitca(t);
        }

        int columna = miniMax(t, prof);
        System.out.println("Jugades explorades = "+ cont_jugades);
        
        //timer.stop();

        return columna;
    }
    
    private int miniMax(Tauler t, int profu) {
        int max = -10000;
        int mejor_mov = -1;
        int alpha = -10000;
        int beta = 10000;

        for (int i = 0; i < t.getMida(); i++) {
            if (t.movpossible(i)) {
                if (mejor_mov == -1) mejor_mov = i;
                Tauler t_aux = new Tauler(t);
                t_aux.afegeix(i, color);
                int valorNou = min(t_aux, i, profu-1, alpha, beta);
                if(valorNou > max){
                    max = valorNou;
                    mejor_mov = i;
                }
            }
        }
        
        return mejor_mov;
    }

    private int min(Tauler t_aux, int columna, int profunditat, int alpha, int beta) {
        
        if (t_aux.solucio(columna, color)) return 1000000;
        else if (profunditat == 0 || !(t_aux.espotmoure())) return heu(t_aux, columna);
        
        int min_value = 10000;
        
        for (int i = 0; i < t_aux.getMida(); i++) {
            if (t_aux.movpossible(i)) {
                Tauler t_min = new Tauler(t_aux);
                t_min.afegeix(i, color*-1);
                min_value = Math.min(max(t_min, i, profunditat-1, alpha, beta), min_value);
                beta = Math.min(beta, min_value);
                // poda alpha-beta
                if (alpha >= beta) break;
            }
        }
        
        return min_value;
    }

    private int max(Tauler t_aux, int columna, int profunditat, int alpha, int beta) {
        
        if (t_aux.solucio(columna, color*-1)) return -1000000;
        else if (profunditat == 0 || !(t_aux.espotmoure())) return heu(t_aux, columna);
        
        int max_value = -10000;
        
        for (int i = 0; i < t_aux.getMida(); i++) {
            if (t_aux.movpossible(i)) {
                Tauler t_max = new Tauler(t_aux);
                t_max.afegeix(i, color);
                max_value = Math.max(min(t_max, i, profunditat-1, alpha, beta), max_value);
                alpha = Math.max(alpha, max_value);
                // poda alpha-beta
                if (alpha >= beta) break;
            }
        }
    
        return max_value;
    }
    
    public int heu(Tauler pt, int columna){
        cont_jugades = cont_jugades + 1;

        int valorHeur = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pt.getColor(i, j) == color) {
                    valorHeur += taulaHeurisitca[i][j];
                } else if (pt.getColor(i, j) == color*-1) {
                    valorHeur -= taulaHeurisitca[i][j];
                }
            }
        }
        return valorHeur;

    }
        
    
    private void creaTaulaHeurisitca (Tauler t) {
        /*taulaHeurisitca = new int [][] {
        {3, 4, 5, 7, 7, 5, 4, 3},
        {4, 6, 8,10,10, 8, 6, 4},
        {5, 8,11,13,13,11, 8, 5},
        {7,10,13,16,16,13,10, 7},
        {7,10,13,16,16,13,10, 7},
        {5, 8,11,13,13,11, 8, 5},
        {4, 6, 8,10,10, 8, 6, 4},
        {3, 4, 5, 7, 7, 5, 4, 3}
        };*/
        /*taulaHeurisitca = new int[t.getMida()][t.getMida()];
        for (int i = 0; i < t.getMida(); i++) {
            for (int j = 0; j < t.getMida(); j++) {
                if(i < t.getMida()/2){
                    if(j < t.getMida()/2){
                        taulaHeurisitca[i][j] = (i*2)+(j*2)+1;
                    }else{
                        taulaHeurisitca[i][j] = (i*2)+((t.getMida()-j-1)*2)+1;
                    }
                }else{
                    if(j < t.getMida()/2){
                        taulaHeurisitca[i][j] = ((t.getMida()-i-1)*2)+(j*2)+1;
                    }else{
                        taulaHeurisitca[i][j] = ((t.getMida()-i-1)*2)+((t.getMida()-j-1)*2)+1;
                    }
                }
            }
        }*/
        taulaHeurisitca = new int[t.getMida()][t.getMida()];
        int ini = 2;
        for (int i = 0; i < t.getMida(); i++) {
            for (int j = 0; j < t.getMida(); j++) {
                    if(j < t.getMida()/2){
                        taulaHeurisitca[j][i] = ini+=1;
                    }
                    else if (j == t.getMida()/2) {
                        taulaHeurisitca[j][i] = ini;
                    }
                    else{
                        taulaHeurisitca[j][i] = ini-=1;
                    }   
            }
            if(i < t.getMida()/2) ini +=1;
            else ini -= i-1;
        }
        for (int i = 0; i < t.getMida(); i++) {
            for (int j = 0; j < t.getMida(); j++) {
                System.out.print(taulaHeurisitca[i][j] + " ");
            }
            System.out.println("");
        }

    }
        
}
