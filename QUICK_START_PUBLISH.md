# Quick Start: Publishing Command API to Maven Central

## For First-Time Publishers

### Step 1: Set Up Sonatype Account
1. Create account at https://issues.sonatype.org
2. Create a JIRA ticket to claim namespace `dev.evokerking`
3. Wait for approval (usually 1-2 hours)

### Step 2: Generate GPG Key
```bash
# Generate GPG key pair
gpg --gen-key

# List your keys
gpg --list-secret-keys --keyid-format LONG

# Export to keyserver (use last 8 chars of key ID)
gpg --keyserver hkp://keyserver.ubuntu.com --send-keys YOUR_KEY_ID
```

### Step 3: Export GPG Key for GitHub
```bash
# Export and encode GPG key (copy the output)
gpg --armor --export-secret-keys YOUR_KEY_ID | base64
```

### Step 4: Set GitHub Secrets
In your GitHub repository settings, add these secrets:
- `MAVEN_USERNAME`: Your Sonatype username
- `MAVEN_PASSWORD`: Your Sonatype password (use authentication token from https://s01.oss.sonatype.org)
- `GPG_SIGNING_KEY`: Base64-encoded GPG key (from Step 3)
- `GPG_SIGNING_PASSWORD`: Your GPG passphrase

### Step 5: Publish a Release

#### Option A: Automatic (GitHub Actions)
```bash
# Create a git tag
git tag v1.0.0

# Push the tag
git push origin v1.0.0
```

GitHub Actions will automatically:
1. Build the project
2. Sign artifacts with GPG
3. Upload to Maven Central staging
4. Create a GitHub release

#### Option B: Manual (Interactive Script)
```bash
# Make script executable
chmod +x publish-to-maven-central.sh

# Run the script
./publish-to-maven-central.sh
```

Choose option 1 for full publish, or test with option 2 first.

#### Option C: Manual (Direct Gradle)
```bash
# Set environment variables
export MAVEN_USERNAME=your_username
export MAVEN_PASSWORD=your_token
export GPG_SIGNING_KEY="$(gpg --armor --export-secret-keys YOUR_KEY_ID | base64)"
export GPG_SIGNING_PASSWORD=your_passphrase

# Publish to Maven Central
gradle publish -p command-api
```

## Step 6: Release from Staging

After publishing, release from Sonatype:

1. Go to https://s01.oss.sonatype.org
2. Login with your Sonatype credentials
3. Click "Staging Repositories"
4. Find `devevokerking-XXXX` repository
5. Click "Close" (validates contents)
6. Click "Release" (promotes to Maven Central)

## Step 7: Verify Release

After ~10-30 minutes:

1. Check Maven Central: https://repo1.maven.org/maven2/dev/evokerking/command-api/
2. Check MVN Repository: https://mvnrepository.com/artifact/dev.evokerking/command-api
3. Try using it in a project:

```gradle
dependencies {
    implementation 'dev.evokerking:command-api:1.0.0'
}
```

## Troubleshooting

### "Command not found: gpg"
Install GPG:
```bash
# macOS
brew install gnupg

# Ubuntu/Debian
sudo apt-get install gnupg

# Windows
choco install gnupg
```

### "401 Unauthorized"
- Verify Sonatype credentials
- Create authentication token: https://s01.oss.sonatype.org/ui/#welcome
- Use token as password (not actual password)

### "No secret key"
- List keys: `gpg --list-secret-keys`
- Generate new key: `gpg --gen-key`
- Send to keyserver: `gpg --keyserver hkp://keyserver.ubuntu.com --send-keys YOUR_KEY_ID`

### Version Already Exists
- Update version in `command-api/build.gradle.kts`
- Example: 1.0.0 → 1.0.1 or 1.1.0 → 2.0.0

### Staging Repository Not Closing
- Check artifacts are complete (JAR, sources, javadoc, POM)
- Look for validation errors in repository details
- Try closing again after fixing issues

## Version Numbering

Use Semantic Versioning: `MAJOR.MINOR.PATCH`

Examples:
- `1.0.0` - First release
- `1.0.1` - Bug fixes
- `1.1.0` - New features
- `2.0.0` - Breaking changes

## Useful Links

- [Sonatype OSSRH](https://central.sonatype.org/pages/ossrh-guide.html)
- [Gradle Maven Publish Plugin](https://docs.gradle.org/current/plugins/maven-publish-plugin.html)
- [Gradle Signing Plugin](https://docs.gradle.org/current/plugins/signing-plugin.html)
- [Maven Central Repository](https://repo1.maven.org/maven2/)
- [Sonatype Staging](https://s01.oss.sonatype.org/)

## Full Documentation

See `MAVEN_CENTRAL_PUBLISHING.md` for comprehensive guide.
