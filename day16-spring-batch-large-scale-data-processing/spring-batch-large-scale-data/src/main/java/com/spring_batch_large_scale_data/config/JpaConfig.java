
package com.spring_batch_large_scale_data.config;

import com.spring_batch_large_scale_data.adapter.out.persistence.entity.SourceUserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class JpaConfig {

    @Bean(name = "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(
            @Qualifier("sourceDataSource") DataSource sourceDataSource) {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(sourceDataSource);
        factory.setPersistenceUnitName("sourcePersistenceUnit");
        factory.setManagedTypes(
                PersistenceManagedTypes.of(SourceUserEntity.class.getName()));
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setJpaPropertyMap(Map.of(
                "hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect",
                "hibernate.hbm2ddl.auto",
                "none"));

        return factory;
    }
}
