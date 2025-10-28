/**
 * Progetto: TheKnife (Lab A)
 * Autori: [NOME COGNOME 1, Matricola, Sede]; [NOME COGNOME 2, Matricola, Sede] ...
 * Corso: Laboratorio Interdisciplinare A (a.a. 2024/2025)
 */
package theknife;

public abstract class Utente {

    protected String username;
    protected String password;
    protected String tipo;

    public Utente(String username, String password, String tipo) {
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    public String getUsername() {
        return username;
    }

    public boolean verificaPassword(String passwordInserita) {
        return password.equals(HashUtil.hashPassword(passwordInserita));
    }

    public String getTipo() {
        return tipo;
    }
}
