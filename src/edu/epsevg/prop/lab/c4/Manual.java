package edu.epsevg.prop.lab.c4;

/**
 * Jugador Manual (sobre la UI)
 * @author Profe
 */
public class Manual implements Jugador{
    public int[][] tablaPuntuacio;
    @Override
    public int moviment(Tauler t, int color) {
        creaTaulaHeurisitca(t.getMida());
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String nom() {
        return "Manual";
    }
    
    public void creaTaulaHeurisitca (int t) {
        int ini = 3;
        for (int i=0;i<t;i++){
            for (int j=0;j<t;j++){
                tablaPuntuacio[i][j] = 3;
              System.out.print(tablaPuntuacio[i][j]);  
            }
            System.out.println("");
        }
    }
}
