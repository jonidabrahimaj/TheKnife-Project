/**
 * Progetto: TheKnife (Lab A)
 * Autori: [NOME COGNOME 1, Matricola, Sede]; [NOME COGNOME 2, Matricola, Sede] ...
 * Corso: Laboratorio Interdisciplinare A (a.a. 2024/2025)
 */
package theknife;

import java.util.ArrayList;
import java.util.List;

public class Ristoratore extends Utente {
    private List<Ristorante> ristorantiGestiti;

    public Ristoratore(String username, String password) {
        super(username, password, "ristoratore");
        ristorantiGestiti = new ArrayList<>();
    }

    public List<Ristorante> getRistorantiGestiti() {
        return ristorantiGestiti;
    }
}
