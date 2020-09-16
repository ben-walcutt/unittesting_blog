package co.newlabs;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public MapperFactory mapperFactory() {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .dumpStateOnException(false)
                .useBuiltinConverters(true)
                .build();

        mapperFactory.classMap(CustomerDTO.class, CustomerEntity.class)
                .mapNulls(true)
                .byDefault()
                .field("name", "name")
                .register();

        return mapperFactory;
    }

    @Bean
    public MapperFacade mapper(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }
}
