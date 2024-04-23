import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TilaustenKäsittelyMockitoTest {
    @Mock
    IHinnoittelija hinnoittelijaMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testaaKäsittelijäWithMockitoHinnoittelija() {
        testaaKäsittelijä(30.0f); // hinta alle 100
    }

    @Test
    public void testaaMockitoHinnoittelijaPriceGreaterOrEqualTo100() {
        testaaKäsittelijä(100.0f); // hinta yhtäsuuri tai enemmän kuin 100
    }

    private void testaaKäsittelijä(float listaHinta) {
        float alkuSaldo = 100.0f;
        float alennus = 20.0f;
        float loppuSaldo = alkuSaldo - (listaHinta * (1 - alennus / 100));
        Asiakas asiakas = new Asiakas(alkuSaldo);
        Tuote tuote = new Tuote("TDD in Action", listaHinta);

        when(hinnoittelijaMock.getAlennusProsentti(asiakas, tuote)).thenReturn(alennus);

        TilaustenKäsittely käsittelijä = new TilaustenKäsittely();
        käsittelijä.setHinnoittelija(hinnoittelijaMock);
        käsittelijä.käsittele(new Tilaus(asiakas, tuote));

        assertEquals(loppuSaldo, asiakas.getSaldo(), 0.001);
        verify(hinnoittelijaMock,times(2)).getAlennusProsentti(asiakas, tuote);
    }
}