#!/bin/bash

# Get the directory of the script and move to the parent directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PARENT_DIR="$(dirname "${SCRIPT_DIR}")"
cd "${PARENT_DIR}"

# Path to the custom settings.xml file
SETTINGS_FILE="${SCRIPT_DIR}/settings.xml"

run_spring_boot_app() {
    local app_dir=$1
    local app_name=$2

    echo "Running ${app_name} in a new terminal..."

    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        gnome-terminal -- bash -c "cd ${app_dir} && mvn spring-boot:run --settings ${SETTINGS_FILE}; exec bash" &
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        osascript -e "tell application \"Terminal\" to do script \"cd ${app_dir} && mvn spring-boot:run --settings ${SETTINGS_FILE}\""
    elif [[ "$OSTYPE" == "msys"* || "$OSTYPE" == "cygwin"* ]]; then
        mintty -e bash -c "cd ${app_dir} && mvn spring-boot:run --settings ${SETTINGS_FILE}; exec bash" &
    else
        echo "Unsupported OS: ${OSTYPE}"
        exit 1
    fi
}

# Build all projects
echo "Building all projects..."
mvn clean install -DskipTests --settings ${SETTINGS_FILE}

# Run procmatrix-core
run_spring_boot_app "procmatrix" "procmatrix"

# Run procmatrix-rotation
run_spring_boot_app "procmatrix-rotation" "procmatrix-rotation"

# Run procmatrix-client
run_spring_boot_app "procmatrix-client" "procmatrix-client"