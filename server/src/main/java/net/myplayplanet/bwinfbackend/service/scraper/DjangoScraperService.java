package net.myplayplanet.bwinfbackend.service.scraper;

import net.myplayplanet.bwinfbackend.model.TaskType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class DjangoScraperService {

    private final WebClient client;
    private List<String> sessionCookies = null;

    public DjangoScraperService() {
        int bufferSize = 10 * 1024 * 1024; // 10 MB, adjust as needed
        WebClient.Builder builder = WebClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .exchangeStrategies(
                        org.springframework.web.reactive.function.client.ExchangeStrategies.builder()
                                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(bufferSize))
                                .build()
                );
        this.client = builder.build();
    }

    // Triggers login and stores session cookies
    public Mono<String> loginAndStoreSession() {
        return getLoginPage()
                .flatMap(loginData -> login(loginData.csrftoken, loginData.cookies))
                .doOnNext(cookies -> this.sessionCookies = cookies)
                .thenReturn("Login successful, session stored.");
    }

    // Scrapes using stored session cookies
    public Mono<String> scrapeWithStoredSession(String host,
                                                TaskType taskType,
                                                Integer taskNumber) {
        if (sessionCookies == null) {
            return Mono.error(new IllegalStateException("No session cookies stored. Please login first."));
        }
        return scrapeTaskPage(sessionCookies, host, taskType, taskNumber);
    }

    // ------------------------------------------------------
    // 1) GET login page -> get csrftoken
    // ------------------------------------------------------
    private Mono<LoginData> getLoginPage() {
        String loginUrl = "http://192.168.146.184/accounts/login/";

        return client.get()
                .uri(loginUrl)
                .exchangeToMono(response -> {
                    List<String> cookies = response.headers().asHttpHeaders().get(HttpHeaders.SET_COOKIE);
                    String csrftoken = extractCookie(cookies, "csrftoken");

                    return Mono.just(new LoginData(csrftoken, cookies));
                });
    }

    // ------------------------------------------------------
    // 2) POST login -> get sessionid
    // ------------------------------------------------------
    private Mono<List<String>> login(String csrftoken, List<String> cookies) {

        String loginUrl = "http://192.168.146.184/accounts/login/";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", "paulf");
        form.add("password", "bewertung");
        form.add("csrfmiddlewaretoken", csrftoken);

        return client.post()
                .uri(loginUrl)
                .header("Cookie", String.join("; ", cookies))
                .header("X-CSRFToken", csrftoken)
                .bodyValue(form)
                .exchangeToMono(response -> {
                    List<String> newCookies = response.headers().asHttpHeaders().get(HttpHeaders.SET_COOKIE);
                    return Mono.just(mergeCookies(cookies, newCookies));
                });
    }

    // ------------------------------------------------------
    // 3) GET the protected page using the session
    // ------------------------------------------------------
    private Mono<String> scrapeTaskPage(List<String> cookies,
                                        String host,
                                        TaskType taskType,
                                        Integer taskNumber) {
        String url = buildUrl(host, taskType, taskNumber);

        WebClient.RequestHeadersSpec<?> headersSpec = (WebClient.RequestHeadersSpec<?>) client.get()
                .uri(url)
                .header("Cookie", String.join("; ", cookies));
        return headersSpec
                .exchangeToMono(clientResponse ->
                        clientResponse.bodyToMono(String.class)
                );
    }

    private String buildUrl(String host,
                            TaskType taskType,
                            Integer taskNumber) {
        return "http://" + host + "/task/" + taskType.shortName() + taskNumber.toString() + "/";
    }

    // ------------------------------------------------------
    // Helpers
    // ------------------------------------------------------
    private String extractCookie(List<String> cookies, String name) {
        return cookies.stream()
                .filter(c -> c.startsWith(name + "="))
                .map(c -> c.split("=")[1].split(";")[0])
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cookie " + name + " not found"));
    }

    private List<String> mergeCookies(List<String> oldCookies, List<String> newCookies) {
        List<String> merged = new ArrayList<>(oldCookies);
        if (newCookies != null) {
            merged.addAll(newCookies);
        }
        return merged;
    }

    private record LoginData(String csrftoken, List<String> cookies) {
    }
}
