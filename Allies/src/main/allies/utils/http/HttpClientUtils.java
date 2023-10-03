package main.allies.utils.http;

import okhttp3.*;

import java.io.IOException;
import java.util.function.Consumer;

import static main.allies.utils.allies.Constants.*;

public class HttpClientUtils {
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    public final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsyncGet(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtils.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static Response runSyncGet(String finalUrl) throws IOException {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtils.HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        return response;
    }

    public static void runAsyncPost(String finalUrl, String body, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HttpClientUtils.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void sendLogoutRequest() {
        String finalUrl = HttpUrl
                .parse(LOGOUT)
                .newBuilder()
                .build()
                .toString();

        runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                removeCookiesOf(BASE_DOMAIN);
                response.close();
            }
        });
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
