package es.upm.grise.profundizacion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoanApprovalServiceTest {

    private final LoanApprovalService service = new LoanApprovalService();

    @Test
    void camino1_score_menor_500_rechazado() {
        var a = new LoanApprovalService.Applicant(3000, 499, false, false);

        assertEquals(LoanApprovalService.Decision.REJECTED,
                service.evaluateLoan(a, 1000, 12));
    }

    @Test
    void camino2_score_medio_ingresos_altos_revision_manual() {
        var a = new LoanApprovalService.Applicant(2500, 600, false, false);

        assertEquals(LoanApprovalService.Decision.MANUAL_REVIEW,
                service.evaluateLoan(a, 1000, 12));
    }

    @Test
    void camino3_score_medio_ingresos_bajos_rechazado() {
        var a = new LoanApprovalService.Applicant(2000, 600, false, false);

        assertEquals(LoanApprovalService.Decision.REJECTED,
                service.evaluateLoan(a, 1000, 12));
    }

    @Test
    void camino4_score_alto_importe_asumible_aprobado() {
        var a = new LoanApprovalService.Applicant(2000, 650, false, false);

        assertEquals(LoanApprovalService.Decision.APPROVED,
                service.evaluateLoan(a, 16000, 12));
    }

    @Test
    void camino5_score_alto_importe_excesivo_revision_manual() {
        var a = new LoanApprovalService.Applicant(2000, 700, false, false);

        assertEquals(LoanApprovalService.Decision.MANUAL_REVIEW,
                service.evaluateLoan(a, 16001, 12));
    }

    @Test
    void camino6_revision_manual_vip_mejora_a_aprobado() {
        var a = new LoanApprovalService.Applicant(2500, 600, false, true);

        assertEquals(LoanApprovalService.Decision.APPROVED,
                service.evaluateLoan(a, 1000, 12));
    }

}
