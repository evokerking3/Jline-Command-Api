# Maven Central Publishing - Complete Setup

## Overview

The Command API has been fully configured for publishing to Maven Central Repository. This includes Gradle configuration, GitHub Actions workflow, interactive scripts, and comprehensive documentation.

## Quick Links

| Document | Purpose |
|----------|---------|
| [QUICK_START_PUBLISH.md](QUICK_START_PUBLISH.md) | **Start here** - Quick setup guide for first-time publishers |
| [PUBLISHING_SETUP_SUMMARY.md](PUBLISHING_SETUP_SUMMARY.md) | Complete overview of all configuration |
| [MAVEN_CENTRAL_PUBLISHING.md](MAVEN_CENTRAL_PUBLISHING.md) | Comprehensive guide with detailed troubleshooting |
| [PUBLISHING_FILES.txt](PUBLISHING_FILES.txt) | File structure and organization reference |

## Three Ways to Publish

### 1. GitHub Actions (Recommended) - Fully Automated
```bash
git tag v1.0.0
git push origin v1.0.0
# GitHub Actions handles everything automatically
```

### 2. Interactive Script - Easy Local Publishing
```bash
./publish-to-maven-central.sh
# Choose from menu:
# 1) Full publish to Maven Central
# 2) Test local (no auth required)
# 3) Build and sign only
```

### 3. Direct Gradle - Manual Control
```bash
export MAVEN_USERNAME=your_username
export MAVEN_PASSWORD=your_token
export GPG_SIGNING_KEY="$(gpg --armor --export-secret-keys KEY_ID | base64)"
export GPG_SIGNING_PASSWORD=your_passphrase

gradle publish -p command-api
```

## What Was Configured

✅ **Gradle Build Configuration** (`command-api/build.gradle.kts`)
- Maven Publish Plugin with complete POM metadata
- GPG Signing Plugin (environment variable based)
- Publication: `dev.evokerking:command-api:1.0.0`
- Generates: JAR, sources JAR, javadoc JAR, signatures
- Repositories: Sonatype OSS staging, local test

✅ **GitHub Actions Workflow** (`.github/workflows/publish-maven-central.yml`)
- Automated on tag push (e.g., `v1.0.0`)
- Builds, signs, publishes, creates release

✅ **Interactive Publishing Script** (`publish-to-maven-central.sh`)
- Menu-driven with prerequisites checking
- Supports testing and full publishing
- Error handling and guidance

✅ **Documentation** (1000+ lines)
- Quick start guide
- Comprehensive publishing guide
- Troubleshooting section
- File structure reference

## Artifacts Generated

When published, the following artifacts are created:
```
dev/evokerking/command-api/1.0.0/
├── command-api-1.0.0.jar          (Main library)
├── command-api-1.0.0-sources.jar  (Source code)
├── command-api-1.0.0-javadoc.jar  (API documentation)
├── command-api-1.0.0.pom          (Maven descriptor)
├── command-api-1.0.0.module       (Gradle metadata)
├── command-api-1.0.0.jar.asc      (GPG signature)
└── [checksums and hashes]         (SHA256, SHA512, MD5, SHA1)
```

## Repository Information

| Item | Value |
|------|-------|
| Group ID | `dev.evokerking` |
| Artifact ID | `command-api` |
| Version | `1.0.0` |
| License | MIT |
| Source Repository | https://github.com/EvokerKing/command-api |
| Maven Central | https://repo1.maven.org/maven2/dev/evokerking/command-api/ |
| Staging Server | https://s01.oss.sonatype.org |
| Search Engines | https://mvnrepository.com/artifact/dev.evokerking/command-api |

## For First-Time Publishers

1. **Read** [QUICK_START_PUBLISH.md](QUICK_START_PUBLISH.md)
2. **Create** Sonatype JIRA account
3. **Generate** GPG key pair
4. **Set up** GitHub secrets (if using GitHub Actions)
5. **Test** with `gradle publishMavenJavaPublicationToLocalTestRepository -p command-api`
6. **Publish** using one of the three methods above
7. **Release** from Sonatype staging to Maven Central

