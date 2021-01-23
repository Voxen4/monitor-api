package pw.keller.api.entity.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pw.keller.api.exception.EntityNotFoundException;
import pw.keller.api.implement.service.IService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UrlService implements IService<Url> {

    private final UrlRepository repository;

    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.repository = urlRepository;
    }

    @Override
    public Url save(Url url) {
        //Check if the Url is already saved
        if (repository.existsById(url.getId())) {
            return url;
        }

        return repository.save(url);
    }

    @Override
    public Url saveAndFlush(Url url) {
        var freshCompany = save(url);
        repository.flush();
        return freshCompany;
    }

    @Override
    public Url update(Url url) {
        //Check if Url is new
        Optional<Url> existingUrl = repository.findById(url.getId());
        if (existingUrl.isPresent()) {
            Url value = existingUrl.get();
            value.setUrl(url.getUrl());
            value.setStatusCode(url.getStatusCode());
            return repository.save(value);
        } else {
            throw new EntityNotFoundException("Company can't be updated, not saved yet.");
        }
    }

    @Override
    public List<Url> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Url> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Url url) {
        repository.delete(url);
    }
}
