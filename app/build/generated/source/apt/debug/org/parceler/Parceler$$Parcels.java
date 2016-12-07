
package org.parceler;

import java.util.HashMap;
import java.util.Map;
import br.com.livroandroid.carros.domain.Carro;
import br.com.livroandroid.carros.domain.Carro$$Parcelable;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2016-12-06T22:46-0200")
@SuppressWarnings("unchecked")
public class Parceler$$Parcels
    implements Repository<org.parceler.Parcels.ParcelableFactory>
{

    private final Map<Class, org.parceler.Parcels.ParcelableFactory> map$$0 = new HashMap<Class, org.parceler.Parcels.ParcelableFactory>();

    public Parceler$$Parcels() {
        map$$0 .put(Carro.class, new Parceler$$Parcels.Carro$$Parcelable$$0());
    }

    public Map<Class, org.parceler.Parcels.ParcelableFactory> get() {
        return map$$0;
    }

    private final static class Carro$$Parcelable$$0
        implements org.parceler.Parcels.ParcelableFactory<Carro>
    {


        @Override
        public Carro$$Parcelable buildParcelable(Carro input) {
            return new Carro$$Parcelable(input);
        }

    }

}
