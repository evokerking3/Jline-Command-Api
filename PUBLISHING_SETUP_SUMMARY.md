# Maven Central Publishing Setup - Summary

## What Was Configured

The Command API has been fully configured for publishing to Maven Central Repository (Sonatype OSS).

### 1. Build Configuration (`command-api/build.gradle.kts`)

✅ **Maven Publish Plugin**
- Configured to publish to Sonatype OSS staging repository
- Publication details: `dev.evokerking:command-api:1.0.0`
- Generates JAR, sources JAR, and javadoc JAR
- Publishes POM with complete metadata

✅ **Signing Plugin**
- Automatically signs artifacts with GPG
- Uses environment variables: `GPG_SIGNING_KEY`, `GPG_SIGNING_PASSWORD`
- Conditionally enabled only when publishing to Maven Central
- Safe for local testing (signing is optional when not publishing)

✅ **POM Metadata**
```xml
<groupId>dev.evokerking</groupId>
<artifactId>command-api</artifactId>
<version>1.0.0</version>
<name>Command API</name>
<description>A lightweight command framework with autocomplete support...</description>
<url>https://github.com/EvokerKing/command-api</url>
<license>MIT License</license>
<developers>EvokerKing</developers>
<scm>GitHub repository</scm>
```

### 2. Publishing Tools

#### A. Interactive Script (`publish-to-maven-central.sh`)
```bash
./publish-to-maven-central.sh
```
Menu options:
1. Full publish to Maven Central (with prerequisites check)
2. Test local publish only
3. Build and sign only
4. Exit

#### B. GitHub Actions Workflow (`.github/workflows/publish-maven-central.yml`)
Automatically publishes when you push a tag like `v1.0.0`:
```bash
git tag v1.0.0
git push origin v1.0.0
```

#### C. Direct Gradle Commands
```bash
# Local testing (no GPG required)
gradle publishMavenJavaPublicationToLocalTestRepository -p command-api

# Publish to Maven Central (with GPG signing)
gradle publish -p command-api
```

### 3. Configuration Files

#### `gradle.properties.template`
Template showing how to set up credentials:
- Sonatype username/password
- GPG key configuration
- JVM settings

#### `MAVEN_CENTRAL_PUBLISHING.md`
Comprehensive guide covering:
- Prerequisites setup (Sonatype account, GPG keys)
- Publishing process
- Release from staging
- Troubleshooting common issues
- Version management

#### `QUICK_START_PUBLISH.md`
Quick reference guide for:
- First-time setup
- Step-by-step instructions
- Three publishing methods
- Verification steps

### 4. Artifacts Generated

When you build, the following artifacts are created:

```
dev/evokerking/command-api/1.0.0/
├── command-api-1.0.0.jar              (Main JAR)
├── command-api-1.0.0-sources.jar      (Source code)
├── command-api-1.0.0-javadoc.jar      (API documentation)
├── command-api-1.0.0.pom              (Maven POM file)
├── command-api-1.0.0.module           (Gradle metadata)
└── [hash files]                        (SHA256, SHA512, MD5, SHA1)
```

When GPG signing is enabled, each artifact also gets a `.asc` signature file.

## How to Publish

### For Immediate Testing (Local)
```bash
# No credentials needed
gradle publishMavenJavaPublicationToLocalTestRepository -p command-api

# Artifacts will be in: command-api/build/repos/releases/
```

### For Release to Maven Central

#### Option 1: GitHub Actions (Recommended)
```bash
# 1. Ensure GitHub secrets are configured:
#    - MAVEN_USERNAME
#    - MAVEN_PASSWORD
#    - GPG_SIGNING_KEY
#    - GPG_SIGNING_PASSWORD

# 2. Push a tag
git tag v1.0.0
git push origin v1.0.0

# 3. GitHub Actions handles everything automatically
```

#### Option 2: Interactive Script
```bash
# 1. Set environment variables or update ~/.gradle/gradle.properties
export MAVEN_USERNAME=your_username
export MAVEN_PASSWORD=your_token
export GPG_SIGNING_KEY="$(gpg --armor --export-secret-keys KEY_ID | base64)"
export GPG_SIGNING_PASSWORD=your_passphrase

# 2. Run the script
chmod +x publish-to-maven-central.sh
./publish-to-maven-central.sh
```

