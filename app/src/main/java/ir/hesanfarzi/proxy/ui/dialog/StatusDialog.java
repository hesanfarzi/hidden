package ir.hesanfarzi.proxy.ui.dialog;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Executors;

import ir.hesanfarzi.proxy.R;

public class StatusDialog {

    TextView ip;
    TextView status;
    TextView no;
    TextView yes;

    public StatusDialog(Context context, String mIP) {
        Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_status);

        ip = dialog.findViewById(R.id.ip);
        status = dialog.findViewById(R.id.status);
        no = dialog.findViewById(R.id.no);
        yes = dialog.findViewById(R.id.yes);

        ip.append(" " + mIP);
        no.setOnClickListener(v -> dialog.dismiss());
        yes.setOnClickListener(v -> {
            copyToClipboard(context, mIP);
            dialog.dismiss();
        });

        checkStatus(mIP);
        dialog.show();
    }


    private void checkStatus(String ip) {
        String[] ips = ip.split(":");
        String hostname = ips[0];
        int port = Integer.parseInt(ips[1]);
        Executors.newSingleThreadExecutor().execute(() -> {
            int code = 0;
            try {
                URL url = new URL("http://google.com");
                InetSocketAddress socket = new InetSocketAddress(hostname, port);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, socket);
                HttpURLConnection c = (HttpURLConnection) url.openConnection(proxy);
                c.setConnectTimeout(5000);
                c.setReadTimeout(3000);
                code = c.getResponseCode();
                c.disconnect();
            } catch (Exception ignore) {}
            int codeB = code;
            new Handler(Looper.getMainLooper()).post(() -> {
                String response;
                if (codeB!=0) response = "Status: active";
                else response = "Status: invalid";
                status.setText(response);
            });
        });
    }


    private void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("L", text);
        clipboard.setPrimaryClip(clipData);
    }


}