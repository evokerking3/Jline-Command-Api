#!/bin/bash
# Maven Central Publishing Script
# This script automates the process of publishing command-api to Maven Central

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_prerequisites() {
    print_info "Checking prerequisites..."
    
    # Check Gradle
    if ! command -v gradle &> /dev/null; then
        print_error "Gradle not found. Please install Gradle first."
        exit 1
    fi
    print_info "✓ Gradle found: $(gradle --version | head -1)"
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java not found. Please install Java first."
        exit 1
    fi
    print_info "✓ Java found: $(java -version 2>&1 | head -1)"
    
    # Check GPG
    if ! command -v gpg &> /dev/null; then
        print_error "GPG not found. Please install GPG first."
        exit 1
    fi
    print_info "✓ GPG found: $(gpg --version | head -1)"
    
    # Prefer credentials from Gradle properties (user or project) over environment variables.
    GRADLE_PROP_PATHS=("$HOME/.gradle/gradle.properties" "./gradle.properties" "./.gradle/gradle.properties")
    for p in "${GRADLE_PROP_PATHS[@]}"; do
        if [ -f "$p" ]; then
            mvUser=$(grep -E '^mavenUsername=' "$p" | cut -d'=' -f2- | tr -d '\r')
            mvPass=$(grep -E '^mavenPassword=' "$p" | cut -d'=' -f2- | tr -d '\r')
            if [ -n "$mvUser" ] && [ -n "$mvPass" ]; then
                export MAVEN_USERNAME="$mvUser"
                export MAVEN_PASSWORD="$mvPass"
                print_info "✓ Loaded Maven credentials from $p (overrides env)"
                break
            fi
        fi
    done

    # If Maven credentials still missing, warn (env may provide them)
    if [ -z "$MAVEN_USERNAME" ] || [ -z "$MAVEN_PASSWORD" ]; then
        if [ -n "$MAVEN_USERNAME" ] && [ -n "$MAVEN_PASSWORD" ]; then
            print_info "✓ MAVEN_USERNAME and MAVEN_PASSWORD are set in environment"
        else
            print_warning "Maven credentials not found in gradle.properties or environment"
        fi
    fi

    # Prefer signing.password from gradle.properties (override env) if present
    for p in "${GRADLE_PROP_PATHS[@]}"; do
        if [ -f "$p" ]; then
            signPass=$(grep -E '^signing.password=' "$p" | cut -d'=' -f2- | tr -d '\r')
            if [ -n "$signPass" ]; then
                export GPG_SIGNING_PASSWORD="$signPass"
                print_info "✓ Loaded signing password from $p (overrides env)"
                break
            fi
        fi
    done
}

build_project() {
    print_info "Building command-api..."
    gradle clean build -p command-api 
    print_info "✓ Build completed successfully"
}

publish_to_staging() {
    print_info "Publishing to Maven Central staging repository..."
    gradle publish -p command-api 
    print_info "✓ Published to staging repository"
}

test_local_publish() {
    print_info "Testing publish to local repository..."
    gradle publishMavenJavaPublicationToLocalTestRepository -p command-api 
    
    # Check if artifacts were created
    if [ -d "command-api/build/repos/releases" ]; then
        print_info "✓ Local publish successful"
        print_info "Artifacts created:"
        find command-api/build/repos/releases -type f | sed 's/^/  /'
    else
        print_error "Local publish failed - no artifacts created"
        exit 1
    fi
}

sign_artifacts() {
    print_info "Signing artifacts..."
    # Only attempt signing non-interactively if an in-memory key is available
    # (GPG_SIGNING_KEY env) or a `signing.key` entry exists in gradle.properties.
    GRADLE_PROP_PATHS=("$HOME/.gradle/gradle.properties" "./gradle.properties" "./.gradle/gradle.properties")
    foundKey=0
    if [ -n "$GPG_SIGNING_KEY" ]; then
        foundKey=1
    else
        for p in "${GRADLE_PROP_PATHS[@]}"; do
            if [ -f "$p" ]; then
                keyVal=$(grep -E '^signing.key=' "$p" | cut -d'=' -f2- | tr -d '\r')
                if [ -n "$keyVal" ]; then
                    foundKey=1
                    break
                fi
            fi
        done
    fi

    if [ "$foundKey" -eq 1 ]; then
        gradle signMavenJavaPublication -p command-api 
        print_info "✓ Artifacts signed"
    else
        print_warning "No in-memory signing key found in gradle.properties or GPG_SIGNING_KEY; skipping signing to avoid interactive gpg failures."
        print_info "If you want automatic signing, set 'signing.key' in gradle.properties (base64) or GPG_SIGNING_KEY env var."
    fi
}

show_next_steps() {
    cat << 'EOF'

╔════════════════════════════════════════════════════════════════════════════╗
║                       NEXT STEPS FOR RELEASE                              ║
╚════════════════════════════════════════════════════════════════════════════╝

1. LOG IN TO SONATYPE
   Visit: https://s01.oss.sonatype.org

2. CLOSE STAGING REPOSITORY
   - Go to "Staging Repositories"
   - Find repository starting with "devevokerking"
   - Click "Close" to validate

3. VERIFY CONTENTS
   - Click on repository to view details
   - Check all artifacts are present

4. RELEASE TO MAVEN CENTRAL
   - Click "Release" to promote

5. VERIFY RELEASE
   - Wait ~10-30 minutes for sync to Maven Central
   - Check: https://mvnrepository.com/artifact/dev.evokerking/command-api
   - Check: https://repo1.maven.org/maven2/dev/evokerking/command-api/

6. UPDATE VERSION FOR NEXT RELEASE
   - Edit: command-api/build.gradle.kts
   - Update version number

For detailed instructions, see: MAVEN_CENTRAL_PUBLISHING.md

EOF
}

# Main execution
main() {
    print_info "╔════════════════════════════════════════════════════════════════╗"
    print_info "║   Maven Central Publishing Script for Command API              ║"
    print_info "╚════════════════════════════════════════════════════════════════╝"
    
    print_info "Choose publishing mode:"
    echo "  1) Full publish to Maven Central staging"
    echo "  2) Test local publish only"
    echo "  3) Build and sign only"
    echo "  4) Exit"
    
    # Allow non-interactive usage: first CLI arg or PUBLISH_MODE env var
    if [ -n "$1" ]; then
        option="$1"
    elif [ -n "$PUBLISH_MODE" ]; then
        option="$PUBLISH_MODE"
    else
        read -p "Enter option (1-4): " option
    fi
    
    case $option in
        1)
            print_info "Starting full publish to Maven Central..."
            check_prerequisites
            build_project
            sign_artifacts
            publish_to_staging
            show_next_steps
            ;;
        2)
            print_info "Starting local test publish..."
            check_prerequisites
            build_project
            test_local_publish
            print_info "Local test publish completed successfully!"
            ;;
        3)
            print_info "Building and signing artifacts..."
            check_prerequisites
            build_project
            sign_artifacts
            print_info "Build and sign completed successfully!"
            ;;
        4)
            print_info "Exiting..."
            exit 0
            ;;
        *)
            print_error "Invalid option: $option"
            exit 1
            ;;
    esac
}

# Run main function
main
