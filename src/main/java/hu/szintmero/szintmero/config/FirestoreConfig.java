package hu.szintmero.szintmero.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirestoreConfig {

    // One shared, thread-safe client for the whole app. Credentials come from ADC
    // (locally: gcloud auth application-default login; on Cloud Run: the service account).
    @Bean
    public Firestore firestore(@Value("${gcp.project-id}") String projectId) {
        return FirestoreOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
    }
}
