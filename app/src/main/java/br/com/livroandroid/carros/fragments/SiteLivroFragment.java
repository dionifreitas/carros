package br.com.livroandroid.carros.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import br.com.livroandroid.carros.R;


public class SiteLivroFragment extends BaseFragment {

    private static final String URL_SOBRE = "http://www.livroandroid.com.br/sobre.htm";
    private WebView webView;
    private ProgressBar progressBar;
    protected SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_site_livro_fragmnet, container, false);

        webView = (WebView)view.findViewById(R.id.webview);
        progressBar = (ProgressBar)view.findViewById(R.id.progress);

        setWebViewCliente(webView);
        webView.loadUrl(URL_SOBRE);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener());

        configJavaScript();

        return view;
    }
    public void setWebViewCliente(WebView webView){
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("livroandroid","WebView url: "+url);
                if (url != null && url.endsWith("sobre.htm")){
                    AboutDialog.showAbout(getFragmentManager());
                    return true;
                }
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener(){
     return new SwipeRefreshLayout.OnRefreshListener() {
         @Override
         public void onRefresh() {
             webView.reload();
         }
     };
    }

    private void configJavaScript(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new LivroAndroidInterface(),"LivroAndroid");
    }

    class LivroAndroidInterface{
        @JavascriptInterface
        public void sobre(){
            toast("Clicou na figura do livro");
        }
    }
}
