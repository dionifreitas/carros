package br.com.livroandroid.carros;

import android.test.AndroidTestCase;

import java.io.IOException;
import java.util.List;

import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.CarroService;
/**
 * Created by thiago on 08/10/16.
 */

public class CarroServiceTest extends AndroidTestCase{
    public void testGetCarros() throws IOException{
        List<Carro> carros = CarroService.getCarros(getContext(), R.string.luxo,true);
        assertNotNull(carros);
        assertTrue(carros.size() == 10);

        Carro c0 = carros.get(0);
        assertEquals("Bugatti Veyron",c0.nome);
        assertEquals("-23.564224",c0.latitude);
        assertEquals("-46.653156",c0.longitude);

        Carro c9 = carros.get(9);
        assertEquals("Lexus LFA",c9.nome);
        assertEquals("-23.564224",c9.latitude);
        assertEquals("-46.653156",c9.longitude);
    }
}
