package pw.keller.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pw.keller.api.entity.url.Url;
import pw.keller.api.entity.url.UrlService;

@Profile({"dev", "test"})
@Component
public class DataLoader {
    private final UrlService urlService;

    @Autowired
    public DataLoader( UrlService urlService) {
        this.urlService = urlService;
        initEntities();
    }

    void initEntities() {

        Url googleUrl = new Url();
        googleUrl.setId(-1L);
        googleUrl.setUrl("https://www.google.de/");
        googleUrl.setStatusCode(200);
        urlService.save(googleUrl);

    }
}
