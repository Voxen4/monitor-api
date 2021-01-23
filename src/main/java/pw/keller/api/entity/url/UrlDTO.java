package pw.keller.api.entity.url;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import pw.keller.api.implement.service.IServiceEntity;

@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"urlId"}, ignoreUnknown = true)
public class UrlDTO implements IServiceEntity {
    private Long urlId = null;

    private String url = null;

    private Integer statusCode = null;

    public UrlDTO() {

    }

    public UrlDTO(Url createdUrl) {
        urlId = createdUrl.getId();
        url = createdUrl.getUrl();
        statusCode = createdUrl.getStatusCode();
    }

    @Override
    public Long getId() {
        return urlId;
    }

    @Override
    public void setId(Long id) {
        this.urlId = id;
    }
}
