package pw.keller.api.entity.url;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.RepresentationModel;
import pw.keller.api.implement.service.IServiceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "url")
@Entity
public class Url implements IServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long urlId;

    @URL
    @NotEmpty
    private String url;

    private int statusCode;

    @Override
    public Long getId() {
        return this.getUrlId();
    }

    @Override
    public void setId(Long id) {
        this.setUrlId(id);
    }
}
