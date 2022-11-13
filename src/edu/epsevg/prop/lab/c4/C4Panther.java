package edu.epsevg.prop.lab.c4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author raulg
 */
public class C4Panther
  implements Jugador, IAuto
{
    private String nom;
    
    private int prof;
    public int[][] taulaHeurisitca = null;
    
    private Timer timer;
    
    public C4Panther(int profunditat)
    {
        nom = "C4Panther";
        prof = profunditat;
        timer = new Timer(5000, new ActionListener() {
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
        if (taulaHeurisitca == null) {
            creaTaulaHeurisitca(t);
            for (int i = 0; i < t.getMida(); i++) {
                for (int j = 0; j < t.getMida(); j++) {
                    System.out.println(taulaHeurisitca[i][j]);
                }
                System.out.println("");
            }
        }
        
        // empezamos el contador para que no pase de 1 minuto
        timer.start();
        
        // random movement
        int col = (int)(t.getMida() * Math.random());
        while (!t.movpossible(col)) {
            col = (int)(t.getMida() * Math.random());
        }

        timer.stop();
        
        return col;
    }
    
    public void creaTaulaHeurisitca (Tauler t) {
        taulaHeurisitca = new int[t.getMida()][t.getMida()];
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
        }
    }
    
}
