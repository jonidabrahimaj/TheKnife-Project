/**
 * Progetto: TheKnife (Lab A)
 * Autori: [NOME COGNOME 1, Matricola, Sede]; [NOME COGNOME 2, Matricola, Sede] ...
 * Corso: Laboratorio Interdisciplinare A (a.a. 2024/2025)
 */
package theknife;

public class Ristorante {
    private double prezzoMedio;

    private String nome;
    private String indirizzo;
    private String citta;
    private String tipologiaCucina;
    private String fasciaPrezzo;
    private boolean delivery;
    private boolean prenotazioneOnline;

    private double mediaStelle;
    private int numeroRecensioni;

    public Ristorante(String nome, String indirizzo, String citta, String tipologiaCucina, String fasciaPrezzo,
                      boolean delivery, boolean prenotazioneOnline) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.tipologiaCucina = tipologiaCucina;
        this.fasciaPrezzo = fasciaPrezzo;
        this.delivery = delivery;
        this.prenotazioneOnline = prenotazioneOnline;
        this.mediaStelle = 0.0;
        this.numeroRecensioni = 0;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getCitta() {
        return citta;
    }

    public String getTipologiaCucina() {
        return tipologiaCucina;
    }

    public String getFasciaPrezzo() {
        return fasciaPrezzo;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public boolean isPrenotazioneOnline() {
        return prenotazioneOnline;
    }

    public double getMediaStelle() {
        return mediaStelle;
    }

    public int getNumeroRecensioni() {
        return numeroRecensioni;
    }

    public void aggiornaValutazione(double nuovaMedia, int nuoveRecensioni) {
        this.mediaStelle = nuovaMedia;
        this.numeroRecensioni = nuoveRecensioni;
    }

    public String toString() {
        return nome + " - " + citta + " - " + tipologiaCucina + " - Prezzo: " + fasciaPrezzo
                + " - Delivery: " + (delivery ? "Sì" : "No")
                + " - Prenotazione Online: " + (prenotazioneOnline ? "Sì" : "No")
                + " - Media Stelle: " + mediaStelle + " (" + numeroRecensioni + " recensioni)";
    }

    public double getPrezzoMedio() { return prezzoMedio; }
    public void setPrezzoMedio(double prezzoMedio) { this.prezzoMedio = prezzoMedio; }

}
