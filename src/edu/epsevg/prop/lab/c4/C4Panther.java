package edu.epsevg.prop.lab.c4;

/**
 * Clase del jugador.
 * @author Raul i Dario.
 */
public class C4Panther implements Jugador, IAuto
{
    private String nom;
    private int[][] taulaHeurisitca = null;
    private int prof, contJugades = 0, color;
    private final boolean poda = true;
    private int millorHeuristica;
    
    /**
     * Constructor de la clase per el jugador.
     * @param profunditat numero de nodes als que baixarem per predir els moviments (profunditat del minimax).
     */
    public C4Panther(int profunditat)
    {
        nom = "C4Panther";
        prof = profunditat;
    }
    
    /**
     * Getter del nom del jugador.
     * @return el nom del jugador.
     */
    @Override
    public String nom()
    {
        return nom;
    }
    
    /**
     * En el primer moviment creem la taula heuristica depenent de la mida del tauler, i calculem la millor columna per tirar la fitxa.
     * @param t tabler actual.
     * @param color fa referencia al color de la fitxa.
     * @return columna on tirarem la fitxa.
     */
    @Override
    public int moviment(Tauler t, int color)
    {
        // en el primer moviment creem la taula heuristica i ens guardarem el color que tenim en el joc
        if (taulaHeurisitca == null) {
            this.color = color;
            creaTaulaHeurisitca(t);
        }
        
        millorHeuristica = -1000000;
        // cridem el minimax per a que ens retorni una columna
        int columna = miniMax(t, prof);
        // imprimim el numero de jugades que explorem i la millor heuristica que hem trobat
        System.out.println("Jugades explorades = "+ contJugades + ". Millor heuristica = " + millorHeuristica);

        return columna;
    }
    
    /**
     * Funcio on començem el calcul del algoritme Min i Max per trobar la millor heuristica.
     * @param t el tauler del joc.
     * @param profunditat numero de nodes als que baixarem per predir els moviments (profunditat del minimax).
     * @return la columna del el millor moviment depenent de la heuristica que haguem calculat.
     */
    private int miniMax(Tauler t, int profunditat) {
        int millorMov = -1;
        int alpha = -10000;
        int beta = 10000;

        // per cada columna del tauler
        for (int i = 0; i < t.getMida(); i++) {
            // si la tirada a la columna i es pot realitzar
            if (t.movpossible(i)) {
                // creem un tauler auxiliar i li afegim la nova tirada
                Tauler tAux = new Tauler(t);
                tAux.afegeix(i, color);
                // començem el min/max per el min amb profunditat -1 ja que ja hem fet una tirada
                int valorNou = min(tAux, i, profunditat-1, alpha, beta);
                // en cas de que la nova heuristica sigui millor que la anterior, actualitzarem la millor heuristica i la columna del millor moviment
                if(valorNou > millorHeuristica){
                    millorHeuristica = valorNou;
                    millorMov = i;
                }
            }
        }
        
        return millorMov;
    }

    /**
     * Funcio que calcula la menor heuristica del seus nodes fills.
     * @param tAux Tauler auxiliar on s'ha afegit una nova tirada.
     * @param columna Columna on hem realitzat l'ultima tirada.
     * @param profunditat numero de nodes als que baixarem per predir els moviments (profunditat del minimax).
     * @param alpha Paramatre per la poda alpha-beta.
     * @param beta Paramatre per la poda alpha-beta.
     * @return La menor heuristica que ha calculat.
     */
    private int min(Tauler tAux, int columna, int profunditat, int alpha, int beta) {
        // si la tirada realitzada resulta ser una solucio, tornem un valor molt alt per dir que hem guanyat la jugada i sumem 1 al numero de jugades
        if (tAux.solucio(columna, color)) {
            contJugades = contJugades + 1;
            return 100000;
            
        //si no es solucio i hem arribat a la profunditat 0 o ja no tenim mes opcions de tirada, sumarem 1 al numero de jugades i retornarem l'heuristica de la tirada.
        } else if (profunditat == 0 || !(tAux.espotmoure())) {
            contJugades = contJugades + 1;
            return heu(tAux);
        }
        
        int minValue = 10000;
        
        // per cada columna del tauler
        for (int i = 0; i < tAux.getMida(); i++) {
            // si la tirada a la columna i es pot realitzar
            if (tAux.movpossible(i)) {
                // creem un nou tauler auxiliar i li afegim la nova tirada (del rival)
                Tauler tMin = new Tauler(tAux);
                tMin.afegeix(i, color*-1);
                // si el max ens retorna un valor mes petit que el que ja tenim en el min, actualitzem aquest valor.
                minValue = Math.min(max(tMin, i, profunditat-1, alpha, beta), minValue);
                // calculem la beta entre el nou min_value i la beta que ja teniem
                beta = Math.min(beta, minValue);
                // si fem la poda alpha-beta i beta es menor a alpha, no fa falta mirar mes nodes
                if (poda && alpha >= beta) break;
            }
        }
        
        return minValue;
    }

