package ir.hesanfarzi.proxy.utility;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProxyViewModel extends AndroidViewModel {

    public MutableLiveData<List<ProxyModel>> list;
    private final RequestQueue queue;

    public ProxyViewModel(Application application) {
        super(application);
        queue = Volley.newRequestQueue(application);
    }

    public MutableLiveData<List<ProxyModel>> getProxyList() {
        if (list==null || App.refresh) {
            list = new MutableLiveData<>();
            if (App.api.startsWith("https://raw")) connectToServer();
            else connectToServerB();
        }
        return list;
    }

    private void connectToServer() {
        StringRequest request = new StringRequest(Request.Method.GET, App.api, response -> {
            try {
                List<ProxyModel> mList = new ArrayList<>();
                String[] a = response.split("\n",76);
                for (int i = 0; i<75; i++) {
                    ProxyModel model = new ProxyModel(a[i]);
                    mList.add(model);
                }
                list.setValue(mList);
                App.refresh = false;
            } catch (Exception ignore) {}}, error -> App.refresh = true
        );
        queue.add(request);
    }

    private void connectToServerB() {
        List<ProxyModel> mList = new ArrayList<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Document doc = Jsoup.connect(App.api).get();
                Elements elements = doc.getElementsByTag("td");
                for (int i = 0; i<150; i++) {
                    String ip = elements.get(i).text();
                    if (!ip.equals("")) mList.add(new ProxyModel(ip));
                }
            } catch (Exception ignore) {
                App.refresh = true;
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                list.setValue(mList);
                if (list != null) App.refresh = false;
            });
        });
    }

}