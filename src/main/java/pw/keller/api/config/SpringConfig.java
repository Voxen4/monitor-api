package pw.keller.api.config;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.zalando.logbook.Logbook;
import pw.keller.api.entity.url.Url;
import pw.keller.api.entity.url.UrlDTO;
import pw.keller.api.scheduler.CheckUrlTask;

import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan
@EnableScheduling
public class SpringConfig{

    @Profile({"dev", "test"})
    @Bean
    @Primary
    public DataSource inMemoryDS() throws Exception {
        //EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("db/migration/V1__Base_version"));
        return EmbeddedPostgres.builder()
                .start().getPostgresDatabase();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //Url
        TypeMap<Url, UrlDTO> urlMap = mapper.createTypeMap(Url.class, UrlDTO.class);
        urlMap.addMapping(Url::getUrl, UrlDTO::setUrl);
        urlMap.addMapping(Url::getStatusCode, UrlDTO::setStatusCode);
        urlMap.addMapping(Url::getUrlId, UrlDTO::setUrlId);
        urlMap.addMappings(map -> map.skip(UrlDTO::setUrlId));

        mapper.validate();
        return mapper;
    }

    @Bean
    public Logbook logbook() {
        return Logbook.create();
    }

    @Bean
    public CustomDynamicSchedule getDynamicScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        return new CustomDynamicSchedule(threadPoolTaskScheduler);
    }
}
