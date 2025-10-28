/**
 * Progetto: TheKnife (Lab A)
 * Autori: [NOME COGNOME 1, Matricola, Sede]; [NOME COGNOME 2, Matricola, Sede] ...
 * Corso: Laboratorio Interdisciplinare A (a.a. 2024/2025)
 */
package theknife;

public class Recensione {

    private String utente;
    private String testo;
    private int stelle;
    private String rispostaRistoratore;

    public Recensione(String utente, String testo, int stelle) {
        this.utente = utente;
        this.testo = testo;
        this.stelle = stelle;
        this.rispostaRistoratore = null;
    }

    public String getUtente() {
        return utente;
    }

    public String getTesto() {
        return testo;
    }

    public int getStelle() {
        return stelle;
    }

    public String getRispostaRistoratore() {
        return rispostaRistoratore;
    }

    public void setRispostaRistoratore(String risposta) {
        this.rispostaRistoratore = risposta;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setStelle(int stelle) {
        this.stelle = stelle;
    }

    public String toString() {
        String base = utente + " - Stelle: " + stelle + " - " + testo;
        if (rispostaRistoratore != null) {
            base += "\n>> Risposta: " + rispostaRistoratore;
        }
        return base;
    }
}
