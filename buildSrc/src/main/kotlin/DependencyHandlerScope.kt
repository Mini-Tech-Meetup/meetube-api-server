import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.springBoot(
    module: String
) = "org.springframework.boot:spring-boot-$module"

fun DependencyHandlerScope.mockk(
    version: String = Ver.mockk
) = "io.mockk:mockk:$version"

fun DependencyHandlerScope.mockitoKotlin2(
    version: String = Ver.mockitoKotlin2
) = "com.nhaarman.mockitokotlin2:mockito-kotlin:$version"

fun DependencyHandlerScope.junit5(
    module: String,
    version: String = Ver.junit5
) = "org.junit.jupiter:junit-jupiter-$module:$version"

fun DependencyHandlerScope.flyway(
    module: String,
    version: String = Ver.flyway
) = "org.flywaydb:flyway-$module:$version"

fun DependencyHandlerScope.microsoftCognitiveServices(
    version: String = Ver.microsoftCognitiveServices
) = "com.microsoft.cognitiveservices.speech:client-sdk:$version"

fun DependencyHandlerScope.apacheHttpClient(
    version: String = Ver.apacheHttpClient
) = "org.apache.httpcomponents:httpclient:$version"

fun DependencyHandlerScope.postgres(
    version: String = Ver.postgres
) = "org.postgresql:postgresql:$version"

fun DependencyHandlerScope.queryDsl(
    module: String,
    version: String = Ver.queryDsl
) = "com.querydsl:$module:$version"

fun DependencyHandlerScope.reactor(
    module: String
) = "io.projectreactor:reactor-$module"

fun DependencyHandlerScope.jacksonModule(
    module: String
) = "com.fasterxml.jackson.module:jackson-module-$module"
