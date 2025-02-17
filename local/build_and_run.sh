#!/bin/bash

# Get the directory of the script and move to the intended parent directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "Script Directory: ${SCRIPT_DIR}"

# Assuming the script is located in qodana-procmatrix/local, we need to go up two levels
PARENT_DIR="$(cd "${SCRIPT_DIR}/../.." && pwd)"
echo "Parent Directory: ${PARENT_DIR}"

cd "${PARENT_DIR}"

# Path to the custom settings.xml file
SETTINGS_FILE="${SCRIPT_DIR}/settings.xml"
echo "Settings File: ${SETTINGS_FILE}"

# Function to run a Spring Boot app in a new terminal
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
mvn clean install --settings ${SETTINGS_FILE}

# Run procmatrix-core
run_spring_boot_app "procmatrix" "procmatrix"

# Run procmatrix-rotation
run_spring_boot_app "procmatrix-rotation" "procmatrix-rotation"

# Run procmatrix-client
run_spring_boot_app "procmatrix-client" "procmatrix-client"