    /**
     * Funcio que calcula la major heuristica del seus nodes fills.
     * @param tAux Tauler auxiliar on s'ha afegit una nova tirada.
     * @param columna Columna on hem realitzat l'ultima tirada.
     * @param profunditat numero de nodes als que baixarem per predir els moviments (profunditat del minimax).
     * @param alpha Paramatre per la poda alpha-beta.
     * @param beta Paramatre per la poda alpha-beta.
     * @return La major heuristica que ha calculat.
     */
    private int max(Tauler tAux, int columna, int profunditat, int alpha, int beta) {
        // si la tirada realitzada resulta ser una solucio, tornem un valor molt baix per dir que hem perdut la jugada i sumem 1 al numero de jugades
        if (tAux.solucio(columna, color*-1)) {
            contJugades = contJugades + 1;
            return -100000;
            
        //si no es solucio i hem arribat a la profunditat 0 o ja no tenim mes opcions de tirada, sumarem 1 al numero de jugades i retornarem l'heuristica de la tirada.
        } else if (profunditat == 0 || !(tAux.espotmoure())) {
            contJugades = contJugades + 1;
            return heu(tAux);
        }
        
        int maxValue = -10000;
        
        // per cada columna del tauler
        for (int i = 0; i < tAux.getMida(); i++) {
            // si la tirada a la columna i es pot realitzar
            if (tAux.movpossible(i)) {
                // creem un nou tauler auxiliar i li afegim la nova tirada
                Tauler tMax = new Tauler(tAux);
                tMax.afegeix(i, color);
                // si el min ens retorna un valor mes gran que el que ja tenim en el max, actualitzem aquest valor.
                maxValue = Math.max(min(tMax, i, profunditat-1, alpha, beta), maxValue);
                // calculem l'alpha entre el nou max_value i l'alpha que ja teniem
                alpha = Math.max(alpha, maxValue);
                // si fem la poda alpha-beta i alpha es major o igual a beta, no fa falta mirar mes nodes
                if (poda && alpha >= beta) break;
            }
        }
        
        return maxValue;
    }
    
    /**
     * Funcio que calcula l'heuristica de la tirada.
     * @param t Tauler on s'ha de calcular l'heuristica de la tirada.
     * @return L'heuristica calculada de la tirada.
     */
    private int heu(Tauler t) {
        int valorHeur = 0;
        // per cada fila
        for (int i = 0; i < 8; i++) {
            // per cada columna
            for (int j = 0; j < 8; j++) {
                // si el color es nostre sumem, si es del rival restem
                if (t.getColor(i, j) == color) {
                    valorHeur += taulaHeurisitca[i][j];
                } else if (t.getColor(i, j) == color*-1) {
                    valorHeur -= taulaHeurisitca[i][j];
                }
            }
        }
        
        return valorHeur;
    }
    
    /**
     * Funcio per crear la taula que ens ajudara a calcular l'heuristica.
     * @param t Tauler de la partida
     */
    private void creaTaulaHeurisitca (Tauler t) {
        // instanciem la taula de la mateixa mida al tauler que juguem
        taulaHeurisitca = new int[t.getMida()][t.getMida()];
        int ini = 2;
        //per cada columna
        for (int i = 0; i < t.getMida(); i++) {
            //per cada fila
            for (int j = 0; j < t.getMida(); j++) {
                    if(j < t.getMida()/2){
                        taulaHeurisitca[j][i] = ini+=1;
                        
                    } else if (j == t.getMida()/2) {
                        taulaHeurisitca[j][i] = ini;
                        
                    } else {
                        taulaHeurisitca[j][i] = ini-=1;
                    }
            }
            if(i < t.getMida()/2) ini +=1;
            else ini -= i-1;
        }
    }
}
