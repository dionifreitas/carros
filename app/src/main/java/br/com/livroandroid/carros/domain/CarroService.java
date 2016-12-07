package br.com.livroandroid.carros.domain;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.livroandroid.carros.R;
import livroandroid.lib.utils.FileUtils;
import livroandroid.lib.utils.HttpHelper;
import livroandroid.lib.utils.IOUtils;
import livroandroid.lib.utils.SDCardUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thiago on 27/09/16.
 */

public class CarroService {

    private static final boolean LOG_ON = false;
    private static String TAG = "CarroService";
    private static final String URL = "http://www.livroandroid.com.br/livro/carros/carros_{tipo}.json";

    public static List<Carro> getCarros(Context context, int tipo, boolean refress) throws IOException {

        List<Carro> carros = !refress ? getCarrosFromBanco(context, tipo) : null;
        Log.d(TAG, "Arquivo: " + carros);
        if (carros != null && carros.size() > 0) {
            return carros;
        }
        carros = getCarrosFromWebService(context, tipo);
        return carros;
    }

    private static List<Carro> getCarrosFromBanco(Context context, int tipo) throws IOException {

            CarroDB db = new CarroDB(context);

            try {
                String tipoString = getTipo(tipo);
                List<Carro> carros = db.findAllByTipo(tipoString);
                Log.d(TAG, "Retornando " + carros.size() + " carros [" + tipoString + "] do banco");
                return carros;
            } finally {
                db.close();
            }
        }

    public static List<Carro> getCarrosFromWebService(Context context, int tipo) throws IOException {

        String tipoString = getTipo(tipo);
        String url = URL.replace("{tipo}", tipoString);

        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Carro> carros = parserJson(context, json);
        salvarCarros(context, tipo, carros);
        return carros;
    }

    private static void salvarCarros(Context context, int tipo, List<Carro> carros) {

        CarroDB db = new CarroDB(context);
        try {
            String tipoString = getTipo(tipo);
            db.deletaCarrosByTipo(tipoString);

            for (Carro c : carros) {
                c.tipo = tipoString;
                Log.d("sql", "Salvando o carro " + c.nome);
                db.save(c);
            }
        } finally {
            db.close();
        }
    }

    public static List<Carro> getCarrosFromArquivo(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String fileName = String.format("carros_%s.json", tipoString);
        Log.d(TAG, "Abrindo o arquivo: " + fileName);

        String json = FileUtils.readFile(context, fileName, "UTF-8");

        if (json == null) {
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }
        List<Carro> carros = parserJson(context, json);
        Log.d(TAG, "Retornando carros do arquivo " + fileName + ".");
        return carros;
    }

    private static String readFile(Context context, int tipo) throws IOException {
        if (tipo == R.string.classicos) {
            return FileUtils.readRawFileString(context, R.raw.carros_classicos, "UTF-8");
        } else if (tipo == R.string.esportivos) {
            return FileUtils.readRawFileString(context, R.raw.carros_esportivos, "UTF-8");
        }
        return FileUtils.readRawFileString(context, R.raw.carros_luxo, "UTF-8");
    }

    private static List<Carro> parserJson(Context context, String json) throws IOException {
        List<Carro> carros = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCarros = obj.getJSONArray("carro");

            for (int i = 0; i < jsonCarros.length(); i++) {
                JSONObject jsonCarro = jsonCarros.getJSONObject(i);
                Carro c = new Carro();

                c.nome = jsonCarro.optString("nome");
                c.desc = jsonCarro.optString("desc");
                c.urlFoto = jsonCarro.optString("url_foto");
                c.urlInfo = jsonCarro.optString("url_info");
                c.urlVideo = jsonCarro.optString("url_video");
                c.latitude = jsonCarro.optString("latitude");
                c.longitude = jsonCarro.optString("longitude");

                if (LOG_ON) {
                    Log.d(TAG, "Carro " + c.nome + " > " + c.urlFoto);
                }
                carros.add(c);
            }
            if (LOG_ON) {
                Log.d(TAG, carros.size() + " encontrados");
            }
        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }
        return carros;
    }

    private static String getTipo(int tipo) {

        if (tipo == R.string.classicos) {
            return "classicos";
        } else if (tipo == R.string.esportivos) {
            return "esportivos";
        }
        return "luxo";
    }

    private static void salvaArquivoNaMemoriaExterna(Context context, String url, String json) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        File file = SDCardUtils.getPrivateFile(context, fileName, Environment.DIRECTORY_DOWNLOADS);
        IOUtils.writeString(file, json);
        Log.d(TAG, "1) Arquivo privado salvo na pasta Downloads: " + file);

        file = SDCardUtils.getPublicFile(fileName, Environment.DIRECTORY_DOWNLOADS);
        IOUtils.writeString(file, json);
        Log.d(TAG, "2) Arquivo publico salvo na pasta Downloads: " + file);
    }

    private static void salvaArquivoNaMemoriaInterna(Context context, String url, String json) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        File file = FileUtils.getFile(context, fileName);
        IOUtils.writeString(file, json);
        Log.d(TAG, "2) Arquivo salvo: " + file);
    }
}
