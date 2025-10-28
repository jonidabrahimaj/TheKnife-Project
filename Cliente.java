/**
 * Progetto: TheKnife (Lab A)
 * Autori: [NOME COGNOME 1, Matricola, Sede]; [NOME COGNOME 2, Matricola, Sede] ...
 * Corso: Laboratorio Interdisciplinare A (a.a. 2024/2025)
 */
package theknife;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Utente {
    private List<Ristorante> preferiti;
    private List<Recensione> recensioni;

    public Cliente(String username, String password) {
        super(username, password, "cliente");
        preferiti = new ArrayList<>();
        recensioni = new ArrayList<>();
    }

    public List<Ristorante> getPreferiti() {
        return preferiti;
    }

    public List<Recensione> getRecensioni() {
        return recensioni;
    }
}
