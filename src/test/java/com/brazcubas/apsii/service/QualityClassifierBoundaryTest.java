package com.brazcubas.apsii.service;

import com.brazcubas.apsii.config.QualityRules.Quality;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Bordas de limiar, valores extremos e entradas inválidas de {@link QualityClassifier}.
 * Limiares (QualityRules): RTT 60 ms / 150 ms; perda 1,0 % / 5,0 %.
 */
class QualityClassifierBoundaryTest {

    private static final String INVALID_METRICS_MESSAGE =
            "Prediction metrics must be finite and non-negative";

    private final QualityClassifier classifier = new QualityClassifier();

    // ---------- Bordas de limiar ----------

    @Test
    @DisplayName("RTT exatamente no limiar moderado (60 ms) é MODERATE: limiar inclusivo")
    void rttNoLimiarModeradoEhModerate() {
        // Arrange
        double rtt = 60.0;
        double packetLoss = 0.0;

        // Act
        String result = classifier.classify(rtt, packetLoss);

        // Assert
        assertEquals("MODERATE", result);
    }

    @Test
    @DisplayName("RTT logo abaixo do limiar moderado (59,99 ms) continua GOOD")
    void rttLogoAbaixoDoLimiarModeradoEhGood() {
        double rtt = 59.99;
        double packetLoss = 0.0;

        String result = classifier.classify(rtt, packetLoss);

        assertEquals("GOOD", result);
    }

    @Test
    @DisplayName("RTT exatamente no limiar instável (150 ms) é UNSTABLE: limiar inclusivo")
    void rttNoLimiarInstavelEhUnstable() {
        double rtt = 150.0;
        double packetLoss = 0.0;

        String result = classifier.classify(rtt, packetLoss);

        assertEquals("UNSTABLE", result);
    }

    @Test
    @DisplayName("Perda de pacotes exatamente no limiar moderado (1,0 %) é MODERATE")
    void perdaNoLimiarModeradoEhModerate() {
        double rtt = 0.0;
        double packetLoss = 1.0;

        String result = classifier.classify(rtt, packetLoss);

        assertEquals("MODERATE", result);
    }

    @Test
    @DisplayName("Perda de pacotes logo abaixo do limiar moderado (0,99 %) continua GOOD")
    void perdaLogoAbaixoDoLimiarModeradoEhGood() {
        double rtt = 0.0;
        double packetLoss = 0.99;

        String result = classifier.classify(rtt, packetLoss);

        assertEquals("GOOD", result);
    }

    // ---------- Valores extremos ----------

    @Test
    @DisplayName("Métricas muito acima das referências: score fica em 0 (não negativo)")
    void scoreComValoresExtremosFicaEmZero() {
        int score = classifier.score(1000.0, 100.0);

        assertEquals(0, score);
    }

    @Test
    @DisplayName("Melhor caso possível (RTT 0, perda 0): score máximo 100")
    void scoreDoMelhorCasoEh100() {
        int score = classifier.score(0.0, 0.0);

        assertEquals(100, score);
    }

    @Test
    @DisplayName("Double.MAX_VALUE nas duas métricas é UNSTABLE com score 0, sem estourar")
    void valoresMaximosDeDoubleSaoUnstableComScoreZero() {
        QualityClassifier.QualityAssessment result =
                classifier.assess(Double.MAX_VALUE, Double.MAX_VALUE);

        assertEquals(Quality.UNSTABLE, result.quality());
        assertEquals(0, result.qualityScore());
    }

    // ---------- Caminho triste: entradas inválidas ----------

    @Test
    @DisplayName("RTT negativo lança IllegalArgumentException")
    void rttNegativoLancaIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> classifier.classify(-1.0, 0.0));

        assertEquals(INVALID_METRICS_MESSAGE, ex.getMessage());
    }

    @Test
    @DisplayName("Perda de pacotes negativa lança IllegalArgumentException")
    void perdaNegativaLancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> classifier.classify(0.0, -0.01));
    }

    @Test
    @DisplayName("RTT NaN lança IllegalArgumentException")
    void rttNaNLancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> classifier.classify(Double.NaN, 0.0));
    }

    @Test
    @DisplayName("Perda de pacotes infinita lança IllegalArgumentException")
    void perdaInfinitaLancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> classifier.score(0.0, Double.POSITIVE_INFINITY));
    }
}
