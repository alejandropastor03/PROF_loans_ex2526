package es.upm.grise.profundizacion;

import java.util.Objects;

public class LoanApprovalService {

    /**
     * Método X: contiene decisiones encadenadas y compuestas para análisis estructural.
     *
     * Regla (simplificada):
     * - Entradas inválidas -> excepción
     * - Score < 500 -> REJECTED
     * - 500..649 -> si income alto y no tiene impagos -> MANUAL_REVIEW; si no -> REJECTED
     * - >= 650 -> si amount <= income*8 -> APPROVED; si no -> MANUAL_REVIEW
     * - Además, si el cliente es VIP y score>=600 y no tiene impagos -> se eleva a APPROVED si estaba en MANUAL_REVIEW
     */
    public Decision evaluateLoan(
            Applicant applicant,
            int amountRequested,
            int termMonths
    ) {
        // Nodo 1: Inicio del código

        // Nodo 2: Validación de entradas
        validate(applicant, amountRequested, termMonths);

        // Nodo 3: Inicialización de variables
        int score = applicant.creditScore();
        boolean hasDefaults = applicant.hasRecentDefaults();
        int income = applicant.monthlyIncome();

        Decision decision;

        // Nodo 4: Decisión score < 500
        if (score < 500) {
            // Nodo 5: Rechazo
            decision = Decision.REJECTED;
        } 
        // Nodo 6: Decisión score < 650
        else if (score < 650) {
            // Nodo 7: Decisión income >= 2500 && !hasDefaults
            if (income >= 2500 && !hasDefaults) {
                // Nodo 8: Revisión manual
                decision = Decision.MANUAL_REVIEW;
            } else {
                // Nodo 9: Rechazo
                decision = Decision.REJECTED;
            }
        } 
        else {
            // Nodo 10: Decisión amountRequested <= income * 8
            if (amountRequested <= income * 8) {
                // Nodo 11: Aprobado
                decision = Decision.APPROVED;
            } else {
                // Nodo 12: Revisión manual
                decision = Decision.MANUAL_REVIEW;
            }
        }

        // Nodo 13: Decisión compuesta para cliente VIP
        if (decision == Decision.MANUAL_REVIEW
                && applicant.isVip()
                && score >= 600
                && !hasDefaults) {
            // Nodo 14: Cambio a aprobado
            decision = Decision.APPROVED;
        }

        // Nodo 15: Devolución de la decisión final
        return decision;
    }


    private void validate(Applicant applicant, int amountRequested, int termMonths) {
        Objects.requireNonNull(applicant, "applicant cannot be null");
        if (amountRequested <= 0) {
            throw new IllegalArgumentException("amountRequested must be > 0");
        }
        if (termMonths < 6 || termMonths > 84) {
            throw new IllegalArgumentException("termMonths must be between 6 and 84");
        }
        if (applicant.monthlyIncome() <= 0) {
            throw new IllegalArgumentException("monthlyIncome must be > 0");
        }
        if (applicant.creditScore() < 0 || applicant.creditScore() > 850) {
            throw new IllegalArgumentException("creditScore must be between 0 and 850");
        }
    }

    public enum Decision {
        APPROVED, MANUAL_REVIEW, REJECTED
    }

    public record Applicant(int monthlyIncome, int creditScore, boolean hasRecentDefaults, boolean isVip) { }
}

