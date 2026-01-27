# Maven Central Publishing Guide for Command API

This guide explains how to publish the Command API to Maven Central Repository (Sonatype OSS).

## Prerequisites

### 1. Sonatype JIRA Account
- Create an account at https://issues.sonatype.org
- Create a JIRA ticket requesting a new project (groupId: `dev.evokerking`)
- Wait for approval (usually a few hours to 1 day)

### 2. GPG Key Pair
Generate a GPG key pair if you don't have one:

```bash
gpg --gen-key
```

List your keys:
```bash
gpg --list-secret-keys --keyid-format LONG
```

Export your public key to a keyserver:
```bash
gpg --keyserver hkp://keyserver.ubuntu.com --send-keys <KEY_ID>
```

### 3. Environment Variables
Set the following environment variables for publishing:

```bash
export MAVEN_USERNAME=<your-sonatype-username>
export MAVEN_PASSWORD=<your-sonatype-password>
export GPG_SIGNING_KEY="$(gpg --armor --export-secret-keys <KEY_ID> | base64)"
export GPG_SIGNING_PASSWORD=<your-gpg-passphrase>
```

Alternatively, you can configure them in `~/.gradle/gradle.properties`:

```properties
org.gradle.jvmargs=-Dorg.gradle.internal.http.socketTimeout=60000
org.gradle.internal.publish.checksums.insecure=true

# Sonatype credentials
mavenUsername=<your-sonatype-username>
mavenPassword=<your-sonatype-password>

# GPG signing (if using local GPG command)
signing.keyId=<last-8-chars-of-gpg-key-id>
signing.password=<your-gpg-passphrase>
signing.secretKeyRingFile=/home/user/.gnupg/secring.gpg
```

## Building and Publishing

### 1. Test Build
First, verify the build works correctly:

```bash
gradle build -p command-api
```

### 2. Generate Artifacts
Create the JAR, sources, Javadoc, and signatures:

```bash
gradle publishMavenJavaPublicationToLocalTestRepository -p command-api
```

This will create artifacts in `command-api/build/repos/releases/` for local testing.

### 3. Publish to Maven Central (Staging)
To upload to Sonatype OSS staging repository:

```bash
gradle publish -p command-api
```

The artifacts will be uploaded to the staging repository at:
https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/

### 4. Release from Staging
Once published to staging, you need to:

1. Log in to Sonatype: https://s01.oss.sonatype.org
2. Click "Staging Repositories" in the left menu
3. Find your repository (name starts with `devevokerking`)
4. Click "Close" to validate the repository
5. Click "Release" to promote to Maven Central

Alternatively, you can use Sonatype's REST API:

```bash
# Close the staging repository
curl -u $MAVEN_USERNAME:$MAVEN_PASSWORD \
  -X POST \
  -d '<promoteRequest><data><stagedRepositoryId>devevokerking-1001</stagedRepositoryId></data></promoteRequest>' \
  -H "Content-Type: application/xml" \
  https://s01.oss.sonatype.org/service/local/staging/bulk/promote

# Release the repository
curl -u $MAVEN_USERNAME:$MAVEN_PASSWORD \
  -X POST \
  -d '<promoteRequest><data><stagedRepositoryId>devevokerking-1001</stagedRepositoryId></data></promoteRequest>' \
  -H "Content-Type: application/xml" \
  https://s01.oss.sonatype.org/service/local/staging/bulk/drop
```

### 5. Verify on Maven Central
After release, artifacts will be available at:
- https://mvnrepository.com/artifact/dev.evokerking/command-api
- https://repo1.maven.org/maven2/dev/evokerking/command-api/

Users can then use the library in their projects:

```gradle
dependencies {
    implementation 'dev.evokerking:command-api:1.0.0'
}
```

## Gradle Tasks

### Publishing Tasks

```bash
# Publish to all configured repositories
gradle publish -p command-api

# Publish only to local test repository
gradle publishMavenJavaPublicationToLocalTestRepository -p command-api

# Publish only to Maven Central
gradle publishMavenJavaPublicationToMavenCentralRepository -p command-api

# Generate signing files
gradle signMavenJavaPublication -p command-api
```

### Verification Tasks

```bash
# Check build
gradle build -p command-api

# Run tests
gradle test -p command-api

# Generate Javadoc
gradle javadoc -p command-api
```

## Troubleshooting

### GPG Signature Issues
If you get GPG signing errors:

1. Ensure GPG is installed: `gpg --version`
2. Verify key exists: `gpg --list-secret-keys`
3. Try signing a test file: `gpg --sign test.txt`
4. Ensure `gpg-agent` is running: `gpg-agent --daemon`

### Authentication Failures
If you get authentication errors:

1. Verify Sonatype credentials at https://s01.oss.sonatype.org
2. Check that credentials are correctly set in environment variables or gradle.properties
3. Ensure there are no special characters in your password that need escaping

### Network Timeouts
If you get timeout errors:

1. Check your internet connection
2. Try increasing the timeout: `org.gradle.jvmargs=-Dorg.gradle.internal.http.socketTimeout=120000`
3. Try again later (Sonatype servers may be busy)

### Version Already Released
If version 1.0.0 is already released:

1. Update version in `command-api/build.gradle.kts` (e.g., to 1.0.1)
2. Update version in `command-api/src/main/java/dev/evokerking/command/*.java` if applicable
3. Commit changes: `git commit -am "Bump version to 1.0.1"`
4. Tag release: `git tag v1.0.1`
5. Publish again

## Configuration Files

### command-api/build.gradle.kts
The publishing configuration is defined here with:
- Maven publication details (groupId, artifactId, version)
- POM metadata (name, description, URL, licenses, developers, SCM)
- Repository configuration (Sonatype OSS staging)
- Signing configuration (GPG keys)

## Version Management

To update the version for future releases:

Edit `command-api/build.gradle.kts` and update:
```kotlin
version = "1.0.0"  // Change to desired version
```

Follow semantic versioning:
- MAJOR.MINOR.PATCH
- Example: 1.0.0, 1.0.1, 1.1.0, 2.0.0

## Additional Resources

- [Sonatype OSS Documentation](https://central.sonatype.org/pages/apache-maven.html)
- [Gradle Maven Publish Plugin](https://docs.gradle.org/current/plugins/maven-publish-plugin.html)
- [Gradle Signing Plugin](https://docs.gradle.org/current/plugins/signing-plugin.html)
- [GPG Key Generation](https://help.github.com/en/articles/generating-a-new-gpg-key)
- [Sonatype Staging Repository Management](https://s01.oss.sonatype.org)