## Gradle Tasks

```bash
# Build and test
gradle build -p command-api
gradle test -p command-api

# Local testing (no auth required)
gradle publishMavenJavaPublicationToLocalTestRepository -p command-api

# Publish to Maven Central (requires credentials)
gradle publish -p command-api

# Generate documentation
gradle javadoc -p command-api
```

## Files Modified/Created

### Modified
- `command-api/build.gradle.kts` - Added Maven publish and signing
- `gradle/libs.versions.toml` - Updated to JUnit 5

### Created
- `.github/workflows/publish-maven-central.yml` - GitHub Actions workflow
- `publish-to-maven-central.sh` - Interactive publishing script
- `gradle.properties.template` - Configuration template
- `PUBLISHING_SETUP_SUMMARY.md` - Setup overview
- `QUICK_START_PUBLISH.md` - Quick reference
- `MAVEN_CENTRAL_PUBLISHING.md` - Comprehensive guide
- `PUBLISHING_FILES.txt` - File structure reference

## Environment Variables

For publishing to Maven Central, set these:

```bash
MAVEN_USERNAME             # Sonatype username
MAVEN_PASSWORD             # Sonatype authentication token
GPG_SIGNING_KEY           # Base64-encoded GPG private key
GPG_SIGNING_PASSWORD      # GPG key passphrase
```

Or configure in `~/.gradle/gradle.properties`:
```properties
mavenUsername=your_username
mavenPassword=your_token
signing.keyId=LAST_8_CHARS_OF_KEY_ID
signing.password=your_passphrase
```

## Publishing Flow

```
Local Testing
└── gradle publishMavenJavaPublicationToLocalTestRepository
    └── Check: command-api/build/repos/releases/

GitHub Actions (Recommended)
└── git tag v1.0.0 && git push
    └── Automatic build, sign, publish, release

Manual Publishing
└── Set env vars + ./publish-to-maven-central.sh
    └── Choose option 1 (full publish)

Release from Staging
└── https://s01.oss.sonatype.org
    └── Close (validate) → Release (promote)

Verify
└── https://mvnrepository.com/artifact/dev.evokerking/command-api
└── https://repo1.maven.org/maven2/dev/evokerking/command-api/
```

## Troubleshooting

Common issues and solutions are documented in:
- **Quick issues**: See [QUICK_START_PUBLISH.md](QUICK_START_PUBLISH.md)
- **Detailed issues**: See [MAVEN_CENTRAL_PUBLISHING.md](MAVEN_CENTRAL_PUBLISHING.md)
- **Setup issues**: See [PUBLISHING_SETUP_SUMMARY.md](PUBLISHING_SETUP_SUMMARY.md)

## Next Steps

1. **For immediate testing** (no credentials needed):
   ```bash
   gradle publishMavenJavaPublicationToLocalTestRepository -p command-api
   ```

2. **For publishing**:
   - Option A: GitHub Actions (recommended)
     ```bash
     git tag v1.0.0 && git push origin v1.0.0
     ```
   - Option B: Interactive script
     ```bash
     ./publish-to-maven-central.sh
     ```

3. **After publishing**:
   - Release from staging at https://s01.oss.sonatype.org
   - Verify on Maven Central after 10-30 minutes

## Support & Documentation

- [Sonatype OSSRH Guide](https://central.sonatype.org/pages/ossrh-guide.html)
- [Gradle Maven Publish Plugin](https://docs.gradle.org/current/plugins/maven-publish-plugin.html)
- [Gradle Signing Plugin](https://docs.gradle.org/current/plugins/signing-plugin.html)
- [GPG Key Generation](https://help.github.com/en/articles/generating-a-new-gpg-key)

---

**Ready to publish?** Start with [QUICK_START_PUBLISH.md](QUICK_START_PUBLISH.md) for step-by-step instructions.