#### Option 3: Direct Gradle
```bash
# Set environment variables (same as Option 2)

# Then run
gradle publish -p command-api
```

### After Publishing to Staging

1. Log in to https://s01.oss.sonatype.org
2. Go to "Staging Repositories"
3. Find `devevokerking-XXXX` repository
4. Click "Close" (validates)
5. Click "Release" (promotes to Maven Central)
6. Wait 10-30 minutes for sync

## Environment Variables Required for Publishing

When publishing to Maven Central, set these environment variables:

```bash
export MAVEN_USERNAME=your_sonatype_username
export MAVEN_PASSWORD=your_sonatype_auth_token
export GPG_SIGNING_KEY=$(gpg --armor --export-secret-keys YOUR_KEY_ID | base64)
export GPG_SIGNING_PASSWORD=your_gpg_passphrase
```

Or configure in `~/.gradle/gradle.properties`:
```properties
mavenUsername=your_username
mavenPassword=your_token
signing.keyId=LAST_8_CHARS_OF_GPG_KEY
signing.password=your_passphrase
```

## Gradle Tasks Available

```bash
# Build and test
gradle build -p command-api
gradle test -p command-api

# Local testing (no auth required)
gradle publishMavenJavaPublicationToLocalTestRepository -p command-api

# Maven Central (requires credentials)
gradle publishMavenJavaPublicationToMavenCentralRepository -p command-api
gradle publish -p command-api                    # All repositories

# Just signing
gradle signMavenJavaPublication -p command-api

# Generate documentation
gradle javadoc -p command-api
```

## Repository Locations

- **Sonatype OSS Staging**: https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/
- **Maven Central**: https://repo1.maven.org/maven2/dev/evokerking/command-api/
- **MVN Repository**: https://mvnrepository.com/artifact/dev.evokerking/command-api
- **Local Test**: `command-api/build/repos/releases/`

## Files Modified/Created

### Modified
- `command-api/build.gradle.kts` - Added Maven publish and signing configuration

### Created
- `.github/workflows/publish-maven-central.yml` - GitHub Actions workflow
- `publish-to-maven-central.sh` - Interactive publishing script
- `gradle.properties.template` - Credentials template
- `MAVEN_CENTRAL_PUBLISHING.md` - Comprehensive publishing guide
- `QUICK_START_PUBLISH.md` - Quick reference guide

## Next Steps

1. **For First-Time Publishers**:
   - Read `QUICK_START_PUBLISH.md`
   - Create Sonatype account
   - Generate GPG key
   - Configure GitHub secrets (if using GitHub Actions)

2. **To Test Locally**:
   ```bash
   gradle publishMavenJavaPublicationToLocalTestRepository -p command-api
   ```

3. **To Publish**:
   - GitHub Actions: Push tag `v1.0.0`
   - Manual: Run `./publish-to-maven-central.sh`
   - Direct: Run `gradle publish -p command-api`

4. **After Publishing**:
   - Release from Sonatype staging
   - Verify on Maven Central
   - Update version for next release

## Verifying Your Package

Once released (after 10-30 minutes):

```gradle
// In your project's build.gradle
dependencies {
    implementation 'dev.evokerking:command-api:1.0.0'
}
```

Then test:
```bash
gradle build
```

Or check:
- https://repo1.maven.org/maven2/dev/evokerking/command-api/1.0.0/
- https://mvnrepository.com/artifact/dev.evokerking/command-api

## Support

For issues:
1. Check troubleshooting in `MAVEN_CENTRAL_PUBLISHING.md`
2. Verify all credentials are correct
3. Ensure GPG key is available locally
4. Check Sonatype repository details for validation errors

For more information:
- [Sonatype OSSRH Guide](https://central.sonatype.org/pages/ossrh-guide.html)
- [Gradle Publishing Guide](https://docs.gradle.org/current/userguide/publishing_overview.html)
