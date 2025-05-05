package com.wm.autospec.common.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Component
@Slf4j
public class AWSSecretsManagerClient {

    public void getSecret() {

        System.out.println("SecretManager Execution Started .... ");

        String secretName = "qa/databaseCredentials/MySql";
        Region region = Region.of("eu-west-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region).
                credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        "accessKey", "secretKey")))
                .build();
        /*
            TODO :
                // Option 1: Create a new IAM User (for programmatic access)
                // Step 2: Attach Permissions
                //Choose Attach policies directly.
                //Select the policy:
                //SecretsManagerReadWrite (for full access)
                //SecretsManagerReadOnly (if just reading secrets)
                // Step 3: Download the Access Key and Secret
                // Best Practices
                // Never commit your credentials to Git or share them.
                // Use environment variables or ~/.aws/credentials.
                // For production workloads, use IAM roles (e.g., EC2 instance roles, ECS task roles, Lambda roles).
         */

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }

        String secret = getSecretValueResponse.secretString();
        System.out.println("Secret for " + secretName + " = "+secret);
    }


}