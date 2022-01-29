import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "com.yrc"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.bytedeco:ffmpeg-platform:4.4-1.5.6")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
//    implementation("org.flywaydb:flyway:8.4.0")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //校验
    implementation("org.valiktor:valiktor-core:0.12.0")
//    implementation("com.yrc:DDStream-common")
    implementation(project("modules:DDStream-common"))
    //jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("com.fasterxml:jackson-xml-databind:0.6.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    //hash
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    //h2驱动
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
