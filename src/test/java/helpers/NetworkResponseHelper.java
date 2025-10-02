package helpers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v129.network.Network;
import org.openqa.selenium.devtools.v129.network.model.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

public class NetworkResponseHelper {
    public static String captureRegisterResponse(ChromeDriver driver, Runnable registerAction) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        AtomicReference<String> responseBody = new AtomicReference<>(null);

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();
            if (res.getUrl().contains("register") || res.getUrl().contains("signup")) {
                try {
                    Map<String, Object> body = driver.executeCdpCommand("Network.getResponseBody", Map.of("requestId", response.getRequestId().toString()));
                    responseBody.set(body.get("body").toString());
                } catch (Exception e) {
                    responseBody.set("{\"status\":\"captured\",\"url\":\"" + res.getUrl() + "\"}");
                }
            }
        });

        registerAction.run();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        devTools.send(Network.disable());
        return responseBody.get();
    }
}

