
package br.com.livroandroid.carros.domain;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.ParcelWrapper;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2016-12-06T22:46-0200")
@SuppressWarnings("unchecked")
public class Carro$$Parcelable
    implements Parcelable, ParcelWrapper<br.com.livroandroid.carros.domain.Carro>
{

    private br.com.livroandroid.carros.domain.Carro carro$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Carro$$Parcelable.Creator$$0 CREATOR = new Carro$$Parcelable.Creator$$0();

    public Carro$$Parcelable(android.os.Parcel parcel$$0) {
        br.com.livroandroid.carros.domain.Carro carro$$2;
        if (parcel$$0 .readInt() == -1) {
            carro$$2 = null;
        } else {
            carro$$2 = readbr_com_livroandroid_carros_domain_Carro(parcel$$0);
        }
        carro$$0 = carro$$2;
    }

    public Carro$$Parcelable(br.com.livroandroid.carros.domain.Carro carro$$4) {
        carro$$0 = carro$$4;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$1, int flags) {
        if (carro$$0 == null) {
            parcel$$1 .writeInt(-1);
        } else {
            parcel$$1 .writeInt(1);
            writebr_com_livroandroid_carros_domain_Carro(carro$$0, parcel$$1, flags);
        }
    }

    private br.com.livroandroid.carros.domain.Carro readbr_com_livroandroid_carros_domain_Carro(android.os.Parcel parcel$$2) {
        br.com.livroandroid.carros.domain.Carro carro$$1;
        carro$$1 = new br.com.livroandroid.carros.domain.Carro();
        carro$$1 .tipo = parcel$$2 .readString();
        carro$$1 .urlVideo = parcel$$2 .readString();
        carro$$1 .urlFoto = parcel$$2 .readString();
        carro$$1 .latitude = parcel$$2 .readString();
        carro$$1 .nome = parcel$$2 .readString();
        carro$$1 .id = parcel$$2 .readLong();
        carro$$1 .urlInfo = parcel$$2 .readString();
        carro$$1 .selected = (parcel$$2 .readInt() == 1);
        carro$$1 .desc = parcel$$2 .readString();
        carro$$1 .longitude = parcel$$2 .readString();
        return carro$$1;
    }

    private void writebr_com_livroandroid_carros_domain_Carro(br.com.livroandroid.carros.domain.Carro carro$$3, android.os.Parcel parcel$$3, int flags$$0) {
        parcel$$3 .writeString(carro$$3 .tipo);
        parcel$$3 .writeString(carro$$3 .urlVideo);
        parcel$$3 .writeString(carro$$3 .urlFoto);
        parcel$$3 .writeString(carro$$3 .latitude);
        parcel$$3 .writeString(carro$$3 .nome);
        parcel$$3 .writeLong(carro$$3 .id);
        parcel$$3 .writeString(carro$$3 .urlInfo);
        parcel$$3 .writeInt((carro$$3 .selected? 1 : 0));
        parcel$$3 .writeString(carro$$3 .desc);
        parcel$$3 .writeString(carro$$3 .longitude);
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public br.com.livroandroid.carros.domain.Carro getParcel() {
        return carro$$0;
    }

    private final static class Creator$$0
        implements Creator<Carro$$Parcelable>
    {


        @Override
        public Carro$$Parcelable createFromParcel(android.os.Parcel parcel$$4) {
            return new Carro$$Parcelable(parcel$$4);
        }

        @Override
        public Carro$$Parcelable[] newArray(int size) {
            return new Carro$$Parcelable[size] ;
        }

    }

}
